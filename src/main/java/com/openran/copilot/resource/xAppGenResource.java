package com.openran.copilot.resource;

import com.openran.copilot.model.GeneratedCode;
import com.openran.copilot.model.xAppConfig;
import com.openran.copilot.generation.xAppGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Path("/api/v1/xapp/generate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class xAppGenResource {

    @Inject
    xAppGenerator xAppGenerator;

    @POST
    @Path("/code")
    public GeneratedCode generateCode(Map<String, Object> request) {
        String description = (String) request.get("description");
        String serviceModel = (String) request.getOrDefault("serviceModel", "KPM");

        return xAppGenerator.generate(description, serviceModel);
    }

    @POST
    @Path("/config")
    public xAppConfig generateConfig(Map<String, Object> request) {
        String xAppType = (String) request.get("xAppType");
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) request.getOrDefault("parameters", Map.of());

        return xAppGenerator.generateConfig(xAppType, parameters);
    }

    @GET
    @Path("/templates")
    public Map<String, Object> listTemplates() {
        return Map.of(
            "templates", List.of(
                Map.of("name", "Traffic Prediction", "serviceModel", "KPM", "description", "ML-based traffic forecasting"),
                Map.of("name", "Handover Optimization", "serviceModel", "KPM", "description", "Optimize HO parameters"),
                Map.of("name", "Power Control", "serviceModel", "KPM", "description", "Dynamic power adjustment"),
                Map.of("name", "Load Balancing", "serviceModel", "KPM", "description", "Inter-frequency load balance")
            )
        );
    }
}
