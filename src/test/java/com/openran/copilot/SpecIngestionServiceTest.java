package com.openran.copilot;

import com.openran.copilot.model.ORANSpec;
import com.openran.copilot.rag.SpecIngestionService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SpecIngestionServiceTest {

    @Inject
    SpecIngestionService specIngestionService;

    @Test
    public void testIngestSpec() {
        String specId = specIngestionService.ingestSpec(
            "E2 Interface Specification",
            "v2.1",
            "The E2 interface provides control plane signaling between Near-RT RIC and O-DU. It supports RAN function realization, xApp lifecycle management, and control loops."
        );

        assertNotNull(specId);
        assertTrue(specId.startsWith("spec-"));
    }

    @Test
    public void testGetAllSpecs() {
        // Add a test spec first
        specIngestionService.ingestSpec("A1 Interface", "v1.0", "A1 interface definition");

        List<ORANSpec> specs = specIngestionService.getAllSpecs();

        assertNotNull(specs);
        assertFalse(specs.isEmpty());
        assertTrue(specs.stream().anyMatch(s -> s.getTitle().contains("A1")));
    }

    @Test
    public void testSearchSpecs() {
        specIngestionService.ingestSpec("O1 Interface", "v2.0", "O1 interface for O-RAN management");

        var results = specIngestionService.searchSpecs("O1 management", 5);

        assertNotNull(results);
        assertTrue(results.size() > 0);
    }
}
