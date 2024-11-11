package com.example.demo.utils;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.exceptions.InvalidLoginException;
import com.example.demo.exceptions.InvalidPasswordException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> "!@#$%^&*()-+=<>?".indexOf(ch) >= 0);

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    public boolean isValidIIN(String IIN) {
        if (IIN == null || IIN.length() != 12) {
            return false;
        }
        return IIN.chars().allMatch(Character::isDigit);
    }

    public boolean isValidUD(String UD) {
        if (UD == null || UD.length() != 15) {
            return false;
        }
        return UD.chars().allMatch(Character::isDigit);
    }
}

