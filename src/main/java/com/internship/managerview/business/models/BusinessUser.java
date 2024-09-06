package com.internship.managerview.business.models;

import com.internship.managerview.controllers.DTOs.CreateUserDTO;
import com.internship.managerview.controllers.DTOs.UpdateUserDTO;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.enums.Role;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

/**
 * Represents a business user in the system.
 * Includes validation methods for user attributes.
 *
 * @author Anne Butter en Mays AlTimemy
 */
public class BusinessUser {

    private int id;
    private String firstName;
    private String prefixes;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    private String securityQuestion1Id;
    private String securityAnswer1;
    private String securityQuestion2Id;
    private String securityAnswer2;

    /**
     * Regular expression pattern for validating email addresses.
     * This pattern ensures the email starts with alphanumeric characters or special characters like dots, percent, plus, and minus signs.
     * It must contain an '@' symbol followed by more alphanumeric characters including dots and hyphens.
     * Finally, it ends with a dot followed by 2 to 6 alphabetic characters, representing the domain part.
     *
     * @author Mays AlTimemy
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    /**
     * Regular expression pattern for validating passwords.
     * This pattern ensures the password contains:
     * 1. At least one digit.
     * 2. At least one lowercase letter.
     * 3. At least one uppercase letter.
     * 4. At least one special character.
     * 5. No whitespace.
     * 6. A minimum length of 8 characters.
     *
     * @author Mays Altimemy
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W])(?!.*\\s).{8,}$"
    );

    /**
     * Regular expression pattern for validating names.
     * This pattern allows names that:
     * 1. Start and only include alphabetic characters and spaces.
     * 2. Contain between 2 to 255 characters.
     * This accommodates most name formats, including those with multiple parts separated by spaces.
     *
     * @author Mays AlTimemy
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,255}$");
    private static final int MAX_PREFIX_LENGTH = 20;

    /**
     * Constructs a BusinessUser object based on provided CreateUserDTO.
     * Automatically validates the user data during object construction.
     *
     * @param createUserDTO Data Transfer Object containing user data from the controller.
     * @author Mays AlTimemy
     */
    public BusinessUser(CreateUserDTO createUserDTO) {
        this.firstName = createUserDTO.getFirstName();
        this.prefixes = createUserDTO.getPrefixes();
        this.lastName = createUserDTO.getLastName();
        this.email = createUserDTO.getEmail();
        this.password = createUserDTO.getPassword();
        this.role = Role.MEMBER;
        this.securityQuestion1Id = createUserDTO.getSecurityQuestion1Id();
        this.securityAnswer1 = createUserDTO.getSecurityAnswer1();
        this.securityQuestion2Id = createUserDTO.getSecurityQuestion2Id();
        this.securityAnswer2 = createUserDTO.getSecurityAnswer2();
    }

    /**
     * Creates a BusinessUser instance.
     *
     * @param id        The user id
     * @param firstName The user first name
     * @param prefixes  The user prefixes
     * @param lastName  The user last name
     * @param email     The user email
     * @author Anne Butter
     */
    public BusinessUser(int id, String firstName, String prefixes, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.prefixes = prefixes;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Creates a BusinessUser instance.
     *
     * @param id The user id
     * @author Anne Butter
     */
    public BusinessUser(int id) {
        this.id = id;
    }

    /**
     * Creates a BusinessUser instance.
     *
     * @param updateUserDTO The updateUserDTO object containing user data, including first name, prefixes, last name, and email.
     * @author mays Altimemy
     */


    public BusinessUser(UpdateUserDTO updateUserDTO) {

        this.firstName = updateUserDTO.getFirstName();
        this.lastName = updateUserDTO.getLastName();
        this.email = updateUserDTO.getEmail();
        this.role = updateUserDTO.getRole();
        this.prefixes = updateUserDTO.getPrefixes();
    }


    /**
     * Validates the updated user information.
     *
     * @throws InvalidInputException If any validation fails, an exception is thrown with the corresponding ErrorCode.
     * @author Mays Altimemy
     */
    public void validateToUpdate() throws InvalidInputException {
        validateField(firstName, NAME_PATTERN, ErrorCode.U_INVALID_LENGTH_FIRST_NAME);
        validateField(lastName, NAME_PATTERN, ErrorCode.U_INVALID_LENGTH_LAST_NAME);
        validateEmail();
        validatePrefixes();
        validateRole();
    }

    /**
     * Validates the role of the user.
     *
     * @throws InvalidInputException If the role is null or does not match the expected roles, an exception is thrown with the ErrorCode.INVALID_ROLE.
     * @author Mays AlTimemy
     */
    private void validateRole() throws InvalidInputException {
        if (role == null || (role != Role.MANAGER && role != Role.MEMBER)) {
            throw new InvalidInputException(ErrorCode.U_INVALID_ROLE);
        }
    }

    /**
     * Validates all fields of the BusinessUser object.
     * Throws InvalidInputException if any validation fails.
     *
     * @author Mays AlTimemy
     */
    public void validate() {
        validateField(firstName, NAME_PATTERN, ErrorCode.U_INVALID_LENGTH_FIRST_NAME);
        validateField(lastName, NAME_PATTERN, ErrorCode.U_INVALID_LENGTH_LAST_NAME);
        validateEmail();
        validatePassword();
        validatePrefixes();
        validateSecurityAnswers();
    }

    /**
     * Validates a specific field against a given pattern.
     * Throws InvalidInputException with a specific ErrorCode if validation fails.
     *
     * @param field     The string field to validate.
     * @param pattern   The regex pattern the field should match.
     * @param errorCode The error code to use if validation fails.
     * @author Mays AlTimemy
     */
    private void validateField(String field, Pattern pattern, ErrorCode errorCode) {
        if (field == null || !pattern.matcher(field).matches()) {
            throw new InvalidInputException(errorCode);
        }
    }

    /**
     * Validates the prefixes field, ensuring it does not exceed the maximum length.
     * Throws InvalidInputException with ErrorCode.INVALID_PREFIX if validation fails.
     *
     * @author Mays AlTimemy
     */
    private void validatePrefixes() {
        if (prefixes != null && prefixes.length() > MAX_PREFIX_LENGTH) {
            throw new InvalidInputException(ErrorCode.U_INVALID_LENGTH_PREFIX);
        }
    }

    /**
     * Validates the email field against a standard email pattern.
     * Throws InvalidInputException with ErrorCode.INVALID_EMAIL if validation fails.
     *
     * @author Mays AlTimemy
     */
    private void validateEmail() {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException(ErrorCode.U_INVALID_EMAIL);
        }
    }

    /**
     * Validates the password field against a complexity requirement pattern.
     * Throws InvalidInputException with ErrorCode.INVALID_PASSWORD if validation fails.
     *
     * @author Mays AlTimemy
     */
    private void validatePassword() {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidInputException(ErrorCode.U_INVALID_PASSWORD);
        }
    }

    /**
     * Validates the password field against a complexity requirement pattern.
     * Throws InvalidInputException with ErrorCode.INVALID_PASSWORD if validation fails.
     *
     * @author Mays AlTimemy
     */
    private void validatePassword(String newPassword) {
        if (newPassword == null || !PASSWORD_PATTERN.matcher(newPassword).matches()) {
            throw new InvalidInputException(ErrorCode.U_INVALID_PASSWORD);
        }
    }

    /**
     *
     */
    private void validateSecurityAnswers() {
        if (securityAnswer1 == null || securityAnswer1.length() < 2 || securityAnswer1.length() > 255) {
            throw new InvalidInputException(ErrorCode.U_INVALID_SECURITY_ANSWER);
        }
        if (securityAnswer2 == null || securityAnswer2.length() < 2 || securityAnswer2.length() > 255) {
            throw new InvalidInputException(ErrorCode.U_INVALID_SECURITY_ANSWER);
        }
    }


    public void changePassword(String existingPassword, String oldPassword, String newPassword, PasswordEncoder passwordEncoder) throws InvalidInputException {
        if (!passwordEncoder.matches(oldPassword, existingPassword)) {
            throw new InvalidInputException(ErrorCode.U_INCORRECT_PASSWORD);
        }
        if (newPassword.equals(oldPassword)) {
            throw new InvalidInputException(ErrorCode.U_NEW_PASSWORD_EQUAL_TO_OLD);
        }
        validatePassword(newPassword);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPrefixes() {
        return prefixes;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPrefixes(String prefixes) {
        this.prefixes = prefixes;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSecurityQuestion1Id() {
        return securityQuestion1Id;
    }

    public void setSecurityQuestion1Id(String securityQuestion1Id) {
        this.securityQuestion1Id = securityQuestion1Id;
    }

    public String getSecurityAnswer1() {
        return securityAnswer1;
    }

    public void setSecurityAnswer1(String securityAnswer1) {
        this.securityAnswer1 = securityAnswer1;
    }

    public String getSecurityQuestion2Id() {
        return securityQuestion2Id;
    }

    public void setSecurityQuestion2Id(String securityQuestion2Id) {
        this.securityQuestion2Id = securityQuestion2Id;
    }

    public String getSecurityAnswer2() {
        return securityAnswer2;
    }

    public void setSecurityAnswer2(String securityAnswer2) {
        this.securityAnswer2 = securityAnswer2;
    }

}
