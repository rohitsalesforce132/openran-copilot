package com.openran.copilot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * O-RAN service model definition.
 * Service models define the data structure for E2 interface subscriptions.
 */
public class ServiceModel {
    private String id;
    private String name;
    private String version;
    private String description;
    private String category; // "KPM", "RC", "GNB-NRT", "EN-DC", "MAC", "RLC"
    private List<IndicatorDefinition> indicators = new ArrayList<>();
    private String specReference;

    public static class IndicatorDefinition {
        private String id;
        private String name;
        private String dataType; // "int", "float", "boolean", "string"
        private String unit;
        private String description;
        private boolean mandatory;

        public IndicatorDefinition() {
        }

        public IndicatorDefinition(String id, String name, String dataType) {
            this.id = id;
            this.name = name;
            this.dataType = dataType;
        }

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

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }
    }

    public ServiceModel() {
    }

    public ServiceModel(String id, String name, String category) {
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public List<IndicatorDefinition> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<IndicatorDefinition> indicators) {
        this.indicators = indicators;
    }

    public void addIndicator(IndicatorDefinition indicator) {
        this.indicators.add(indicator);
    }

    public String getSpecReference() {
        return specReference;
    }

    public void setSpecReference(String specReference) {
        this.specReference = specReference;
    }

    @Override
    public String toString() {
        return "ServiceModel{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", category='" + category + '\'' +
                ", indicatorCount=" + indicators.size() +
                '}';
    }
}
