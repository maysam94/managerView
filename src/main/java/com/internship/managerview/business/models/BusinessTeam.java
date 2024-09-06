package com.internship.managerview.business.models;

import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BusinessTeam {
    private String id;
    private String name;
    private List<BusinessUser> members;
    private Date createdAt;
    private BusinessUser createdBy;

    /**
     * - The string can have letters, numbers, spaces and _!-
     */
    private final Pattern nameRegex = Pattern.compile("^[\\p{IsAlphabetic}0-9 _!-]+$");

    /**
     * Creates a BusinessTeam instance for a new team.
     *
     * @param name      The team name
     * @param members   The ids of the team members
     * @param createdBy The id of the manager who created the team
     */
    public BusinessTeam(String name, List<BusinessUser> members, BusinessUser createdBy) {
        this.name = name;
        this.members = members;
        this.createdBy = createdBy;
    }

    /**
     * Creates a BusinessTeam instance for an updated team.
     *
     * @param name    The team name
     * @param members The ids of the team members
     */
    public BusinessTeam(String name, List<BusinessUser> members) {
        this.name = name;
        this.members = members;
    }

    /**
     * Creates a BusinessTeam instance.
     *
     * @param id        The team id
     * @param name      The team name
     * @param members   The List of team members
     * @param createdAt The date on which the team was created
     * @param createdBy The user who created the team
     */
    public BusinessTeam(String id, String name, List<BusinessUser> members, Date createdAt, BusinessUser createdBy) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    /**
     * Validates team name, createdBy id and member ids
     *
     * @throws InvalidInputException when user input data is invalid
     */
    public void validateToCreate() throws InvalidInputException {
        validateName();
        validateCreator();
        validateMembers();
        validateUniqueMemberIds();
    }

    /**
     * Validates team name and member ids
     *
     * @throws InvalidInputException when user input data is invalid
     */
    public void validateToUpdate() throws InvalidInputException {
        validateName();
        validateMembers();
        validateUniqueMemberIds();
    }

    /**
     * Validates the team name presence, length and used characters.
     *
     * @throws InvalidInputException when team name is invalid
     */
    private void validateName() throws InvalidInputException {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException(ErrorCode.T_NAME_IS_EMPTY);
        } else {
            validateNameLength();
            validateNameCharacters();
        }
    }

    /**
     * Validates if the team name length is between 2 and 63 characters.
     *
     * @throws InvalidInputException when team name is invalid
     */
    private void validateNameLength() throws InvalidInputException {
        int minimumLength = 2, maximumLength = 63;
        if (name.length() < minimumLength || name.length() > maximumLength) {
            throw new InvalidInputException(ErrorCode.T_INVALID_NAME_LENGTH);
        }
    }

    /**
     * Checks if the team name only contains valid characters by matching it with a regular expression.
     * Checks if the team name starts with a letter.
     *
     * @throws InvalidInputException when team name is invalid
     */
    private void validateNameCharacters() throws InvalidInputException {
        Matcher regexMatcher = nameRegex.matcher(name);
        if (!regexMatcher.find()) {
            throw new InvalidInputException(ErrorCode.T_INVALID_NAME_CHARACTER);
        } else if (!Character.isLetter(name.charAt(0))) {
            throw new InvalidInputException(ErrorCode.T_INVALID_STARTING_CHARACTER);
        }
    }

    /**
     * Checks whether the createdBy id is a valid number for an id.
     *
     * @throws InvalidInputException if createdBy is invalid
     */
    private void validateCreator() throws InvalidInputException {
        if (createdBy.getId() == 0) throw new InvalidInputException(ErrorCode.T_CREATOR_IS_EMPTY);
    }

    /**
     * Checks whether the amount of team members within the team is appropriate and whether all member ids are valid.
     *
     * @throws InvalidInputException when the member amount or a member id is invalid
     */
    private void validateMembers() throws InvalidInputException {
        if (members == null) {
            throw new InvalidInputException(ErrorCode.T_MEMBER_IS_EMPTY);
        } else {
            int memberAmount = members.size();
            if (memberAmount < 1 || memberAmount > 10)
                throw new InvalidInputException(ErrorCode.T_INVALID_MEMBER_AMOUNT);
            for (BusinessUser member : members) {
                if (member.getId() == 0) throw new InvalidInputException(ErrorCode.T_MEMBER_IS_EMPTY);
            }
        }
    }

    /**
     * Checks whether all given member ids are unique.
     *
     * @throws InvalidInputException when a member id occurs more than once in the List
     */
    private void validateUniqueMemberIds() throws InvalidInputException {
        List<Integer> memberIds = members
                .stream()
                .map(BusinessUser::getId)
                .toList();
        Set<Integer> ids = new HashSet<>(memberIds);
        if (memberIds.size() != ids.size()) throw new InvalidInputException(ErrorCode.T_DUPLICATE_MEMBER_IDS);
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

    public List<BusinessUser> getMembers() {
        return members;
    }

    public BusinessUser getCreatedBy() {
        return createdBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
