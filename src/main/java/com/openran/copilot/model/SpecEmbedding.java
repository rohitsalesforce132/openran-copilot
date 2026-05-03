package com.openran.copilot.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an embedding vector for a spec chunk.
 * Stores the vector, the text it represents, and metadata
 * for filtering and retrieval.
 */
public class SpecEmbedding {
    private String id;
    private ORANSpec spec;
    private SpecSection section;
    private String chunkText;
    private double[] embedding;
    private LocalDateTime createdAt;
    private Map<String, String> metadata = new HashMap<>();

    public SpecEmbedding() {
    }

    public SpecEmbedding(String id, String chunkText, double[] embedding) {
        this.id = id;
        this.chunkText = chunkText;
        this.embedding = embedding;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ORANSpec getSpec() {
        return spec;
    }

    public void setSpec(ORANSpec spec) {
        this.spec = spec;
    }

    public SpecSection getSection() {
        return section;
    }

    public void setSection(SpecSection section) {
        this.section = section;
    }

    public String getChunkText() {
        return chunkText;
    }

    public void setChunkText(String chunkText) {
        this.chunkText = chunkText;
    }

    public double[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(double[] embedding) {
        this.embedding = embedding;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    public int getEmbeddingDimension() {
        return embedding != null ? embedding.length : 0;
    }

    @Override
    public String toString() {
        return "SpecEmbedding{" +
                "id='" + id + '\'' +
                ", chunkTextLength=" + (chunkText != null ? chunkText.length() : 0) +
                ", embeddingDimension=" + getEmbeddingDimension() +
                ", metadata=" + metadata.size() +
                '}';
    }
}
