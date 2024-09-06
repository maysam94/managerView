package com.internship.managerview.data.models.mappers;

import com.internship.managerview.business.models.BusinessImage;
import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.data.entities.Image;
import com.internship.managerview.data.entities.Team;
import com.internship.managerview.data.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("entityToBusinessModel")
public class EntityToBusinessModelMapper {

    /**
     * Maps an Iterable of Team entities to a List of BusinessTeams.
     *
     * @param dataTeams The Team entities to be mapped
     * @return A List of BusinessTeams with an id, name, List of members, createdAt and createdBy
     */
    public List<BusinessTeam> mapToBusinessTeams(Iterable<Team> dataTeams) {
        List<Team> teamList = new ArrayList<>();
        dataTeams.forEach(teamList::add);
        return teamList.stream()
                .map(team -> new BusinessTeam(
                        team.getId(),
                        team.getName(),
                        mapToBusinessUsers(team.getMembers()),
                        team.getCreatedAt(),
                        mapToBusinessUser(team.getCreatedBy())
                ))
                .toList();
    }

    /**
     * Maps a Team entity to a BusinessTeam.
     *
     * @param dataTeam The Team entity to be mapped
     * @return A BusinessTeam with an id, name, List of members, createdAt and createdBy
     */
    public BusinessTeam mapToBusinessTeam(Team dataTeam) {
        List<BusinessUser> businessMembers = mapToBusinessUsers(dataTeam.getMembers());
        BusinessUser businessCreator = mapToBusinessUser(dataTeam.getCreatedBy());
        return new BusinessTeam(
                dataTeam.getId(),
                dataTeam.getName(),
                businessMembers,
                dataTeam.getCreatedAt(),
                businessCreator);
    }

    /**
     * Maps a Collection of User entities to a List of BusinessUsers.
     *
     * @param users The Collection of User entities to be mapped
     * @return A List of BusinessUsers with an id, firstName, prefixes, lastName and email
     */
    public List<BusinessUser> mapToBusinessUsers(Collection<User> users) {
        return users
                .stream()
                .map(this::mapToBusinessUser)
                .toList();
    }

    /**
     * Turns an Iterable of User entities into a List and maps it to a List of BusinessUsers.
     *
     * @param users The Iterable of User entities to be mapped
     * @return A List of BusinessUsers with an id, firstName, prefixes, lastName and email
     */
    public List<BusinessUser> mapToBusinessUsers(Iterable<User> users) {
        List<User> userList = new ArrayList<>();
        users.forEach(userList::add);
        return mapToBusinessUsers(userList);
    }

    /**
     * Maps a User entity to a BusinessUser.
     *
     * @param user The User entity to be mapped
     * @return a BusinessUsers with an id, firstName, prefixes, lastName and email
     */
    public BusinessUser mapToBusinessUser(User user) {
        return new BusinessUser(
                user.getId(),
                user.getFirstName(),
                user.getPrefixes(),
                user.getLastName(),
                user.getEmail());
    }

    /**
     * Maps an Image entity to a BusinessImage.
     *
     * @param image The Image entity to be mapped
     * @return A BusinessImage with a type and the image byte[]
     */
    public BusinessImage mapToBusinessImage(Image image) {
        return new BusinessImage(image.getType(), image.getImage());
    }
}
