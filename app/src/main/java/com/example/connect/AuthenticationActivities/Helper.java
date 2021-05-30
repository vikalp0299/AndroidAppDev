package com.example.connect.AuthenticationActivities;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper<password> {
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);
    public static boolean validateEmailAddress(String emailString) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailString);
        return matcher.find();
    }

    public static boolean validatePassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.find();
    }

    public static long getUniqueLongID(){
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

}
