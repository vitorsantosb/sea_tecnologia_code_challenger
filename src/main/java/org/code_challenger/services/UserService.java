package org.code_challenger.services;

import org.code_challenger.repository.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
}
