package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;
import java.util.Objects;

/** модель, описывающая вакансию*/

public class Vacancy {
    private int id;

    private String title;

    private String description;

    private final LocalDateTime creationDate;

    private boolean visible;

    private int cityId;

    private int fileId;

    public Vacancy(int id, String title, String description, boolean visible, int cityId, int fileId) {
        this.id = id;
        this.title = title;
        this.description = description;
        creationDate = LocalDateTime.now();
        this.visible = visible;
        this.cityId = cityId;
        this.fileId = fileId;
    }

    public Vacancy() {
        creationDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Vacancy vacancy = (Vacancy) object;
        return id == vacancy.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
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

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
