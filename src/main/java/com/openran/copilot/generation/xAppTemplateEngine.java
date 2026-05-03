package com.openran.copilot.generation;

import com.openran.copilot.model.xAppTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages xApp code templates.
 * Provides template expansion with variable substitution.
 */
public class xAppTemplateEngine {
    private final Map<String, xAppTemplate> templates = new HashMap<>();

    public xAppTemplateEngine() {
        initializeBuiltinTemplates();
    }

    /**
     * Initialize built-in xApp templates.
     */
    private void initializeBuiltinTemplates() {
        // Monitoring xApp template
        xAppTemplate monitoring = new xAppTemplate("monitoring", "KPI Monitoring xApp", "monitoring");
        monitoring.setDescription("Monitors RAN KPIs and generates alerts when thresholds are exceeded");
        monitoring.addTag("kpi");
        monitoring.addTag("monitoring");
        monitoring.addTag("alerting");
        monitoring.setServiceModel("KPM");

        monitoring.addFile("KpiMonitorXApp.java", """
package com.openran.xapp;

import org.o-ran.e2.E2Node;
import org.o-ran.e2.indication.Indication;
import org.o-ran.e2.subscription.Subscription;
import org.o-ran.e2.service.kpm.KPMIndication;
import org.o-ran.e2.service.kpm.KPMSubscription;

/**
 * {{XAPP_NAME}} - KPI Monitoring xApp
 * {{DESCRIPTION}}
 */
public class {{XAPP_CLASS_NAME}} extends AbstractXApp {

    private static final String XAPP_NAME = "{{XAPP_NAME}}";
    private static final double THRESHOLD_{{INDICATOR_UPPER}} = {{THRESHOLD_VALUE}};

    @Override
    public void onStart() {
        log.info("Starting {} xApp", XAPP_NAME);
        subscribeToKPM();
    }

    @Override
    public void onIndication(Indication indication) {
        if (indication instanceof KPMIndication) {
            handleKPMIndication((KPMIndication) indication);
        }
    }

    private void subscribeToKPM() {
        KPMSubscription subscription = new KPMSubscription();
        subscription.addIndicator("{{INDICATOR_NAME}}");
        subscription.setReportingIntervalMs({{REPORTING_INTERVAL_MS}});

        e2Client.subscribe(subscription, this::onIndication);
        log.info("Subscribed to KPM service model");
    }

    private void handleKPMIndication(KPMIndication indication) {
        double value = indication.getIndicatorValue("{{INDICATOR_NAME}}");

        if (value > THRESHOLD_{{INDICATOR_UPPER}}) {
            log.warn("{{INDICATOR_NAME}} exceeded threshold: {} > {}", value, THRESHOLD_{{INDICATOR_UPPER}});
            triggerAlert(value);
        }
    }

    private void triggerAlert(double currentValue) {
        Alert alert = new Alert();
        alert.setSource(XAPP_NAME);
        alert.setType("THRESHOLD_EXCEEDED");
        alert.setMessage("{{INDICATOR_NAME}} exceeded threshold: " + currentValue);
        alert.setSeverity("{{ALERT_SEVERITY}}");

        a1Client.reportAlert(alert);
    }
}
""");

        monitoring.addFile("application.yaml", """
xapp:
  name: {{XAPP_NAME}}
  version: "1.0.0"
  description: "{{DESCRIPTION}}"

ric:
  address: {{RIC_ADDRESS}}
  port: {{RIC_PORT}}

subscription:
  serviceModel: KPM
  indicators:
    - {{INDICATOR_NAME}}
  reportingIntervalMs: {{REPORTING_INTERVAL_MS}}

thresholds:
  {{INDICATOR_NAME}}: {{THRESHOLD_VALUE}}

alerts:
  severity: {{ALERT_SEVERITY}}
  endpoints:
    - {{A1_ENDPOINT}}
""");

        templates.put("monitoring", monitoring);

        // Handover optimization xApp template
        xAppTemplate handover = new xAppTemplate("handover", "Handover Optimization xApp", "optimization");
        handover.setDescription("Optimizes handover parameters based on network conditions");
        handover.addTag("handover");
        handover.addTag("optimization");
        handover.addTag("mobility");
        handover.setServiceModel("KPM");

        handover.addFile("HandoverOptimizerXApp.java", """
package com.openran.xapp;

import org.o-ran.e2.indication.Indication;
import org.o-ran.e2.service.kpm.KPMIndication;

/**
 * {{XAPP_NAME}} - Handover Optimization xApp
 * {{DESCRIPTION}}
 */
public class {{XAPP_CLASS_NAME}} extends AbstractXApp {

    private static final String XAPP_NAME = "{{XAPP_NAME}}";
    private static final double TARGET_HO_SUCCESS_RATE = {{TARGET_HO_SUCCESS_RATE}};

    @Override
    public void onStart() {
        log.info("Starting {} xApp", XAPP_NAME);
        subscribeToKPM();
    }

    @Override
    public void onIndication(Indication indication) {
        if (indication instanceof KPMIndication) {
            handleKPMIndication((KPMIndication) indication);
        }
    }

    private void handleKPMIndication(KPMIndication indication) {
        double hoSuccessRate = indication.getIndicatorValue("HO.ExecSuccess");

        if (hoSuccessRate < TARGET_HO_SUCCESS_RATE) {
            log.warn("Handover success rate below target: {} < {}", hoSuccessRate, TARGET_HO_SUCCESS_RATE);
            optimizeHandoverParameters();
        }
    }

    private void optimizeHandoverParameters() {
        // Retrieve current parameters via RC service model
        // Apply optimization algorithm
        // Update parameters via A1 policy

        log.info("Optimizing handover parameters");
    }
}
""");

        handover.addFile("application.yaml", """
xapp:
  name: {{XAPP_NAME}}
  version: "1.0.0"
  description: "{{DESCRIPTION}}"

ric:
  address: {{RIC_ADDRESS}}
  port: {{RIC_PORT}}

subscription:
  serviceModel: KPM
  indicators:
    - HO.ExecSuccess
    - HO.ExecFailure
  reportingIntervalMs: 1000

optimization:
  targetHOSuccessRate: {{TARGET_HO_SUCCESS_RATE}}
  adjustmentStep: 0.1
""");

        templates.put("handover", handover);
    }

    /**
     * Get a template by name.
     */
    public xAppTemplate getTemplate(String name) {
        return templates.get(name);
    }

    /**
     * Get all templates.
     */
    public Map<String, xAppTemplate> getAllTemplates() {
        return new HashMap<>(templates);
    }

    /**
     * Add a custom template.
     */
    public void addTemplate(xAppTemplate template) {
        templates.put(template.getId(), template);
    }

    /**
     * Expand template with variables.
     */
    public String expandTemplate(String templateContent, Map<String, String> variables) {
        String result = templateContent;

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue());
        }

        return result;
    }

    /**
     * Extract variables from template.
     */
    public Map<String, String> extractVariables(String templateContent) {
        Map<String, String> variables = new HashMap<>();
        Pattern pattern = Pattern.compile("\\{\\{(\\w+)\\}\\}");
        Matcher matcher = pattern.matcher(templateContent);

        while (matcher.find()) {
            String varName = matcher.group(1);
            variables.put(varName, "");
        }

        return variables;
    }
}
