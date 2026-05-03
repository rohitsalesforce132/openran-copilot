package com.openran.copilot;

import com.openran.copilot.model.DiagnosisResult;
import com.openran.copilot.troubleshoot.TroubleshootingService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TroubleshootingServiceTest {

    @Inject
    TroubleshootingService troubleshootingService;

    @Test
    public void testDiagnose() {
        Map<String, Object> request = Map.of("symptoms", "High DCR rate of 3.5% and low throughput");

        DiagnosisResult result = troubleshootingService.diagnose("High DCR rate of 3.5% and low throughput");

        assertNotNull(result);
        assertNotNull(result.getRootCause());
        assertFalse(result.getRecommendations().isEmpty());
        assertTrue(result.getConfidence() >= 0.0 && result.getConfidence() <= 1.0);
    }

    @Test
    public void testDiagnoseHighDCR() {
        DiagnosisResult result = troubleshootingService.diagnose(
            "Cell has high dropped call rate of 5%"
        );

        assertNotNull(result);
        assertTrue(result.getRootCause().toLowerCase().contains("interference") ||
                   result.getRootCause().toLowerCase().contains("dcr"));
    }

    @Test
    public void testGetAllCases() {
        var cases = troubleshootingService.getAllCases();

        assertNotNull(cases);
        assertFalse(cases.isEmpty());
        assertTrue(cases.size() >= 3);
    }
}
