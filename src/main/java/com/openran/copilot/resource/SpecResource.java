package com.openran.copilot.resource;

import com.openran.copilot.model.ORANSpec;
import com.openran.copilot.rag.SpecIngestionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Path("/api/v1/specs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecResource {

    @Inject
    SpecIngestionService specIngestionService;

    @GET
    public List<ORANSpec> getAllSpecs() {
        return specIngestionService.getAllSpecs();
    }

    @GET
    @Path("/{specId}")
    public ORANSpec getSpec(@PathParam("specId") String specId) {
        return specIngestionService.getSpec(specId);
    }

    @POST
    @Path("/search")
    public List<Map<String, Object>> search(Map<String, Object> request) {
        String query = (String) request.get("query");
        int limit = (Integer) request.getOrDefault("limit", 10);

        return specIngestionService.searchSpecs(query, limit).stream()
                .map(spec -> Map.of(
                    "id", spec.getId(),
                    "title", spec.getTitle(),
                    "version", spec.getVersion(),
                    "relevance", 0.95
                ))
                .toList();
    }

    @POST
    @Path("/ingest")
    public Map<String, String> ingestSpec(Map<String, Object> request) {
        String title = (String) request.get("title");
        String version = (String) request.get("version");
        String content = (String) request.get("content");

        String specId = specIngestionService.ingestSpec(title, version, content);
        return Map.of("status", "ingested", "specId", specId);
    }
}
