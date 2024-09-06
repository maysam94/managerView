package com.internship.managerview.testHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.managerview.controllers.DTOs.UserByIdDTO;
import com.internship.managerview.controllers.DTOs.UserNameAndIdDTO;

import java.util.List;

public class UserTestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static String kees;
    private static String jaap;
    private static String helmerd;
    private static String john;
    private static String katie;
    private static String karel;

    /**
     * Instantiates this class' user properties with UserByNameDTOs in the form of Strings.
     *
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public static void createUserDTOJsonStrings() throws JsonProcessingException {
        kees = objectMapper.writeValueAsString(new UserNameAndIdDTO(1, "Kees", "van den", "Kaas"));
        jaap = objectMapper.writeValueAsString(new UserNameAndIdDTO(2, "Jaap", null, "Schipper"));
        helmerd = objectMapper.writeValueAsString(new UserNameAndIdDTO(3, "Helmerd", null, "Robin"));
        john = objectMapper.writeValueAsString(new UserNameAndIdDTO(4, "John", null, "Kerk"));
        katie = objectMapper.writeValueAsString(new UserNameAndIdDTO(5, "Katie", "van", "Vliet"));
        karel = objectMapper.writeValueAsString(new UserNameAndIdDTO(6, "Karel", null, "Keurig"));
    }

    /**
     * Gets the JSON object as a string that should be the result of UserControllerIntegrationTests.getExistingUser()
     *
     * @return A string containing the necessary JSON object
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public static String getExistingUserResult() throws JsonProcessingException {
        UserByIdDTO user = new UserByIdDTO("Kees", "van den", "Kaas", "keesvandenkaas@mail.com");
        return objectMapper.writeValueAsString(user);
    }

    /**
     * Gets the JSON objects as a string that should be the result of UserControllerIntegrationTests.getUsersWithK()
     *
     * @return A string containing the necessary JSON object
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public static String getResultForParameterK() throws JsonProcessingException {
        List<String> userList = List.of(karel, katie, kees, john);
        return userList.toString();
    }

    /**
     * Gets the JSON objects as a string that should be the result of UserControllerIntegrationTests.getUsersWithKe()
     *
     * @return A string containing the necessary JSON object
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public static String getResultForParameterKe() throws JsonProcessingException {
        List<String> userList = List.of(kees, john, karel);
        return userList.toString();
    }

    /**
     * Gets the JSON objects as a string that should be the result of UserControllerIntegrationTests.getUsersWithNothing()
     *
     * @return A string containing the necessary JSON object
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public static String getResultForNoParameter() throws JsonProcessingException {
        List<String> userList = List.of(helmerd, jaap, john, karel, katie, kees);
        return userList.toString();
    }
}
