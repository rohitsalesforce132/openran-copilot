package com.openran.copilot;

import com.openran.copilot.rag.VectorStore;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class VectorStoreTest {

    @Inject
    VectorStore vectorStore;

    @Test
    public void testAddDocument() {
        String docId = vectorStore.addDocument(
            "test-doc-001",
            "The Near-RT RIC processes real-time RAN data and executes xApp control actions.",
            List.of("near-rt-ric", "xapp", "real-time")
        );

        assertNotNull(docId);
        assertEquals("test-doc-001", docId);
    }

    @Test
    public void testSearch() {
        // Add test documents
        vectorStore.addDocument("doc-001", "E2 interface connects RIC to O-DU",
                List.of("e2", "ric", "odu"));
        vectorStore.addDocument("doc-002", "A1 interface connects SMO to non-RT RIC",
                List.of("a1", "smo", "ric"));

        var results = vectorStore.search("RIC connections", 5);

        assertNotNull(results);
        assertTrue(results.size() >= 2);
    }

    @Test
    public void testCosineSimilarity() {
        vectorStore.addDocument("doc-001", "E2 interface for RIC", List.of("e2", "ric"));
        vectorStore.addDocument("doc-002", "A1 interface for RIC", List.of("a1", "ric"));

        var results = vectorStore.search("E2 interface", 2);

        assertNotNull(results);
        assertTrue(results.get(0).contains("E2") || results.get(0).contains("e2"));
    }
}
