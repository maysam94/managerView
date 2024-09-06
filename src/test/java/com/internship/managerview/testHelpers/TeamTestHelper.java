package com.internship.managerview.testHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.managerview.controllers.DTOs.TeamAvatarDTO;
import com.internship.managerview.controllers.DTOs.TeamDetailsDTO;
import com.internship.managerview.controllers.DTOs.TeamListElementDTO;
import com.internship.managerview.controllers.DTOs.UserNameAndIdDTO;
import com.internship.managerview.data.entities.Image;
import com.internship.managerview.data.entities.Team;
import com.internship.managerview.data.entities.User;
import com.internship.managerview.data.repositories.ImageRepository;
import com.internship.managerview.data.repositories.TeamRepository;
import com.internship.managerview.data.repositories.UserRepository;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeamTestHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Date testDate = new Date();

    private Team team1;
    private Team team2;
    private Team team3;
    private Team team4;
    private Team team5;

    private Image image1;
    private Image image2;
    private Image image3;

    private User user1;
    private User user2;
    private User user3;
    private List<User> users;

    public TeamTestHelper() {
    }

    /**
     * Saves Users, Images and teams to the database, so they can be used in tests.
     *
     * @param userRepository  The repository necessary for saving a User.
     * @param imageRepository The repository necessary for saving an Image.
     * @param teamRepository  The repository necessary for saving a Team.
     */
    public void fillDatabase(UserRepository userRepository, ImageRepository imageRepository, TeamRepository teamRepository) {
        addUsersToDatabase(userRepository);
        addImagesToDatabase(imageRepository);
        addTeamsToDatabase(teamRepository);
    }

    /**
     * Saves Users to the database, so they can be used in tests. Sets the user attributes of this class.
     *
     * @param userRepository The repository necessary for saving a User.
     */
    private void addUsersToDatabase(UserRepository userRepository) {
        user1 = userRepository.save(new User("Kees", "van den", "Kaas", "keesvandenkaas@mail.com"));
        user2 = userRepository.save(new User("Jaap", null, "Schipper", "jaapschipper@mail.com"));
        user3 = userRepository.save(new User("Helmerd", null, "Robin", "helmerdrobin@mail.com"));
        users = List.of(user1, user2, user3);
    }

    /**
     * Saves Images to the database, so they can be used in tests. Sets the image attributes of this class.
     *
     * @param imageRepository The repository necessary for saving an Image.
     */
    private void addImagesToDatabase(ImageRepository imageRepository) {
        Image savableImage = new Image("image/png", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        image1 = imageRepository.save(savableImage);
        image2 = imageRepository.save(savableImage);
        image3 = imageRepository.save(savableImage);
    }

    /**
     * Saves Teams to the database, so they can be used in tests. Sets the team attributes of this class.
     *
     * @param teamRepository The repository necessary for saving a Team.
     */
    private void addTeamsToDatabase(TeamRepository teamRepository) {
        team1 = teamRepository.save(new Team("Existing team", testDate, user1, users, image1));
        team2 = teamRepository.save(new Team("BugBytes", testDate, user1, users));
        team3 = teamRepository.save(new Team("Div Dev F5", testDate, user1, users, image2));
        team4 = teamRepository.save(new Team("Badman", testDate, user1, null));
        team5 = teamRepository.save(new Team("Frontend Avenue Girls", testDate, user1, users, image3));
    }

    /**
     * Gets the JSON object as a string that should be the result of TeamControllerIntegrationTests.getTeams()
     *
     * @return A string containing the necessary JSON object
     */
    public String getGetTeamsResult() throws JsonProcessingException {
        TeamAvatarDTO teamAvatarDTO = new TeamAvatarDTO("image/png", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        String team1DTO = objectMapper.writeValueAsString(new TeamListElementDTO(team1.getId(), "Existing team"));
        String team2DTO = objectMapper.writeValueAsString(new TeamListElementDTO(team2.getId(), "BugBytes"));
        String team3DTO = objectMapper.writeValueAsString(new TeamListElementDTO(team3.getId(), "Div Dev F5"));
        String team4DTO = objectMapper.writeValueAsString(new TeamListElementDTO(team4.getId(), "Badman"));
        String team5DTO = objectMapper.writeValueAsString(new TeamListElementDTO(team5.getId(), "Frontend Avenue Girls"));
        List<String> teamList = List.of(team4DTO, team2DTO, team3DTO, team1DTO, team5DTO);
        return teamList.toString();
    }

    /**
     * Gets the JSON object as a string that should be the result of TeamControllerIntegrationTests.getTeamById1()
     *
     * @return A string containing the expected TeamDetailsDto
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public String getTeamById1Result() throws JsonProcessingException {
        ArrayList<UserNameAndIdDTO> members = new ArrayList<>();
        members.add(new UserNameAndIdDTO(user3.getId(), user3.getFirstName(), user3.getPrefixes(), user3.getLastName()));
        members.add(new UserNameAndIdDTO(user2.getId(), user2.getFirstName(), user2.getPrefixes(), user2.getLastName()));
        members.add(new UserNameAndIdDTO(user1.getId(), user1.getFirstName(), user1.getPrefixes(), user1.getLastName()));
        TeamDetailsDTO team = new TeamDetailsDTO("Existing team", members, testDate);
        return objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")).writeValueAsString(team);
    }

    /**
     * Gets the JSON object as a string that should be the result of GetTeamAvatarTests.getAvatarByTeamId1()
     *
     * @return A string containing the necessary JSON object
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public String getAvatarByTeamId1Result() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new TeamAvatarDTO("image/png", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
    }

    /**
     * Gets the JSON object as a string that should be the result of GetTeamMembersTests.getMembersByTeamId1()
     *
     * @return A string containing the necessary JSON object
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public String getMembersByTeamId1Result() throws JsonProcessingException {
        ArrayList<UserNameAndIdDTO> members = new ArrayList<>();
        members.add(new UserNameAndIdDTO(user3.getId(), user3.getFirstName(), user3.getPrefixes(), user3.getLastName()));
        members.add(new UserNameAndIdDTO(user2.getId(), user2.getFirstName(), user2.getPrefixes(), user2.getLastName()));
        members.add(new UserNameAndIdDTO(user1.getId(), user1.getFirstName(), user1.getPrefixes(), user1.getLastName()));
        return objectMapper.writeValueAsString(members);
    }

    /**
     * Gets a valid jpg image from file and turns in into a MockMultipartFile.
     *
     * @return The jpg image as a MockMultipartFile
     * @throws IOException The exception potentially thrown by FileInputStream
     */
    public MockMultipartFile getValidJpg() throws IOException {
        File imageFile = new File("src/test/java/com/internship/managerview/testHelpers/testImages/ValidJpgImage.jpg");
        return getMockMultipartFile(imageFile, "image/jpeg");
    }

    /**
     * Gets a valid png image from file and turns in into a MockMultipartFile.
     *
     * @return A png image as a MockMultipartFile
     * @throws IOException The exception potentially thrown by getMockMultipartFile()
     */
    public MockMultipartFile getValidPng() throws IOException {
        File imageFile = new File("src/test/java/com/internship/managerview/testHelpers/testImages/ValidPngImage.png");
        return getMockMultipartFile(imageFile, "image/png");
    }

    /**
     * Gets a rectangular image from file and turns in into a MockMultipartFile.
     *
     * @return A jpg image as a MockMultipartFile
     * @throws IOException The exception potentially thrown by getMockMultipartFile()
     */
    public MockMultipartFile getRectangularJpg() throws IOException {
        File imageFile = new File("src/test/java/com/internship/managerview/testHelpers/testImages/NotSquare.jpg");
        return getMockMultipartFile(imageFile, "image/jpeg");
    }

    /**
     * Gets an image that is larger than 2MB from file and turns in into a MockMultipartFile.
     *
     * @return A image as a MockMultipartFile
     * @throws IOException The exception potentially thrown by getMockMultipartFile()
     */
    public MockMultipartFile getTooLargeJpg() throws IOException {
        File imageFile = new File("src/test/java/com/internship/managerview/testHelpers/testImages/TooLarge.jpg");
        return getMockMultipartFile(imageFile, "image/jpeg");
    }

    /**
     * Gets a text file from file and turns in into a MockMultipartFile.
     *
     * @return A text file as a MockMultipartFile
     * @throws IOException The exception potentially thrown by getMockMultipartFile()
     */
    public MockMultipartFile getTextFile() throws IOException {
        File imageFile = new File("src/test/java/com/internship/managerview/testHelpers/testImages/NotAnImage.txt");
        return getMockMultipartFile(imageFile, "text/plain");
    }

    /**
     * Gets a text file from file and turns in into a MockMultipartFile with an image MimeType.
     *
     * @return A text file as a MockMultipartFile
     * @throws IOException The exception potentially thrown by getMockMultipartFile()
     */
    public MockMultipartFile getTextFileWithImageMimeType() throws IOException {
        File imageFile = new File("src/test/java/com/internship/managerview/testHelpers/testImages/NotAnImage.txt");
        return getMockMultipartFile(imageFile, "image/png");
    }

    /**
     * Turns an original file into a MockMultipartFile.
     *
     * @return The file as a MockMultipartFile
     * @throws IOException The exception potentially thrown by FileInputStream
     */
    private MockMultipartFile getMockMultipartFile(File originalFile, String contentType) throws IOException {
        String originalFileName = originalFile.getName();

        FileInputStream fileInputStream = new FileInputStream(originalFile);
        MockMultipartFile image = new MockMultipartFile("image", originalFileName, contentType, fileInputStream);
        fileInputStream.close();

        return image;
    }

    public String getTeam1Id() {
        return team1.getId();
    }

    public String getTeam2Id() {
        return team2.getId();
    }

    public String getTeam4Id() {
        return team4.getId();
    }

    public List<User> getUserEntities() {
        return users;
    }
}