package com.openran.copilot.resource;

import com.openran.copilot.model.DiagnosisResult;
import com.openran.copilot.model.TroubleshootingCase;
import com.openran.copilot.troubleshoot.TroubleshootingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

@Path("/api/v1/troubleshoot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TroubleshootResource {

    @Inject
    TroubleshootingService troubleshootingService;

    @POST
    @Path("/diagnose")
    public DiagnosisResult diagnose(Map<String, Object> request) {
        String symptoms = (String) request.get("symptoms");
        return troubleshootingService.diagnose(symptoms);
    }

    @GET
    @Path("/cases")
    public List<TroubleshootingCase> getAllCases() {
        return troubleshootingService.getAllCases();
    }

    @GET
    @Path("/cases/{caseId}")
    public TroubleshootingCase getCase(@PathParam("caseId") String caseId) {
        return troubleshootingService.getAllCases().stream()
                .filter(c -> c.getId().equals(caseId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Case not found: " + caseId));
    }
}
