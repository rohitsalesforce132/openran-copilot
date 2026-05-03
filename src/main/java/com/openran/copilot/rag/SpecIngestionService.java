package com.openran.copilot.rag;

import com.openran.copilot.model.ORANSpec;
import com.openran.copilot.model.SpecEmbedding;
import com.openran.copilot.model.SpecSection;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for ingesting O-RAN specifications into the vector store.
 * Parses spec documents, chunks them, and generates embeddings.
 */
public class SpecIngestionService {
    private static final int MAX_CHUNK_TOKENS = 512;
    private static final int CHUNK_OVERLAP_TOKENS = 50;
    private static final int TOKENS_PER_CHAR_ESTIMATE = 4; // Rough estimate

    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    public SpecIngestionService(EmbeddingService embeddingService, VectorStore vectorStore) {
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
    }

    /**
     * Ingest a spec from a markdown file.
     */
    public int ingestSpecFromFile(Path filePath, String specId, String specTitle, String category) throws Exception {
        String content = Files.readString(filePath);
        return ingestSpecFromText(content, specId, specTitle, category, filePath.toString());
    }

    /**
     * Ingest a spec from text content.
     */
    public int ingestSpecFromText(String content, String specId, String specTitle, String category, String source) {
        ORANSpec spec = parseSpecFromMarkdown(content, specId, specTitle, category, source);
        return ingestSpec(spec);
    }

    /**
     * Parse O-RAN spec from markdown format.
     */
    private ORANSpec parseSpecFromMarkdown(String content, String specId, String specTitle, String category, String source) {
        ORANSpec spec = new ORANSpec(specId, specTitle, "1.0", category);
        spec.setSourceUrl(source);
        spec.setDescription("O-RAN Specification: " + specTitle);

        // Split by headers to create sections
        String[] lines = content.split("\n");
        StringBuilder currentSection = new StringBuilder();
        String currentTitle = "Introduction";
        int sectionOrder = 0;
        Pattern headerPattern = Pattern.compile("^(#{1,3})\\s+(.+)$");

        for (String line : lines) {
            Matcher matcher = headerPattern.matcher(line);
            if (matcher.find()) {
                // Save previous section
                if (currentSection.length() > 0) {
                    SpecSection section = new SpecSection();
                    section.setId(UUID.randomUUID().toString());
                    section.setSpec(spec);
                    section.setTitle(currentTitle);
                    section.setContent(currentSection.toString().trim());
                    section.setOrder(sectionOrder++);
                    section.setDepth(matcher.group(1).length() - 1);
                    spec.addSection(section);
                }

                // Start new section
                currentTitle = matcher.group(2).trim();
                currentSection = new StringBuilder();
            } else {
                currentSection.append(line).append("\n");
            }
        }

        // Add last section
        if (currentSection.length() > 0) {
            SpecSection section = new SpecSection();
            section.setId(UUID.randomUUID().toString());
            section.setSpec(spec);
            section.setTitle(currentTitle);
            section.setContent(currentSection.toString().trim());
            section.setOrder(sectionOrder);
            spec.addSection(section);
        }

        return spec;
    }

    /**
     * Ingest an ORANSpec object into the vector store.
     */
    public int ingestSpec(ORANSpec spec) {
        int chunksIngested = 0;

        for (SpecSection section : spec.getSections()) {
            List<String> chunks = chunkText(section.getContent(), MAX_CHUNK_TOKENS, CHUNK_OVERLAP_TOKENS);

            for (int i = 0; i < chunks.size(); i++) {
                String chunk = chunks.get(i);
                double[] embedding = embeddingService.embed(chunk);

                SpecEmbedding specEmbedding = new SpecEmbedding(
                    UUID.randomUUID().toString(),
                    chunk,
                    embedding
                );
                specEmbedding.setSpec(spec);
                specEmbedding.setSection(section);

                // Add metadata
                specEmbedding.addMetadata("specId", spec.getId());
                specEmbedding.addMetadata("specTitle", spec.getTitle());
                specEmbedding.addMetadata("specCategory", spec.getCategory());
                specEmbedding.addMetadata("sectionTitle", section.getTitle());
                specEmbedding.addMetadata("chunkIndex", String.valueOf(i));
                specEmbedding.addMetadata("totalChunks", String.valueOf(chunks.size()));

                vectorStore.store(specEmbedding);
                chunksIngested++;
            }
        }

        return chunksIngested;
    }

    /**
     * Split text into chunks with overlap.
     * Simple character-based chunking (token estimation).
     */
    private List<String> chunkText(String text, int maxTokens, int overlapTokens) {
        List<String> chunks = new ArrayList<>();

        if (text == null || text.trim().isEmpty()) {
            return chunks;
        }

        int maxChars = maxTokens * TOKENS_PER_CHAR_ESTIMATE;
        int overlapChars = overlapTokens * TOKENS_PER_CHAR_ESTIMATE;

        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + maxChars, text.length());

            // Try to break at a sentence or paragraph boundary
            if (end < text.length()) {
                // Look for sentence boundary (., !, ? followed by space)
                int lastSentence = text.lastIndexOf(". ", end - 1);
                int lastParagraph = text.lastIndexOf("\n\n", end - 1);

                if (lastParagraph > start + maxChars / 2) {
                    end = lastParagraph + 2;
                } else if (lastSentence > start + maxChars / 2) {
                    end = lastSentence + 2;
                }
            }

            String chunk = text.substring(start, end).trim();
            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            start = end - overlapChars;
            if (start < 0) {
                start = 0;
            }
        }

        return chunks;
    }

    /**
     * Get the vector store.
     */
    public VectorStore getVectorStore() {
        return vectorStore;
    }
}
