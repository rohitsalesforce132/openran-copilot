package com.openran.copilot.model;

import java.time.LocalDateTime;

/**
 * Represents a message in a conversation session.
 * Messages can be from the user (engineer) or the assistant (copilot).
 */
public class ChatMessage {
    private String id;
    private ChatSession session;
    private String role; // "user" or "assistant"
    private String content;
    private LocalDateTime timestamp;
    private boolean hasCitations;
    private String citations; // JSON string of referenced spec sections

    public ChatMessage() {
    }

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.id = java.util.UUID.randomUUID().toString();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatSession getSession() {
        return session;
    }

    public void setSession(ChatSession session) {
        this.session = session;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isHasCitations() {
        return hasCitations;
    }

    public void setHasCitations(boolean hasCitations) {
        this.hasCitations = hasCitations;
    }

    public String getCitations() {
        return citations;
    }

    public void setCitations(String citations) {
        this.citations = citations;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "role='" + role + '\'' +
                ", content='" + (content != null && content.length() > 50 ? content.substring(0, 50) + "..." : content) + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
