package com.internship.managerview.data.models;

import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.data.entities.SecurityAnswer;
import com.internship.managerview.data.entities.User;
import com.internship.managerview.data.models.mappers.EntityToBusinessModelMapper;
import com.internship.managerview.data.repositories.SecurityAnswerRepository;
import com.internship.managerview.data.repositories.SecurityQuestionRepository;
import com.internship.managerview.data.repositories.UserRepository;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.DataRowNotFoundException;
import com.internship.managerview.util.exceptionHandling.exceptions.EntityAlreadyExistsException;
import com.internship.managerview.util.exceptionHandling.exceptions.GenericRegistrationException;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserModel {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("entityToBusinessModel")
    EntityToBusinessModelMapper businessModelMapper;

    @Autowired
    private SecurityQuestionRepository securityQuestionRepository;

    @Autowired
    private SecurityAnswerRepository securityAnswerRepository;


    /**
     * Saves a User object to the database.
     *
     * @param businessUser The User object to be saved.
     * @author Mays AlTimemy
     */
    public void createUser(BusinessUser businessUser) {
        User userEntity = new User(businessUser);
        if (userRepository.existsByEmail(businessUser.getEmail())) {
            throw new GenericRegistrationException(ErrorCode.U_EMAIL_ALREADY_EXISTS);
        }
        userRepository.save(userEntity);
        saveSecurityAnswers(userEntity, businessUser);
    }

    private void saveSecurityAnswers(User userEntity, BusinessUser businessUser) throws GenericRegistrationException {
        saveSecurityAnswer(userEntity, businessUser.getSecurityQuestion1Id(), businessUser.getSecurityAnswer1());
        saveSecurityAnswer(userEntity, businessUser.getSecurityQuestion2Id(), businessUser.getSecurityAnswer2());
    }

    private void saveSecurityAnswer(User userEntity, String questionId, String answer) throws DataRowNotFoundException, GenericRegistrationException {
        SecurityAnswer securityAnswer = new SecurityAnswer();
        securityAnswer.setUser(userEntity);
        securityAnswer.setQuestion(securityQuestionRepository.findById(questionId)
                .orElseThrow(() -> new DataRowNotFoundException(ErrorCode.U_NOT_FOUND)));
        securityAnswer.setAnswer(answer);
        securityAnswerRepository.save(securityAnswer);
    }

    /**
     * Gets the requested user from the database and converts it to a BusinessUser.
     *
     * @param id The id of the requested user
     * @return The data of the requested user, converted to a business BusinessUser
     * @throws DataRowNotFoundException when a user with the given id is not found in the database
     */
    public BusinessUser getUserById(int id) throws DataRowNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User savedUser = user.get();
            return businessModelMapper.mapToBusinessUser(savedUser);
        } else {
            throw new DataRowNotFoundException(ErrorCode.U_NOT_FOUND);
        }
    }


    /**
     * Gets the requested users from the database and converts them to BusinessUsers.
     *
     * @param name The name of the requested users
     * @return The data of the requested users, converted to business users
     */
    public List<BusinessUser> getUsersByName(String name) {
        Iterable<User> users = userRepository.findAllUsersByName(name);
        return businessModelMapper.mapToBusinessUsers(users);
    }

    /**
     * Updates an existing user with the provided information.
     *
     * @param id         The ID of the user to be updated
     * @param updateUser The updated user information
     * @throws EntityExistsException    If the updated email already exists for another user
     * @throws DataRowNotFoundException If the user with the provided ID is not found
     * @author Mays altimemy
     */
    public void updateUser(int id, BusinessUser updateUser) throws EntityExistsException, DataRowNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User savedUser = optionalUser.get();
            if (!savedUser.getEmail().equals(updateUser.getEmail()) && userRepository.existsByEmail(updateUser.getEmail())) {
                throw new EntityAlreadyExistsException(ErrorCode.U_EMAIL_ALREADY_EXISTS);
            }
            savedUser.update(updateUser);
            userRepository.save(savedUser);
        } else {
            throw new DataRowNotFoundException(ErrorCode.U_NOT_FOUND);
        }
    }

    @Transactional
    public void changePassword(int id, String newPassword) {
        userRepository.updatePassword(id, newPassword);
    }

    public String getPasswordById(int id) throws DataRowNotFoundException {
        String password = userRepository.findPasswordById(id);
        if (password == null) {
            throw new DataRowNotFoundException(ErrorCode.U_NOT_FOUND);
        }
        return password;
    }
}
