package org.code_challenger.services;

import org.code_challenger.repository.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;

@Service
public class UserService {

    public static Map<String, Object> BuildUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        Map<String, Object> addressResponse = new HashMap<>();

        userResponse.put("id", user.getId());
        userResponse.put("username", user.getUsername());
        userResponse.put("email", user.getEmails());
        userResponse.put("phone", user.getPhones());
        userResponse.put("cpf", user.getCpf());

        if (user.getAddress() != null) {
            addressResponse.put("street", user.getAddress().getStreet());
            addressResponse.put("number", user.getAddress().getNumber());
            addressResponse.put("neighborhood", user.getAddress().getNeighborhood());
            addressResponse.put("city", user.getAddress().getCity());
            addressResponse.put("state", user.getAddress().getState());
            addressResponse.put("zipCode", user.getAddress().getZipCode());
        }

        userResponse.put("address", addressResponse);

        return userResponse;
    }
    public static List<String> ProcessUserEmails(User _user) {
        if (_user.getEmails() != null) {
            List<String> userEmails = new ArrayList<>();

            for (String email : _user.getEmails()) {
                String normalizedEmail = EmailService.NormalizeEmail(email);
                userEmails.add(normalizedEmail);
            }

            if (!EmailService.ValidateUserEmail(userEmails)) {
                throw new IllegalArgumentException("One or more emails are invalid");
            }

            return userEmails;
        }
        return new ArrayList<>();
    }
    public static User.Address SetUserAddress(User _user) {
        User.Address address = new User.Address();
        address.setStreet(_user.getAddress().getStreet());
        address.setNumber(_user.getAddress().getNumber());
        address.setNeighborhood(_user.getAddress().getNeighborhood());
        address.setCity(_user.getAddress().getCity());
        address.setState(_user.getAddress().getState());
        address.setZipCode(_user.getAddress().getZipCode());

        return address;
    }
    public static List<User.UserPhone> ProcessUserPhones(User _user) {
        List<User.UserPhone> userPhones = new ArrayList<>();

        if (_user.getPhones() != null) {
            for (User.UserPhone phoneDTO : _user.getPhones()) {
                User.UserPhone userPhone = new User.UserPhone();
                userPhone.setPhoneNumber(phoneDTO.getPhoneNumber());
                userPhone.setPhoneType(phoneDTO.getPhoneType());
                userPhones.add(userPhone);
            }
        }
        return userPhones;
    }

}
