package com.openran.copilot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an xApp code generation template.
 * Templates contain boilerplate code for common xApp patterns.
 */
public class xAppTemplate {
    private String id;
    private String name;
    private String description;
    private String category; // "monitoring", "optimization", "handover", etc.
    private List<String> tags = new ArrayList<>();
    private Map<String, String> files = new HashMap<>(); // filename -> content
    private String serviceModel; // KPM, RC, GNB-NRT, etc.
    private Map<String, Object> defaultConfig = new HashMap<>();

    public xAppTemplate() {
    }

    public xAppTemplate(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public void addFile(String filename, String content) {
        this.files.put(filename, content);
    }

    public String getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(String serviceModel) {
        this.serviceModel = serviceModel;
    }

    public Map<String, Object> getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(Map<String, Object> defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    @Override
    public String toString() {
        return "xAppTemplate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", fileCount=" + files.size() +
                ", serviceModel='" + serviceModel + '\'' +
                '}';
    }
}
