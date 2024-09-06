package com.internship.managerview.business.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {
    /**
     * Checks whether a string contains a non-alphanumeric character.
     *
     * @param string The string to be checked
     * @return whether the string contains a non-alphanumeric character
     */
    public static boolean containsNonAlphanumeric(String string) {
        Pattern nonAlphanumericPattern = Pattern.compile("[^\\p{IsAlphabetic}\\d ]");
        Matcher matcher = nonAlphanumericPattern.matcher(string);
        return matcher.find();
    }
}
