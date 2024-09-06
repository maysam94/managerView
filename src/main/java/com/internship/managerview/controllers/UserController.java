package com.internship.managerview.controllers;

import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.business.services.UserService;
import com.internship.managerview.controllers.DTOs.*;
import com.internship.managerview.controllers.DTOs.CreateUserDTO;
import com.internship.managerview.controllers.DTOs.UpdateUserDTO;
import com.internship.managerview.controllers.DTOs.UserByIdDTO;
import com.internship.managerview.controllers.DTOs.UserNameAndIdDTO;
import com.internship.managerview.controllers.mappers.BusinessModelToDTOMapper;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.ErrorResponse;
import com.internship.managerview.util.exceptionHandling.exceptions.DataRowNotFoundException;
import com.internship.managerview.util.exceptionHandling.exceptions.EntityAlreadyExistsException;
import com.internship.managerview.util.exceptionHandling.exceptions.GenericRegistrationException;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("businessModelToDTO")
    private BusinessModelToDTOMapper dtoMapper;

    /**
     * Maps the POST /users request to create a new user.
     * Validates the user data and creates the user if valid.
     *
     * @param createUserDTO The user data to be validated and created.
     * @return A ResponseEntity with a success message or error message and a corresponding status code.
     * @author Mays Altimemy
     */
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody CreateUserDTO createUserDTO) {
        userService.createUser(createUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates an existing user's data based on the given ID. This method handles the request to update user details
     * using data provided in the UpdateUserDTO object. It performs validation checks and updates the user if valid data is provided.
     *
     * @param id            The ID of the user to be updated. This ID should correspond to an existing user in the database.
     * @param updateUserDTO The data transfer object containing the updated information, including fields such as name, email, etc.
     * @return A ResponseEntity representing the outcome of the operation:
     *         - Returns OK (HttpStatus.OK / 200) status if the update is successful, indicating that the user data was successfully updated.
     *         - Returns BAD_REQUEST (HttpStatus.BAD_REQUEST / 400) if validation fails, indicating that the input data was incorrect or insufficient.
     *         - Returns NOT_FOUND (HttpStatus.NOT_FOUND / 404) if no user with the provided ID can be found, indicating that the operation cannot proceed.
     *         - Returns INTERNAL_SERVER_ERROR (HttpStatus.INTERNAL_SERVER_ERROR / 500) for any other exceptions that may occur during the operation, indicating an unexpected server-side error.
     * @author Mays Altimemy
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> updateUser(@PathVariable("id") int id, @RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUser(id, updateUserDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Object> changePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, @PathVariable Integer id) {
        userService.changePassword(id, updatePasswordDTO.getOldPassword(), updatePasswordDTO.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Maps the GET /users/:id request and returns a UserByIdDTO in the response.
     *
     * @param id The id of the requested user
     * @return A ResponseEntity with a UserByIdDTO and status 200
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") int id) {
        BusinessUser businessUser = userService.getUserById(id);
        UserByIdDTO userDetails = dtoMapper.mapToUserByIdDTO(businessUser);
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    /**
     * Maps the GET /users?name=<name_input> request and returns a UserNameAndIdDTO list in the response.
     *
     * @param name The name of the requested users
     * @return A ResponseEntity with a UserNameAndIdDTO list and status 200
     */
    @GetMapping()
    public ResponseEntity<Object> getUsersByName(@RequestParam String name) {
        List<BusinessUser> businessUsers = userService.getUsersByName(name);
        List<UserNameAndIdDTO> userNameAndIdDTOs = dtoMapper.mapToUserNameAndIdDTOs(businessUsers);
        return new ResponseEntity<>(userNameAndIdDTOs, HttpStatus.OK);
    }


    /**
     * Handler for InvalidInputExceptions
     *
     * @param exception The exception containing the error code
     * @return A ResponseEntity with status 400 and the exception's error code.
     */
    @ExceptionHandler(InvalidInputException.class)
    private ResponseEntity<Object> getInvalidInputResponse(InvalidInputException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getErrorCode().getCode()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for DataRowNotFoundExceptions
     *
     * @param exception The exception containing the error code
     * @return A ResponseEntity with status 404 and the exception's error code.
     */
    @ExceptionHandler(DataRowNotFoundException.class)
    private ResponseEntity<Object> getDataRowNotFoundResponse(DataRowNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getErrorCode().getCode()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handler for EntityAlreadyExistsExceptions
     *
     * @param exception The exception containing the error code
     * @return A ResponseEntity with status 409 and the exception's error code.
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    private ResponseEntity<Object> getEntityAlreadyExistsResponse(EntityAlreadyExistsException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getErrorCode().getCode()), HttpStatus.CONFLICT);
    }

    /**
     * Handler for GenericRegistrationExceptions
     *
     * @param exception The exception containing the error code
     * @return A ResponseEntity with status 409 and the exception's error code.
     */
    @ExceptionHandler(GenericRegistrationException.class)
    private ResponseEntity<Object> getGenericRegistrationResponse(GenericRegistrationException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getErrorCode().getCode()), HttpStatus.CONFLICT);
    }

    /**
     * Handler for any other exception
     *
     * @return A ResponseEntity with status 500 and the exception's error code.
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> getGenericErrorResponse() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.G_GENERIC_ERROR.getCode()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
