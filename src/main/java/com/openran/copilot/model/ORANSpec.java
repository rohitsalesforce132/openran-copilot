package com.openran.copilot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an O-RAN specification document.
 * O-RAN specifications are published at o-ran.org and cover architecture,
 * interfaces, use cases, and implementation guidelines.
 */
public class ORANSpec {
    private String id;
    private String title;
    private String version;
    private String category; // Architecture, Interface, Service Model, Use Case
    private String description;
    private LocalDateTime publishedDate;
    private String sourceUrl;
    private List<SpecSection> sections = new ArrayList<>();

    public ORANSpec() {
    }

    public ORANSpec(String id, String title, String version, String category) {
        this.id = id;
        this.title = title;
        this.version = version;
        this.category = category;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public List<SpecSection> getSections() {
        return sections;
    }

    public void setSections(List<SpecSection> sections) {
        this.sections = sections;
    }

    public void addSection(SpecSection section) {
        this.sections.add(section);
    }

    @Override
    public String toString() {
        return "ORANSpec{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", version='" + version + '\'' +
                ", category='" + category + '\'' +
                ", sections=" + sections.size() +
                '}';
    }
}
