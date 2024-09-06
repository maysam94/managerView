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
import java.util.Date;
import java.util.List;

@Component("businessModelToEntity")
public class BusinessModelToEntityMapper {

    /**
     * Maps a BusinessTeam to a Team entity.
     *
     * @param businessTeam The BusinessTeam to be mapped
     * @return A new Team entity with a name, date, createdBy and members
     */
    public Team mapToTeamEntity(BusinessTeam businessTeam) {
        User creator = mapToUserEntity(businessTeam.getCreatedBy());
        Collection<User> members = mapToUserEntities(businessTeam.getMembers());
        return new Team(businessTeam.getName(), new Date(), creator, members);
    }

    /**
     * Maps a BusinessUser to a User entity.
     *
     * @param businessUser The BusinessUser to be mapped
     * @return A User entity with just an id
     */
    public User mapToUserEntity(BusinessUser businessUser) {
        return new User(businessUser.getId());
    }

    /**
     * Maps a List of BusinessUsers to a Collection of User entities.
     *
     * @param businessUsers The list of BusinessUsers to be mapped
     * @return A Collection of User entities with just ids
     */
    public Collection<User> mapToUserEntities(List<BusinessUser> businessUsers) {
        Collection<User> users = new ArrayList<>();
        for (BusinessUser businessUser : businessUsers) {
            users.add(new User(businessUser.getId()));
        }
        return users;
    }

    /**
     * Maps a BusinessImage to an Image entity.
     *
     * @param businessImage The BusinessImage to be mapped
     * @return An Image entity with a type and the image as byte[]
     */
    public Image mapToImageEntity(BusinessImage businessImage) {
        return new Image(businessImage.getType(), businessImage.getSavableImage());
    }
}
