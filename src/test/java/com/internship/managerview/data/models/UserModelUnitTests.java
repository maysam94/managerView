package com.internship.managerview.data.models;

import com.internship.managerview.business.models.BusinessUser;
import com.internship.managerview.data.entities.User;
import com.internship.managerview.data.models.mappers.EntityToBusinessModelMapper;
import com.internship.managerview.data.repositories.UserRepository;
import com.internship.managerview.util.exceptionHandling.exceptions.DataRowNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserModelUnitTests {
    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private EntityToBusinessModelMapper businessModelMapperMock;

    @InjectMocks
    private UserModel userModel;

    @Nested
    @DisplayName("getUserById tests")
    public class GetBusinessUserByIDTests {
        /**
         * Tests if the right information is returned if a user id exists.
         *
         */
        @Test
        @DisplayName("Returns a business model BusinessUser if the id exists.")
        public void getExistingUser() {
            User dataUser = new User(1, "Kees", "van den", "Kaas", "keesvandenkaas@mail.com");
            when(userRepositoryMock.findById(1)).thenReturn(Optional.of(dataUser));
            when(businessModelMapperMock.mapToBusinessUser(dataUser)).thenReturn(new BusinessUser(1, "Kees", "van den", "Kaas", "keesvandenkaas@mail.com"));

            assertInstanceOf(BusinessUser.class, userModel.getUserById(1));

            BusinessUser resultBusinessUser = userModel.getUserById(1);
            assertEquals("Kees", resultBusinessUser.getFirstName());
            assertEquals("van den", resultBusinessUser.getPrefixes());
            assertEquals("Kaas", resultBusinessUser.getLastName());
            assertEquals("keesvandenkaas@mail.com", resultBusinessUser.getEmail());
            assertNull(resultBusinessUser.getPassword());
        }

        /**
         * Tests if the right exception is thrown if a user id doesn't exist.
         *
         */
        @Test
        @DisplayName("Throws a DataRowNotFoundException when id does not exist.")
        public void getNonexistentUser() {
            when(userRepositoryMock.findById(2)).thenReturn(Optional.empty());
            assertThrows(DataRowNotFoundException.class, () -> {
                userModel.getUserById(2);
            });
        }
    }
}
