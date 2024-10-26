package org.code_challenger.config;

import org.code_challenger.repository.UserRepository;
import org.code_challenger.repository.dto.User;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
public class CreateDefaultUser {

    @Bean
    public ApplicationRunner defaultUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("Initialize default user...");

            if (userRepository.count() == 0) {
                User defaultUser = new User();
                defaultUser.setUsername("admin");
                defaultUser.setPassword(passwordEncoder.encode("admin"));
                defaultUser.setEmails(Collections.singletonList("admin@exemplo.com"));
                defaultUser.setRole("ADMIN");

                List<User.UserPhone> userPhones = new ArrayList<>();
                User.UserPhone phone1 = new User.UserPhone();
                phone1.setPhoneNumber("+5511999999999");
                phone1.setPhoneType("cellphone");
                userPhones.add(phone1);
                defaultUser.setPhones(userPhones);

                userRepository.save(defaultUser);
                System.out.println("Successfully created default user.");
            } else {
                System.out.println("Default user already exists.");
            }
        };
    }
}
