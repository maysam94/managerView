package com.internship.managerview.controllers.DTOs;

/**
 * Data Transfer Object for user creation requests.
 * Validates the user input data.
 *
 * @author Mays Altimemy
 */
public class CreateUserDTO {

    private String firstName;
    private String prefixes;
    private String lastName;
    private String email;
    private String password;
    private String securityQuestion1Id;
    private String securityAnswer1;
    private String securityQuestion2Id;
    private String securityAnswer2;

    /**
     * Creates a CreateTeamDTO instance.
     *
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user. This should be unique per user.
     * @param password  The password for the user's account.
     * @author Mays Altimemy
     */
    public CreateUserDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     * Empty constructor for CreateUserDTO. Used to initialize an empty CreateUserDTO object.
     *
     * @author Mays AlTimemy
     */
    public CreateUserDTO() {

    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(String prefixes) {
        this.prefixes = prefixes;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
