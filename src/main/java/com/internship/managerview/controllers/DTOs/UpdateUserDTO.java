package com.internship.managerview.controllers.DTOs;

import com.internship.managerview.util.enums.Role;

public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String prefixes;
    private String email;
    private Role role;

    /**
     * Constructs a new UpdateUserDTO with the specified parameters.
     *
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user.
     * @param role      The role of the user.
     * @author Mays AlTimemy
     */
    public UpdateUserDTO(String firstName, String lastName, String email, Role role, String prefixes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.prefixes = prefixes;
        this.email = email;
        this.role = role;
    }


    // Getters en setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(String prefixes) {
        this.prefixes = prefixes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
