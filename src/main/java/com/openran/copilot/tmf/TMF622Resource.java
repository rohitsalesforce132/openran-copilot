package com.openran.copilot.tmf;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

/**
 * TMF622 Resource - Product Ordering.
 * Orders xApp generation.
 */
@Path("/tmf/v6/productOrder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TMF622Resource {

    @POST
    public Response placeOrder(Map<String, Object> orderRequest) {
        String productId = (String) orderRequest.get("productId");
        String description = (String) orderRequest.get("description");

        // Create order
        String orderId = UUID.randomUUID().toString();

        return Response.status(Response.Status.CREATED)
                .entity(Map.of(
                        "orderId", orderId,
                        "productId", productId,
                        "status", "ACKNOWLEDGED",
                        "description", description
                ))
                .build();
    }
}
