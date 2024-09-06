package com.internship.managerview.controllers.mappers;

import com.internship.managerview.business.models.BusinessImage;
import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.controllers.DTOs.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("businessModelToDTO")
public class BusinessModelToDTOMapper {

    /**
     * Maps a BusinessUser to a UserByIdDTO.
     *
     * @param businessUser The BusinessUser to be mapped
     * @return A UserByIdDTO
     */
    public UserByIdDTO mapToUserByIdDTO(BusinessUser businessUser) {
        return new UserByIdDTO(
                businessUser.getFirstName(),
                businessUser.getPrefixes(),
                businessUser.getLastName(),
                businessUser.getEmail());
    }

    /**
     * Maps a List of BusinessTeams to a List of TeamListElementDTOs.
     *
     * @param businessTeams The List of BusinessTeams to be mapped
     * @return A List of TeamListElementDTOs
     */
    public List<TeamListElementDTO> mapToTeamListElementDTOs(List<BusinessTeam> businessTeams) {
        return businessTeams
                .stream()
                .map(businessTeam -> new TeamListElementDTO(businessTeam.getId(), businessTeam.getName()))
                .toList();
    }

    /**
     * Maps a BusinessImage to a TeamAvatarDTO.
     *
     * @param businessImage The BusinessImage to be mapped
     * @return A TeamAvatarDTO
     */
    public TeamAvatarDTO mapToTeamAvatarDTO(BusinessImage businessImage) {
        return new TeamAvatarDTO(businessImage.getType(), businessImage.getSavableImage());
    }

    /**
     * Maps a List of BusinessUsers to a List of UserNameAndIdDTOs.
     *
     * @param businessUsers The List of BusinessUsers to be mapped
     * @return A List of UserNameAndIdDTOs
     */
    public List<UserNameAndIdDTO> mapToUserNameAndIdDTOs(List<BusinessUser> businessUsers) {
        return businessUsers
                .stream()
                .map(businessUser -> new UserNameAndIdDTO(
                        businessUser.getId(),
                        businessUser.getFirstName(),
                        businessUser.getPrefixes(),
                        businessUser.getLastName()))
                .toList();
    }

    /**
     * Maps a BusinessTeam to a TeamDetailsDTO.
     *
     * @param businessTeam The BusinessTeam to be mapped
     * @return A TeamDetailsDTO
     */
    public TeamDetailsDTO mapToTeamDetailsDTO(BusinessTeam businessTeam) {
        List<UserNameAndIdDTO> memberDTOs = mapToUserNameAndIdDTOs(businessTeam.getMembers());
        return new TeamDetailsDTO(businessTeam.getName(), memberDTOs, businessTeam.getCreatedAt());
    }

    /**
     * Maps an id to a IdDTO.
     *
     * @param id The id to be mapped
     * @return An IdDTO
     */
    public IdDTO mapToIdDTO(String id) {
        return new IdDTO(id);
    }
}
