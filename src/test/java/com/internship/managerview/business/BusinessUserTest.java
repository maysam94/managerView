package com.internship.managerview.business;

import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.controllers.DTOs.CreateUserDTO;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BusinessUser validations.
 * Validates the `validate` method within the BusinessUser class,
 * focusing on email, first name, last name, password, and security answers.
 *
 * @author Mays Altimemy
 */
class BusinessUserTest {

    /**
     * Tests valid email with an invalid email throws InvalidInputException.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid email throws InvalidInputException")
    void validateBusinessUser_InvalidEmail_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setEmail("invalidemail");
        BusinessUser businessUser = new BusinessUser(createUserDTO);

        InvalidInputException thrown = assertThrows(InvalidInputException.class, businessUser::validate);
        assertEquals(ErrorCode.U_INVALID_EMAIL.getCode(), thrown.getErrorCode().getCode());

        assertThrows(InvalidInputException.class, () -> {
            createUserDTO.setEmail("@mail.com");
            new BusinessUser(createUserDTO).validate();
        });
        assertThrows(InvalidInputException.class, () -> {
            createUserDTO.setEmail("keeskaasmail.com");
            new BusinessUser(createUserDTO).validate();
        });
        assertThrows(InvalidInputException.class, () -> {
            createUserDTO.setEmail("keeskaas@.com");
            new BusinessUser(createUserDTO).validate();
        });
        assertThrows(InvalidInputException.class, () -> {
            createUserDTO.setEmail("keeskaas@mailcom");
            new BusinessUser(createUserDTO).validate();
        });
        assertThrows(InvalidInputException.class, () -> {
            createUserDTO.setEmail("keeskaas@mail.");
            new BusinessUser(createUserDTO).validate();
        });
    }

    /**
     * Tests valid email with a valid email does not throw any exception.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with valid email does not throw exception")
    void validateBusinessUser_ValidEmail_DoesNotThrowException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setEmail("test@example.com");

        assertDoesNotThrow(() -> new BusinessUser(createUserDTO).validate());
    }

    /**
     * Tests invalid firstName  with an invalid first name throws InvalidInputException.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid firstName throws InvalidInputException")
    void validateBusinessUser_InvalidFirstName_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setFirstName("a");
        BusinessUser businessUser = new BusinessUser(createUserDTO);

        InvalidInputException thrown = assertThrows(InvalidInputException.class, businessUser::validate);
        assertEquals(ErrorCode.U_INVALID_LENGTH_FIRST_NAME, thrown.getErrorCode());

        assertThrows(InvalidInputException.class, () -> {
            createUserDTO.setFirstName("");
            new BusinessUser(createUserDTO).validate();
        });
        assertThrows(InvalidInputException.class, () -> {
            createUserDTO.setFirstName(" ");
            new BusinessUser(createUserDTO).validate();
        });
    }

    /**
     * Tests invalid lastName with an invalid last name throws InvalidInputException.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid lastName throws InvalidInputException")
    void validateBusinessUser_InvalidLastName_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setLastName("");
        BusinessUser businessUser = new BusinessUser(createUserDTO);

        InvalidInputException thrown = assertThrows(InvalidInputException.class, businessUser::validate);
        assertEquals(ErrorCode.U_INVALID_LENGTH_LAST_NAME, thrown.getErrorCode());
    }

    /**
     * Tests invalid password of invalid length throws an InvalidInputException.
     * The password must meet the length requirement defined in the BusinessUser validation logic.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid password length throws InvalidInputException")
    void passwordWithInvalidLength_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setPassword("short");

        InvalidInputException thrown = assertThrows(InvalidInputException.class,
                () -> new BusinessUser(createUserDTO).validate());

        assertEquals(ErrorCode.U_INVALID_PASSWORD, thrown.getErrorCode());
    }

    /**
     * Tests invalid password without an uppercase letter in the password throws an InvalidInputException.
     * The password must include at least one uppercase letter as per the validation logic.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid password uppercase throws InvalidInputException")
    void passwordWithoutUppercaseLetter_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setPassword("nouppercase123!");

        InvalidInputException thrown = assertThrows(InvalidInputException.class,
                () -> new BusinessUser(createUserDTO).validate());

        assertEquals(ErrorCode.U_INVALID_PASSWORD, thrown.getErrorCode());
    }

    /**
     * Tests invalid password without a lowercase letter in the password throws an InvalidInputException.
     * The password must include at least one lowercase letter according to the validation rules.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid password lowercase throws InvalidInputException")
    void passwordWithoutLowercaseLetter_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setPassword("NOLOWERCASE123!");

        InvalidInputException thrown = assertThrows(InvalidInputException.class,
                () -> new BusinessUser(createUserDTO).validate());

        assertEquals(ErrorCode.U_INVALID_PASSWORD, thrown.getErrorCode());
    }

    /**
     * Tests invalid password without a digit in the password throws an InvalidInputException.
     * According to the BusinessUser's password validation logic, at least one digit is required.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid password digit throws InvalidInputException")
    void passwordWithoutDigit_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setPassword("NoDigitHere!");

        InvalidInputException thrown = assertThrows(InvalidInputException.class,
                () -> new BusinessUser(createUserDTO).validate());

        assertEquals(ErrorCode.U_INVALID_PASSWORD, thrown.getErrorCode());
    }

    /**
     * Tests invalid password without a special character in the password throws an InvalidInputException.
     * The validation logic requires the presence of at least one special character in the password.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid password special char throws InvalidInputException")
    void passwordWithoutSpecialCharacter_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setPassword("NoSpecialCharacter123");

        InvalidInputException thrown = assertThrows(InvalidInputException.class,
                () -> new BusinessUser(createUserDTO).validate());

        assertEquals(ErrorCode.U_INVALID_PASSWORD, thrown.getErrorCode());
    }

    /**
     * Tests invalid password with whitespace in the password throws an InvalidInputException.
     * The password validation logic disallows any whitespace characters within the password.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid password whitespace throws InvalidInputException")
    void passwordWithWhitespace_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setPassword("Invalid Password123!");

        BusinessUser businessUser = new BusinessUser(createUserDTO);

        InvalidInputException thrown = assertThrows(InvalidInputException.class, businessUser::validate);
        assertEquals(ErrorCode.U_INVALID_PASSWORD, thrown.getErrorCode());
    }

    /**
     * Tests invalid security answer with an invalid security answer throws InvalidInputException.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with invalid security answer throws InvalidInputException")
    void validateBusinessUser_InvalidSecurityAnswer_ThrowsInvalidInputException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setSecurityQuestion1Id("1");
        createUserDTO.setSecurityAnswer1("");
        createUserDTO.setSecurityQuestion2Id("2");
        createUserDTO.setSecurityAnswer2("Answer2");
        BusinessUser businessUser = new BusinessUser(createUserDTO);

        InvalidInputException thrown = assertThrows(InvalidInputException.class, businessUser::validate);
        assertEquals(ErrorCode.U_INVALID_SECURITY_ANSWER.getCode(), thrown.getErrorCode().getCode());

        assertThrows(InvalidInputException.class, () -> {
            createUserDTO.setSecurityAnswer1(null);
            new BusinessUser(createUserDTO).validate();
        });
    }

    /**
     * Tests valid security question and answer does not throw any exception.
     *
     * @author Mays Altimemy
     */
    @Test
    @DisplayName("Validate with valid security question and answer does not throw exception")
    void validateBusinessUser_ValidSecurityQuestionAndAnswer_DoesNotThrowException() {
        CreateUserDTO createUserDTO = createValidCreateUserDTO();
        createUserDTO.setSecurityQuestion1Id("1");
        createUserDTO.setSecurityAnswer1("Answer1");
        createUserDTO.setSecurityQuestion2Id("2");
        createUserDTO.setSecurityAnswer2("Answer2");

        assertDoesNotThrow(() -> new BusinessUser(createUserDTO).validate());
    }

    /**
     * Helper method to create a valid CreateUserDTO object for test purposes.
     *
     * @return A CreateUserDTO filled with valid sample data.
     * @author Mays Altimemy
     */
    private CreateUserDTO createValidCreateUserDTO() {
        CreateUserDTO createUserDTO = new CreateUserDTO("ValidFirstName", "ValidLastName", "valid@example.com", "ValidPass1$");
        createUserDTO.setSecurityQuestion1Id("1");
        createUserDTO.setSecurityAnswer1("Answer1");
        createUserDTO.setSecurityQuestion2Id("2");
        createUserDTO.setSecurityAnswer2("Answer2");
        return createUserDTO;
    }
}
