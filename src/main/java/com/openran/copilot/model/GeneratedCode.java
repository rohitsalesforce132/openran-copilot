package com.openran.copilot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents generated xApp code with metadata.
 * Contains all generated files and validation results.
 */
public class GeneratedCode {
    private String id;
    private String xAppName;
    private LocalDateTime generatedAt;
    private String description;
    private Map<String, String> files = new HashMap<>(); // filename -> content
    private List<String> warnings = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    private boolean isValid;
    private String generatingPrompt;
    private List<String> referencedSpecs = new ArrayList<>();

    public GeneratedCode() {
        this.id = java.util.UUID.randomUUID().toString();
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXAppName() {
        return xAppName;
    }

    public void setXAppName(String xAppName) {
        this.xAppName = xAppName;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getGeneratingPrompt() {
        return generatingPrompt;
    }

    public void setGeneratingPrompt(String generatingPrompt) {
        this.generatingPrompt = generatingPrompt;
    }

    public List<String> getReferencedSpecs() {
        return referencedSpecs;
    }

    public void setReferencedSpecs(List<String> referencedSpecs) {
        this.referencedSpecs = referencedSpecs;
    }

    public void addReferencedSpec(String specId) {
        this.referencedSpecs.add(specId);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    @Override
    public String toString() {
        return "GeneratedCode{" +
                "id='" + id + '\'' +
                ", xAppName='" + xAppName + '\'' +
                ", fileCount=" + files.size() +
                ", valid=" + isValid +
                ", errorCount=" + errors.size() +
                '}';
    }
}
