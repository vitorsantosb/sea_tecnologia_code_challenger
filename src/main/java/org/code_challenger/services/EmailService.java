package org.code_challenger.services;


import lombok.Data;
import org.code_challenger.repository.dto.User;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailService {
    public static boolean ValidateUserEmail(List<String> _emailList) {
        String _emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(_emailRegex);

        for (String value : _emailList) {
            Matcher matcher = pattern.matcher(value);

            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }
    public static String NormalizeEmail(String _email){
        return _email.trim().toLowerCase();
    }
}
