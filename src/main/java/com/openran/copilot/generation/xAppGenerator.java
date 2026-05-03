package com.openran.copilot.generation;

import com.openran.copilot.model.GeneratedCode;
import com.openran.copilot.model.SpecEmbedding;
import com.openran.copilot.model.xAppTemplate;
import com.openran.copilot.rag.SpecKnowledgeBase;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates xApp code from natural language descriptions.
 */
public class xAppGenerator {
    private final SpecKnowledgeBase knowledgeBase;
    private final ChatLanguageModel chatModel;
    private final xAppTemplateEngine templateEngine;
    private final xAppConfigGenerator configGenerator;
    private final CodeValidator codeValidator;

    private static final String SYSTEM_PROMPT = """
        You are an expert O-RAN xApp developer. Generate production-ready xApp code
        based on the user's description and the retrieved O-RAN specification content.

        Your code must:
        1. Follow O-RAN architecture patterns (E2 interface, service models, A1 policies)
        2. Use proper O-RAN service models (KPM, RC, GNB-NRT, etc.)
        3. Include proper error handling and logging
        4. Be complete and compilable
        5. Follow Java best practices

        Generate the following files:
        1. Main xApp class (extends AbstractXApp)
        2. Configuration file (application.yaml)
        3. Build file (pom.xml if needed)
        4. Any additional helper classes

        Output format: Provide each file with its filename as a header, e.g.:
        === File: MyXApp.java ===
        [code content]
        === File: application.yaml ===
        [config content]
        """;

    public xAppGenerator(SpecKnowledgeBase knowledgeBase, String apiKey) {
        this.knowledgeBase = knowledgeBase;
        this.chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o")
                .temperature(0.2)
                .build();
        this.templateEngine = new xAppTemplateEngine();
        this.configGenerator = new xAppConfigGenerator();
        this.codeValidator = new CodeValidator();
    }

    public xAppGenerator(SpecKnowledgeBase knowledgeBase, ChatLanguageModel chatModel) {
        this.knowledgeBase = knowledgeBase;
        this.chatModel = chatModel;
        this.templateEngine = new xAppTemplateEngine();
        this.configGenerator = new xAppConfigGenerator();
        this.codeValidator = new CodeValidator();
    }

    /**
     * Generate xApp code from a natural language description.
     */
    public GeneratedCode generate(String description) {
        // Retrieve relevant spec content
        List<SpecEmbedding> relevantSpecs = knowledgeBase.retrieve(description, 5, null);

        // Build prompt with context
        String context = buildContext(relevantSpecs);
        String prompt = buildGenerationPrompt(description, context);

        // Generate code
        String generatedContent = chatModel.generate(prompt);

        // Parse generated files
        Map<String, String> files = parseGeneratedFiles(generatedContent);

        // Create generated code object
        GeneratedCode result = new GeneratedCode();
        result.setDescription(description);
        result.setGeneratingPrompt(description);
        result.setFiles(files);

        // Extract xApp name from description or files
        String xAppName = extractXAppName(description, files);
        result.setXAppName(xAppName);

        // Add referenced specs
        for (SpecEmbedding spec : relevantSpecs) {
            result.addReferencedSpec(spec.getMetadata().get("specId"));
        }

        // Validate generated code
        validateGeneratedCode(result);

        return result;
    }

    /**
     * Generate xApp code using a template.
     */
    public GeneratedCode generateFromTemplate(String templateName, Map<String, String> variables) {
        xAppTemplate template = templateEngine.getTemplate(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }

        GeneratedCode result = new GeneratedCode();
        result.setXAppName(variables.getOrDefault("XAPP_NAME", "GeneratedXApp"));
        result.setDescription("Generated from template: " + templateName);

        // Expand template files
        for (Map.Entry<String, String> entry : template.getFiles().entrySet()) {
            String filename = entry.getKey();
            String content = templateEngine.expandTemplate(entry.getValue(), variables);
            result.addFile(filename, content);
        }

        // Validate
        validateGeneratedCode(result);

        return result;
    }

    /**
     * Build context from retrieved specs.
     */
    private String buildContext(List<SpecEmbedding> specs) {
        if (specs.isEmpty()) {
            return "No relevant specification content found.";
        }

        StringBuilder context = new StringBuilder();
        context.append("Relevant O-RAN Specification Content:\n\n");

        for (int i = 0; i < specs.size(); i++) {
            SpecEmbedding spec = specs.get(i);
            context.append("[Spec ").append(i + 1).append("] ");
            context.append(spec.getMetadata().get("specTitle"));
            context.append(" - ").append(spec.getMetadata().get("sectionTitle"));
            context.append("\n");
            context.append(spec.getChunkText());
            context.append("\n\n");
        }

        return context.toString();
    }

    /**
     * Build the generation prompt.
     */
    private String buildGenerationPrompt(String description, String context) {
        return SYSTEM_PROMPT + "\n\n" +
                "User Description:\n" + description + "\n\n" +
                context + "\n\n" +
                "Generate the complete xApp code now.";
    }

    /**
     * Parse generated files from LLM output.
     */
    private Map<String, String> parseGeneratedFiles(String content) {
        Map<String, String> files = new HashMap<>();

        String[] parts = content.split("=== File:");
        for (String part : parts) {
            if (part.trim().isEmpty()) {
                continue;
            }

            int newlineIndex = part.indexOf('\n');
            if (newlineIndex > 0) {
                String filename = part.substring(0, newlineIndex).trim();
                String fileContent = part.substring(newlineIndex + 1).trim();
                files.put(filename, fileContent);
            }
        }

        return files;
    }

    /**
     * Extract xApp name from description or files.
     */
    private String extractXAppName(String description, Map<String, String> files) {
        // Try to find in Java file
        for (String filename : files.keySet()) {
            if (filename.endsWith(".java")) {
                String content = files.get(filename);
                if (content.contains("XAPP_NAME = \"")) {
                    int start = content.indexOf("XAPP_NAME = \"") + 14;
                    int end = content.indexOf("\"", start);
                    if (end > start) {
                        return content.substring(start, end);
                    }
                }
            }
        }

        // Default: generate from description
        return "CustomXApp";
    }

    /**
     * Validate generated code.
     */
    private void validateGeneratedCode(GeneratedCode generated) {
        for (Map.Entry<String, String> entry : generated.getFiles().entrySet()) {
            String filename = entry.getKey();
            String content = entry.getValue();

            if (filename.endsWith(".java")) {
                String className = filename.replace(".java", "");
                CodeValidator.ValidationResult result = codeValidator.validateJava(content, className);

                if (!result.isValid()) {
                    generated.setValid(false);
                    for (String error : result.getErrors()) {
                        generated.addError(filename + ": " + error);
                    }
                }

                for (String warning : result.getWarnings()) {
                    generated.addWarning(filename + ": " + warning);
                }
            } else if (filename.endsWith(".yaml") || filename.endsWith(".yml")) {
                CodeValidator.ValidationResult result = codeValidator.validateYAML(content);

                for (String warning : result.getWarnings()) {
                    generated.addWarning(filename + ": " + warning);
                }
            }
        }

        if (!generated.hasErrors()) {
            generated.setValid(true);
        }
    }

    /**
     * Get available templates.
     */
    public Map<String, xAppTemplate> getAvailableTemplates() {
        return templateEngine.getAllTemplates();
    }

    /**
     * Get the template engine.
     */
    public xAppTemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    /**
     * Get the config generator.
     */
    public xAppConfigGenerator getConfigGenerator() {
        return configGenerator;
    }
}
