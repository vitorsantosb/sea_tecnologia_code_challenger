package org.code_challenger.controller;

import lombok.var;
import org.code_challenger.repository.UserRepository;
import org.code_challenger.repository.dto.User;
import org.code_challenger.services.ResponseBuilder;
import org.code_challenger.services.UserService;
import org.code_challenger.services.bcryptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.code_challenger.services.ZipCodeService.VerifyZipCode;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> GetUsersList() {
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

        User.Address address = new User.Address();
        address.setStreet(_user.getAddress().getStreet());
        address.setNumber(_user.getAddress().getNumber());
        address.setNeighborhood(_user.getAddress().getNeighborhood());
        address.setCity(_user.getAddress().getCity());
        address.setState(_user.getAddress().getState());
        address.setZipCode(_user.getAddress().getZipCode());

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

        System.out.println(_user);

        if (_user.getEmails() != null) {
            List<String> userEmails = new ArrayList<>(_user.getEmails());
            _user.setEmails(userEmails);
        }

        if (_user.getPhones() != null) {
            List<User.UserPhone> userPhones = new ArrayList<>();
            for (User.UserPhone phoneDTO : _user.getPhones()) {
                System.out.println("Received Phone DTO: " + phoneDTO);
                User.UserPhone userPhone = new User.UserPhone();
                userPhone.setPhoneNumber(phoneDTO.getPhoneNumber());
                userPhone.setPhoneType(phoneDTO.getPhoneType());
                userPhones.add(userPhone);
            }
            _user.setPhones(userPhones);
        }


        String _hashPassword = bcryptService.CreateHashPassword(_user.getPassword());
        _user.setPassword(_hashPassword);

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
}
