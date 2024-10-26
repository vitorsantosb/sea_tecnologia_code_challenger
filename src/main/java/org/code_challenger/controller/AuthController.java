package org.code_challenger.controller;

import org.code_challenger.repository.UserRepository;
import org.code_challenger.repository.dto.AuthCredentials;
import org.code_challenger.repository.dto.AuthCredentialsDTO;
import org.code_challenger.repository.dto.User;
import org.code_challenger.services.BcryptService;
import org.code_challenger.services.ResponseBuilder;
import org.code_challenger.services.UuidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody AuthCredentials credentials) {
        System.out.println(credentials);
        if (!userRepository.existsByEmailsContaining(credentials.getEmail())) {
            return ResponseBuilder.CreateHttpResponse(
                    "Not Found",
                    HttpStatus.NOT_FOUND,
                    "User with credentials not found",
                    "POST",
                    "/auth/login",
                    null
            );
        }

        Optional<User> optionalUser = userRepository.findUserByEmailInEmailsList(credentials.getEmail());

        if (optionalUser.isPresent()) {
            User _currentUser = optionalUser.get();
            System.out.println("_queryResult: " + _currentUser);
            String _userHashedPassword = _currentUser.getPassword();

            if (BcryptService.VerifyHashPassword(credentials.getPassword(), _userHashedPassword)) {
                String _uuidToken = UuidService.GenerateUUID();
                AuthCredentialsDTO _responseData = new AuthCredentialsDTO(
                        _currentUser.getId(),
                        _currentUser.getEmails().get(0),
                        _uuidToken,
                        _currentUser.getRole()
                );

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
