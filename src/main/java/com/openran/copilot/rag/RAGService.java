package com.openran.copilot.rag;

import com.openran.copilot.model.ChatMessage;
import com.openran.copilot.model.ChatSession;
import com.openran.copilot.model.SpecEmbedding;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main RAG orchestrator.
 * Combines retrieval with LLM generation to produce grounded responses.
 */
public class RAGService {
    private final SpecKnowledgeBase knowledgeBase;
    private final ChatLanguageModel chatModel;

    private static final String SYSTEM_PROMPT = """
        You are an expert O-RAN (Open Radio Access Network) assistant helping engineers
        with xApp development, network troubleshooting, and O-RAN specification questions.

        Your role:
        1. Answer questions about O-RAN architecture, interfaces, and specifications
        2. Help engineers understand xApp and rApp development
        3. Provide guidance on E2, A1, O1, and other O-RAN interfaces
        4. Assist with troubleshooting RAN network issues

        Guidelines:
        - Base all answers on the retrieved O-RAN specification content
        - Cite the spec sections you reference (e.g., [O-RAN.WG1.Arch: Section 4.2])
        - If the retrieved content doesn't contain the answer, say so clearly
        - Be precise and technical - your audience are RAN engineers
        - When uncertain, suggest where to find more information in the specs
        - Keep responses concise but complete
        """;

    public RAGService(SpecKnowledgeBase knowledgeBase, String apiKey) {
        this.knowledgeBase = knowledgeBase;
        this.chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o")
                .temperature(0.3)
                .build();
    }

    public RAGService(SpecKnowledgeBase knowledgeBase, ChatLanguageModel chatModel) {
        this.knowledgeBase = knowledgeBase;
        this.chatModel = chatModel;
    }

    /**
     * Generate a RAG response to a question.
     */
    public RAGResponse answerQuestion(String question) {
        return answerQuestion(question, 5);
    }

    /**
     * Generate a RAG response with configurable top-K retrieval.
     */
    public RAGResponse answerQuestion(String question, int topK) {
        // Retrieve relevant spec chunks
        List<SpecEmbedding> retrievedChunks = knowledgeBase.retrieve(question, topK, null);

        // Build context from retrieved chunks
        String context = buildContext(retrievedChunks);

        // Generate response using LLM
        String prompt = buildPrompt(question, context);
        String answer = chatModel.generate(prompt);

        // Build response object
        RAGResponse response = new RAGResponse();
        response.setQuestion(question);
        response.setAnswer(answer);
        response.setRetrievedChunks(retrievedChunks);
        response.setContext(context);
        response.setHasRetrieval(!retrievedChunks.isEmpty());

        return response;
    }

    /**
     * Generate a RAG response within a chat session.
     */
    public RAGResponse answerQuestionInSession(ChatSession session, String question) {
        // Build conversation history
        StringBuilder history = new StringBuilder();
        for (ChatMessage msg : session.getMessages()) {
            if (msg.getRole().equals("user")) {
                history.append("User: ").append(msg.getContent()).append("\n");
            } else {
                history.append("Assistant: ").append(msg.getContent()).append("\n");
            }
        }
        history.append("User: ").append(question).append("\n");

        // Retrieve relevant chunks
        List<SpecEmbedding> retrievedChunks = knowledgeBase.retrieve(question, 5, null);
        String context = buildContext(retrievedChunks);

        // Generate response with conversation history
        String prompt = SYSTEM_PROMPT + "\n\n" +
                "Conversation History:\n" + history.toString() + "\n\n" +
                "Relevant O-RAN Specification Content:\n" + context + "\n\n" +
                "Please answer the latest question based on the conversation and specification content above.";

        String answer = chatModel.generate(prompt);

        RAGResponse response = new RAGResponse();
        response.setQuestion(question);
        response.setAnswer(answer);
        response.setRetrievedChunks(retrievedChunks);
        response.setContext(context);
        response.setHasRetrieval(!retrievedChunks.isEmpty());

        return response;
    }

    /**
     * Build context string from retrieved chunks.
     */
    private String buildContext(List<SpecEmbedding> chunks) {
        if (chunks.isEmpty()) {
            return "No relevant specification content found.";
        }

        StringBuilder context = new StringBuilder();
        for (int i = 0; i < chunks.size(); i++) {
            SpecEmbedding chunk = chunks.get(i);
            context.append("[Chunk ").append(i + 1).append("] ");
            context.append("Spec: ").append(chunk.getMetadata().get("specTitle"));
            context.append(", Section: ").append(chunk.getMetadata().get("sectionTitle"));
            context.append("\n");
            context.append(chunk.getChunkText());
            context.append("\n\n");
        }
        return context.toString();
    }

    /**
     * Build prompt for LLM.
     */
    private String buildPrompt(String question, String context) {
        return SYSTEM_PROMPT + "\n\n" +
                "Relevant O-RAN Specification Content:\n" + context + "\n\n" +
                "Question: " + question + "\n\n" +
                "Please answer the question based on the specification content above.";
    }

    /**
     * RAG response containing answer and retrieved chunks.
     */
    public static class RAGResponse {
        private String question;
        private String answer;
        private List<SpecEmbedding> retrievedChunks;
        private String context;
        private boolean hasRetrieval;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public List<SpecEmbedding> getRetrievedChunks() {
            return retrievedChunks;
        }

        public void setRetrievedChunks(List<SpecEmbedding> retrievedChunks) {
            this.retrievedChunks = retrievedChunks;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public boolean isHasRetrieval() {
            return hasRetrieval;
        }

        public void setHasRetrieval(boolean hasRetrieval) {
            this.hasRetrieval = hasRetrieval;
        }

        public List<String> getCitations() {
            if (retrievedChunks == null || retrievedChunks.isEmpty()) {
                return new ArrayList<>();
            }

            return retrievedChunks.stream()
                    .map(chunk -> String.format("%s: %s",
                        chunk.getMetadata().get("specTitle"),
                        chunk.getMetadata().get("sectionTitle")))
                    .distinct()
                    .collect(Collectors.toList());
        }
    }
}
