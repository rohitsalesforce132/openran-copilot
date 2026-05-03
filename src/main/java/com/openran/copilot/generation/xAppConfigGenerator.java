package com.openran.copilot.generation;

import com.openran.copilot.model.ServiceModel;
import com.openran.copilot.model.xAppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates xApp configuration files from specifications.
 */
public class xAppConfigGenerator {

    /**
     * Generate configuration for a KPI monitoring xApp.
     */
    public xAppConfig generateMonitoringConfig(
            String xAppName,
            String ricAddress,
            int ricPort,
            String indicatorName,
            double thresholdValue,
            int reportingIntervalMs
    ) {
        xAppConfig config = new xAppConfig();
        config.setXAppName(xAppName);
        config.setRicAddress(ricAddress);
        config.setRicPort(ricPort);
        config.setServiceModel("KPM");

        // Add subscription
        xAppConfig.SubscriptionConfig subscription = new xAppConfig.SubscriptionConfig(
                "KPM", reportingIntervalMs
        );
        subscription.addIndicator(indicatorName);
        config.addSubscription(subscription);

        // Add app config
        config.getAppConfig().put("thresholds." + indicatorName, thresholdValue);
        config.getAppConfig().put("alerting.enabled", true);
        config.getAppConfig().put("alerting.severity", "MEDIUM");

        return config;
    }

    /**
     * Generate configuration for a handover optimization xApp.
     */
    public xAppConfig generateHandoverConfig(
            String xAppName,
            String ricAddress,
            int ricPort,
            double targetHOSuccessRate
    ) {
        xAppConfig config = new xAppConfig();
        config.setXAppName(xAppName);
        config.setRicAddress(ricAddress);
        config.setRicPort(ricPort);
        config.setServiceModel("KPM");

        // Add subscription
        xAppConfig.SubscriptionConfig subscription = new xAppConfig.SubscriptionConfig(
                "KPM", 1000
        );
        subscription.addIndicator("HO.ExecSuccess");
        subscription.addIndicator("HO.ExecFailure");
        config.addSubscription(subscription);

        // Add app config
        config.getAppConfig().put("optimization.targetHOSuccessRate", targetHOSuccessRate);
        config.getAppConfig().put("optimization.adjustmentStep", 0.1);
        config.getAppConfig().put("optimization.minHOHysteresis", 1.0);
        config.getAppConfig().put("optimization.maxHOHysteresis", 10.0);

        return config;
    }

    /**
     * Generate configuration for a load balancing xApp.
     */
    public xAppConfig generateLoadBalancingConfig(
            String xAppName,
            String ricAddress,
            int ricPort,
            List<String> targetCells
    ) {
        xAppConfig config = new xAppConfig();
        config.setXAppName(xAppName);
        config.setRicAddress(ricAddress);
        config.setRicPort(ricPort);
        config.setServiceModel("KPM");

        // Add subscription
        xAppConfig.SubscriptionConfig subscription = new xAppConfig.SubscriptionConfig(
                "KPM", 500
        );
        subscription.addIndicator("RRU.PrbUsedDl");
        subscription.addIndicator("RRU.PrbUsedUl");
        subscription.addIndicator("DRB.UEThpDl");
        subscription.addIndicator("DRB.UEThpUl");
        config.addSubscription(subscription);

        // Add app config
        config.getAppConfig().put("loadBalancing.enabled", true);
        config.getAppConfig().put("loadBalancing.targetCells", targetCells);
        config.getAppConfig().put("loadBalancing.targetLoad", 0.8);
        config.getAppConfig().put("loadBalancing.loadTolerance", 0.1);

        return config;
    }

    /**
     * Generate configuration from a service model.
     */
    public xAppConfig generateFromServiceModel(
            String xAppName,
            String ricAddress,
            int ricPort,
            ServiceModel serviceModel,
            List<String> indicators
    ) {
        xAppConfig config = new xAppConfig();
        config.setXAppName(xAppName);
        config.setRicAddress(ricAddress);
        config.setRicPort(ricPort);
        config.setServiceModel(serviceModel.getName());

        // Add subscription with selected indicators
        xAppConfig.SubscriptionConfig subscription = new xAppConfig.SubscriptionConfig(
                serviceModel.getName(), 1000
        );

        for (String indicator : indicators) {
            subscription.addIndicator(indicator);
        }

        config.addSubscription(subscription);

        return config;
    }

    /**
     * Convert configuration to YAML format.
     */
    public String configToYAML(xAppConfig config) {
        StringBuilder yaml = new StringBuilder();
        yaml.append("xapp:\n");
        yaml.append("  name: ").append(config.getXAppName()).append("\n");
        yaml.append("  serviceModel: ").append(config.getServiceModel()).append("\n");
        yaml.append("\n");
        yaml.append("ric:\n");
        yaml.append("  address: ").append(config.getRicAddress()).append("\n");
        yaml.append("  port: ").append(config.getRicPort()).append("\n");
        yaml.append("\n");
        yaml.append("subscriptions:\n");

        for (xAppConfig.SubscriptionConfig sub : config.getSubscriptions()) {
            yaml.append("  - serviceModel: ").append(sub.getServiceModelName()).append("\n");
            yaml.append("    reportingIntervalMs: ").append(sub.getReportingIntervalMs()).append("\n");
            yaml.append("    indicators:\n");
            for (String ind : sub.getIndicators()) {
                yaml.append("      - ").append(ind).append("\n");
            }
        }

        if (!config.getAppConfig().isEmpty()) {
            yaml.append("\n");
            yaml.append("config:\n");
            for (String key : config.getAppConfig().keySet()) {
                Object value = config.getAppConfig().get(key);
                yaml.append("  ").append(key).append(": ").append(value).append("\n");
            }
        }

        return yaml.toString();
    }
}
