package com.internship.managerview.controllers.mappers;

import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.controllers.DTOs.CreateTeamDTO;
import com.internship.managerview.controllers.DTOs.UpdateTeamDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("dtoToBusinessModel")
public class DTOToBusinessModelMapper {

    /**
     * Maps a CreateTeamDTO to a BusinessTeam.
     *
     * @param createTeamDTO The DTO to be mapped
     * @return A BusinessTeam
     */
    public BusinessTeam mapToBusinessTeam(CreateTeamDTO createTeamDTO) {
        List<BusinessUser> members = mapToBusinessUsers(createTeamDTO.members());
        BusinessUser createdBy = mapToBusinessUser(createTeamDTO.createdBy());
        return new BusinessTeam(createTeamDTO.name(), members, createdBy);
    }

    /**
     * Maps a UpdateTeamDTO to a BusinessTeam.
     *
     * @param updateTeamDTO The DTO to be mapped
     * @return A BusinessTeam
     */
    public BusinessTeam mapToBusinessTeam(UpdateTeamDTO updateTeamDTO) {
        List<BusinessUser> members = mapToBusinessUsers(updateTeamDTO.members());
        return new BusinessTeam(updateTeamDTO.name(), members);
    }

    /**
     * Maps a List of userIds to a list of BusinessUsers.
     *
     * @param userIds The List of userIds to be mapped
     * @return A List of BusinessUsers
     */
    public List<BusinessUser> mapToBusinessUsers(List<Integer> userIds) {
        if (userIds != null) {
            return userIds
                    .stream()
                    .map(BusinessUser::new)
                    .toList();
        }
        return null;
    }

    /**
     * Maps a userId to a BusinessUser.
     *
     * @param userId The userId to be mapped
     * @return A BusinessUser
     */
    public BusinessUser mapToBusinessUser(int userId) {
        return new BusinessUser(userId);
    }
}
