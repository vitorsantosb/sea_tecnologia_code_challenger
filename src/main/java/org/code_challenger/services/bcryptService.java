package org.code_challenger.services;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;

public class bcryptService {

    public static String CreateHashPassword(String _password) {
        return BCrypt.hashpw(_password, BCrypt.gensalt(10, new SecureRandom()));
    }

    public static boolean VerifyHashPassword(String _password, String _hashPassword) {
        return BCrypt.checkpw(_password, _hashPassword);
    }
}
