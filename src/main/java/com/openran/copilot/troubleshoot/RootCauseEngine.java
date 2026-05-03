package com.openran.copilot.troubleshoot;

import com.openran.copilot.model.DiagnosisResult;
import com.openran.copilot.model.SpecEmbedding;
import com.openran.copilot.rag.SpecKnowledgeBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Identifies root causes using spec knowledge and symptom analysis.
 */
public class RootCauseEngine {
    private final SpecKnowledgeBase knowledgeBase;
    private final SymptomAnalyzer symptomAnalyzer;

    public RootCauseEngine(SpecKnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
        this.symptomAnalyzer = new SymptomAnalyzer();
    }

    /**
     * Identify root causes for a set of symptoms.
     */
    public List<DiagnosisResult> identifyRootCauses(String symptomDescription) {
        return identifyRootCauses(symptomDescription, 3);
    }

    /**
     * Identify root causes with specified count.
     */
    public List<DiagnosisResult> identifyRootCauses(String symptomDescription, int count) {
        // Analyze symptoms
        SymptomAnalyzer.SymptomAnalysis analysis = symptomAnalyzer.analyze(symptomDescription);

        // Build queries based on symptom category
        List<String> queries = buildQueries(analysis);

        // Retrieve relevant spec content
        List<SpecEmbedding> relevantSpecs = retrieveRelevantSpecs(queries);

        // Generate root cause hypotheses
        List<DiagnosisResult> results = generateHypotheses(analysis, relevantSpecs);

        // Sort by confidence and return top N
        return results.stream()
                .sorted((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Build search queries based on symptom analysis.
     */
    private List<String> buildQueries(SymptomAnalyzer.SymptomAnalysis analysis) {
        List<String> queries = new ArrayList<>();
        String category = analysis.getCategory();

        switch (category) {
            case "DROPS":
                queries.add("O-RAN call drop causes handover failure");
                queries.add("O-RAN radio link failure interference coverage");
                queries.add("O-RAN drop rate threshold troubleshooting");
                break;
            case "THROUGHPUT":
                queries.add("O-RAN low throughput causes PRB utilization");
                queries.add("O-RAN scheduling algorithm MIMO");
                queries.add("O-RAN throughput optimization modulation");
                break;
            case "LATENCY":
                queries.add("O-RAN latency causes processing delay");
                queries.add("O-RAN transport network latency");
                queries.add("O-RAN real-time requirements Near-RT RIC");
                break;
            case "HANDOVER":
                queries.add("O-RAN handover failure causes hysteresis");
                queries.add("O-RAN handover procedure A3 event");
                queries.add("O-RAN handover optimization configuration");
                break;
            case "INTERFERENCE":
                queries.add("O-RAN interference detection mitigation");
                queries.add("O-RAN co-channel interference PCI");
                queries.add("O-RAN interference management");
                break;
            case "CAPACITY":
                queries.add("O-RAN capacity exhaustion PRB congestion");
                queries.add("O-RAN load balancing optimization");
                queries.add("O-RAN cell splitting");
                break;
            default:
                queries.add("O-RAN troubleshooting network issues");
                queries.add("O-RAN KPI analysis root cause");
                break;
        }

        // Add time-specific queries
        if ("PEAK_HOURS".equals(analysis.getTimeContext())) {
            queries.add("O-RAN peak hours congestion optimization");
        }

        return queries;
    }

    /**
     * Retrieve relevant specification content.
     */
    private List<SpecEmbedding> retrieveRelevantSpecs(List<String> queries) {
        List<SpecEmbedding> allSpecs = new ArrayList<>();

        for (String query : queries) {
            List<SpecEmbedding> results = knowledgeBase.retrieve(query, 3, null);
            allSpecs.addAll(results);
        }

        // Deduplicate by ID
        return allSpecs.stream()
                .collect(Collectors.toMap(SpecEmbedding::getId, e -> e, (a, b) -> a))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * Generate root cause hypotheses based on analysis and specs.
     */
    private List<DiagnosisResult> generateHypotheses(
            SymptomAnalyzer.SymptomAnalysis analysis,
            List<SpecEmbedding> specs
    ) {
        List<DiagnosisResult> hypotheses = new ArrayList<>();
        String category = analysis.getCategory();

        switch (category) {
            case "DROPS":
                hypotheses.add(createHypothesis(
                        "Interference causing poor signal quality",
                        0.85,
                        specs,
                        "Check RSRP and RSRQ levels",
                        "Analyze interference heatmap",
                        "Adjust antenna tilt or power"
                ));
                hypotheses.add(createHypothesis(
                        "Handover failure during mobility",
                        0.75,
                        specs,
                        "Review handover success rate",
                        "Check A3 event parameters",
                        "Optimize hysteresis and TTT"
                ));
                hypotheses.add(createHypothesis(
                        "Coverage hole in service area",
                        0.65,
                        specs,
                        "Map coverage with drive test",
                        "Identify weak signal areas",
                        "Consider small cell deployment"
                ));
                break;

            case "THROUGHPUT":
                if (analysis.getPrbUtilization() != null && analysis.getPrbUtilization() > 80) {
                    hypotheses.add(createHypothesis(
                            "Capacity congestion - high PRB utilization",
                            0.90,
                            specs,
                            "Check PRB utilization per cell",
                            "Analyze user distribution",
                            "Enable load balancing"
                    ));
                }
                hypotheses.add(createHypothesis(
                        "Poor radio conditions limiting MCS",
                        0.75,
                        specs,
                        "Check SINR and CQI",
                        "Review antenna configuration",
                        "Optimize MIMO settings"
                ));
                hypotheses.add(createHypothesis(
                        "Backhaul bottleneck",
                        0.60,
                        specs,
                        "Monitor transport link utilization",
                        "Check for packet loss",
                        "Upgrade backhaul capacity"
                ));
                break;

            case "HANDOVER":
                hypotheses.add(createHypothesis(
                        "Handover parameters misconfigured",
                        0.80,
                        specs,
                        "Review A3 offset values",
                        "Check TTT (Time to Trigger)",
                        "Compare with neighbor cells"
                ));
                hypotheses.add(createHypothesis(
                        "Neighbor relation missing",
                        0.70,
                        specs,
                        "Verify neighbor cell list",
                        "Check ANR (Automatic Neighbor Relation)",
                        "Add missing neighbors manually"
                ));
                hypotheses.add(createHypothesis(
                        "Load imbalance causing ping-pong handovers",
                        0.65,
                        specs,
                        "Analyze handover frequency",
                        "Check load distribution",
                        "Enable load balancing"
                ));
                break;

            case "INTERFERENCE":
                hypotheses.add(createHypothesis(
                        "Co-channel interference from adjacent cells",
                        0.85,
                        specs,
                        "Analyze PCI collision/confusion",
                        "Check frequency planning",
                        "Reallocate PCI or frequency"
                ));
                hypotheses.add(createHypothesis(
                        "External interference source",
                        0.70,
                        specs,
                        "Perform interference hunting",
                        "Check spectrum analyzer",
                        "Identify and mitigate source"
                ));
                break;

            default:
                hypotheses.add(createHypothesis(
                        "Multiple potential causes - need further investigation",
                        0.50,
                        specs,
                        "Collect comprehensive KPI data",
                        "Perform drive test",
                        "Analyze with network optimization tools"
                ));
                break;
        }

        return hypotheses;
    }

    /**
     * Create a diagnosis result (hypothesis).
     */
    private DiagnosisResult createHypothesis(
            String rootCause,
            double confidence,
            List<SpecEmbedding> specs,
            String diagnosticCmd1,
            String diagnosticCmd2,
            String recommendedAction1
    ) {
        DiagnosisResult result = new DiagnosisResult(rootCause, confidence);

        // Add evidence from specs
        for (SpecEmbedding spec : specs) {
            result.addEvidence("Referenced: " + spec.getMetadata().get("specTitle"));
        }

        // Add diagnostic commands
        result.addDiagnosticCommand(diagnosticCmd1);
        result.addDiagnosticCommand(diagnosticCmd2);

        // Add recommended actions
        result.addRecommendedAction(recommendedAction1);

        // Set priority based on confidence
        if (confidence >= 0.8) {
            result.setPriority("high");
        } else if (confidence >= 0.6) {
            result.setPriority("medium");
        } else {
            result.setPriority("low");
        }

        return result;
    }

    /**
     * Get the symptom analyzer.
     */
    public SymptomAnalyzer getSymptomAnalyzer() {
        return symptomAnalyzer;
    }
}
