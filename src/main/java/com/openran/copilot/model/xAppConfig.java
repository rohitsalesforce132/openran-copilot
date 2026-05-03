package com.openran.copilot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration parameters for an xApp.
 * Includes RIC connection details, service model subscriptions,
 * and application-specific settings.
 */
public class xAppConfig {
    private String xAppName;
    private String ricAddress;
    private int ricPort;
    private String serviceModel;
    private List<SubscriptionConfig> subscriptions = new ArrayList<>();
    private Map<String, Object> appConfig = new HashMap<>();
    private Map<String, String> a1PolicyEndpoints = new HashMap<>();
    private LoggingConfig logging = new LoggingConfig();

    public static class SubscriptionConfig {
        private String serviceModelName;
        private List<String> indicators = new ArrayList<>();
        private int reportingIntervalMs;

        public SubscriptionConfig() {
        }

        public SubscriptionConfig(String serviceModelName, int reportingIntervalMs) {
            this.serviceModelName = serviceModelName;
            this.reportingIntervalMs = reportingIntervalMs;
        }

        public String getServiceModelName() {
            return serviceModelName;
        }

        public void setServiceModelName(String serviceModelName) {
            this.serviceModelName = serviceModelName;
        }

        public List<String> getIndicators() {
            return indicators;
        }

        public void setIndicators(List<String> indicators) {
            this.indicators = indicators;
        }

        public void addIndicator(String indicator) {
            this.indicators.add(indicator);
        }

        public int getReportingIntervalMs() {
            return reportingIntervalMs;
        }

        public void setReportingIntervalMs(int reportingIntervalMs) {
            this.reportingIntervalMs = reportingIntervalMs;
        }
    }

    public static class LoggingConfig {
        private String level = "INFO";
        private boolean enableMetrics = true;
        private boolean enableTracing = false;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public boolean isEnableMetrics() {
            return enableMetrics;
        }

        public void setEnableMetrics(boolean enableMetrics) {
            this.enableMetrics = enableMetrics;
        }

        public boolean isEnableTracing() {
            return enableTracing;
        }

        public void setEnableTracing(boolean enableTracing) {
            this.enableTracing = enableTracing;
        }
    }

    // Getters and Setters
    public String getXAppName() {
        return xAppName;
    }

    public void setXAppName(String xAppName) {
        this.xAppName = xAppName;
    }

    public String getRicAddress() {
        return ricAddress;
    }

    public void setRicAddress(String ricAddress) {
        this.ricAddress = ricAddress;
    }

    public int getRicPort() {
        return ricPort;
    }

    public void setRicPort(int ricPort) {
        this.ricPort = ricPort;
    }

    public String getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(String serviceModel) {
        this.serviceModel = serviceModel;
    }

    public List<SubscriptionConfig> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionConfig> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void addSubscription(SubscriptionConfig subscription) {
        this.subscriptions.add(subscription);
    }

    public Map<String, Object> getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(Map<String, Object> appConfig) {
        this.appConfig = appConfig;
    }

    public Map<String, String> getA1PolicyEndpoints() {
        return a1PolicyEndpoints;
    }

    public void setA1PolicyEndpoints(Map<String, String> a1PolicyEndpoints) {
        this.a1PolicyEndpoints = a1PolicyEndpoints;
    }

    public LoggingConfig getLogging() {
        return logging;
    }

    public void setLogging(LoggingConfig logging) {
        this.logging = logging;
    }
}
