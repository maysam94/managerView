package com.internship.managerview.data.models;

import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.data.entities.Team;
import com.internship.managerview.data.entities.User;
import com.internship.managerview.data.models.mappers.BusinessModelToEntityMapper;
import com.internship.managerview.data.repositories.TeamRepository;
import com.internship.managerview.testHelpers.TeamTestHelper;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.DataRowNotFoundException;
import com.internship.managerview.util.exceptionHandling.exceptions.EntityAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamModelUnitTests {

    @Mock
    private TeamRepository teamRepositoryMock;

    @Mock
    private BusinessModelToEntityMapper entityMapperMock;

    @InjectMocks
    private TeamModel teamModel;

    private static final TeamTestHelper teamTestHelper = new TeamTestHelper();

    private static final List<BusinessUser> validMembers = List.of(new BusinessUser(1), new BusinessUser(2), new BusinessUser(3));
    private static final BusinessUser validCreator = new BusinessUser(1);
    private static final Collection<User> memberEntities = teamTestHelper.getUserEntities();

    @Nested
    @DisplayName("createTeam tests")
    public class createTeamTests {

        /**
         * Tests if the validate method throws an DataRowNotFoundException when a requested member does not exist.
         *
         */
        @Test
        @DisplayName("Throws an DataRowNotFoundException when a requested member does not exist.")
        public void memberDoesNotExist() {
            List<BusinessUser> invalidMembers = List.of(new BusinessUser(1), new BusinessUser(2), new BusinessUser(4));
            BusinessTeam businessTeam = new BusinessTeam("New team", invalidMembers, validCreator);

            Collection<User> invalidMemberEntities = List.of(new User(1), new User(2), new User(4));
            Team savableTeam = new Team(businessTeam.getName(), new Date(), new User(1), invalidMemberEntities);

            when(entityMapperMock.mapToTeamEntity(businessTeam)).thenReturn(savableTeam);
            when(teamRepositoryMock.save(savableTeam)).thenThrow(new DataIntegrityViolationException("could not execute statement [Cannot add or update a child row: a foreign key constraint fails (`management_view`.`team_user`, CONSTRAINT `fk_team_member` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`))] [insert into team_user (team_id,user_id) values (?,?)]; SQL [insert into team_user (team_id,user_id) values (?,?)]; constraint [null]"));

            DataRowNotFoundException exception = assertThrows(DataRowNotFoundException.class, () -> {
                teamModel.createTeam(businessTeam);
            });
            assertSame(ErrorCode.T_MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        /**
         * Tests if the validate method throws an DataRowNotFoundException when the requested creator does not exist.
         *
         */
        @Test
        @DisplayName("Throws an DataRowNotFoundException when the requested creator does not exist.")
        public void creatorDoesNotExist() {
            BusinessUser nonExistentCreator = new BusinessUser(4);
            BusinessTeam businessTeam = new BusinessTeam("New team", validMembers, nonExistentCreator);

            User creator = new User(4);
            Team savableTeam = new Team(businessTeam.getName(), new Date(), creator, memberEntities);

            when(teamRepositoryMock.save(savableTeam)).thenThrow(new DataIntegrityViolationException("could not execute statement [Cannot add or update a child row: a foreign key constraint fails (`management_view`.`team`, CONSTRAINT `fk_team_creator` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`))] [insert into team (created_at,created_by,name,id) values (?,?,?,?)]; SQL [insert into team (created_at,created_by,name,id) values (?,?,?,?)]; constraint [null]"));
            when(entityMapperMock.mapToTeamEntity(businessTeam)).thenReturn(savableTeam);

            DataRowNotFoundException exception = assertThrows(DataRowNotFoundException.class, () -> {
                teamModel.createTeam(businessTeam);
            });
            assertSame(ErrorCode.T_CREATOR_NOT_FOUND, exception.getErrorCode());
        }

        /**
         * Tests if the validate method throws an EntityExistsException when a team name already exists.
         *
         */
        @Test
        @DisplayName("Throws an EntityExistsException when team name already exists.")
        public void nameAlreadyExists() {
            BusinessTeam businessTeam = new BusinessTeam("Existing team", validMembers, validCreator);
            User creator = new User(1);
            Team savableTeam = new Team(businessTeam.getName(), new Date(), creator, memberEntities);

            when(teamRepositoryMock.save(savableTeam)).thenThrow(new DataIntegrityViolationException("Duplicate entry 'Existing team' for key 'team.uk_team_name'"));
            when(entityMapperMock.mapToTeamEntity(businessTeam)).thenReturn(savableTeam);

            EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () -> {
                teamModel.createTeam(businessTeam);
            });
            assertSame(ErrorCode.T_NAME_ALREADY_EXISTS, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("updateTeam tests")
    public class updateTeamTests {

        /**
         * Tests if the validate method throws an DataRowNotFoundException when the requested team does not exist.
         *
         */
        @Test
        @DisplayName("Throws an DataRowNotFoundException when the requested team does not exist.")
        public void teamDoesNotExist() {
            BusinessTeam businessTeam = new BusinessTeam("Updated title", validMembers);

            when(teamRepositoryMock.findById("0109dbca-26e5-4a20-ad8b-ff7ba2fcc8ae")).thenReturn(Optional.empty());
            when(entityMapperMock.mapToUserEntities(businessTeam.getMembers())).thenReturn(memberEntities);

            DataRowNotFoundException exception = assertThrows(DataRowNotFoundException.class, () -> {
                teamModel.updateTeam("0109dbca-26e5-4a20-ad8b-ff7ba2fcc8ae", businessTeam);
            });
            assertSame(ErrorCode.T_NOT_FOUND, exception.getErrorCode());
        }

    }
}
