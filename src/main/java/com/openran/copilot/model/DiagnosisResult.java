package com.openran.copilot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Diagnosis result with root cause and fix recommendations.
 */
public class DiagnosisResult {
    private String id;
    private TroubleshootingCase troubleshootingCase;
    private LocalDateTime timestamp;
    private String rootCause;
    private double confidence; // 0.0 to 1.0
    private List<String> evidence = new ArrayList<>();
    private List<String> recommendedActions = new ArrayList<>();
    private List<String> diagnosticCommands = new ArrayList<>();
    private String priority; // "low", "medium", "high"
    private String referencedSpecSection;

    public DiagnosisResult() {
        this.id = java.util.UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public DiagnosisResult(String rootCause, double confidence) {
        this();
        this.rootCause = rootCause;
        this.confidence = confidence;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TroubleshootingCase getTroubleshootingCase() {
        return troubleshootingCase;
    }

    public void setTroubleshootingCase(TroubleshootingCase troubleshootingCase) {
        this.troubleshootingCase = troubleshootingCase;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public List<String> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<String> evidence) {
        this.evidence = evidence;
    }

    public void addEvidence(String evidenceItem) {
        this.evidence.add(evidenceItem);
    }

    public List<String> getRecommendedActions() {
        return recommendedActions;
    }

    public void setRecommendedActions(List<String> recommendedActions) {
        this.recommendedActions = recommendedActions;
    }

    public void addRecommendedAction(String action) {
        this.recommendedActions.add(action);
    }

    public List<String> getDiagnosticCommands() {
        return diagnosticCommands;
    }

    public void setDiagnosticCommands(List<String> diagnosticCommands) {
        this.diagnosticCommands = diagnosticCommands;
    }

    public void addDiagnosticCommand(String command) {
        this.diagnosticCommands.add(command);
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReferencedSpecSection() {
        return referencedSpecSection;
    }

    public void setReferencedSpecSection(String referencedSpecSection) {
        this.referencedSpecSection = referencedSpecSection;
    }

    @Override
    public String toString() {
        return "DiagnosisResult{" +
                "rootCause='" + rootCause + '\'' +
                ", confidence=" + confidence +
                ", priority='" + priority + '\'' +
                ", actionCount=" + recommendedActions.size() +
                '}';
    }
}
