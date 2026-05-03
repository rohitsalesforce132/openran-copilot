package com.openran.copilot.resource;

import com.openran.copilot.model.ChatSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Path("/api/v1/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionResource {

    private final Map<String, ChatSession> sessions = new ConcurrentHashMap<>();

    @GET
    public List<Map<String, Object>> getAllSessions() {
        return sessions.values().stream()
                .map(session -> Map.of(
                    "id", session.getId(),
                    "messageCount", session.getMessages().size(),
                    "createdAt", session.getCreatedAt(),
                    "lastActivity", session.getLastActivity()
                ))
                .collect(Collectors.toList());
    }

    @POST
    public Map<String, String> createSession() {
        String sessionId = UUID.randomUUID().toString();
        ChatSession session = new ChatSession(sessionId);
        sessions.put(sessionId, session);
        return Map.of("sessionId", sessionId);
    }

    @GET
    @Path("/{sessionId}")
    public ChatSession getSession(@PathParam("sessionId") String sessionId) {
        ChatSession session = sessions.get(sessionId);
        if (session == null) {
            throw new NotFoundException("Session not found: " + sessionId);
        }
        return session;
    }

    @DELETE
    @Path("/{sessionId}")
    public Map<String, String> deleteSession(@PathParam("sessionId") String sessionId) {
        ChatSession removed = sessions.remove(sessionId);
        if (removed == null) {
            throw new NotFoundException("Session not found: " + sessionId);
        }
        return Map.of("status", "deleted", "sessionId", sessionId);
    }
}
