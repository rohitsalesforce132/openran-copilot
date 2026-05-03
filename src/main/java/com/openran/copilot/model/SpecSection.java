package com.openran.copilot.model;

/**
 * Represents a section within an O-RAN specification.
 * Sections can be chapters, clauses, or subsections that
 * contain technical content to be indexed for RAG.
 */
public class SpecSection {
    private String id;
    private ORANSpec spec;
    private String title;
    private int order;
    private String content;
    private String parentId;
    private int depth; // 0 = chapter, 1 = clause, 2 = subclause

    public SpecSection() {
    }

    public SpecSection(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ORANSpec getSpec() {
        return spec;
    }

    public void setSpec(ORANSpec spec) {
        this.spec = spec;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "SpecSection{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", contentLength=" + (content != null ? content.length() : 0) +
                '}';
    }
}
