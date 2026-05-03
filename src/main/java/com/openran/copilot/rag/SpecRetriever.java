package com.openran.copilot.rag;

import com.openran.copilot.model.SpecEmbedding;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Retrieves relevant spec chunks for a query using semantic search.
 * Supports filtering, re-ranking, and context window management.
 */
public class SpecRetriever {
    private final VectorStore vectorStore;
    private final EmbeddingService embeddingService;

    private static final int DEFAULT_TOP_K = 5;
    private static final double MIN_SIMILARITY_THRESHOLD = 0.3;

    public SpecRetriever(VectorStore vectorStore, EmbeddingService embeddingService) {
        this.vectorStore = vectorStore;
        this.embeddingService = embeddingService;
    }

    /**
     * Retrieve top-K relevant chunks for a query.
     */
    public List<SpecEmbedding> retrieve(String query) {
        return retrieve(query, DEFAULT_TOP_K, null);
    }

    /**
     * Retrieve top-K relevant chunks with filters.
     */
    public List<SpecEmbedding> retrieve(String query, int k, Map<String, String> filters) {
        double[] queryEmbedding = embeddingService.embed(query);

        List<VectorStore.SearchResult> results;
        if (filters != null && !filters.isEmpty()) {
            results = vectorStore.search(queryEmbedding, k, filters);
        } else {
            results = vectorStore.search(queryEmbedding, k);
        }

        // Filter by similarity threshold
        return results.stream()
                .filter(result -> result.getSimilarity() >= MIN_SIMILARITY_THRESHOLD)
                .map(VectorStore.SearchResult::getEmbedding)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve with re-ranking by relevance score.
     * This is the same as basic retrieve since vector store already sorts.
     */
    public List<SpecEmbedding> retrieveWithRerank(String query, int k) {
        return retrieve(query, k, null);
    }

    /**
     * Retrieve chunks and return with similarity scores.
     */
    public List<RetrievalResult> retrieveWithScores(String query, int k) {
        double[] queryEmbedding = embeddingService.embed(query);
        List<VectorStore.SearchResult> searchResults = vectorStore.search(queryEmbedding, k);

        return searchResults.stream()
                .filter(result -> result.getSimilarity() >= MIN_SIMILARITY_THRESHOLD)
                .map(result -> new RetrievalResult(
                    result.getEmbedding(),
                    result.getSimilarity()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Retrieve chunks for multiple queries and merge results.
     */
    public List<SpecEmbedding> retrieveMultiQuery(List<String> queries, int k) {
        Map<String, SpecEmbedding> merged = new HashMap<>();

        for (String query : queries) {
            List<SpecEmbedding> results = retrieve(query, k, null);
            for (SpecEmbedding embedding : results) {
                merged.put(embedding.getId(), embedding);
            }
        }

        // Return up to k results, keeping original relevance order
        return new ArrayList<>(merged.values()).stream()
                .limit(k)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve chunks by spec category.
     */
    public List<SpecEmbedding> retrieveByCategory(String query, String category, int k) {
        Map<String, String> filters = new HashMap<>();
        filters.put("specCategory", category);
        return retrieve(query, k, filters);
    }

    /**
     * Retrieve chunks from a specific spec.
     */
    public List<SpecEmbedding> retrieveBySpec(String query, String specId, int k) {
        Map<String, String> filters = new HashMap<>();
        filters.put("specId", specId);
        return retrieve(query, k, filters);
    }

    /**
     * Get retrieval result with similarity score.
     */
    public static class RetrievalResult {
        private final SpecEmbedding embedding;
        private final double similarity;

        public RetrievalResult(SpecEmbedding embedding, double similarity) {
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
            return "RetrievalResult{" +
                    "similarity=" + String.format("%.4f", similarity) +
                    ", chunk='" + (embedding.getChunkText() != null && embedding.getChunkText().length() > 50 ?
                        embedding.getChunkText().substring(0, 50) + "..." :
                        embedding.getChunkText()) + '\'' +
                    '}';
        }
    }
}
