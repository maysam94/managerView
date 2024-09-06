package com.internship.managerview.business;

import com.internship.managerview.business.models.BusinessImage;
import com.internship.managerview.testHelpers.TeamTestHelper;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessImageUnitTests {

    private static final TeamTestHelper teamTestHelper = new TeamTestHelper();

    @Nested
    @DisplayName("Edge cases image validation")
    public class ImageValidation {

        /**
         * Tests if a valid jpg or png does not throw an exception.
         *
         * @param displayName The name that will be displayed when the test has run
         * @param inputImage  The MultipartFile that is input for the test
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("validTestDataProvider")
        public void validImages(String displayName, MultipartFile inputImage) {
            BusinessImage businessImage = new BusinessImage(inputImage.getContentType(), inputImage);
            assertDoesNotThrow(businessImage::validate);
        }

        /**
         * Tests if an invalid image throws an InvalidInputException.
         *
         * @param displayName The name that will be displayed when the test has run
         * @param inputImage  The MultipartFile that is input for the test
         */
        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidTestDataProvider")
        public void invalidImages(String displayName, MultipartFile inputImage, ErrorCode errorCode) {
            BusinessImage businessImage = new BusinessImage(inputImage.getContentType(), inputImage);
            InvalidInputException exception = assertThrows(InvalidInputException.class, businessImage::validate);
            assertSame(errorCode, exception.getErrorCode());

        }

        /**
         * Provides input for the validImages test.
         *
         * @return A stream of arguments for the validImages test
         * @throws IOException The exception potentially thrown by the TeamTestHelper methods
         */
        private static Stream<Arguments> validTestDataProvider() throws IOException {
            return Stream.of(
                    Arguments.of("Throws no exception when the image is a valid jpg.", teamTestHelper.getValidJpg()),
                    Arguments.of("Throws no exception when the image is a valid png.", teamTestHelper.getValidPng())
            );
        }

        /**
         * Provides input for the invalidImages test.
         *
         * @return A stream of arguments for the invalidImages test
         * @throws IOException The exception potentially thrown by the TeamTestHelper methods
         */
        private static Stream<Arguments> invalidTestDataProvider() throws IOException {
            return Stream.of(
                    Arguments.of("Throws an InvalidInputException when the image is larger than 2MB.", teamTestHelper.getTooLargeJpg(), ErrorCode.T_INVALID_SIZE),
                    Arguments.of("Throws an InvalidInputException when the image is not actually an image.", teamTestHelper.getTextFile(), ErrorCode.T_INVALID_EXTENSION),
                    Arguments.of("Throws an InvalidInputException when the image has a valid MimeType but is not actually an image.", teamTestHelper.getTextFileWithImageMimeType(), ErrorCode.T_INVALID_EXTENSION),
                    Arguments.of("Throws an InvalidInputException when the image is not square.", teamTestHelper.getRectangularJpg(), ErrorCode.T_INVALID_DIMENSIONS)
            );
        }
    }

    @Nested
    @DisplayName("Image conversion")
    public class ImageConversion {
        /**
         * Tests if a square image wil be converted and set to the savableImage property of the BusinessImage.
         *
         * @throws IOException The exception potentially thrown by TeamTestHelper.getValidJpg()
         */
        @Test
        @DisplayName("Converts a square image and sets it to the savableImage property of the BusinessImage.")
        public void validJpg() throws IOException {
            MultipartFile inputImage = teamTestHelper.getValidJpg();
            BusinessImage businessImage = new BusinessImage(inputImage.getContentType(), inputImage);
            assertDoesNotThrow(businessImage::convertInputImageToSavableImage);
            assertNotNull(businessImage.getSavableImage());
        }
    }
}
