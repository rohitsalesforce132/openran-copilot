package com.openran.copilot.troubleshoot;

import com.openran.copilot.model.DiagnosisResult;
import com.openran.copilot.model.TroubleshootingCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TroubleshootingService {

    @Inject
    SymptomAnalyzer symptomAnalyzer;

    @Inject
    RootCauseEngine rootCauseEngine;

    @Inject
    FixRecommender fixRecommender;

    public DiagnosisResult diagnose(String symptoms) {
        // Analyze symptoms to identify patterns
        Map<String, Object> symptomPattern = symptomAnalyzer.analyze(symptoms);

        // Determine root cause
        String rootCause = rootCauseEngine.identifyRootCause(symptomPattern);

        // Find similar troubleshooting cases
        List<TroubleshootingCase> similarCases = findSimilarCases(rootCause);

        // Generate recommendations
        List<String> recommendations = fixRecommender.recommendFixes(rootCause, similarCases);

        return new DiagnosisResult(
            symptoms,
            symptomPattern,
            rootCause,
            similarCases,
            recommendations,
            calculateConfidence(similarCases)
        );
    }

    private List<TroubleshootingCase> findSimilarCases(String rootCause) {
        // In production, this would query the knowledge base
        return List.of(
            new TroubleshootingCase(
                "CASE-001",
                "High Cell Dropped Call Rate",
                rootCause,
                "Adjust downlink power and antenna tilt",
                "Network Operations",
                0.92
            ),
            new TroubleshootingCase(
                "CASE-002",
                "Low Throughput at Cell Edge",
                rootCause,
                "Enable inter-frequency load balancing",
                "RAN Optimization",
                0.88
            )
        );
    }

    private double calculateConfidence(List<TroubleshootingCase> cases) {
        if (cases.isEmpty()) return 0.0;
        return cases.stream()
                .mapToDouble(TroubleshootingCase::getSimilarityScore)
                .average()
                .orElse(0.0);
    }

    public List<TroubleshootingCase> getAllCases() {
        return List.of(
            new TroubleshootingCase("CASE-001", "High DCR", "Downlink Interference", "Adjust power", "Ops", 0.92),
            new TroubleshootingCase("CASE-002", "Low Throughput", "Congestion", "Add carrier", "Capacity", 0.88),
            new TroubleshootingCase("CASE-003", "Handover Failures", "Neighbor Missing", "Add neighbor", "Opt", 0.85)
        );
    }
}
