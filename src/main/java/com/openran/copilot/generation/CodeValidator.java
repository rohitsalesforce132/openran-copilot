package com.openran.copilot.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Basic syntax validation for generated code.
 */
public class CodeValidator {

    /**
     * Validate Java code.
     */
    public ValidationResult validateJava(String code, String className) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Check for class declaration
        if (!code.contains("class " + className)) {
            errors.add("Missing class declaration for: " + className);
        }

        // Check for package declaration
        if (!code.contains("package ")) {
            warnings.add("Missing package declaration");
        }

        // Check for balanced braces
        if (!hasBalancedBraces(code)) {
            errors.add("Unbalanced braces in code");
        }

        // Check for balanced parentheses
        if (!hasBalancedParentheses(code)) {
            errors.add("Unbalanced parentheses in code");
        }

        // Check for common syntax errors
        if (code.contains(";;")) {
            warnings.add("Double semicolon detected");
        }

        // Check for import statements
        if (!code.contains("import ")) {
            warnings.add("No import statements found");
        }

        return new ValidationResult(errors.isEmpty(), errors, warnings);
    }

    /**
     * Validate YAML configuration.
     */
    public ValidationResult validateYAML(String yaml) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Check for YAML structure
        String[] lines = yaml.split("\n");
        int expectedIndent = -1;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().isEmpty()) {
                continue;
            }

            int indent = countLeadingSpaces(line);

            if (expectedIndent == -1 && indent > 0) {
                expectedIndent = indent;
            }

            // Check for consistent indentation
            if (expectedIndent > 0 && indent % expectedIndent != 0) {
                warnings.add("Inconsistent indentation at line " + (i + 1));
            }
        }

        return new ValidationResult(errors.isEmpty(), errors, warnings);
    }

    /**
     * Validate xApp configuration.
     */
    public ValidationResult validateConfig(String configYaml) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Check for required fields
        if (!configYaml.contains("xapp:")) {
            errors.add("Missing 'xapp' section");
        }
        if (!configYaml.contains("ric:")) {
            errors.add("Missing 'ric' section");
        }
        if (!configYaml.contains("subscriptions:")) {
            warnings.add("No subscriptions defined");
        }

        return new ValidationResult(errors.isEmpty(), errors, warnings);
    }

    /**
     * Check if code has balanced braces.
     */
    private boolean hasBalancedBraces(String code) {
        int balance = 0;
        for (char c : code.toCharArray()) {
            if (c == '{') {
                balance++;
            } else if (c == '}') {
                balance--;
            }
            if (balance < 0) {
                return false;
            }
        }
        return balance == 0;
    }

    /**
     * Check if code has balanced parentheses.
     */
    private boolean hasBalancedParentheses(String code) {
        int balance = 0;
        for (char c : code.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
            }
            if (balance < 0) {
                return false;
            }
        }
        return balance == 0;
    }

    /**
     * Count leading spaces in a line.
     */
    private int countLeadingSpaces(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    /**
     * Validation result.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final List<String> warnings;

        public ValidationResult(boolean valid, List<String> errors, List<String> warnings) {
            this.valid = valid;
            this.errors = errors;
            this.warnings = warnings;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }

        @Override
        public String toString() {
            return "ValidationResult{" +
                    "valid=" + valid +
                    ", errorCount=" + errors.size() +
                    ", warningCount=" + warnings.size() +
                    '}';
        }
    }
}
