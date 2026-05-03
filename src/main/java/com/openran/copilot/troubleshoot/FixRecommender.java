package com.openran.copilot.troubleshoot;

import com.openran.copilot.model.TroubleshootingCase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FixRecommender {

    public List<String> recommendFixes(String rootCause, List<TroubleshootingCase> similarCases) {
        List<String> recommendations = new ArrayList<>();

        // Add fix from similar cases
        for (TroubleshootingCase caze : similarCases) {
            recommendations.add("Apply fix from case " + caze.getId() + ": " + caze.getRecommendedAction());
        }

        // Add specific recommendations based on root cause
        recommendations.addAll(getSpecificRecommendations(rootCause));

        return recommendations;
    }

    private List<String> getSpecificRecommendations(String rootCause) {
        if (rootCause.contains("Interference")) {
            return List.of(
                "Check for external interference sources",
                "Adjust antenna down-tilt to reduce interference from neighboring cells",
                "Consider ICIC (Inter-Cell Interference Coordination) activation",
                "Run drive test for interference mapping"
            );
        } else if (rootCause.contains("Congestion")) {
            return List.of(
                "Add additional carrier frequency",
                "Enable load balancing between frequencies",
                "Consider cell splitting or site addition",
                "Implement QoS throttling for non-critical users"
            );
        } else if (rootCause.contains("Coverage")) {
            return List.of(
                "Increase transmit power if within limits",
                "Add repeaters or small cells",
                "Adjust antenna height or orientation",
                "Check for physical obstructions"
            );
        } else if (rootCause.contains("Handover")) {
            return List.of(
                "Review and adjust handover parameters (A3 offset, TTT)",
                "Add missing neighbor relations",
                "Check for missing coverage holes",
                "Adjust mobility parameters"
            );
        }

        return List.of(
            "Collect additional KPI data",
            "Perform drive test for further analysis",
            "Engage RAN optimization team"
        );
    }

    public List<Map<String, String>> generateRemediationPlan(String rootCause) {
        List<Map<String, String>> plan = new ArrayList<>();

        // Generate step-by-step remediation
        if (rootCause.contains("Interference")) {
            plan.add(Map.of("step", "1", "action", "Identify interference source", "priority", "HIGH", "estimatedTime", "2h"));
            plan.add(Map.of("step", "2", "action", "Run interference analysis", "priority", "HIGH", "estimatedTime", "4h"));
            plan.add(Map.of("step", "3", "action", "Implement antenna adjustments", "priority", "MEDIUM", "estimatedTime", "1h"));
            plan.add(Map.of("step", "4", "action", "Monitor KPIs post-adjustment", "priority", "MEDIUM", "estimatedTime", "24h"));
        } else {
            plan.add(Map.of("step", "1", "action", "Analyze KPI trends", "priority", "HIGH", "estimatedTime", "2h"));
            plan.add(Map.of("step", "2", "action", "Determine root cause", "priority", "HIGH", "estimatedTime", "4h"));
            plan.add(Map.of("step", "3", "action", "Implement remediation", "priority", "MEDIUM", "estimatedTime", "2h"));
            plan.add(Map.of("step", "4", "action", "Validate fix", "priority", "MEDIUM", "estimatedTime", "24h"));
        }

        return plan;
    }
}
