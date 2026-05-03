package com.openran.copilot.rag;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for generating embeddings using LangChain4j.
 * Configured to use OpenAI's text-embedding-ada-002 or similar model.
 */
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;
    private final int embeddingDimension;

    public EmbeddingService(String apiKey) {
        this.embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName("text-embedding-3-small")
                .build();
        this.embeddingDimension = 1536; // text-embedding-3-small dimension
    }

    public EmbeddingService(EmbeddingModel embeddingModel, int embeddingDimension) {
        this.embeddingModel = embeddingModel;
        this.embeddingDimension = embeddingDimension;
    }

    /**
     * Generate embedding for a single text.
     */
    public double[] embed(String text) {
        Embedding embedding = embeddingModel.embed(text).content();
        return embedding.vector();
    }

    /**
     * Generate embeddings for multiple texts.
     */
    public List<double[]> embedAll(List<String> texts) {
        List<double[]> embeddings = new ArrayList<>();

        // Process in batches to avoid rate limits
        int batchSize = 10;
        for (int i = 0; i < texts.size(); i += batchSize) {
            int end = Math.min(i + batchSize, texts.size());
            List<String> batch = texts.subList(i, end);

            for (String text : batch) {
                embeddings.add(embed(text));
            }
        }

        return embeddings;
    }

    /**
     * Get the embedding dimension.
     */
    public int getEmbeddingDimension() {
        return embeddingDimension;
    }
}
