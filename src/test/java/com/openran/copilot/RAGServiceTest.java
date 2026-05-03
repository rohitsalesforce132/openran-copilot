package com.openran.copilot;

import com.openran.copilot.rag.RAGService;
import com.openran.copilot.rag.VectorStore;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class RAGServiceTest {

    @Inject
    RAGService ragService;

    @Inject
    VectorStore vectorStore;

    @Test
    public void testRetrieveRelevantContext() {
        // Add test document
        vectorStore.addDocument("test-001", "E2 interface connects RIC to O-DU",
                "e2-interface", List.of("e2", "interface", "ric", "odu"));

        List<String> context = ragService.retrieveRelevantContext("What is E2 interface?", 3);

        assertNotNull(context);
        assertFalse(context.isEmpty());
        assertTrue(context.get(0).contains("E2") || context.get(0).contains("RIC"));
    }

    @Test
    public void testGenerateResponse() {
        String context = "The A1 interface connects non-real-time RIC to SMO.";
        String response = ragService.generateResponse("What connects non-RT RIC to SMO?", context);

        assertNotNull(response);
        assertTrue(response.length() > 10);
    }
}
