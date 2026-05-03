package com.openran.copilot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a network issue being diagnosed.
 * Captures symptoms, context, and diagnostic progress.
 */
public class TroubleshootingCase {
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private List<String> symptoms = new ArrayList<>();
    private Map<String, Object> context = new HashMap<>(); // cell ID, time, etc.
    private String severity; // "low", "medium", "high", "critical"
    private String status; // "open", "diagnosing", "resolved"
    private List<DiagnosisResult> diagnosisResults = new ArrayList<>();
    private String resolvedBy; // User or system
    private String resolutionNotes;

    public TroubleshootingCase() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "open";
    }

    public TroubleshootingCase(String title, String severity) {
        this();
        this.title = title;
        this.severity = severity;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public void addSymptom(String symptom) {
        this.symptoms.add(symptom);
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public void addContext(String key, Object value) {
        this.context.put(key, value);
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public List<DiagnosisResult> getDiagnosisResults() {
        return diagnosisResults;
    }

    public void setDiagnosisResults(List<DiagnosisResult> diagnosisResults) {
        this.diagnosisResults = diagnosisResults;
    }

    public void addDiagnosisResult(DiagnosisResult result) {
        this.diagnosisResults.add(result);
        this.updatedAt = LocalDateTime.now();
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public boolean isResolved() {
        return "resolved".equals(status);
    }

    @Override
    public String toString() {
        return "TroubleshootingCase{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", severity='" + severity + '\'' +
                ", status='" + status + '\'' +
                ", symptomCount=" + symptoms.size() +
                '}';
    }
}
