package com.openran.copilot.rag;

import com.openran.copilot.model.ORANSpec;
import com.openran.copilot.model.ServiceModel;
import com.openran.copilot.model.SpecEmbedding;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the O-RAN knowledge base.
 * Coordinates spec ingestion, retrieval, and service model definitions.
 */
public class SpecKnowledgeBase {
    private final SpecIngestionService ingestionService;
    private final SpecRetriever retriever;
    private final Map<String, ORANSpec> specs = new HashMap<>();
    private final List<ServiceModel> serviceModels = new ArrayList<>();

    public SpecKnowledgeBase(EmbeddingService embeddingService, VectorStore vectorStore) {
        this.ingestionService = new SpecIngestionService(embeddingService, vectorStore);
        this.retriever = new SpecRetriever(vectorStore, embeddingService);

        // Initialize standard O-RAN service models
        initializeServiceModels();
    }

    /**
     * Initialize standard O-RAN service models.
     */
    private void initializeServiceModels() {
        // KPM - Key Performance Measurement Service Model
        ServiceModel kpm = new ServiceModel("kpm", "KPM", "KPM");
        kpm.setVersion("2.0.0");
        kpm.setDescription("Key Performance Measurement service model for reporting RAN KPIs");

        kpm.addIndicator(new ServiceModel.IndicatorDefinition("RRU.PrbUsedDl", "DL PRB Usage", "float"));
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setUnit("%");
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setDescription("Downlink Physical Resource Block utilization");

        kpm.addIndicator(new ServiceModel.IndicatorDefinition("RRU.PrbUsedUl", "UL PRB Usage", "float"));
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setUnit("%");
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setDescription("Uplink Physical Resource Block utilization");

        kpm.addIndicator(new ServiceModel.IndicatorDefinition("DRB.UEThpDl", "DL UE Throughput", "float"));
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setUnit("Mbps");
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setDescription("Downlink User Equipment throughput");

        kpm.addIndicator(new ServiceModel.IndicatorDefinition("DRB.UEThpUl", "UL UE Throughput", "float"));
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setUnit("Mbps");
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setDescription("Uplink User Equipment throughput");

        kpm.addIndicator(new ServiceModel.IndicatorDefinition("HO.ExecSuccess", "Handover Success Rate", "float"));
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setUnit("%");
        kpm.getIndicators().get(kpm.getIndicators().size() - 1).setDescription("Handover execution success rate");

        serviceModels.add(kpm);

        // RC - Radio Control Service Model
        ServiceModel rc = new ServiceModel("rc", "RC", "RC");
        rc.setVersion("1.0.0");
        rc.setDescription("Radio Control service model for controlling RAN parameters");

        rc.addIndicator(new ServiceModel.IndicatorDefinition("Cell.CCMode", "Cell Configuration", "string"));
        rc.getIndicators().get(rc.getIndicators().size() - 1).setDescription("Cell configuration mode (e.g., TDD, FDD)");

        rc.addIndicator(new ServiceModel.IndicatorDefinition("Cell.PCI", "PCI", "int"));
        rc.getIndicators().get(rc.getIndicators().size() - 1).setDescription("Physical Cell Identifier");

        serviceModels.add(rc);

        // GNB-NRT - gNB Non-Real-Time Radio Resource Control
        ServiceModel gnbNrt = new ServiceModel("gnb-nrt", "GNB-NRT", "GNB-NRT");
        gnbNrt.setVersion("1.1.0");
        gnbNrt.setDescription("gNB Non-Real-Time service model for resource management");

        gnbNrt.addIndicator(new ServiceModel.IndicatorDefinition("GNB.FreqBand", "Frequency Band", "string"));
        gnbNrt.getIndicators().get(gnbNrt.getIndicators().size() - 1).setDescription("Operating frequency band");

        gnbNrt.addIndicator(new ServiceModel.IndicatorDefinition("GNB.Bandwidth", "Bandwidth", "int"));
        gnbNrt.getIndicators().get(gnbNrt.getIndicators().size() - 1).setUnit("MHz");
        gnbNrt.getIndicators().get(gnbNrt.getIndicators().size() - 1).setDescription("Channel bandwidth");

        serviceModels.add(gnbNrt);
    }

    /**
     * Ingest a spec from a file.
     */
    public int ingestSpec(Path filePath, String specId, String title, String category) throws Exception {
        int chunksIngested = ingestionService.ingestSpecFromFile(filePath, specId, title, category);
        return chunksIngested;
    }

    /**
     * Ingest a spec from text.
     */
    public int ingestSpec(String content, String specId, String title, String category) {
        int chunksIngested = ingestionService.ingestSpecFromText(content, specId, title, category, null);
        return chunksIngested;
    }

    /**
     * Retrieve relevant chunks for a query.
     */
    public List<SpecEmbedding> retrieve(String query) {
        return retriever.retrieve(query);
    }

    /**
     * Retrieve relevant chunks with filters.
     */
    public List<SpecEmbedding> retrieve(String query, int k, Map<String, String> filters) {
        return retriever.retrieve(query, k, filters);
    }

    /**
     * Get all service models.
     */
    public List<ServiceModel> getServiceModels() {
        return new ArrayList<>(serviceModels);
    }

    /**
     * Get service model by name.
     */
    public ServiceModel getServiceModel(String name) {
        return serviceModels.stream()
                .filter(sm -> sm.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the vector store.
     */
    public VectorStore getVectorStore() {
        return ingestionService.getVectorStore();
    }

    /**
     * Get the ingestion service.
     */
    public SpecIngestionService getIngestionService() {
        return ingestionService;
    }

    /**
     * Get the retriever.
     */
    public SpecRetriever getRetriever() {
        return retriever;
    }
}
