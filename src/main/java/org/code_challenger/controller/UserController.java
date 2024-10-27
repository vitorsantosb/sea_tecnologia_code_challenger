package org.code_challenger.controller;

import org.code_challenger.repository.UserRepository;
import org.code_challenger.repository.dto.User;
import org.code_challenger.services.EmailService;
import org.code_challenger.services.ResponseBuilder;
import org.code_challenger.services.UserService;
import org.code_challenger.services.BcryptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.code_challenger.services.ZipCodeService.VerifyZipCode;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final ResponseBuilder responseBuilder;

    public UserController(UserRepository userRepository, ResponseBuilder responseBuilder) {
        this.userRepository = userRepository;
        this.responseBuilder = responseBuilder;
    }


    @GetMapping
    @RequestMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> GetUsersList() {
        System.out.println("Access route /users");

        List<User> users = userRepository.findAll();

        List<Map<String, Object>> usersList = users.stream()
                .map(UserService::BuildUserResponse)
                .collect(Collectors.toList());

        if (!usersList.isEmpty()) {
            return ResponseBuilder.CreateHttpResponse(
                    "Successfully",
                    HttpStatus.OK,
                    "List of users",
                    "GET",
                    "/users",
                    usersList
            );
        } else {
            return ResponseBuilder.CreateHttpResponse(
                    "Empty",
                    HttpStatus.OK,
                    "List of users",
                    "GET",
                    "/users",
                    null
            );
        }
    }

    @PostMapping
    @RequestMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> CreateUser(@RequestBody User _user) {
        if (userRepository.existsByCpf(_user.getCpf())) {
            return ResponseBuilder.CreateHttpResponse(
                    "User already exists",
                    HttpStatus.CONFLICT,
                    "Conflict when trying to create user",
                    "POST",
                    "/user/create",
                    null
            );
        }

        User.Address address = UserService.SetUserAddress(_user);

        if (!VerifyZipCode(address.getZipCode())) {
            return ResponseBuilder.CreateHttpResponse(
                    "Invalid zip code",
                    HttpStatus.BAD_REQUEST,
                    "Invalid zip code",
                    "POST",
                    "user/create",
                    null
            );
        }
        _user.setAddress(address);

        List<String> userEmail = UserService.ProcessUserEmails(_user);
        _user.setEmails(userEmail);

        List<User.UserPhone> _userPhones = UserService.ProcessUserPhones(_user);
        _user.setPhones(_userPhones);

        String _hashPassword = BcryptService.CreateHashPassword(_user.getPassword());
        _user.setPassword(_hashPassword);

        _user.setRole(_user.getRole());

        userRepository.save(_user);

        return ResponseBuilder.CreateHttpResponse(
                "Successfully",
                HttpStatus.CREATED,
                "Successfully created new user",
                "POST",
                "/user/create",
                UserService.BuildUserResponse(_user)
        );
    }

    @PutMapping
    @RequestMapping("/me/update")
    public ResponseEntity<Map<String, Object>> UpdateUser(@RequestBody User _userData, @RequestParam UUID id) {
        if (!userRepository.existsById(id)) {
            return ResponseBuilder.CreateHttpResponse(
                    "User not found",
                    HttpStatus.NOT_FOUND,
                    "User provided not exists on database",
                    "PUT",
                    "/user/me",
                    null
            );
        }
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User _currentUser = optionalUser.get();

            _currentUser.setCpf(_userData.getCpf());
            _currentUser.setUsername(_userData.getUsername());

            User.Address address = UserService.SetUserAddress(_userData);
            if (!VerifyZipCode(address.getZipCode())) {
                return ResponseBuilder.CreateHttpResponse(
                        "Invalid zip code",
                        HttpStatus.BAD_REQUEST,
                        "Invalid zip code",
                        "PUT",
                        "/user/me",
                        null
                );
            }
            _currentUser.setAddress(address);
            _currentUser.setRole(_userData.getRole());

            List<String> userEmail = UserService.ProcessUserEmails(_userData);
            _currentUser.setEmails(userEmail);

            List<User.UserPhone> _userPhones = UserService.ProcessUserPhones(_userData);
            _currentUser.setPhones(_userPhones);

            _currentUser.setUpdatedAt(new Date());

            userRepository.save(_currentUser);

            return ResponseBuilder.CreateHttpResponse(
                    "User updated successfully",
                    HttpStatus.ACCEPTED,
                    "User updated successfully",
                    "PUT",
                    "/user/me",
                    null
            );
        }else {
            return ResponseBuilder.CreateHttpResponse(
                    "User not found",
                    HttpStatus.NOT_FOUND,
                    "User provided not exists on database",
                    "PUT",
                    "/user/me",
                    null
            );
        }
    }

    @GetMapping
    @RequestMapping("/me")
    public ResponseEntity<Map<String, Object>> GetCurrentUser() {
        return null;
    }
}