package com.openran.copilot.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.Instant;
import java.util.Map;

@Path("/api/v1/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @GET
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "timestamp", Instant.now().toString(),
            "service", "OpenRAN Copilot",
            "version", "1.0.0"
        );
    }

    @GET
    @Path("/readiness")
    public Map<String, String> readiness() {
        return Map.of("status", "READY");
    }

    @GET
    @Path("/liveness")
    public Map<String, String> liveness() {
        return Map.of("status", "ALIVE");
    }
}
