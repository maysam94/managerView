package com.internship.managerview.data.entities;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String type;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB", length = 500000)
    private byte[] image;

    /**
     * Creates a Image entity instance.
     *
     */
    public Image() {
    }

    /**
     * Creates a Image entity instance.
     *
     * @param type  The mime type
     * @param image The image as a byte[]
     */
    public Image(String type, byte[] image) {
        this.type = type;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public byte[] getImage() {
        return image;
    }
}
