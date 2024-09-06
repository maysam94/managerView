package com.internship.managerview.controllers;

import com.internship.managerview.data.entities.Image;
import com.internship.managerview.data.entities.Team;
import com.internship.managerview.data.repositories.ImageRepository;
import com.internship.managerview.data.repositories.TeamRepository;
import com.internship.managerview.data.repositories.UserRepository;
import com.internship.managerview.testHelpers.GeneralTestHelper;
import com.internship.managerview.testHelpers.TeamTestHelper;
import com.internship.managerview.util.enums.ErrorCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
public class TeamControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private static final TeamTestHelper teamTestHelper = new TeamTestHelper();

    /**
     * Saves Users and teams to the database, so they can be used in the tests.
     *
     * @param userRepository The repository necessary for saving a User.
     * @param teamRepository The repository necessary for saving a Team.
     */
    @BeforeAll
    public static void setup(@Autowired UserRepository userRepository, @Autowired ImageRepository imageRepository, @Autowired TeamRepository teamRepository) {
        teamTestHelper.fillDatabase(userRepository, imageRepository, teamRepository);
    }

    @Nested
    @DisplayName("Tests for endpoint: Get /teams")
    public class GetTeamsTests {

        /**
         * Tests if all team ids and names can be retrieved from the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("GET: Existing teams can be retrieved from the database.")
        public void getTeams() throws Exception {
            mockMvc.perform(get("/teams")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(teamTestHelper.getGetTeamsResult(), true));
        }

        /**
         * Tests if an empty array is returned if no teams exist in the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @Transactional
        @DisplayName("GET: When no teams exist, an empty array is returned.")
        public void getNoTeams(@Autowired TeamRepository teamRepository) throws Exception {
            teamRepository.deleteAll();
            mockMvc.perform(get("/teams")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().string("[]"));
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: Get /teams/{id}")
    public class GetTeamByIdTests {

        /**
         * Tests if all team ids and names can be retrieved from the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("GET: An existing team can be retrieved from the database by its id.")
        public void getTeamById1() throws Exception {
            String teamId = teamTestHelper.getTeam1Id();
            mockMvc.perform(get("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(teamTestHelper.getTeamById1Result(), true));
        }

        /**
         * Tests if a nonexistent team id returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @Transactional
        @DisplayName("GET: A nonexistent team id gets a response with status 400, errorCode for team not found.")
        public void getNonexistentTeam() throws Exception {
            String teamId = "50";
            mockMvc.perform(get("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NOT_FOUND)));
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: Get /teams/{id}/avatar")
    public class GetTeamAvatarTests {
        /**
         * Tests if an existing team avatar can be retrieved from the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform()
         */
        @Test
        @DisplayName("GET: An existing team avatar can be retrieved from the database by the team id.")
        public void getAvatarByTeamId1() throws Exception {
            String teamId = teamTestHelper.getTeam1Id();
            mockMvc.perform(get("/teams/" + teamId + "/avatar")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(teamTestHelper.getAvatarByTeamId1Result(), true));
        }

        /**
         * Tests if a existing team id with a nonexistent avatar returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform()
         */
        @Test
        @Transactional
        @DisplayName("GET: An existing team with a nonexistent avatar gets a response with status 400, errorCode for avatar not found.")
        public void getNonexistentAvatar() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            mockMvc.perform(get("/teams/" + teamId + "/avatar")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_AVATAR_NOT_FOUND)));
        }

        /**
         * Tests if a nonexistent team id returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform()
         */
        @Test
        @Transactional
        @DisplayName("GET: A nonexistent team id gets a response with status 400, errorCode for team not found.")
        public void getNonexistentTeam() throws Exception {
            String teamId = "50";
            mockMvc.perform(get("/teams/" + teamId + "/avatar")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NOT_FOUND)));
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: Get /teams/{id}/members")
    public class GetTeamMembersTests {
        /**
         * Tests if the members from an existing team can be retrieved from the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform()
         */
        @Test
        @DisplayName("GET: The members from an existing team can be retrieved from the database by the team id.")
        public void getMembersByTeamId1() throws Exception {
            String teamId = teamTestHelper.getTeam1Id();
            mockMvc.perform(get("/teams/" + teamId + "/members")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(teamTestHelper.getMembersByTeamId1Result(), true));
        }

        /**
         * Tests if existing team without any members gets a response with an empty array and status 200.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform()
         */
        @Test
        @Transactional
        @DisplayName("GET: An existing team without any members gets a response with an empty array and status 200.")
        public void getNonexistentAvatar() throws Exception {
            String teamId = teamTestHelper.getTeam4Id();
            mockMvc.perform(get("/teams/" + teamId + "/members")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json("[]", true));
        }

        /**
         * Tests if a nonexistent team id returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform()
         */
        @Test
        @Transactional
        @DisplayName("GET: A nonexistent team id gets a response with status 400, errorCode for team not found.")
        public void getNonexistentTeam() throws Exception {
            String teamId = "50";
            mockMvc.perform(get("/teams/" + teamId + "/members")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NOT_FOUND)));
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: POST /teams")
    public class PostTeamsTests {
        /**
         * Tests if a valid team can be saved to the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A valid team can be saved to the database.")
        public void saveValidTeam(@Autowired TeamRepository teamRepository) throws Exception {
            String requestBody = """
                    {
                        "name": "New team",
                        "members": [1, 2, 3],
                        "createdBy": 1
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isCreated());

            Team newTeam = teamRepository.findByName("New team");
            teamRepository.delete(newTeam);
        }

        /**
         * Tests if a team with an invalid name returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team with an invalid name gets a response with status 400, errorCode for invalid name length.")
        public void invalidName() throws Exception {
            String requestBody = """
                    {
                        "name": "T",
                        "members": [1, 2, 3],
                        "createdBy": 1
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_INVALID_NAME_LENGTH)));
        }

        /**
         * Tests if a team with an invalid member amount returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team with an invalid member amount gets a response with status 400, errorCode for invalid member amount.")
        public void invalidMemberAmount() throws Exception {
            String requestBody = """
                    {
                        "name": "Too many members",
                        "members": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11],
                        "createdBy": 1
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_INVALID_MEMBER_AMOUNT)));
        }

        /**
         * Tests if a team with double members returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team with double member ids gets a response with status 400, errorCode for duplicate member ids.")
        public void doubleMembers() throws Exception {
            String requestBody = """
                    {
                        "name": "Double members",
                        "members": [1, 2, 3, 3],
                        "createdBy": 1
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_DUPLICATE_MEMBER_IDS)));
        }

        /**
         * Tests if a team with a nonexistent member returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team with a nonexistent member gets a response with status 404, errorCode for member not found.")
        public void memberNotFound() throws Exception {
            String requestBody = """
                    {
                        "name": "Nonexistent member",
                        "members": [1, 2, 4],
                        "createdBy": 1
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_MEMBER_NOT_FOUND)));

        }

        /**
         * Tests if a team with a nonexistent creator returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team with a nonexistent creator gets a response with status 404, errorCode for creator not found.")
        public void creatorNotFound() throws Exception {
            String requestBody = """
                    {
                        "name": "Nonexistent creator",
                        "members": [1, 2, 3],
                        "createdBy": 4
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_CREATOR_NOT_FOUND)));
        }

        /**
         * Tests if a team with a name that already exists returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team with a name that already exists gets a response with status 409, errorCode for name already exists.")
        public void nameAlreadyExists() throws Exception {
            String requestBody = """
                    {
                        "name": "Existing team",
                        "members": [1, 2, 3],
                        "createdBy": 4
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isConflict())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NAME_ALREADY_EXISTS)));
        }

        /**
         * Tests if a team with a name that is null returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team with a name that is null gets a response with status 400, errorCode for name is empty.")
        public void nameIsNull() throws Exception {
            String requestBody = """
                    {
                        "name": null,
                        "members": [1, 2, 3],
                        "createdBy": 4
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NAME_IS_EMPTY)));
        }

        /**
         * Tests if a team where members are null returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team where members are null gets a response with status 400, errorCode for member is empty.")
        public void membersIsNull() throws Exception {
            String requestBody = """
                    {
                        "name": "Members is null",
                        "members": null,
                        "createdBy": 4
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_MEMBER_IS_EMPTY)));
        }

        /**
         * Tests if a team where createdBy is null returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("POST: A team where createdBy is null gets a response with status 400, errorCode for creator is empty.")
        public void createdByIsNull() throws Exception {
            String requestBody = """
                    {
                        "name": "CreatedBy is null",
                        "members": [1, 2, 3],
                        "createdBy": null
                    }
                    """;
            mockMvc.perform(post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_CREATOR_IS_EMPTY)));
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: PUT /teams/{id}")
    public class PutTeamTests {
        /**
         * Tests if a valid team can be updated in the database.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @Transactional
        @DisplayName("PUT: A valid team can be updated in the database.")
        public void updateValidTeam() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            String requestBody = """
                    {
                        "name": "Updated team",
                        "members": [1, 2, 3]
                    }
                    """;
            mockMvc.perform(put("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isOk());
        }

        /**
         * Tests if a team with an invalid name returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("PUT: A team with an invalid name gets a response with status 400, errorCode for invalid name length.")
        public void invalidName() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            String requestBody = """
                    {
                        "name": "T",
                        "members": [1, 2, 3]
                    }
                    """;
            mockMvc.perform(put("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_INVALID_NAME_LENGTH)));
        }

        /**
         * Tests if a team with an invalid member amount returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("PUT: A team with an invalid member amount gets a response with status 400, errorCode for invalid member amount.")
        public void invalidMemberAmount() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            String requestBody = """
                    {
                        "name": "Too many members",
                        "members": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
                    }
                    """;
            mockMvc.perform(put("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_INVALID_MEMBER_AMOUNT)));
        }

        /**
         * Tests if a team with double members returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("PUT: A team with double member ids gets a response with status 400, errorCode for duplicate member ids.")
        public void doubleMembers() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            String requestBody = """
                    {
                        "name": "Double members",
                        "members": [1, 2, 3, 3]
                    }
                    """;
            mockMvc.perform(put("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_DUPLICATE_MEMBER_IDS)));
        }

        /**
         * Tests if a team with a nonexistent member returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("PUT: A team with a nonexistent member gets a response with status 404, errorCode for member not found.")
        public void memberNotFound() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            String requestBody = """
                    {
                        "name": "Nonexistent member",
                        "members": [1, 2, 50]
                    }
                    """;
            mockMvc.perform(put("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_MEMBER_NOT_FOUND)));
        }

        /**
         * Tests if a team with a name that already exists returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("PUT: A team with a name that already exists gets a response with status 409, errorCode for name already exists.")
        public void nameAlreadyExists() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            String requestBody = """
                    {
                        "name": "Existing team",
                        "members": [1, 2, 3]
                    }
                    """;
            mockMvc.perform(put("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isConflict())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NAME_ALREADY_EXISTS)));
        }

        /**
         * Tests if a team with an id that does not exist returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("PUT: A team with an id that does not exist gets a response with status 404, errorCode for team not found.")
        public void idDoesNotExists() throws Exception {
            String teamId = "50";
            String requestBody = """
                    {
                        "name": "Id does not exist",
                        "members": [1, 2, 3]
                    }
                    """;
            mockMvc.perform(get("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NOT_FOUND)));
        }

        /**
         * Tests if a team with a name that is null returns the right error response.
         *
         */
        @Test
        @DisplayName("PUT: A team with a name that is null gets a response with status 404, errorCode for name is empty.")
        public void nameIsNull() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            String requestBody = """
                    {
                        "name": null,
                        "members": [1, 2, 3]
                    }
                    """;
            mockMvc.perform(put("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NAME_IS_EMPTY)));
        }

        /**
         * Tests if a team where members are null returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform
         */
        @Test
        @DisplayName("PUT: A team where members are null gets a response with status 404, errorCode for member is empty.")
        public void membersIsNull() throws Exception {
            String teamId = teamTestHelper.getTeam2Id();
            String requestBody = """
                    {
                        "name": "Members are null",
                        "members": null
                    }
                    """;
            mockMvc.perform(put("/teams/" + teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_MEMBER_IS_EMPTY)));
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: POST /teams/{id}/avatar")
    public class AddTeamAvatarTests {

        /**
         * Tests if a valid png or jpg image can be saved to the database and linked to an existing team.
         *
         * @param teamRepository  The repository called to get the existing team
         * @param imageRepository The repository called to check if the image is saved correctly
         * @throws Exception The exception potentially thrown by mockMvc.perform() or TeamTestHelper.getValidJpg()
         *                   will be an IOException if thrown by TeamTestHelper.getValidJpg()
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("validTestDataProvider")
        @Transactional
        public void saveValidImage(String displayName, MockMultipartFile mockRequestImage, @Autowired TeamRepository teamRepository, @Autowired ImageRepository imageRepository) throws Exception {
            String teamId = teamTestHelper.getTeam1Id();

            mockMvc.perform(multipart("/teams/" + teamId + "/avatar").file(mockRequestImage))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            Team team = teamRepository.findById(teamId).get();
            String imageId = team.getImage().getId();

            Optional<Image> optionalSavedImage = imageRepository.findById(imageId);

            assertTrue(optionalSavedImage.isPresent());
        }

        /**
         * Tests if an invalid image returns the right error response.
         *
         * @param teamRepository The repository called to get the existing team
         * @throws Exception The exception potentially thrown by mockMvc.perform() or TeamTestHelper.getValidJpg()
         *                   will be an IOException if thrown by TeamTestHelper.getValidJpg()
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidTestDataProvider")
        @Transactional
        public void invalidImage(String displayName, MockMultipartFile mockRequestImage, ErrorCode errorCode, @Autowired TeamRepository teamRepository) throws Exception {
            String teamId = teamTestHelper.getTeam2Id();

            mockMvc.perform(multipart("/teams/" + teamId + "/avatar").file(mockRequestImage))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(errorCode)));

            Team team = teamRepository.findById(teamId).get();
            assertNull(team.getImage());
        }

        /**
         * Provides input for the saveValidImage test.
         *
         * @return A stream of arguments for the invalidImages test
         * @throws IOException The exception potentially thrown by the TeamTestHelper methods
         */
        private static Stream<Arguments> validTestDataProvider() throws IOException {
            return Stream.of(
                    Arguments.of("POST: A valid jpg image can be saved to the database and linked to an existing team.", teamTestHelper.getValidJpg()),
                    Arguments.of("POST: A valid png image can be saved to the database and linked to an existing team.", teamTestHelper.getValidPng())
            );
        }

        /**
         * Provides input for the invalidImage test.
         *
         * @return A stream of arguments for the invalidImages test
         * @throws IOException The exception potentially thrown by the TeamTestHelper methods
         */
        private static Stream<Arguments> invalidTestDataProvider() throws IOException {
            return Stream.of(
                    Arguments.of("POST: A rectangular jpg image gets a response with status 400, errorCode for invalid dimensions.", teamTestHelper.getRectangularJpg(), ErrorCode.T_INVALID_DIMENSIONS),
                    Arguments.of("POST: An image that is larger than 2MB gets a response with status 400, errorCode for invalid size.", teamTestHelper.getTooLargeJpg(), ErrorCode.T_INVALID_SIZE),
                    Arguments.of("POST: A text file gets a response with status 400, errorCode for invalid extension.", teamTestHelper.getTextFile(), ErrorCode.T_INVALID_EXTENSION),
                    Arguments.of("POST: A text file with an image MimeType gets a response with status 400, errorCode for invalid extension.", teamTestHelper.getTextFileWithImageMimeType(), ErrorCode.T_INVALID_EXTENSION)
            );
        }
    }

    @Nested
    @DisplayName("Tests for endpoint: PUT /teams/{id}/status")
    public class UpdateTeamStatusTests {
        /**
         * Tests if the status of an existing team can be updated in the database.
         *
         * @param teamRepository The repository necesary to check the team status
         * @throws Exception The exception potentially thrown by mockMvc.perform()
         */
        @Test
        @Transactional
        @DisplayName("PUT: The status of an existing team can be updated in the database.")
        public void updateTeamStatus(@Autowired TeamRepository teamRepository) throws Exception {
            String teamId = teamTestHelper.getTeam1Id();
            mockMvc.perform(put("/teams/" + teamId + "/status"))
                    .andExpect(status().isOk());

            Team inactiveTeam = teamRepository.findById(teamId).get();
            assertFalse(inactiveTeam.getIsActive());

            mockMvc.perform(put("/teams/" + teamId + "/status"))
                    .andExpect(status().isOk());

            Team activeTeam = teamRepository.findById(teamId).get();
            assertTrue(activeTeam.getIsActive());
        }

        /**
         * Tests if a nonexistent team returns the right error response.
         *
         * @throws Exception The exception potentially thrown by mockMvc.perform()
         */
        @Test
        @DisplayName("PUT: A nonexistent team id gets a response with status 404, errorCode for team not found.")
        public void updateValidTeam() throws Exception {
            String teamId = "50";
            mockMvc.perform(put("/teams/" + teamId + "/status"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(GeneralTestHelper.getErrorResponse(ErrorCode.T_NOT_FOUND)));
        }
    }
}
