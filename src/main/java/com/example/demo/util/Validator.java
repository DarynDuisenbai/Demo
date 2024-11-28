package com.example.demo.util;

import com.example.demo.domain.Conclusion;
import com.example.demo.domain.TemporaryConclusion;
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
        if (password == null || password.length() < 6) {
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

    public boolean isValidConclusion(TemporaryConclusion conclusion){
        if(conclusion.getUD().isEmpty() || conclusion.getUD() == null){
            return false;
        } if(conclusion.getInvestigatorIIN().isEmpty() || conclusion.getInvestigatorIIN() == null){
            return false;
        } if(conclusion.getIINofCalled().isEmpty() || conclusion.getIINofCalled() == null){
            return false;
        } if(conclusion.getJobTitleOfCalled().isEmpty() || conclusion.getJobTitleOfCalled() == null){
            return false;
        } if(conclusion.getJobPlace().isEmpty() || conclusion.getJobPlace() == null){
            return false;
        } if(conclusion.getPlannedActions().isEmpty() || conclusion.getPlannedActions() == null){
            return false;
        } if(conclusion.getEventPlace().isEmpty() || conclusion.getEventPlace() == null){
            return false;
        } if(conclusion.getEventTime() == null) {
            return false;
        } if(conclusion.getRelation().isEmpty() || conclusion.getRelation() == null){
            return false;
        } if(conclusion.getIINDefender().isEmpty() || conclusion.getIINDefender() == null){
            return false;
        } if(conclusion.getJustification().isEmpty() || conclusion.getJustification() == null){
            return false;
        } if(conclusion.getResult().isEmpty() || conclusion.getResult() == null){
            return false;
        } if(conclusion.getInvestigation().isEmpty() || conclusion.getInvestigation() == null){
            return false;
        } if(conclusion.getIsBusiness().isEmpty() || conclusion.getIsBusiness() == null){
            return false;
        }

        return true;
    }
}

