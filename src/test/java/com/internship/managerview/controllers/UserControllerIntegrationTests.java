package com.internship.managerview.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.managerview.controllers.DTOs.CreateUserDTO;
import com.internship.managerview.controllers.DTOs.UpdateUserDTO;
import com.internship.managerview.data.entities.User;
import com.internship.managerview.data.repositories.UserRepository;
import com.internship.managerview.testHelpers.GeneralTestHelper;
import com.internship.managerview.testHelpers.UserTestHelper;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.enums.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Saves a User to the database, so it can be used to test its retrieval.
     *
     * @param userRepository The repository necessary for saving a BusinessUser.
     */
    @BeforeAll
    public static void setup(@Autowired UserRepository userRepository) throws JsonProcessingException {
        User user1 = new User(1, "Kees", "van den", "Kaas", "keesvandenkaas@mail.com");
        User user2 = new User(2, "Jaap", null, "Schipper", "jaapschipper@mail.com");
        User user3 = new User(3, "Helmerd", null, "Robin", "helmerdrobin@mail.com");
        User user4 = new User(4, "John", null, "Kerk", "johnkerk@mail.com");
        User user5 = new User(5, "Katie", "van", "Vliet", "katievanvliet@mail.com");
        User user6 = new User(6, "Karel", null, "Keurig", "karelkeurig@mail.com");
        List<User> users = List.of(user1, user2, user3, user4, user5, user6);
        userRepository.saveAll(users);
    }

    @Nested
    @DisplayName("Tests for endpoint: POST /users")
    class PostUserTests {

        /**
         * Test to ensure that attempting to create a user with an email that already exists in the system
         * results in a conflict response. This test posts a new user with an existing email to the /users endpoint
         * and expects a 409 Conflict status, indicating that the email is already used by another user.
         *
         * @author Mays AlTimemy
         */
        @Test
        @DisplayName("Create user with existing email - conflict")
        void whenCreateUserWithExistingEmail_thenReturnsStatus409() throws Exception {
            CreateUserDTO duplicateUser = new CreateUserDTO("Duplicate", "User", "keesvandenkaas@mail.com", "Password123!");
            duplicateUser.setSecurityQuestion1Id("1");
            duplicateUser.setSecurityAnswer1("Answer1");
            duplicateUser.setSecurityQuestion2Id("2");
            duplicateUser.setSecurityAnswer2("Answer2");

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(duplicateUser)))
                    .andExpect(status().isConflict());
        }

        /**
         * Test to ensure that updating an existing user with invalid data results in a bad request response.
         * This test sends a PUT request to the /users/{id} endpoint with invalid data for an existing user
         * and expects a 400 Bad Request status, indicating that the request is invalid.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Mays Altimemy
         */
        @Test
        @DisplayName("Update existing user with invalid data - bad request")
        void whenUpdateExistingUserWithInvalidDataThenReturnsStatus400() throws Exception {
            UpdateUserDTO updateUser = new UpdateUserDTO("", "Timemy", "invalid@email.com", Role.MEMBER, null);
            mockMvc.perform(put("/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateUser)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(ErrorCode.U_INVALID_LENGTH_FIRST_NAME.getCode()));
        }

        /**
         * Test to ensure that updating an existing user with valid data results in a successful update.
         * This test sends a PUT request to the /users/{id} endpoint with valid data for an existing user
         * and expects a 200 OK status, indicating a successful update.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Mays Altimemy
         */
        @Test
        @DisplayName("Update existing user - success")
        void whenUpdateExistingUserThenReturnsStatus200() throws Exception {
            UpdateUserDTO updateUser = new UpdateUserDTO("NewFirstName", "NewLastName", "newemail@example.com", Role.MEMBER, null);
            mockMvc.perform(put("/users/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateUser)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: GET /users/:id")
    public class GetUserByIDTests {

        /**
         * Tests if an existing user can be retrieved from the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Anne Butter
         */
        @Test
        @DisplayName("An existing user can be retrieved from the database.")
        public void getExistingUser() throws Exception {
            mockMvc.perform(get("/users/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(UserTestHelper.getExistingUserResult()));
        }

        /**
         * Tests if errorCode for user not found is returned if a user does not exist in the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Anne Butter
         */
        @Test
        @DisplayName("An errorCode for user not found is returned if a user does not exist in the database.")
        public void getNonexistentUser() throws Exception {
            mockMvc.perform(get("/users/50")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.U_NOT_FOUND)));
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: GET /users?name=<name_input>")
    public class GetUsersByNameTests {

        @BeforeAll
        public static void setup() throws JsonProcessingException {
            UserTestHelper.createUserDTOJsonStrings();
        }

        /**
         * Tests if all users starting with "k" are retrieved in alphabetical order
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Anne Butter
         */
        @Test
        @DisplayName("Users starting with \"k\" are retrieved in alphabetical order.")
        public void getUsersWithK() throws Exception {
            mockMvc.perform(get("/users?name=k")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(UserTestHelper.getResultForParameterK(), true));
        }

        /**
         * Tests if all users starting with "ke" are retrieved in alphabetical order
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Anne Butter
         */
        @Test
        @DisplayName("Users starting with \"ke\" are retrieved in alphabetical order.")
        public void getUsersWithKe() throws Exception {
            mockMvc.perform(get("/users?name=ke")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(UserTestHelper.getResultForParameterKe(), true));

        }

        /**
         * Tests if all users are retrieved in alphabetical order by first name when no name is given.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Anne Butter
         */
        @Test
        @DisplayName("All Users are retrieved in alphabetical order when no name is given.")
        public void getUsersWithNothing() throws Exception {
            mockMvc.perform(get("/users?name=")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(UserTestHelper.getResultForNoParameter(), true));
        }

        /**
         * Tests if no users are retrieved and no exception is thrown if the given name is not found.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Anne Butter
         */
        @Test
        @DisplayName("No Users are retrieved and no exception is thrown if the given name is not found.")
        public void noUsersWithNonexistentName() throws Exception {
            mockMvc.perform(get("/users?name=Ditisgeennaam")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json("[]", true));
        }

        /**
         * Tests if no users are retrieved and no exception is thrown if the given name is not found.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         * @author Anne Butter
         */
        @Test
        @DisplayName("No Users are retrieved and ErrorCode for invalid query character is returned if the name contains an invalid character.")
        public void getUsersWithInvalidCharacter() throws Exception {
            mockMvc.perform(get("/users?name=kees$")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.G_INVALID_QUERY_CHARACTER)));
        }
    }


}
