package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

/** модель, описывающая кандидата*/
public class Candidate {
    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "name", "name",
            "description", "description",
            "creation_date", "creationDate",
            "file_id", "fileId"
    );
    private int id;

    private String name;

    private String description;

    private final LocalDateTime creationDate;

    private int fileId;

    public Candidate(int id, String name, String description, int fileId) {
        this.id = id;
        this.name = name;
        this.description = description;
        creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        this.fileId = fileId;
    }

    public Candidate() {
        creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) object;
        return id == candidate.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
