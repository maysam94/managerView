package com.internship.managerview.business.services;

import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.business.models.StringValidator;
import com.internship.managerview.controllers.DTOs.CreateUserDTO;
import com.internship.managerview.controllers.DTOs.UpdateUserDTO;
import com.internship.managerview.data.models.UserModel;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.DataRowNotFoundException;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Provides services related to user management such as creating and retrieving users.
 * Utilizes BusinessUser for business logic and validation, UserRepository for data persistence,
 * and UserModel for data retrieval and conversion.
 *
 * @author Anne Butter en Mays AlTimemy
 */
@Service
public class UserService {
    @Autowired
    private UserModel userModel;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a user based on the provided CreateUserDTO.
     * Validates the user data, encodes the password, and handles the persistence through UserModel.
     *
     * @param createUserDTO DTO containing data for the new user.
     */
    public void createUser(CreateUserDTO createUserDTO) {
        BusinessUser businessUser = new BusinessUser(createUserDTO);
        businessUser.validate();
        businessUser.setPassword(passwordEncoder.encode(businessUser.getPassword()));
        userModel.createUser(businessUser);
    }

    /**
     * Gets the requested user by their id.
     *
     * @param id The id of the requested user
     * @return The requested user
     * @throws DataRowNotFoundException when a user with the given id is not found in the database
     */
    public BusinessUser getUserById(int id) throws DataRowNotFoundException {
        return userModel.getUserById(id);
    }

    /**
     * Checks the name for non-alphanumeric characters and gets the requested users by their first name or last name.
     *
     * @param name The name of the requested users
     * @return The requested users
     * @throws InvalidInputException when the name contains a non-numeric character
     */
    public List<BusinessUser> getUsersByName(String name) throws InvalidInputException {
        if (StringValidator.containsNonAlphanumeric(name)) {
            throw new InvalidInputException(ErrorCode.G_INVALID_QUERY_CHARACTER);
        }
        name = name.replaceAll("\\s+", " ");
        return userModel.getUsersByName(name);
    }

    /**
     * Updates a user with the given ID using the information provided in the UpdateUserDTO object.
     *
     * @param id            The ID of the user to update.
     * @param updateUserDTO The DTO containing the updated information for the user.
     * @throws InvalidInputException    If the input provided in the updateUserDTO is invalid.
     * @throws DataRowNotFoundException If the user with the given ID is not found in the database.
     * @author Mays AlTimemy
     */
    public void updateUser(int id, UpdateUserDTO updateUserDTO) throws InvalidInputException, DataRowNotFoundException {
        BusinessUser updatedBusinessUser = new BusinessUser(updateUserDTO);
        updatedBusinessUser.validateToUpdate();
        userModel.updateUser(id, updatedBusinessUser);
    }

    /**
     *
     */
    @Transactional
    public void changePassword(int id, String oldPassword, String newPassword) throws InvalidInputException, DataRowNotFoundException {
        BusinessUser user = userModel.getUserById(id);
        String existingPassword = userModel.getPasswordById(id);
        user.changePassword(existingPassword, oldPassword, newPassword, passwordEncoder);


        String encodedPassword = passwordEncoder.encode(newPassword);
        userModel.changePassword(id, encodedPassword);
    }
}
