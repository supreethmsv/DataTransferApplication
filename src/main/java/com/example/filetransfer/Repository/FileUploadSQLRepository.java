package com.example.filetransfer.Repository;
import org.springframework.data.annotation.Id;


public class FileUploadSQLRepository {

    public FileUploadSQLRepository() {
    }

    public FileUploadSQLRepository(String description, String details) {
        this.description = description;
        this.details = details;
    }

    @Id
    private Long id;

    private String description;

    private String details;

    private boolean done;

    public Long getId() {
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