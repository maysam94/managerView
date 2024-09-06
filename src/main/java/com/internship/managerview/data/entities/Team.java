package com.internship.managerview.data.entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_team_name", columnNames = "name"))
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_team_creator"))
    private User createdBy;

    @ManyToMany
    @JoinTable(
            name = "team_user",
            joinColumns = @JoinColumn(
                    name = "teamId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "userId", referencedColumnName = "id"),
            inverseForeignKey = @ForeignKey(name = "fk_team_member")
    )
    @OrderBy("firstName")
    private Collection<User> members;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "fk_team_image"))
    private Image image;

    private boolean isActive = true;

    /**
     * Creates a Team entity instance.
     *
     */
    public Team() {
    }

    /**
     * Creates a Team entity instance.
     *
     * @param name      The team name
     * @param createdAt The date and time of when the team was first created
     * @param createdBy The id of the manager who created the team
     * @param members   The collection of User ids that should be linked to this team as members
     */
    public Team(String name, Date createdAt, User createdBy, Collection<User> members) {
        this.name = name;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.members = members;
    }

    /**
     * Creates a Team entity instance.
     *
     * @param name      The team name
     * @param createdAt The date and time of when the team was first created
     * @param createdBy The id of the manager who created the team
     * @param members   The collection of User ids that should be linked to this team as members
     * @param image     The team avatar
     */
    public Team(String name, Date createdAt, User createdBy, Collection<User> members, Image image) {
        this.name = name;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.members = members;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Collection<User> getMembers() {
        return members;
    }

    public Image getImage() {
        return image;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(Collection<User> members) {
        this.members = members;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
