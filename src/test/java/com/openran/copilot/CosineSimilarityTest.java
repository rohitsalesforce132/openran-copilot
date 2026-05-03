package com.openran.copilot;

import com.openran.copilot.rag.VectorStore;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CosineSimilarityTest {

    @Inject
    VectorStore vectorStore;

    @Test
    public void testCosineSimilarityCalculation() {
        // Add documents with known content
        vectorStore.addDocument("doc-1", "E2 interface connects Near-RT RIC to O-DU",
                List.of("e2", "near-rt-ric", "odu"));
        vectorStore.addDocument("doc-2", "A1 interface connects SMO to non-RT RIC",
                List.of("a1", "smo", "non-rt-ric"));

        // Search for RIC-related content
        var results = vectorStore.search("RIC interfaces", 5);

        assertNotNull(results);
        assertTrue(results.size() >= 2);
    }

    @Test
    public void testSimilarityOrdering() {
        vectorStore.addDocument("doc-1", "E2 interface definition and specifications",
                List.of("e2", "interface", "spec"));
        vectorStore.addDocument("doc-2", "O1 interface for O-RAN management",
                List.of("o1", "management"));
        vectorStore.addDocument("doc-3", "E2 interface procedures and message flows",
                List.of("e2", "procedure", "message"));

        var results = vectorStore.search("E2 interface details", 3);

        assertNotNull(results);
        // Should prioritize E2-related docs
        assertTrue(results.get(0).toLowerCase().contains("e2"));
    }

    @Test
    public void testZeroSimilarityHandling() {
        vectorStore.addDocument("doc-1", "RAN slicing concepts",
                List.of("slicing", "ran"));

        var results = vectorStore.search("completely unrelated topic", 5);

        // Should still return results but with lower relevance
        assertNotNull(results);
    }
}
