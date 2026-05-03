package com.openran.copilot.rag;

import com.openran.copilot.model.SpecEmbedding;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory vector store with cosine similarity search.
 * Stores embeddings with metadata for filtering and retrieval.
 */
public class VectorStore {
    private final Map<String, SpecEmbedding> embeddings = new ConcurrentHashMap<>();
    private final int embeddingDimension;

    public VectorStore(int embeddingDimension) {
        this.embeddingDimension = embeddingDimension;
    }

    /**
     * Store an embedding in the vector store.
     */
    public void store(SpecEmbedding embedding) {
        if (embedding.getEmbedding().length != embeddingDimension) {
            throw new IllegalArgumentException(
                "Embedding dimension mismatch: expected " + embeddingDimension +
                ", got " + embedding.getEmbedding().length
            );
        }
        embeddings.put(embedding.getId(), embedding);
    }

    /**
     * Store multiple embeddings.
     */
    public void storeAll(List<SpecEmbedding> embeddingList) {
        for (SpecEmbedding embedding : embeddingList) {
            store(embedding);
        }
    }

    /**
     * Retrieve an embedding by ID.
     */
    public Optional<SpecEmbedding> retrieve(String id) {
        return Optional.ofNullable(embeddings.get(id));
    }

    /**
     * Find the top-K most similar embeddings to the query vector.
     * Uses cosine similarity.
     *
     * @param queryVector Query embedding vector
     * @param k Number of results to return
     * @return List of embeddings sorted by similarity (highest first)
     */
    public List<SearchResult> search(double[] queryVector, int k) {
        if (queryVector.length != embeddingDimension) {
            throw new IllegalArgumentException(
                "Query vector dimension mismatch: expected " + embeddingDimension +
                ", got " + queryVector.length
            );
        }

        return embeddings.values().parallelStream()
                .map(embedding -> new SearchResult(
                    embedding,
                    cosineSimilarity(queryVector, embedding.getEmbedding())
                ))
                .filter(result -> result.getSimilarity() > 0)
                .sorted((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()))
                .limit(k)
                .collect(Collectors.toList());
    }

    /**
     * Search with filters on metadata.
     */
    public List<SearchResult> search(double[] queryVector, int k, Map<String, String> filters) {
        return embeddings.values().parallelStream()
                .filter(embedding -> matchesFilters(embedding, filters))
                .map(embedding -> new SearchResult(
                    embedding,
                    cosineSimilarity(queryVector, embedding.getEmbedding())
                ))
                .filter(result -> result.getSimilarity() > 0)
                .sorted((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()))
                .limit(k)
                .collect(Collectors.toList());
    }

    /**
     * Calculate cosine similarity between two vectors.
     * Formula: (A · B) / (||A|| * ||B||)
     */
    public double cosineSimilarity(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }

        normA = Math.sqrt(normA);
        normB = Math.sqrt(normB);

        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct / (normA * normB);
    }

    /**
     * Check if an embedding matches all filter criteria.
     */
    private boolean matchesFilters(SpecEmbedding embedding, Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String value = embedding.getMetadata().get(filter.getKey());
            if (value == null || !value.equals(filter.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the number of stored embeddings.
     */
    public int size() {
        return embeddings.size();
    }

    /**
     * Clear all embeddings.
     */
    public void clear() {
        embeddings.clear();
    }

    /**
     * Search result with similarity score.
     */
    public static class SearchResult {
        private final SpecEmbedding embedding;
        private final double similarity;

        public SearchResult(SpecEmbedding embedding, double similarity) {
            this.embedding = embedding;
            this.similarity = similarity;
        }

        public SpecEmbedding getEmbedding() {
            return embedding;
        }

        public double getSimilarity() {
            return similarity;
        }

        @Override
        public String toString() {
            return "SearchResult{" +
                    "similarity=" + String.format("%.4f", similarity) +
                    ", chunk='" + (embedding.getChunkText() != null && embedding.getChunkText().length() > 50 ?
                        embedding.getChunkText().substring(0, 50) + "..." :
                        embedding.getChunkText()) + '\'' +
                    '}';
        }
    }
}
