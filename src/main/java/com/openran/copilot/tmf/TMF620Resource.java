package com.openran.copilot.tmf;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

/**
 * TMF620 Resource - Product Catalog.
 * Browses xApp templates as products.
 */
@Path("/tmf/v6/productCatalog")
@Produces(MediaType.APPLICATION_JSON)
public class TMF620Resource {

    @GET
    public Response listProducts() {
        // Return xApp templates as products
        return Response.ok(Map.of(
                "products", Map.of(
                        "monitoring-xapp", Map.of(
                                "id", "prod-001",
                                "name", "KPI Monitoring xApp",
                                "description", "Monitors RAN KPIs and generates alerts",
                                "price", 0
                        ),
                        "handover-xapp", Map.of(
                                "id", "prod-002",
                                "name", "Handover Optimization xApp",
                                "description", "Optimizes handover parameters",
                                "price", 0
                        )
                )
        )).build();
    }
}
