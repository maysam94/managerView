package com.internship.managerview.data.entities;

import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.util.enums.Role;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String firstName;
    private String prefixes;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String password;


    @ManyToMany(mappedBy = "members")
    public Collection<Team> teams;

    /**
     * Creates a User entity instance.
     *
     * @author Anne Butter
     */
    public User() {

    }

    /**
     * Creates a User entity instance.
     *
     * @param id The user id
     * @author Anne Butter
     */
    public User(int id) {
        this.id = id;
    }

    /**
     * Creates a User entity instance.
     *
     * @param id        The user id
     * @param firstName The user first name
     * @param prefixes  The user prefixes
     * @param lastName  The user last name
     * @param email     The user email
     * @author Anne Butter
     */
    public User(int id, String firstName, String prefixes, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.prefixes = prefixes;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Creates a User entity instance.
     *
     * @param firstName The user first name
     * @param prefixes  The user prefixes
     * @param lastName  The user last name
     * @param email     The user email
     * @author Anne Butter
     */
    public User(String firstName, String prefixes, String lastName, String email) {
        this.firstName = firstName;
        this.prefixes = prefixes;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Constructs a User entity instance from a BusinessUser object.
     * This constructor is useful for transforming business layer users into entity layer users,
     * commonly used when persisting user data in the database. It initializes the User entity with
     * information from the BusinessUser, setting the role to MANAGER by default.
     *
     * @param businessUser The BusinessUser object containing user data from the business layer.
     * @author Mays ATimmemy
     */

    public User(BusinessUser businessUser) {
        this.firstName = businessUser.getFirstName();
        this.prefixes = businessUser.getPrefixes();
        this.lastName = businessUser.getLastName();
        this.email = businessUser.getEmail();
        this.password = businessUser.getPassword();
        this.role = Role.MEMBER;
    }

    /**
     * Updates the user information with the provided BusinessUser.
     *
     * @param updateUser The BusinessUser containing the updated information
     */
    public void update(BusinessUser updateUser) {
        this.setFirstName(updateUser.getFirstName());
        this.setLastName(updateUser.getLastName());
        this.setPrefixes(updateUser.getPrefixes());
        this.setEmail(updateUser.getEmail());
        this.setRole(updateUser.getRole());
        this.setPassword(updateUser.getPassword());
    }

    public int getId() {
        return id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public void setId(long id) {
        this.id = (int) id;
    }

}