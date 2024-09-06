package com.internship.managerview.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {

    /**
     * Resizes the image to the requested dimensions.
     *
     * @param originalImage The image to be resized
     * @param targetWidth   The target width for the image
     * @param targetHeight  The target height for the image
     * @return The resized BufferedImage
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage resultingImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        resultingImage.getGraphics().drawImage(scaledImage, 0, 0, null);
        return resultingImage;
    }

}
