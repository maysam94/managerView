package com.internship.managerview.business.models;


import com.internship.managerview.util.ImageUtil;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.exceptions.InvalidInputException;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BusinessImage {

    private String id;
    private String type;
    private MultipartFile inputImage;
    private byte[] savableImage;

    /**
     * Creates a BusinessImage instance for a new team.
     *
     * @param type       The original file type of the image
     * @param inputImage The full image as a MultipartFile
     */
    public BusinessImage(String type, MultipartFile inputImage) {
        this.type = type;
        this.inputImage = inputImage;
    }

    /**
     * Creates a BusinessImage instance for a saved avatar.
     *
     * @param type         The file type of the image
     * @param savableImage The image itself as a byte array
     */
    public BusinessImage(String type, byte[] savableImage) {
        this.type = type;
        this.savableImage = savableImage;
    }

    /**
     * Validates the size, extension and mimetype of the image.
     *
     * @throws IOException           in case of access errors in validateMimeType() (if the temporary store fails)
     * @throws InvalidInputException when the image is invalid
     */
    public void validate() throws IOException, InvalidInputException {
        validateSize();
        validateExtension();
        validateMimeType();
        validateDimensions();
    }

    /**
     * Converts the MultipartFile image to a byte array so it can be saved to the database.
     *
     * @throws IOException in case of access errors in ImageIO methods (if the temporary store fails)
     */
    public void convertInputImageToSavableImage() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(inputImage.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BufferedImage originalImage = ImageIO.read(inputStream);
        BufferedImage scaledImage = ImageUtil.resizeImage(originalImage, 200, 200);
        ImageIO.write(scaledImage, "jpg", byteArrayOutputStream);
        savableImage = byteArrayOutputStream.toByteArray();
    }

    /**
     * Validates that the image is no more than 2MB in size.
     *
     * @throws InvalidInputException when the image size exceeds 2MB
     */
    private void validateSize() throws InvalidInputException {
        if (inputImage.getSize() > 2000000) {
            throw new InvalidInputException(ErrorCode.T_INVALID_SIZE);
        }
    }

    /**
     * Validates that the file extension is either "jpg", "jpeg" or "png".
     *
     * @throws InvalidInputException when the file extension is invalid

     */
    private void validateExtension() throws InvalidInputException {
        String extension = FilenameUtils.getExtension(inputImage.getOriginalFilename());
        if (extension == null || !extension.matches("((jpe?g|png)$)")) {
            throw new InvalidInputException(ErrorCode.T_INVALID_EXTENSION);
        }
    }

    /**
     * Validates that the mimetype is either "image/jpg", "image/jpeg" or "image/png"
     *
     * @throws IOException           if the stream can not be read by Tika
     * @throws InvalidInputException when the mimetype is invalid
     */
    private void validateMimeType() throws IOException, InvalidInputException {
        Tika tika = new Tika();
        String mimeType = tika.detect(inputImage.getInputStream());
        if (!mimeType.matches("((image/jpe?g|image/png)$)")) {
            throw new InvalidInputException(ErrorCode.T_INVALID_MIME_TYPE);
        }
    }

    /**
     * Validates that the image is square.
     *
     * @throws InvalidInputException when the image is not square
     */
    private void validateDimensions() throws InvalidInputException, IOException {
        InputStream inputStream = new ByteArrayInputStream(inputImage.getBytes());
        BufferedImage originalImage = ImageIO.read(inputStream);
        if (originalImage.getHeight() != originalImage.getWidth()) {
            throw new InvalidInputException(ErrorCode.T_INVALID_DIMENSIONS);
        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public byte[] getSavableImage() {
        return savableImage;
    }
}
