package com.openran.copilot.rag;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cosine similarity calculations.
 */
public class CosineSimilarityTest {

    @Test
    public void testIdenticalVectors() {
        VectorStore vs = new VectorStore(3);
        double[] v1 = {1.0, 2.0, 3.0};
        double[] v2 = {1.0, 2.0, 3.0};

        double similarity = vs.cosineSimilarity(v1, v2);
        assertEquals(1.0, similarity, 0.001);
    }

    @Test
    public void testOrthogonalVectors() {
        VectorStore vs = new VectorStore(3);
        double[] v1 = {1.0, 0.0, 0.0};
        double[] v2 = {0.0, 1.0, 0.0};

        double similarity = vs.cosineSimilarity(v1, v2);
        assertEquals(0.0, similarity, 0.001);
    }

    @Test
    public void testOppositeVectors() {
        VectorStore vs = new VectorStore(3);
        double[] v1 = {1.0, 1.0, 1.0};
        double[] v2 = {-1.0, -1.0, -1.0};

        double similarity = vs.cosineSimilarity(v1, v2);
        assertEquals(-1.0, similarity, 0.001);
    }

    @Test
    public void testPartialSimilarity() {
        VectorStore vs = new VectorStore(3);
        double[] v1 = {1.0, 2.0, 3.0};
        double[] v2 = {2.0, 4.0, 6.0}; // Scaled by 2

        double similarity = vs.cosineSimilarity(v1, v2);
        assertEquals(1.0, similarity, 0.001); // Same direction
    }

    @Test
    public void testDimensionMismatch() {
        VectorStore vs = new VectorStore(3);
        double[] v1 = {1.0, 2.0, 3.0};
        double[] v2 = {1.0, 2.0}; // Different dimension

        assertThrows(IllegalArgumentException.class, () -> {
            vs.cosineSimilarity(v1, v2);
        });
    }

    @Test
    public void testZeroVector() {
        VectorStore vs = new VectorStore(3);
        double[] v1 = {0.0, 0.0, 0.0};
        double[] v2 = {1.0, 2.0, 3.0};

        double similarity = vs.cosineSimilarity(v1, v2);
        assertEquals(0.0, similarity, 0.001);
    }
}
