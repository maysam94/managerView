package com.internship.managerview.business;

import com.internship.managerview.business.models.StringValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringValidatorUnitTests {

    @Nested
    @DisplayName("Tests for containsNonAlphanumeric()")
    public class PostTeamsTests {

        /**
         * Tests if containsNonAlphanumeric() returns false if the given string only contains letters.
         *
         */
        @Test
        @DisplayName("Returns false if the string only contains letters.")
        public void withOnlyLetters() {
            boolean result = StringValidator.containsNonAlphanumeric("letters");
            assertFalse(result);
        }

        /**
         * Tests if containsNonAlphanumeric() returns false if the given string only contains numbers.
         */
        @Test
        @DisplayName("Returns false if the string only contains numbers.")
        public void withOnlyNumbers() {
            boolean result = StringValidator.containsNonAlphanumeric("12345");
            assertFalse(result);
        }

        /**
         * Tests if containsNonAlphanumeric() returns false if the given string contains letters and numbers.
         */
        @Test
        @DisplayName("Returns false if the string only contains letters and numbers.")
        public void withLettersAndNumbers() {
            boolean result = StringValidator.containsNonAlphanumeric("letters12345");
            assertFalse(result);
        }

        /**
         * Tests if containsNonAlphanumeric() returns false if the given string contains letters, numbers and whitespace.
         */
        @Test
        @DisplayName("Returns false if the string only contains letters, numbers and a whitespace.")
        public void withLettersNumbersWhitespace() {
            boolean result = StringValidator.containsNonAlphanumeric("letters 12345");
            assertFalse(result);
        }

        /**
         * Tests if containsNonAlphanumeric() returns true if the given string contains a dollar sign '$'.
         */
        @Test
        @DisplayName("Returns true if the string contains a dollar sign '$'.")
        public void withDollarSign() {
            boolean result = StringValidator.containsNonAlphanumeric("dollar sign $");
            assertTrue(result);
        }

        /**
         * Tests if containsNonAlphanumeric() returns true if the given string contains a decimal point '.'.
         */
        @Test
        @DisplayName("Returns true if the string contains a decimal point '.'.")
        public void withDecimalPoint() {
            boolean result = StringValidator.containsNonAlphanumeric("decimal point.");
            assertTrue(result);
        }

        /**
         * Tests if containsNonAlphanumeric() returns true if the given string contains a double quote '"'.
         */
        @Test
        @DisplayName("Returns true if the string contains a double quote '\"'.")
        public void withDoubleQuote() {
            boolean result = StringValidator.containsNonAlphanumeric("double quote \"");
            assertTrue(result);
        }

        /**
         * Tests if containsNonAlphanumeric() returns true if the given string only contains a backtick '`'.
         */
        @Test
        @DisplayName("Returns true if the string contains a backtick '`'.")
        public void withBacktick() {
            boolean result = StringValidator.containsNonAlphanumeric("backtick `");
            assertTrue(result);
        }
    }
}
