package com.example.filetransfer.Models;
import org.springframework.data.annotation.Id;

public class Todo {

    public Todo() {
    }

    public Todo(String description, String details) {
        this.description = description;
        this.details = details;
    }

    @Id
    private Long id;

    private String description;

    private String details;


    public Long getId() {
        System.out.println("here "+id);
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}