package com.openran.copilot;

import com.openran.copilot.generation.xAppGenerator;
import com.openran.copilot.model.GeneratedCode;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class xAppGeneratorTest {

    @Inject
    xAppGenerator xAppGenerator;

    @Test
    public void testGenerateCode() {
        GeneratedCode code = xAppGenerator.generate(
            "Create an xApp that monitors cell throughput and triggers load balancing when > 80%",
            "KPM"
        );

        assertNotNull(code);
        assertNotNull(code.getCode());
        assertTrue(code.getCode().length() > 100);
        assertEquals("KPM", code.getServiceModel());
    }

    @Test
    public void testGenerateConfig() {
        var config = xAppGenerator.generateConfig(
            "TrafficPrediction",
            Map.of("predictionHorizon", "5min", "modelType", "LSTM")
        );

        assertNotNull(config);
        assertEquals("TrafficPrediction", config.getXAppType());
        assertNotNull(config.getParameters());
    }

    @Test
    public void testCodeIncludesEssentialComponents() {
        GeneratedCode code = xAppGenerator.generate(
            "Simple throughput monitoring xApp",
            "KPM"
        );

        String generated = code.getCode();
        assertTrue(generated.contains("class") || generated.contains("xApp"));
        assertTrue(generated.contains("ric") || generated.contains("RIC") ||
                   generated.contains("E2") || generated.contains("e2"));
    }
}
