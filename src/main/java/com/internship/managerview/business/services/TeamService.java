package com.internship.managerview.business.services;

import com.internship.managerview.business.models.BusinessImage;
import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.data.models.TeamModel;
import com.internship.managerview.util.exceptionHandling.exceptions.DataRowNotFoundException;
import com.internship.managerview.util.exceptionHandling.exceptions.EntityAlreadyExistsException;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;


@Service
public class TeamService {
    @Autowired
    TeamModel teamModel;

    /**
     * Gets the first 100 team ids, names and avatars in TeamListElementDTO.
     *
     * @return A list with the first 100 team ids and names in TeamListElementDTO
     * @author Anne Butter
     */
    public List<BusinessTeam> getTeamList() {
        return teamModel.getTeamList();
    }

    /**
     * Gets the requested team by its id
     *
     * @param id The id of the requested team
     * @return A TeamDetailsDTO containing the details of the requested team
     * @throws DataRowNotFoundException when a team with the given id is not found in the database
     * @author Anne Butter
     */
    public BusinessTeam getTeamById(String id) throws DataRowNotFoundException {
        return teamModel.getTeamById(id);
    }

    /**
     * Gets the team avatar by the team id
     *
     * @param teamId The id of the team whose avatar is requested
     * @return A BusinessImage containing the details of the requested avatar
     * @throws DataRowNotFoundException when a team with the given id is not found in the database or
     *                                  when the team with the given id does not have an avatar linked
     */
    public BusinessImage getAvatarByTeamId(String teamId) throws DataRowNotFoundException {
        return teamModel.getAvatarByTeamId(teamId);
    }

    /**
     * Gets the team members by the team id
     *
     * @param teamId The id of the team whose members are requested
     * @return A list with BusinessUsers containing the details of the requested members
     * @throws DataRowNotFoundException when a team with the given id is not found in the database
     */
    public List<BusinessUser> getMembersByTeamId(String teamId) throws DataRowNotFoundException {
        return teamModel.getMembersByTeamId(teamId);
    }

    /**
     * Validates the team data and creates the new team if valid.
     *
     * @param team A BusinessTeam containing the necessary team data for creating a new team
     * @return The id of het newly created team
     * @throws InvalidInputException when user input data is invalid
     */
    public String createTeam(BusinessTeam team) throws InvalidInputException, EntityAlreadyExistsException, DataRowNotFoundException {
        team.validateToCreate();
        return teamModel.createTeam(team);
    }

    /**
     * Validates the team data and creates the new team if valid.
     *
     * @param id   The id of the team that needs to be updated
     * @param team A BusinessTeam containing the necessary team data for updating an existing team
     * @throws InvalidInputException when user input data is invalid
     */
    public void updateTeam(String id, BusinessTeam team) throws InvalidInputException, EntityAlreadyExistsException, DataRowNotFoundException {
        team.validateToUpdate();
        teamModel.updateTeam(id, team);
    }

    /**
     * Validates the incoming image and saves the image if valid.
     *
     * @param teamId     The id of the team the image should be linked to
     * @param inputImage The incoming image in a MultipartFile
     * @return The id of the newly saved image
     * @throws IOException           in case of access errors in validate() or convertInputImageToSavableImage() (if the temporary store fails)
     * @throws InvalidInputException when user input data is invalid
     */
    public String addTeamAvatar(String teamId, MultipartFile inputImage) throws IOException, InvalidInputException {
        BusinessImage businessImage = new BusinessImage(inputImage.getContentType(), inputImage);
        businessImage.validate();
        businessImage.convertInputImageToSavableImage();
        return teamModel.addTeamAvatar(teamId, businessImage);
    }

    /**
     * Updates the team status.
     *
     * @param id The id of the team whose status needs to be updated
     */
    public void updateTeamStatus(String id) {
        teamModel.updateTeamStatus(id);
    }

}
