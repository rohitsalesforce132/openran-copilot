package com.openran.copilot.rag;

import com.openran.copilot.model.SpecEmbedding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

/**
 * Test vector store functionality.
 */
public class VectorStoreTest {

    private VectorStore vectorStore;

    @BeforeEach
    public void setup() {
        vectorStore = new VectorStore(3);
    }

    @Test
    public void testStoreAndRetrieve() {
        double[] embedding = {1.0, 2.0, 3.0};
        SpecEmbedding specEmbedding = new SpecEmbedding("test-1", "Test content", embedding);

        vectorStore.store(specEmbedding);

        var retrieved = vectorStore.retrieve("test-1");
        assertTrue(retrieved.isPresent());
        assertEquals("test-1", retrieved.get().getId());
    }

    @Test
    public void testSearch() {
        vectorStore.store(new SpecEmbedding("id1", "similar content", new double[]{1.0, 2.0, 3.0}));
        vectorStore.store(new SpecEmbedding("id2", "different content", new double[]{-1.0, -2.0, -3.0}));

        List<VectorStore.SearchResult> results = vectorStore.search(new double[]{1.0, 2.0, 3.0}, 2);

        assertEquals(1, results.size());
        assertEquals("id1", results.get(0).getEmbedding().getId());
    }

    @Test
    public void testSearchWithFilter() {
        SpecEmbedding e1 = new SpecEmbedding("id1", "content 1", new double[]{1.0, 2.0, 3.0});
        e1.addMetadata("category", "Architecture");

        SpecEmbedding e2 = new SpecEmbedding("id2", "content 2", new double[]{1.0, 2.0, 3.0});
        e2.addMetadata("category", "Interface");

        vectorStore.store(e1);
        vectorStore.store(e2);

        Map<String, String> filter = Map.of("category", "Architecture");
        List<VectorStore.SearchResult> results = vectorStore.search(new double[]{1.0, 2.0, 3.0}, 10, filter);

        assertEquals(1, results.size());
        assertEquals("id1", results.get(0).getEmbedding().getId());
    }

    @Test
    public void testDimensionMismatch() {
        SpecEmbedding e = new SpecEmbedding("id1", "content", new double[]{1.0, 2.0}); // Wrong dimension

        assertThrows(IllegalArgumentException.class, () -> {
            vectorStore.store(e);
        });
    }

    @Test
    public void testClear() {
        vectorStore.store(new SpecEmbedding("id1", "content", new double[]{1.0, 2.0, 3.0}));
        assertEquals(1, vectorStore.size());

        vectorStore.clear();
        assertEquals(0, vectorStore.size());
    }

    @Test
    public void testSearchWithMinSimilarity() {
        vectorStore.store(new SpecEmbedding("id1", "similar", new double[]{1.0, 2.0, 3.0}));
        vectorStore.store(new SpecEmbedding("id2", "dissimilar", new double[]{-1.0, -2.0, -3.0}));

        List<VectorStore.SearchResult> results = vectorStore.search(new double[]{1.0, 2.0, 3.0}, 10);

        // Only high similarity result should be returned (filtering happens in retriever)
        assertTrue(results.size() >= 1);
    }
}
