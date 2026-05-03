package com.openran.copilot.resource;

import com.openran.copilot.model.ChatMessage;
import com.openran.copilot.model.ChatSession;
import com.openran.copilot.rag.RAGService;
import com.openran.copilot.troubleshoot.TroubleshootingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Path("/api/v1/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatResource {

    @Inject
    RAGService ragService;

    @Inject
    TroubleshootingService troubleshootingService;

    private final Map<String, ChatSession> sessions = new ConcurrentHashMap<>();

    @POST
    public Map<String, Object> chat(Map<String, Object> request) {
        String message = (String) request.get("message");
        String sessionId = (String) request.getOrDefault("sessionId", UUID.randomUUID().toString());

        // Get or create session
        ChatSession session = sessions.computeIfAbsent(sessionId, id -> new ChatSession(id));

        // Add user message
        ChatMessage userMsg = new ChatMessage("user", message);
        session.addMessage(userMsg);

        // Determine response type
        String lowerMessage = message.toLowerCase();
        String response;

        if (lowerMessage.contains("troubleshoot") || lowerMessage.contains("problem") || lowerMessage.contains("issue")) {
            // Use troubleshooting service
            var diagnosis = troubleshootingService.diagnose(message);
            response = "Based on the symptoms, I've identified the following:\n\n" +
                       "**Root Cause:** " + diagnosis.getRootCause() + "\n\n" +
                       "**Confidence:** " + String.format("%.1f%%", diagnosis.getConfidence() * 100) + "\n\n" +
                       "**Recommendations:**\n" + String.join("\n", diagnosis.getRecommendations());
        } else {
            // Use RAG service
            String context = ragService.retrieveRelevantContext(message, 3);
            response = ragService.generateResponse(message, context);
        }

        // Add assistant response
        ChatMessage assistantMsg = new ChatMessage("assistant", response);
        session.addMessage(assistantMsg);

        return Map.of(
            "sessionId", sessionId,
            "response", response,
            "context", context != null ? context : "",
            "timestamp", System.currentTimeMillis()
        );
    }

    @GET
    @Path("/sessions/{sessionId}")
    public ChatSession getSession(@PathParam("sessionId") String sessionId) {
        return sessions.get(sessionId);
    }
}
