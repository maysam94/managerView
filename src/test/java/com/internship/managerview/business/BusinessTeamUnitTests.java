package com.internship.managerview.business;


import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessTeamUnitTests {
    private static final List<BusinessUser> validMembers = List.of(new BusinessUser(1), new BusinessUser(2), new BusinessUser(3));
    private static final BusinessUser validCreator = new BusinessUser(1);

    /**
     * Tests if the validateToCreate method doesn't throw an exception when the Team is valid.
     *
     */
    @Test
    @DisplayName("Throws no exception when all team data is valid.")
    public void validTeam() {
        BusinessTeam businessTeam = new BusinessTeam("New team", validMembers, validCreator);
        assertDoesNotThrow(businessTeam::validateToCreate);
    }

    @Nested
    @DisplayName("Edge cases name")
    public class NameValidation {
        @Nested
        @DisplayName("Valid names")
        public class ValidNames {
            /**
             * Tests if the validateToCreate method doesn't throw an exception when the team name is exactly minimum length.
             */
            @Test
            @DisplayName("Throws no exception when team name is exactly minimum length (2).")
            public void minimumLength() {
                BusinessTeam businessTeam = new BusinessTeam("Ne", validMembers, validCreator);
                assertDoesNotThrow(businessTeam::validateToCreate);
            }

            /**
             * Tests if the validateToCreate method doesn't throw an exception when the team name is exactly maximum length.
             *
             */
            @Test
            @DisplayName("Throws no exception when team name is exactly maximum length (63).")
            public void maximumLength() {
                BusinessTeam businessTeam = new BusinessTeam("This team name is exactly at maximum length which is still fine", validMembers, validCreator);
                assertDoesNotThrow(businessTeam::validateToCreate);
            }

            /**
             * Tests if the validateToCreate method doesn't throw an exception when the team name contains numbers.
             *
             */
            @Test
            @DisplayName("Throws no exception when team name contains numbers.")
            public void hasNumbers() {
                BusinessTeam businessTeam = new BusinessTeam("Name with numbers 123", validMembers, validCreator);
                assertDoesNotThrow(businessTeam::validateToCreate);
            }

            /**
             * Tests if the validateToCreate method doesn't throw an exception when the team name contains - _ and !.
             *
             */
            @Test
            @DisplayName("Throws no exception when team name contains - _ or !")
            public void hasSpecialCharacters() {
                BusinessTeam businessTeam = new BusinessTeam("Name with special characters -_!", validMembers, validCreator);
                assertDoesNotThrow(businessTeam::validateToCreate);
            }
        }

        @Nested
        @DisplayName("Invalid names")
        public class InvalidNames {
            /**
             * Tests if the validateToCreate method throws an InvalidInputException when the team name is 1 too short.
             *
             */
            @Test
            @DisplayName("Throws an InvalidInputException when team name is too short by 1 character.")
            public void tooShort() {
                BusinessTeam businessTeam = new BusinessTeam("N", validMembers, validCreator);
                InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
                assertSame(ErrorCode.T_INVALID_NAME_LENGTH, exception.getErrorCode());
            }

            /**
             * Tests if the validateToCreate method throws an InvalidInputException when the team name is 1 too long.
             *
             */
            @Test
            @DisplayName("Throws an InvalidInputException when team name is too long by 1 character.")
            public void tooLong() {
                BusinessTeam businessTeam = new BusinessTeam("This teamname is too long because a team name is allowed only 63", validMembers, validCreator);
                InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
                assertSame(ErrorCode.T_INVALID_NAME_LENGTH, exception.getErrorCode());
            }

            /**
             * Tests if the validateToCreate method throws an InvalidInputException when the team name starts with a number.
             *
             */
            @Test
            @DisplayName("Throws an InvalidInputException when team name starts with a number.")
            public void startsWithNumber() {
                BusinessTeam businessTeam = new BusinessTeam("1 Team", validMembers, validCreator);
                InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
                assertSame(ErrorCode.T_INVALID_STARTING_CHARACTER, exception.getErrorCode());
            }

            /**
             * Tests if the validateToCreate method throws an InvalidInputException when the team name starts with a special character.
             *
             */
            @Test
            @DisplayName("Throws an InvalidInputException when team name starts with a special character (-).")
            public void startsWithSpecialCharacter() {
                BusinessTeam businessTeam = new BusinessTeam("-Team", validMembers, validCreator);
                InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
                assertSame(ErrorCode.T_INVALID_STARTING_CHARACTER, exception.getErrorCode());
            }

            /**
             * Tests if the validateToCreate method throws an InvalidInputException when the team name starts with a special character.
             *
             */
            @Test
            @DisplayName("Throws an InvalidInputException when team name contains an illegal character ($).")
            public void illegalCharacter() {
                BusinessTeam businessTeam = new BusinessTeam("Team$", validMembers, validCreator);
                InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
                assertSame(ErrorCode.T_INVALID_NAME_CHARACTER, exception.getErrorCode());
            }

            /**
             * Tests if the validateToCreate method throws an InvalidInputException when the team name is null.
             */
            @Test
            @DisplayName("Throws an InvalidInputException when team name is null.")
            public void isNull() {
                BusinessTeam businessTeam = new BusinessTeam(null, validMembers, validCreator);
                InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
                assertSame(ErrorCode.T_NAME_IS_EMPTY, exception.getErrorCode());
            }

            /**
             * Tests if the validateToCreate method throws an InvalidInputException when the team name is an empty string.
             */
            @Test
            @DisplayName("Throws an InvalidInputException when team name is an empty string.")
            public void isEmptyString() {
                BusinessTeam businessTeam = new BusinessTeam("", validMembers, validCreator);
                InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
                assertSame(ErrorCode.T_NAME_IS_EMPTY, exception.getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("Edge cases member ids")
    public class MemberIdValidation {
        /**
         * Tests if the validateToCreate method throws an InvalidInputException when no member ids are added.
         */
        @Test
        @DisplayName("Throws an InvalidInputException when no member ids are added.")
        public void noMemberIds() {
            List<BusinessUser> emptyArrayList = new ArrayList<>();
            BusinessTeam businessTeam = new BusinessTeam("No members", emptyArrayList, validCreator);
            InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
            assertSame(ErrorCode.T_INVALID_MEMBER_AMOUNT, exception.getErrorCode());
        }

        /**
         * Tests if the validateToCreate method throws an InvalidInputException when too many member ids are added.
         */
        @Test
        @DisplayName("Throws an InvalidInputException when too many member ids are added.")
        public void tooManyMemberIds() {
            List<BusinessUser> tooManyMemberIdsArrayList = new ArrayList<>();
            for (int i = 1; i <= 11; i++) {
                tooManyMemberIdsArrayList.add(new BusinessUser(i));
            }
            BusinessTeam businessTeam = new BusinessTeam("Too many members", tooManyMemberIdsArrayList, validCreator);
            InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
            assertSame(ErrorCode.T_INVALID_MEMBER_AMOUNT, exception.getErrorCode());
        }

        /**
         * Tests if the validateToCreate method throws an InvalidInputException when two of the same member id are added.
         */
        @Test
        @DisplayName("Throws an InvalidInputException when two of the same member id are added.")
        public void doubleMemberIds() {
            List<BusinessUser> doubleMemberIdsArrayList = List.of(new BusinessUser(1), new BusinessUser(2), new BusinessUser(2));

            BusinessTeam businessTeam = new BusinessTeam("Double members", doubleMemberIdsArrayList, validCreator);
            InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
            assertSame(ErrorCode.T_DUPLICATE_MEMBER_IDS, exception.getErrorCode());
        }

        /**
         * Tests if the validateToCreate method throws an InvalidInputException when memberIds is null.
         */
        @Test
        @DisplayName("Throws an InvalidInputException when memberIds is null.")
        public void memberIdsIsNull() {
            BusinessTeam businessTeam = new BusinessTeam("memberIds is null", null, validCreator);
            InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
            assertSame(ErrorCode.T_MEMBER_IS_EMPTY, exception.getErrorCode());
        }

        /**
         * Tests if the validateToCreate method throws an InvalidInputException when one memberId is 0.
         */
        @Test
        @DisplayName("Throws an InvalidInputException when when one memberId is 0.")
        public void memberIdIs0() {
            List<BusinessUser> MemberIdsWith0ArrayList = List.of(new BusinessUser(1), new BusinessUser(0), new BusinessUser(2));

            BusinessTeam businessTeam = new BusinessTeam("One memberId is 0", MemberIdsWith0ArrayList, validCreator);
            InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
            assertSame(ErrorCode.T_MEMBER_IS_EMPTY, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Edge cases creator ids")
    public class InvalidCreatorIds {
        /**
         * Tests if the validateToCreate method throws an InvalidInputException when the createdBy is 0.
         */
        @Test
        @DisplayName("Throws an InvalidInputException when createdBy is 0.")
        public void creatorIdIsNull() {
            BusinessUser creatorWithId0 = new BusinessUser(0);
            BusinessTeam businessTeam = new BusinessTeam("createdBy is null", validMembers, creatorWithId0);
            InvalidInputException exception = assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
            assertSame(ErrorCode.T_CREATOR_IS_EMPTY, exception.getErrorCode());
        }
    }
}
