package com.openran.copilot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a conversation session between an engineer and the copilot.
 * Sessions maintain context across multiple message exchanges.
 */
public class ChatSession {
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ChatMessage> messages = new ArrayList<>();
    private String context; // User-provided context (e.g., "Working on KPM xApp")
    private boolean active;

    public ChatSession() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    public ChatSession(String title) {
        this();
        this.title = title;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
        this.updatedAt = LocalDateTime.now();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ChatMessage getLastMessage() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    @Override
    public String toString() {
        return "ChatSession{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", messageCount=" + messages.size() +
                ", active=" + active +
                '}';
    }
}
