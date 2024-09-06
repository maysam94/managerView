package com.internship.managerview.business;

import com.internship.managerview.business.models.BusinessTeam;
import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import net.jqwik.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessTeamPropertyBasedTests {

    /**
     * Tests if a business team is valid with a valid name.
     *
     * @param name The valid name provided by the validNames provider
     */
    @Property(tries = 500)
    public void randomValidNames(@ForAll("validNames") String name) {
        List<BusinessUser> validMembers = List.of(new BusinessUser(1), new BusinessUser(2), new BusinessUser(3));
        BusinessTeam businessTeam = new BusinessTeam(name, validMembers, new BusinessUser(1));
        assertDoesNotThrow(businessTeam::validateToCreate);
    }

    /**
     * Provides valid team names:
     * Minimum of 2 characters
     * Maximum of 63 characters
     * Starting with a letter
     * Possibly containing letters
     * Possibly containing numbers
     * Possibly containing "_", "-", "!" and/or spaces
     * Combines the starting letter with the rest of the string, meeting the business requirements as a whole.
     *
     * @return The valid team name
     */
    @Provide
    Arbitrary<String> validNames() {
        Arbitrary<Character> firstChar = Arbitraries.chars().alpha();
        Arbitrary<String> restOfString = Arbitraries.strings().ofMinLength(1).ofMaxLength(62)
                .numeric()
                .alpha()
                .withChars('_', '-', ' ', '!');

        return Combinators.combine(firstChar, restOfString).as((first, rest) -> first + rest);
    }

    /**
     * Tests if a business team is valid with valid member ids.
     *
     * @param memberIds The valid member ids provide by the validMemberIds provider
     */
    @Property(tries = 100)
    public void randomValidMemberIds(@ForAll("validMembers") ArrayList<Integer> memberIds) {
        List<BusinessUser> validMembers = memberIds.stream().map(BusinessUser::new).toList();
        BusinessUser validCreator = new BusinessUser(1);
        BusinessTeam businessTeam = new BusinessTeam("Valid name", validMembers, validCreator);
        assertDoesNotThrow(businessTeam::validateToCreate);
    }

    /**
     * Provides a valid list member ids, varying is size.
     *
     * @return The list of member ids
     */
    @Provide
    Arbitrary<ArrayList<Integer>> validMembers() {
        Arbitrary<Integer> integerArbitrary = Arbitraries.integers().between(1, 10000);
        Arbitrary<List<Integer>> listArbitrary = integerArbitrary.list().ofMinSize(1).ofMaxSize(10).uniqueElements();
        return listArbitrary.map(ArrayList::new);
    }

    /**
     * Tests if a business team with a name of an invalid length will be invalid.
     *
     * @param name The invalid team name provided by the invalidNameLengths provider
     */
    @Property(tries = 100)
    public void randomInvalidNameLengths(@ForAll("invalidNameLengths") String name) {
        List<BusinessUser> validMembers = List.of(new BusinessUser(1), new BusinessUser(2), new BusinessUser(3));
        BusinessUser validCreator = new BusinessUser(1);
        BusinessTeam businessTeam = new BusinessTeam(name, validMembers, validCreator);
        assertThrows(InvalidInputException.class, businessTeam::validateToCreate);
    }

    /**
     * Provides team names of invalid lengths.
     *
     * @return The invalid team names
     */
    @Provide
    Arbitrary<String> invalidNameLengths() {
        Arbitrary<String> tooLong = Arbitraries.strings().ofMinLength(64).ofMaxLength(100).alpha();
        Arbitrary<String> tooShort = Arbitraries.strings().ofMinLength(0).ofMaxLength(1).alpha();
        return Arbitraries.oneOf(tooLong, tooShort);
    }
}
