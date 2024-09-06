package com.internship.managerview.controllers;

import com.internship.managerview.business.models.BusinessImage;
import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.business.services.TeamService;
import com.internship.managerview.controllers.DTOs.*;
import com.internship.managerview.controllers.mappers.BusinessModelToDTOMapper;
import com.internship.managerview.controllers.mappers.DTOToBusinessModelMapper;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.ErrorResponse;
import com.internship.managerview.util.exceptionHandling.exceptions.DataRowNotFoundException;
import com.internship.managerview.util.exceptionHandling.exceptions.EntityAlreadyExistsException;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
    @Autowired
    TeamService teamService;

    @Autowired
    @Qualifier("businessModelToDTO")
    BusinessModelToDTOMapper dtoMapper;

    @Autowired
    @Qualifier("dtoToBusinessModel")
    DTOToBusinessModelMapper businessModelMapper;

    /**
     * Maps the GET /teams request and returns a TeamListElementDTO list in the response.
     *
     * @return A ResponseEntity with a TeamListElementDTO list and status 200
     */
    @GetMapping
    public ResponseEntity<Object> getTeamList() {
        List<BusinessTeam> businessTeams = teamService.getTeamList();
        List<TeamListElementDTO> teamListElementDTOS = dtoMapper.mapToTeamListElementDTOs(businessTeams);
        return new ResponseEntity<>(teamListElementDTOS, HttpStatus.OK);
    }

    /**
     * Maps the GET /teams/{id} request and returns a TeamDetailsDTO in the response.
     *
     * @param id The id of the requested team
     * @return A ResponseEntity with a TeamDetailsDTO and status 200
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getTeamById(@PathVariable("id") String id) {
        BusinessTeam businessTeam = teamService.getTeamById(id);
        TeamDetailsDTO teamDetailsDTO = dtoMapper.mapToTeamDetailsDTO(businessTeam);
        return new ResponseEntity<>(teamDetailsDTO, HttpStatus.OK);
    }

    /**
     * Maps the GET /teams/{id}/avatar request and returns a TeamAvatarDTO in the response.
     *
     * @param teamId The id of the team whose avatar is requested
     * @return A ResponseEntity with a TeamAvatarDTO and status 200
     */
    @GetMapping(value = "/{id}/avatar")
    public ResponseEntity<Object> getAvatarByTeamId(@PathVariable("id") String teamId) {
        BusinessImage businessImage = teamService.getAvatarByTeamId(teamId);
        TeamAvatarDTO teamAvatarDTO = dtoMapper.mapToTeamAvatarDTO(businessImage);
        return new ResponseEntity<>(teamAvatarDTO, HttpStatus.OK);
    }

    /**
     * Maps the GET /teams/{id}/members request and returns a UserNameAndIdDTO list in the response.
     *
     * @param teamId The id of the team whose members are requested
     * @return A ResponseEntity with a UserNameAndIdDTO list and status 200
     */
    @GetMapping(value = "/{id}/members")
    public ResponseEntity<Object> getMembersByTeamId(@PathVariable("id") String teamId) {
        List<BusinessUser> businessUsers = teamService.getMembersByTeamId(teamId);
        List<UserNameAndIdDTO> userDTOs = dtoMapper.mapToUserNameAndIdDTOs(businessUsers);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    /**
     * Maps the POST /teams request to save a new team in the database and returns the newly created team id.
     *
     * @param team The team from the request body
     * @return A ResponseEntity with the id of the newly created team and status 201

     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createTeam(@RequestBody CreateTeamDTO team) {
        BusinessTeam businessTeam = businessModelMapper.mapToBusinessTeam(team);
        String createdTeamId = teamService.createTeam(businessTeam);
        IdDTO idDTO = dtoMapper.mapToIdDTO(createdTeamId);
        return new ResponseEntity<>(idDTO, HttpStatus.CREATED);
    }

    /**
     * Maps the POST /teams/{id} request to update an existing team in the database.
     *
     * @param id   The id of the team that needs to be updated
     * @param team The team from the request body
     * @return A ResponseEntity with status 200
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> updateTeam(@PathVariable("id") String id, @RequestBody UpdateTeamDTO team) {
        BusinessTeam businessTeam = businessModelMapper.mapToBusinessTeam(team);
        teamService.updateTeam(id, businessTeam);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Maps the POST /teams/{id}/avatar request to save a new image to the database and link it to an existing team.
     *
     * @param teamId The id of the team the image should be linked to
     * @param image  The incoming image in a MultipartFile
     * @return A ResponseEntity with the newly created avatar id and status 200
     */
    @PostMapping(value = "/{id}/avatar")
    public ResponseEntity<Object> addTeamAvatar(@PathVariable("id") String teamId, @RequestParam("image") MultipartFile image) throws IOException {
        String createdImageId = teamService.addTeamAvatar(teamId, image);
        IdDTO idDTO = dtoMapper.mapToIdDTO(createdImageId);
        return new ResponseEntity<>(idDTO, HttpStatus.CREATED);
    }

    /**
     * Maps the PUT /teams/{id}/status request to update the status of the team in the database.
     *
     * @param id The id of the team whose status needs to be updated
     * @return A ResponseEntity with status 200
     */
    @PutMapping(value = "/{id}/status")
    public ResponseEntity<Object> updateTeamStatus(@PathVariable("id") String id) {
        teamService.updateTeamStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
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
     * Handler for any other exception
     *
     * @return A ResponseEntity with status 500 and the exception's error code.
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> getGenericErrorResponse() {
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.G_GENERIC_ERROR.getCode()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
