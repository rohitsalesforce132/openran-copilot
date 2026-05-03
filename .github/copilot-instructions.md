# OpenRAN Copilot - Copilot Instructions

## Project Overview

This is a Quarkus-based microservice that serves as an AI copilot for O-RAN engineers. It uses RAG (Retrieval-Augmented Generation) over O-RAN specifications to answer questions, generate xApp code, and troubleshoot network issues.

## Architecture

```
Quarkus Application
├── REST API (Chat, xApp Gen, Troubleshoot)
├── RAG Engine (LangChain4j + OpenAI)
├── Vector Store (In-memory with cosine similarity)
├── xApp Generator (Templates + LLM)
└── Troubleshooting Service (Symptom analysis + Root cause)
```

## Key Technologies

- **Framework**: Quarkus 3.15.1
- **Language**: Java 21
- **AI/ML**: LangChain4j 0.36.2
- **LLM**: OpenAI GPT-4o
- **Embeddings**: OpenAI text-embedding-3-small (1536 dimensions)

## Project Structure

```
src/main/java/com/openran/copilot/
├── model/           # Domain models (20+ classes)
├── rag/             # RAG engine
├── generation/      # xApp code generation
├── troubleshoot/    # Troubleshooting service
├── tmf/             # TMF Open API (placeholder)
├── resource/        # REST API endpoints (6 resources)
└── Config.java      # Bean initialization and spec ingestion
```

## Important Files

### Configuration
- `src/main/resources/application.properties` - Quarkus config
- `src/main/java/com/openran/copilot/Config.java` - CDI beans

### Knowledge Base
- `src/main/resources/oran-specs/` - 8 O-RAN specification documents

### Documentation
- `STAR.md` - STAR method project documentation
- `README.md` - Project overview and usage
- `docs/QA-BANK.md` - 50+ interview questions

## Build and Run

```bash
# Set OpenAI API key
export OPENAI_API_KEY=your-key

# Build
mvn clean package

# Run dev mode
mvn quarkus:dev

# Access
# Web UI: http://localhost:8080/
# API Docs: http://localhost:8080/api/openapi
```

## API Endpoints

### Chat
- `POST /api/v1/chat` - Send message with session
- `POST /api/v1/chat/simple` - One-shot Q&A

### xApp Generation
- `POST /api/v1/xapp/generate` - Generate from description
- `POST /api/v1/xapp/generate/template` - Generate from template
- `GET /api/v1/xapp/templates` - List templates

### Troubleshooting
- `POST /api/v1/troubleshoot/diagnose` - Diagnose issue
- `GET /api/v1/troubleshoot/cases` - List cases

### Knowledge Base
- `GET /api/v1/specs/search` - Search specs
- `GET /api/v1/specs/stats` - Get stats

### System
- `GET /api/v1/health` - Health check

## Coding Guidelines

### Java Style
- Use Java 21 features
- Follow standard naming conventions
- Add Javadoc for public methods
- Use `@Inject` for CDI

### Error Handling
- Wrap external API calls in try-catch
- Log errors with appropriate level
- Return meaningful error messages
- Use HTTP status codes correctly

### Logging
- Use `java.util.logging.Logger`
- Log at appropriate levels (SEVERE, WARNING, INFO, FINE)
- Include context in log messages

### Testing
- Write unit tests for service classes
- Test REST endpoints with REST Assured
- Mock external dependencies (OpenAI API)

## Common Tasks

### Add New O-RAN Spec

1. Create markdown file in `src/main/resources/oran-specs/`
2. Add entry to `Config.ingestSpecs()`
3. Rebuild and restart

### Add New xApp Template

1. Add to `xAppTemplateEngine.initializeBuiltinTemplates()`
2. Use `{{VARIABLE}}` syntax for placeholders
3. Update `xAppGenResource` if needed

### Add New Service Model

1. Add to `SpecKnowledgeBase.initializeServiceModels()`
2. Define indicators with `IndicatorDefinition`
3. Test with `SpecResource`

## Debugging

### Enable Debug Logging
```properties
quarkus.log.level=DEBUG
quarkus.log.category."com.openran.copilot".level=DEBUG
```

### Check Vector Store
```bash
curl http://localhost:8080/api/v1/specs/stats
```

### Test RAG Query
```bash
curl -X POST http://localhost:8080/api/v1/chat/simple \
  -H "Content-Type: application/json" \
  -d '{"question": "What is O-RAN?"}'
```

## Known Limitations

- In-memory vector store (not persistent)
- No authentication/authorization
- OpenAI API dependency (rate limits, cost)
- Limited to embedded specs

## Future Work

- Persistent vector database (Pinecone, Weaviate)
- User authentication and authorization
- External document upload
- More xApp templates
- Integration with real O-RAN RIC
- Comprehensive test suite
- Observability (metrics, tracing)

## Key Design Decisions

1. **In-memory vector store**: Simple for demo, production should use vector DB
2. **Cosine similarity**: Standard for semantic search
3. **Chunk size 512 tokens**: Balance between precision and context
4. **Overlap 50 tokens**: Preserve context across boundaries
5. **Quarkus**: Fast startup, low memory, cloud-native

## References

- [O-RAN Alliance](https://www.o-ran.org/)
- [LangChain4j Docs](https://docs.langchain4j.dev/)
- [Quarkus Guide](https://quarkus.io/guides/)
- [arxiv 2407.09619](https://arxiv.org/abs/2407.09619)
