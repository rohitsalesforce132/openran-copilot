package com.openran.copilot.troubleshoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyzes network symptoms to extract key information.
 */
public class SymptomAnalyzer {

    /**
     * Analyze a symptom description and extract key metrics.
     */
    public SymptomAnalysis analyze(String symptom) {
        SymptomAnalysis analysis = new SymptomAnalysis();
        analysis.setOriginalDescription(symptom);

        // Extract metrics
        analysis.setDropRate(extractPercentage(symptom, "drop rate"));
        analysis.setThroughput(extractValueWithUnit(symptom, "throughput", "Mbps"));
        analysis.setLatency(extractValueWithUnit(symptom, "latency", "ms"));
        analysis.setPrbUtilization(extractPercentage(symptom, "PRB"));
        analysis.setHandoverSuccessRate(extractPercentage(symptom, "handover"));

        // Extract time context
        analysis.setTimeContext(extractTimeContext(symptom));

        // Extract cell/site information
        analysis.setCellId(extractCellId(symptom));

        // Categorize symptom
        analysis.setCategory(categorizeSymptom(symptom));

        return analysis;
    }

    /**
     * Extract percentage value from text.
     */
    private Double extractPercentage(String text, String keyword) {
        Pattern pattern = Pattern.compile(keyword + "\\s*(?:of|exceeds?|is)?\\s*(\\d+(?:\\.\\d+)?)\\s*%");
        Matcher matcher = pattern.matcher(text.toLowerCase());

        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Extract value with unit.
     */
    private Double extractValueWithUnit(String text, String keyword, String expectedUnit) {
        Pattern pattern = Pattern.compile(keyword + "\\s*(?:is|of)?\\s*(\\d+(?:\\.\\d+)?)\\s*" + expectedUnit);
        Matcher matcher = pattern.matcher(text.toLowerCase());

        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Extract time context from symptom.
     */
    private String extractTimeContext(String text) {
        if (text.toLowerCase().contains("peak hour") || text.toLowerCase().contains("peak hours")) {
            return "PEAK_HOURS";
        } else if (text.toLowerCase().contains("off-peak") || text.toLowerCase().contains("off peak")) {
            return "OFF_PEAK";
        } else if (text.toLowerCase().contains("morning")) {
            return "MORNING";
        } else if (text.toLowerCase().contains("evening")) {
            return "EVENING";
        } else if (text.toLowerCase().contains("night")) {
            return "NIGHT";
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * Extract cell ID from symptom.
     */
    private String extractCellId(String text) {
        Pattern pattern = Pattern.compile("cell\\s*(?:site)?\\s*[:#]?\\s*([\\w-]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * Categorize the symptom type.
     */
    private String categorizeSymptom(String text) {
        String lower = text.toLowerCase();

        if (lower.contains("drop") || lower.contains("call failure")) {
            return "DROPS";
        } else if (lower.contains("throughput") || lower.contains("speed")) {
            return "THROUGHPUT";
        } else if (lower.contains("latency") || lower.contains("delay")) {
            return "LATENCY";
        } else if (lower.contains("handover") || lower.contains("ho")) {
            return "HANDOVER";
        } else if (lower.contains("interference")) {
            return "INTERFERENCE";
        } else if (lower.contains("capacity") || lower.contains("congestion")) {
            return "CAPACITY";
        } else {
            return "GENERAL";
        }
    }

    /**
     * Symptom analysis result.
     */
    public static class SymptomAnalysis {
        private String originalDescription;
        private Double dropRate;
        private Double throughput;
        private Double latency;
        private Double prbUtilization;
        private Double handoverSuccessRate;
        private String timeContext;
        private String cellId;
        private String category;
        private Map<String, Object> extractedMetrics = new HashMap<>();

        // Getters and Setters
        public String getOriginalDescription() {
            return originalDescription;
        }

        public void setOriginalDescription(String originalDescription) {
            this.originalDescription = originalDescription;
        }

        public Double getDropRate() {
            return dropRate;
        }

        public void setDropRate(Double dropRate) {
            this.dropRate = dropRate;
            if (dropRate != null) {
                extractedMetrics.put("dropRate", dropRate);
            }
        }

        public Double getThroughput() {
            return throughput;
        }

        public void setThroughput(Double throughput) {
            this.throughput = throughput;
            if (throughput != null) {
                extractedMetrics.put("throughput", throughput);
            }
        }

        public Double getLatency() {
            return latency;
        }

        public void setLatency(Double latency) {
            this.latency = latency;
            if (latency != null) {
                extractedMetrics.put("latency", latency);
            }
        }

        public Double getPrbUtilization() {
            return prbUtilization;
        }

        public void setPrbUtilization(Double prbUtilization) {
            this.prbUtilization = prbUtilization;
            if (prbUtilization != null) {
                extractedMetrics.put("prbUtilization", prbUtilization);
            }
        }

        public Double getHandoverSuccessRate() {
            return handoverSuccessRate;
        }

        public void setHandoverSuccessRate(Double handoverSuccessRate) {
            this.handoverSuccessRate = handoverSuccessRate;
            if (handoverSuccessRate != null) {
                extractedMetrics.put("handoverSuccessRate", handoverSuccessRate);
            }
        }

        public String getTimeContext() {
            return timeContext;
        }

        public void setTimeContext(String timeContext) {
            this.timeContext = timeContext;
        }

        public String getCellId() {
            return cellId;
        }

        public void setCellId(String cellId) {
            this.cellId = cellId;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Map<String, Object> getExtractedMetrics() {
            return extractedMetrics;
        }

        @Override
        public String toString() {
            return "SymptomAnalysis{" +
                    "category='" + category + '\'' +
                    ", dropRate=" + dropRate +
                    ", throughput=" + throughput +
                    ", latency=" + latency +
                    ", prbUtilization=" + prbUtilization +
                    ", timeContext='" + timeContext + '\'' +
                    ", cellId='" + cellId + '\'' +
                    '}';
        }
    }
}
