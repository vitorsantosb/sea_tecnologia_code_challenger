package org.code_challenger.controller;

import org.code_challenger.repository.TokenRepository;
import org.code_challenger.repository.UserRepository;
import org.code_challenger.repository.dto.AuthCredentials;
import org.code_challenger.repository.dto.AuthCredentialsDTO;
import org.code_challenger.repository.dto.User;
import org.code_challenger.services.BcryptService;
import org.code_challenger.services.ResponseBuilder;
import org.code_challenger.services.TokenService;
import org.code_challenger.services.UuidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.code_challenger.services.EmailService.NormalizeEmail;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    public AuthController(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @RequestMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody AuthCredentials credentials) {
        String _normalizedUserEmail = NormalizeEmail(credentials.getEmail());
        if (!userRepository.existsByEmailsContaining(_normalizedUserEmail)) {
            return ResponseBuilder.CreateHttpResponse(
                    "Not Found",
                    HttpStatus.NOT_FOUND,
                    "User with credentials not found",
                    "POST",
                    "/auth/login",
                    null
            );
        }

        Optional<User> optionalUser = userRepository.findUserByEmailInEmailsList(_normalizedUserEmail);

        if (optionalUser.isPresent()) {
            User _currentUser = optionalUser.get();
            String _userHashedPassword = _currentUser.getPassword();

            if (BcryptService.VerifyHashPassword(credentials.getPassword(), _userHashedPassword)) {
                String _uuidToken = UuidService.GenerateUUID();
                AuthCredentialsDTO _responseData = new AuthCredentialsDTO(
                        _currentUser.getId(),
                        _currentUser.getEmails().get(0),
                        _uuidToken,
                        _currentUser.getRole()
                );

                tokenService.StoreSecurityToken(_uuidToken, _currentUser.getId(), LocalDateTime.now().plusMinutes(30));

                return ResponseBuilder.CreateHttpResponse(
                        "Success",
                        HttpStatus.OK,
                        "User authenticated",
                        "POST",
                        "/auth/login",
                        _responseData
                );
            } else {
                return ResponseBuilder.CreateHttpResponse(
                        "Unauthorized",
                        HttpStatus.UNAUTHORIZED,
                        "Invalid credentials",
                        "POST",
                        "/auth/login",
                        null
                );
            }
        } else {
            return ResponseBuilder.CreateHttpResponse(
                    "Not Found",
                    HttpStatus.NOT_FOUND,
                    "User with credentials not found",
                    "POST",
                    "/auth/login",
                    null
            );
        }
    }

}
