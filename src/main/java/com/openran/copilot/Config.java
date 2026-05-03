package com.openran.copilot;

import com.openran.copilot.generation.xAppGenerator;
import com.openran.copilot.rag.EmbeddingService;
import com.openran.copilot.rag.RAGService;
import com.openran.copilot.rag.SpecKnowledgeBase;
import com.openran.copilot.rag.VectorStore;
import com.openran.copilot.troubleshoot.TroubleshootingService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Application configuration and bean initialization.
 */
@ApplicationScoped
public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    @ConfigProperty(name = "openai.api.key")
    String openaiApiKey;

    @ConfigProperty(name = "openai.chat.model", defaultValue = "gpt-4o")
    String chatModel;

    @ConfigProperty(name = "openai.embedding.model", defaultValue = "text-embedding-3-small")
    String embeddingModel;

    @ConfigProperty(name = "vector-store.dimension", defaultValue = "1536")
    int embeddingDimension;

    @ConfigProperty(name = "knowledge-base.specs-path", defaultValue = "./specs")
    String specsPath;

    @Singleton
    public VectorStore vectorStore() {
        return new VectorStore(embeddingDimension);
    }

    @Singleton
    public EmbeddingService embeddingService() {
        return new EmbeddingService(openaiApiKey);
    }

    @Singleton
    public SpecKnowledgeBase specKnowledgeBase(VectorStore vectorStore, EmbeddingService embeddingService) {
        return new SpecKnowledgeBase(embeddingService, vectorStore);
    }

    @Singleton
    public RAGService ragService(SpecKnowledgeBase knowledgeBase) {
        return new RAGService(knowledgeBase, openaiApiKey);
    }

    @Singleton
    public xAppGenerator xAppGenerator(SpecKnowledgeBase knowledgeBase) {
        return new xAppGenerator(knowledgeBase, openaiApiKey);
    }

    @Singleton
    public TroubleshootingService troubleshootingService(SpecKnowledgeBase knowledgeBase) {
        return new TroubleshootingService(knowledgeBase);
    }

    void onStart(@Observes StartupEvent event, SpecKnowledgeBase knowledgeBase) {
        LOGGER.info("Starting OpenRAN Copilot...");
        LOGGER.info("Ingesting O-RAN specifications...");

        try {
            ingestSpecs(knowledgeBase);
            LOGGER.info("Knowledge base initialized with " + knowledgeBase.getVectorStore().size() + " chunks");
        } catch (Exception e) {
            LOGGER.severe("Failed to ingest specs: " + e.getMessage());
            e.printStackTrace();
        }

        LOGGER.info("OpenRAN Copilot started successfully");
    }

    private void ingestSpecs(SpecKnowledgeBase knowledgeBase) throws Exception {
        // Ingest specs from resources
        String[] specFiles = {
            "oran-specs/architecture-overview.md",
            "oran-specs/near-rt-ric.md",
            "oran-specs/e2-interface.md",
            "oran-specs/a1-interface.md",
            "oran-specs/kpm-service-model.md",
            "oran-specs/xapp-development.md",
            "oran-specs/ran-slicing.md",
            "oran-specs/o1-interface.md"
        };

        String[] specIds = {
            "WG1.Arch",
            "WG4.NearRT",
            "WG2.E2",
            "WG2.A1",
            "WG3.KPM",
            "WG4.xApp",
            "WG3.Slicing",
            "WG1.O1"
        };

        String[] specTitles = {
            "O-RAN Architecture Overview",
            "Near-RT RIC Specification",
            "E2 Interface Specification",
            "A1 Interface Specification",
            "KPM Service Model",
            "xApp Development Guide",
            "RAN Slicing Specification",
            "O1 Interface Specification"
        };

        String[] categories = {
            "Architecture",
            "Architecture",
            "Interface",
            "Interface",
            "Service Model",
            "Development",
            "Use Case",
            "Interface"
        };

        // Read from classpath (resources)
        ClassLoader classLoader = getClass().getClassLoader();

        for (int i = 0; i < specFiles.length; i++) {
            try {
                String content = new String(
                    classLoader.getResourceAsStream(specFiles[i]).readAllBytes()
                );

                int chunks = knowledgeBase.ingestSpec(
                    content, specIds[i], specTitles[i], categories[i]
                );

                LOGGER.info("Ingested " + specTitles[i] + ": " + chunks + " chunks");
            } catch (Exception e) {
                LOGGER.warning("Failed to ingest " + specFiles[i] + ": " + e.getMessage());
            }
        }
    }
}
