package com.internship.managerview.data.models;

import com.internship.managerview.business.models.BusinessImage;
import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.data.entities.Image;
import com.internship.managerview.data.entities.Team;
import com.internship.managerview.data.entities.User;
import com.internship.managerview.data.repositories.ImageRepository;
import com.internship.managerview.data.repositories.TeamRepository;

import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.DataRowNotFoundException;
import com.internship.managerview.util.exceptionHandling.exceptions.EntityAlreadyExistsException;
import com.internship.managerview.data.models.mappers.BusinessModelToEntityMapper;
import com.internship.managerview.data.models.mappers.EntityToBusinessModelMapper;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TeamModel {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    @Qualifier("entityToBusinessModel")
    EntityToBusinessModelMapper businessModelMapper;

    @Autowired
    @Qualifier("businessModelToEntity")
    BusinessModelToEntityMapper entityMapper;

    /**
     * Gets the first 100 teams from the database and converts them to BusinessTeams.
     *
     * @return A list with the first 100 teams as BusinessTeams
     */
    public List<BusinessTeam> getTeamList() {
        Iterable<Team> teams = teamRepository.findTop100ByIsActiveTrueOrderByName();
        return businessModelMapper.mapToBusinessTeams(teams);
    }

    /**
     * Gets the requested team from the database and converts it to a BusinessTeam.
     *
     * @param id The id of the requested team
     * @return The requested team as a BusinessTeam
     */
    public BusinessTeam getTeamById(String id) throws DataRowNotFoundException {
        Team savedTeam = findTeamById(id);
        return businessModelMapper.mapToBusinessTeam(savedTeam);
    }

    /**
     * Finds a team by id and converts its avatar to a BusinessImage.
     *
     * @param teamId The id of the team whose avatar is requested
     * @return The avatar as a BusinessImage
     */
    public BusinessImage getAvatarByTeamId(String teamId) {
        Team team = findTeamById(teamId);
        Image teamAvatar = team.getImage();
        if (teamAvatar != null) {
            return businessModelMapper.mapToBusinessImage(teamAvatar);
        } else {
            throw new DataRowNotFoundException(ErrorCode.T_AVATAR_NOT_FOUND);
        }
    }

    /**
     * Finds a team by id and converts its members to an array list of BusinessUsers.
     *
     * @param teamId The id of the team whose members are requested
     * @return The array list of team members as BusinessUsers
     * @throws DataRowNotFoundException if no team with the given id is found
     */
    public List<BusinessUser> getMembersByTeamId(String teamId) throws DataRowNotFoundException {
        Team team = findTeamById(teamId);
        return businessModelMapper.mapToBusinessUsers(team.getMembers());
    }

    /**
     * Creates a new Team entity and saves it to the database.
     *
     * @param businessTeam The business model containing all necessary team data.
     * @return The id of the newly created team
     * @throws EntityExistsException    if a team with the same name already exists
     * @throws DataRowNotFoundException if the creator or one or more members are not found
     */
    public String createTeam(BusinessTeam businessTeam) throws EntityExistsException, DataRowNotFoundException {
        Team team = entityMapper.mapToTeamEntity(businessTeam);
        Team createdTeam = saveTeam(team);
        return createdTeam.getId();
    }

    /**
     * Updates the values of an existing team to the given values and saves it to the database again.
     *
     * @param id           The id of the team that needs to be updated
     * @param businessTeam The business model containing all necessary and validated team data.
     * @throws EntityExistsException    if a team with the same name already exists
     * @throws DataRowNotFoundException if the team that needs to be updated or one or more members are not found
     */
    public void updateTeam(String id, BusinessTeam businessTeam) throws EntityExistsException, DataRowNotFoundException {
        Collection<User> members = entityMapper.mapToUserEntities(businessTeam.getMembers());
        Team savedTeam = findTeamById(id);
        savedTeam.setName(businessTeam.getName());
        savedTeam.setMembers(members);
        saveTeam(savedTeam);
    }

    /**
     * Saves the image to the database and links it to an existing team.
     *
     * @param teamId The id of the team the image should be linked to
     * @param image  The BusinessImage containing the data that should be saved to the database
     * @return The id of the newly created image
     * @throws DataRowNotFoundException when no team is found with the given id
     */
    public String addTeamAvatar(String teamId, BusinessImage image) throws DataRowNotFoundException {
        Team savedTeam = findTeamById(teamId);
        Image avatar = entityMapper.mapToImageEntity(image);
        Image createdImage = imageRepository.save(avatar);
        savedTeam.setImage(createdImage);
        saveTeam(savedTeam);
        return createdImage.getId();
    }

    /**
     * Gets the team from the database, toggles its status and saves it back to the database.
     *
     * @param id The id of the team whose status needs to be updated
     */
    public void updateTeamStatus(String id) {
        Team team = findTeamById(id);
        team.setIsActive(!team.getIsActive());
        teamRepository.save(team);
    }

    /**
     * Finds a team by its id or throws a DataRowNotFoundException if no team is found.
     *
     * @param id The id of the requested team
     * @return The requested team entity
     * @throws DataRowNotFoundException if no team with the given id is found
     */
    private Team findTeamById(String id) throws DataRowNotFoundException {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (optionalTeam.isPresent()) {
            return optionalTeam.get();
        } else {
            throw new DataRowNotFoundException(ErrorCode.T_NOT_FOUND);
        }
    }

    /**
     * Saves the Team to the database
     *
     * @param team The entity to be saved in the database
     * @return The newly created team from the database
     * @throws DataRowNotFoundException     if a User that should be linked to the team does not exist,
     *                                      either creator or member
     * @throws EntityAlreadyExistsException if a team with the given name already exists.
     */
    private Team saveTeam(Team team) throws DataRowNotFoundException, EntityAlreadyExistsException {
        Team savedTeam = new Team();
        try {
            savedTeam = teamRepository.save(team);
        } catch (DataIntegrityViolationException exception) {
            handleDataIntegrityViolationException(exception);
        } catch (JpaObjectRetrievalFailureException exception) {
            handleJpaObjectRetrievalFailureException(exception);
        }
        return savedTeam;
    }

    /**
     * Handles the DataIntegrityViolationException that was thrown by the TeamRepository.
     *
     * @param exception The DataIntegrityViolationException caught
     * @throws DataRowNotFoundException     if a User that should be linked to the team does not exist,
     *                                      *                           either creator or member
     * @throws EntityAlreadyExistsException if a team with the given name already exists.
     */
    private void handleDataIntegrityViolationException(DataIntegrityViolationException exception) throws DataRowNotFoundException, EntityAlreadyExistsException {
        if (exception.getMessage().toLowerCase().contains("fk_team_creator")) {
            throw new DataRowNotFoundException(ErrorCode.T_CREATOR_NOT_FOUND);
        } else if (exception.getMessage().toLowerCase().contains("fk_team_member")) {
            throw new DataRowNotFoundException(ErrorCode.T_MEMBER_NOT_FOUND);
        } else if (exception.getMessage().toLowerCase().contains("uk_team_name")) {
            throw new EntityAlreadyExistsException(ErrorCode.T_NAME_ALREADY_EXISTS);
        }
    }

    /**
     * Handles the JpaObjectRetrievalFailureException that was thrown by the TeamRepository.
     *
     * @param exception The JpaObjectRetrievalFailureException caught
     * @throws DataRowNotFoundException if a User that should be linked to the team as a member does not exist
     */
    private void handleJpaObjectRetrievalFailureException(JpaObjectRetrievalFailureException exception) throws DataRowNotFoundException {
        if (exception.getMessage().toLowerCase().contains("unable to find com.internship.managerview.data.entities.user with id")) {
            throw new DataRowNotFoundException(ErrorCode.T_MEMBER_NOT_FOUND);
        }
    }
}

