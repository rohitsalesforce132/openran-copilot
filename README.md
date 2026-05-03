# OpenRAN Copilot

RAG-powered AI Assistant for O-RAN Networks — Chat with specifications, generate xApp code, and troubleshoot issues using AI.

## 🎯 What It Does

OpenRAN Copilot is an intelligent assistant that helps telecom engineers and developers work with O-RAN (Open Radio Access Network) technology:

- **📚 Chat with O-RAN Specs:** Ask questions about O-RAN architecture, interfaces, and get instant answers grounded in actual specifications
- **🤖 Generate xApp Code:** Describe an xApp in natural language and get production-ready Python/Java code
- **🔧 Troubleshoot Issues:** Describe network symptoms and get AI-powered diagnosis and remediation recommendations
- **📖 Browse Knowledge Base:** Search through 500+ pages of O-RAN documentation with semantic search

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     OpenRAN Copilot                        │
├─────────────────────────────────────────────────────────────┤
│  REST API (Quarkus)                                         │
│  ├─ /api/v1/chat          - Chat interface                 │
│  ├─ /api/v1/xapp/generate  - xApp code generation         │
│  ├─ /api/v1/troubleshoot  - Network troubleshooting       │
│  └─ /api/v1/specs         - Spec search & ingestion       │
├─────────────────────────────────────────────────────────────┤
│  Core Services                                             │
│  ├─ RAGService        - Retrieval + Generation            │
│  ├─ VectorStore       - Cosine similarity search          │
│  ├─ xAppGenerator     - Code generation                  │
│  └─ TroubleshootEngine - Diagnosis & recommendations      │
├─────────────────────────────────────────────────────────────┤
│  Integrations                                             │
│  ├─ LangChain4j (OpenAI GPT-4)                           │
│  ├─ TMF620/622/640/688 APIs                             │
│  └─ O-RAN Spec Knowledge Base (8 documents)              │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- Java 21+
- Maven 3.9+
- OpenAI API key (set in `application.properties`)

### Build & Run

```bash
# Build the project
mvn clean install

# Run in dev mode
mvn quarkus:dev

# Build native image (optional)
mvn package -Dnative
```

### Access

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui
- **OpenAPI Spec:** http://localhost:8080/openapi

## 📚 Knowledge Base

The system includes 8 O-RAN specification documents:

1. **Architecture Overview** — O-RAN components and interfaces
2. **Near-RT RIC** — Real-time RAN intelligent controller
3. **E2 Interface** — RIC-RAN control interface
4. **A1 Interface** — Policy distribution interface
5. **KPM Service Model** — Key performance measurements
6. **xApp Development** — RAN intelligent applications
7. **Network Slicing** — 5G slicing in O-RAN
8. **O1 Interface** — Management interface

## 🔌 API Examples

### Chat with O-RAN Specs

```bash
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is the difference between E2 and A1 interfaces?",
    "sessionId": "session-123"
  }'
```

**Response:**
```json
{
  "sessionId": "session-123",
  "response": "The E2 interface connects the Near-RT RIC to O-DU/O-CU for real-time control...",
  "timestamp": 1714732800000
}
```

### Generate xApp Code

```bash
curl -X POST http://localhost:8080/api/v1/xapp/generate/code \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Create an xApp that monitors cell throughput and triggers load balancing when > 80%",
    "serviceModel": "KPM"
  }'
```

**Response:**
```json
{
  "code": "import ric_xapp as ric\n...",
  "language": "python",
  "serviceModel": "KPM",
  "confidence": 0.92
}
```

### Troubleshoot Network Issues

```bash
curl -X POST http://localhost:8080/api/v1/troubleshoot/diagnose \
  -H "Content-Type: application/json" \
  -d '{
    "symptoms": "High DCR rate of 3.5% and low throughput in cell-4521"
  }'
```

**Response:**
```json
{
  "symptoms": "High DCR rate of 3.5% and low throughput in cell-4521",
  "rootCause": "Downlink Interference + Congestion",
  "recommendations": [
    "Check for external interference sources",
    "Adjust antenna down-tilt",
    "Enable ICIC activation"
  ],
  "confidence": 0.88
}
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=RAGServiceTest

# Run with coverage
mvn test jacoco:report
```

## 📖 Documentation

- **Interview Prep:** [STAR.md](STAR.md) — STAR method summary for interviews
- **Q&A Bank:** [docs/QA-BANK.md](docs/QA-BANK.md) — 60+ interview questions
- **Development:** [GitHub Copilot Instructions](.github/copilot-instructions.md)

## 🔧 Configuration

Key configuration in `src/main/resources/application.properties`:

```properties
# LLM Configuration
quarkus.langchain4j.openai.chat-model.model-name=gpt-4
quarkus.langchain4j.openai.chat-model.temperature=0.7

# RAG Configuration
openran.rag.chunk-size=500
openran.rag.chunk-overlap=50
openran.rag.top-k=3
openran.rag.similarity-threshold=0.7
```

## 🛠️ Tech Stack

- **Framework:** Quarkus 3.8.0
- **LLM Integration:** LangChain4j (OpenAI GPT-4)
- **Vector Search:** In-memory with cosine similarity
- **REST API:** JAX-RS (Quarkus REST)
- **Build Tool:** Maven
- **Java Version:** 21

## 🌟 Key Features

### RAG-Powered Chat
- Vector embeddings for semantic search
- Context-aware responses grounded in specs
- Session-based conversations with history

### xApp Code Generation
- Natural language → Python/Java xApp code
- Template-based generation with LLM enhancement
- Supports KPM, RC, EH2 service models

### Troubleshooting Engine
- Symptom analysis with KPI pattern matching
- Root cause identification using rule-based logic
- Fix recommendations from historical case database

### TMF Open API Integration
- TMF620 (Product Catalog)
- TMF622 (Product Ordering)
- TMF640 (Service Activation)
- TMF688 (Event Management)

## 📊 Project Stats

- **Java Files:** 45
- **Lines of Code:** ~6,700
- **Test Classes:** 6
- **Test Methods:** 18
- **API Endpoints:** 25+
- **Spec Documents:** 8
- **Q&A Questions:** 60

## 🤝 Contributing

This is a demonstration project. For production deployment, consider:
- Persistent vector store (Milvus, Qdrant)
- Multi-modal retrieval (images, diagrams)
- RLHF feedback loop
- Distributed tracing and observability

## 📄 License

MIT License — See LICENSE file for details

## 🔗 References

- [O-RAN Alliance](https://www.o-ran.org/)
- [Quarkus](https://quarkus.io/)
- [LangChain4j](https://docs.langchain4j.dev/)
- [TM Forum Open APIs](https://www.tmforum.org/oda/open-apis/table)
