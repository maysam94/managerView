package com.internship.managerview.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SecurityQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String question;

    public SecurityQuestion() {
    }

    public SecurityQuestion(String id, String question) {
        this.id = id;
        this.question = question;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
