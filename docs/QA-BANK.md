# OpenRAN Copilot - Question Bank

## 1. O-RAN Architecture

**Q1.1**: What are the main components of the O-RAN architecture?
**A1.1**: The main components are:
- **O-RU** (Radio Unit): RF signal processing, beamforming
- **O-DU** (Distributed Unit): Real-time L2 processing (MAC, RLC)
- **O-CU** (Centralized Unit): Non-real-time L3 processing (PDCP, RRC)
- **Near-RT RIC**: Real-time intelligence, hosts xApps
- **Non-RT RIC**: Strategic intelligence, hosts rApps

**Q1.2**: What is the difference between Near-RT RIC and Non-RT RIC?
**A1.2**:
- **Near-RT RIC**: Real-time control (< 10-50ms), hosts xApps, connects via E2 interface
- **Non-RT RIC**: Non-real-time control (> 1s), hosts rApps, connects via A1 interface

**Q1.3**: What are the key O-RAN interfaces?
**A1.3**:
- **E2**: Near-RT RIC ↔ O-DU/O-CU (data and control)
- **A1**: Non-RT RIC ↔ Near-RT RIC (policy management)
- **O1**: O-RAN components ↔ SMO (management and orchestration)
- **Fronthaul**: O-RU ↔ O-DU (eCPRI)
- **F1**: O-CU ↔ O-DU (3GPP standard)

**Q1.4**: What is an xApp?
**A1.4**: An xApp is a real-time application running on the Near-RT RIC that observes RAN conditions via E2 and makes control decisions. Examples: load balancing, handover optimization, interference management.

**Q1.5**: What is an rApp?
**A1.5**: An rApp is a non-real-time application running on the Non-RT RIC for strategic optimization, AI/ML model training, and policy generation.

**Q1.6**: What are the benefits of O-RAN?
**A1.6**: Openness (multi-vendor interoperability), Intelligence (built-in AI/ML), Flexibility (virtualization), Innovation (open ecosystem), Cost Efficiency (disaggregation).

---

## 2. RAG Fundamentals

**Q2.1**: What is RAG?
**A2.1**: Retrieval-Augmented Generation is a technique that combines information retrieval with LLM generation. It retrieves relevant context from a knowledge base, then uses that context to generate more accurate and grounded responses.

**Q2.2**: What are the key components of a RAG system?
**A2.2**:
- **Document Ingestion**: Parse and chunk documents
- **Embedding Generation**: Convert text to vector representations
- **Vector Store**: Store and search embeddings
- **Retriever**: Find relevant chunks for a query
- **Generator (LLM)**: Synthesize response using retrieved context

**Q2.3**: What is chunking and why is it important?
**A2.3**: Chunking splits documents into smaller pieces (typically 512-1000 tokens) for embedding and retrieval. It's important because:
- Embedding models have token limits
- Smaller chunks improve retrieval precision
- Overlapping chunks preserve context

**Q2.4**: What is cosine similarity?
**A2.4**: Cosine similarity measures the angle between two vectors, calculated as (A · B) / (||A|| × ||B||). It ranges from -1 (opposite) to 1 (identical), with 0 indicating orthogonality. It's used in vector search to find semantically similar text.

**Q2.5**: What is the purpose of overlapping chunks?
**A2.5**: Overlapping chunks (typically 10-20% overlap) preserve context across chunk boundaries, ensuring that relevant information isn't split in a way that hurts retrieval.

**Q2.6**: How do you choose the chunk size?
**A2.6**: Consider:
- Embedding model's token limit
- Desired granularity (smaller = more precise, larger = more context)
- Query complexity (complex queries need larger chunks)
- Typical values: 256-1024 tokens

**Q2.7**: What is re-ranking in RAG?
**A2.7**: Re-ranking is a second-stage retrieval process that reorders initially retrieved results using a more sophisticated model (e.g., cross-encoder) to improve relevance.

---

## 3. Vector Stores

**Q3.1**: What is a vector store?
**A3.1**: A vector store (or vector database) is a specialized database optimized for storing and searching high-dimensional vectors (embeddings) using similarity search.

**Q3.2**: What are common vector similarity metrics?
**A3.2**:
- **Cosine Similarity**: Measures angle, magnitude-independent
- **Euclidean Distance**: Measures straight-line distance
- **Dot Product**: Combines magnitude and angle
- **Manhattan Distance**: Sum of absolute differences

**Q3.3**: What is HNSW (Hierarchical Navigable Small World)?
**A3.3**: HNSW is an approximate nearest neighbor algorithm that builds a graph structure for fast, scalable vector search with high recall. It's used by vector databases like Weaviate and Milvus.

**Q3.4**: What is the difference between exact and approximate search?
**A3.4**:
- **Exact search**: Guarantees finding the true nearest neighbors, slower for large datasets
- **Approximate search**: Very fast, may miss some results, suitable for large-scale applications

**Q3.5**: How does metadata filtering work in vector stores?
**A3.5**: Metadata filtering allows you to constrain search results by attached metadata (e.g., spec category, date, author). It's applied before or after similarity search to narrow results.

**Q3.6**: What is the typical dimension of OpenAI embeddings?
**A3.6**: text-embedding-ada-002: 1536 dimensions, text-embedding-3-small: 1536 dimensions, text-embedding-3-large: 3072 dimensions.

---

## 4. LangChain4j

**Q4.1**: What is LangChain4j?
**A4.1**: LangChain4j is a Java library for building LLM-powered applications, providing abstractions for chat models, embedding models, retrievers, and RAG pipelines.

**Q4.2**: What are the main abstractions in LangChain4j?
**A4.2**:
- **ChatLanguageModel**: For text generation (e.g., OpenAI GPT)
- **EmbeddingModel**: For generating embeddings
- **ChatMemory**: For conversation history
- **Retriever**: For context retrieval
- **ContentRetriever**: For document retrieval
- **Augmentor**: For context augmentation

**Q4.3**: How do you create a chat model in LangChain4j?
**A4.3**:
```java
ChatLanguageModel model = OpenAiChatModel.builder()
    .apiKey(apiKey)
    .modelName("gpt-4o")
    .temperature(0.3)
    .build();
```

**Q4.4**: How do you generate embeddings in LangChain4j?
**A4.4**:
```java
EmbeddingModel model = OpenAiEmbeddingModel.builder()
    .apiKey(apiKey)
    .modelName("text-embedding-3-small")
    .build();

Embedding embedding = model.embed("text").content();
double[] vector = embedding.vector();
```

**Q4.5**: What is a system prompt in LangChain4j?
**A4.5**: A system prompt sets the behavior and context for the chat model, defining its role, guidelines, and constraints. In OpenRAN Copilot, it instructs the model to be an O-RAN expert.

**Q4.6**: How do you implement RAG with LangChain4j?
**A4.6**: Use the `ContentRetriever` and `Augmentor` abstractions, or build a custom RAG pipeline that:
1. Generates query embedding
2. Searches vector store
3. Builds context from retrieved chunks
4. Passes context + question to chat model

---

## 5. xApp Development

**Q5.1**: What are the key steps to develop an xApp?
**A5.1**:
1. Define use case (monitoring, optimization, troubleshooting)
2. Choose service model(s) (KPM, RC, etc.)
3. Design subscription and indication handling
4. Implement business logic
5. Add control operations if needed
6. Test with O-RAN simulator
7. Package and deploy

**Q5.2**: What is the E2 interface used for in xApps?
**A5.2**: The E2 interface connects xApps to the RAN, allowing them to:
- Subscribe to RAN data (KPIs, measurements)
- Receive indications (events, periodic reports)
- Send control operations (parameter changes)

**Q5.3**: What is a service model?
**A5.3**: A service model defines the data structure and procedures for E2 interface communication. Standard models include KPM (KPIs), RC (radio control), GNB-NRT (gNB info).

**Q5.4**: How do you subscribe to KPM indicators?
**A5.4**:
```java
KPMSubscription subscription = new KPMSubscription();
subscription.addIndicator("RRU.PrbUsedDl");
subscription.addIndicator("HO.ExecSuccess");
subscription.setReportingIntervalMs(1000);
e2Client.subscribe(subscription, this::onIndication);
```

**Q5.5**: What are the xApp lifecycle callbacks?
**A5.5**:
- `onStart()`: Initialize resources, subscribe to E2
- `onIndication(Indication)`: Handle RAN events
- `onStop()`: Cleanup, unsubscribe

**Q5.6**: What is the difference between ACK-REQUIRED and NO-ACK control styles?
**A5.6**:
- **ACK-REQUIRED**: O-DU must respond with success/failure, used for critical operations
- **NO-ACK**: Fire-and-forget, used for non-critical updates

**Q5.7**: How do you handle errors in xApps?
**A5.7**:
- Wrap E2 operations in try-catch
- Log all exceptions
- Implement retry logic for transient errors
- Use circuit breakers for persistent failures
- Report errors via A1 or O1

---

## 6. O-RAN Service Models

**Q6.1**: What is the KPM service model?
**A6.1**: KPM (Key Performance Measurement) provides real-time KPI monitoring including PRB utilization, throughput, handover success rate, and connection metrics.

**Q6.2**: What are common KPM indicators?
**A6.2**:
- `RRU.PrbUsedDl/Ul`: PRB utilization
- `DRB.UEThpDl/Ul`: UE throughput
- `HO.ExecSuccess`: Handover success rate
- `RRC.ConnSuccess`: RRC connection success rate
- `CALL.DroppedRate`: Call drop rate

**Q6.3**: What is the RC service model?
**A6.3**: RC (Radio Control) enables RAN parameter control including cell configuration (PCI, bandwidth), transmission power, beamforming parameters, and scheduling parameters.

**Q6.4**: What is the GNB-NRT service model?
**A6.4**: GNB-NRT provides gNB-level information including capacity information, resource utilization, coverage data, and neighbor cell relations.

**Q6.5**: How do you choose which service model to use?
**A6.5**:
- Use **KPM** for monitoring KPIs and performance
- Use **RC** for controlling RAN parameters
- Use **GNB-NRT** for gNB-level information
- Combine multiple models for complex xApps

---

## 7. Quarkus Implementation

**Q7.1**: What is Quarkus?
**A7.1**: Quarkus is a Kubernetes-native Java framework optimized for fast startup, low memory footprint, and developer productivity. It's ideal for microservices and serverless.

**Q7.2**: What is CDI in Quarkus?
**A7.2**: CDI (Contexts and Dependency Injection) is the Java EE standard for dependency injection, used in Quarkus for managing beans and their lifecycles (`@Singleton`, `@ApplicationScoped`, etc.).

**Q7.3**: How do you create a REST endpoint in Quarkus?
**A7.3**:
```java
@Path("/api/v1/chat")
public class ChatResource {
    @POST
    public Response chat(ChatRequest request) {
        return Response.ok(response).build();
    }
}
```

**Q7.4**: What is the difference between `@Singleton` and `@ApplicationScoped`?
**A7.4**:
- `@Singleton`: One instance per JVM (eager)
- `@ApplicationScoped`: One instance per application (lazy, proxyable)

**Q7.5**: How do you configure Quarkus?
**A7.5**: Use `application.properties` or `application.yml` for configuration. Inject values with `@ConfigProperty`:
```java
@ConfigProperty(name = "openai.api.key")
String apiKey;
```

**Q7.6**: What is Quarkus Dev Mode?
**A7.6**: Dev Mode provides live reload (code changes are reflected immediately), hot deployment, and development tools. Start with `mvn quarkus:dev`.

**Q7.7**: How do you handle async operations in Quarkus?
**A7.7**: Use `@Asynchronous` or `CompletionStage`/`Uni` for reactive programming:
```java
@Asynchronous
public CompletableFuture<Response> asyncOperation() { ... }
```

---

## 8. Prompt Engineering

**Q8.1**: What is a system prompt?
**A8.1**: A system prompt sets the persona, role, and behavior guidelines for the LLM. It's the first message sent and defines how the model should respond.

**Q8.2**: What is few-shot prompting?
**A8.2**: Few-shot prompting provides examples in the prompt to guide the model's output format and style. It improves performance without fine-tuning.

**Q8.3**: What is chain-of-thought prompting?
**A8.3**: Chain-of-thought encourages the model to show its reasoning process by asking it to "think step by step," improving performance on complex tasks.

**Q8.4**: How do you design a good RAG prompt?
**A8.4**:
1. Define the role (e.g., "You are an O-RAN expert")
2. Provide guidelines (cite sources, be precise)
3. Include retrieved context clearly labeled
4. State the question clearly
5. Specify output format if needed

**Q8.5**: What is the role of temperature in LLMs?
**A8.5**: Temperature controls randomness:
- Low (0.1-0.3): Deterministic, factual
- Medium (0.5-0.7): Balanced creativity
- High (0.8-1.0): Creative, diverse

**Q8.6**: How do you reduce hallucinations in RAG?
**A8.6**:
- Ground responses in retrieved context
- Require citations
- Use lower temperature
- Add instructions to say "I don't know" if uncertain
- Validate generated content

---

## 9. System Design

**Q9.1**: How would you scale the RAG system?
**A9.1**:
- Use a distributed vector database (Pinecone, Weaviate)
- Implement caching for repeated queries
- Use approximate nearest neighbor (ANN) for fast search
- Shard the vector store by category
- Use load balancing for the API

**Q9.2**: How would you handle spec updates?
**A9.2**:
- Watch spec file changes
- Re-ingest updated documents
- Re-generate embeddings
- Update vector store
- Version specs for rollback

**Q9.3**: How would you add authentication?
**A9.3**:
- Use JWT tokens for API authentication
- Implement OAuth2/OIDC
- Add API keys for external access
- Use TLS for all communication
- Implement role-based access control

**Q9.4**: How would you persist sessions?
**A9.4**: Use a database (PostgreSQL, MongoDB) to store chat sessions, messages, and context. Add indexes for efficient querying.

**Q9.5**: How would you deploy this to production?
**A9.5**:
- Containerize with Docker
- Deploy to Kubernetes
- Use external services (PostgreSQL, vector DB)
- Implement CI/CD pipeline
- Add monitoring and logging (Prometheus, Grafana, ELK)

**Q9.6**: How would you handle rate limiting on OpenAI API?
**A9.6**:
- Implement exponential backoff
- Use a queue with retry logic
- Cache repeated queries
- Use batch processing for embeddings
- Consider local embedding models for high volume

---

## 10. Interview Scenarios

**Q10.1**: Describe how you would implement a load balancing xApp.
**A10.1**:
1. Subscribe to KPM indicators (PRB utilization, UE count)
2. Identify overloaded (>80%) and underloaded (<50%) cells
3. For overloaded cells:
   - Reduce transmission power to offload UEs
   - Increase handover offset to make cell less attractive
4. For underloaded cells:
   - Increase transmission power to attract UEs
   - Decrease handover offset to make cell more attractive
5. Monitor results and adjust parameters

**Q10.2**: How would you troubleshoot high handover failure rates?
**A10.2**:
1. Subscribe to HO.ExecSuccess and HO.ExecFailure indicators
2. Analyze failure distribution by cause
3. Check A3 offset and TTT parameters
4. Verify neighbor cell relations
5. Check for coverage issues or interference
6. Adjust parameters or add missing neighbors
7. Monitor improvement

**Q10.3**: How would you implement an anomaly detection xApp?
**A10.3**:
1. Collect historical KPI data
2. Train ML model (in rApp) to detect anomalies
3. Deploy model to xApp
4. Real-time scoring of KPIs
5. Generate alerts when anomalies detected
6. Provide root cause analysis

**Q10.4**: Describe a challenging bug you fixed in this project.
**A10.4**: Example: The vector store was returning zero similarity for all queries. Root cause: Embedding dimension mismatch (expected 1536, got 768). Fix: Updated embedding model configuration and re-ingested all specs.

**Q10.5**: How would you explain RAG to a non-technical stakeholder?
**A10.5**: "RAG is like giving an AI assistant a reference library before asking questions. Instead of relying only on what it learned during training, it first searches our documents for relevant information, then uses that to answer questions accurately with citations."

**Q10.6**: What would you improve in this project given more time?
**A10.6**:
- Add persistent vector database
- Implement user authentication
- Support external document upload
- Add more xApp templates
- Integrate with real O-RAN RIC
- Add comprehensive test suite
- Implement caching layer
- Add observability (metrics, tracing)

---

## Bonus Questions

**Q11.1**: What is the difference between D-RAN, C-RAN, and Cloud RAN?
**A11.1**:
- **D-RAN**: Distributed RAN - all components co-located at cell site
- **C-RAN**: Centralized RAN - O-RUs distributed, O-DU/CU centralized
- **Cloud RAN**: Full virtualization of O-DU/CU on cloud infrastructure

**Q11.2**: What is network slicing?
**A11.2**: Network slicing creates multiple logical networks on shared physical infrastructure, each tailored to specific service requirements (eMBB, URLLC, mMTC).

**Q11.3**: What is SMO in O-RAN?
**A11.3**: SMO (Service Management and Orchestration) is the management layer that handles lifecycle management, configuration, and orchestration of O-RAN components via the O1 interface.

**Q11.4**: What is the A3 event in handover?
**A11.4**: The A3 event triggers handover when a neighbor cell becomes better than the serving cell by a configured offset. Key parameters: A3 offset (dB) and TTT (Time to Trigger in ms).

**Q11.5**: What is PRB?
**A11.5**: PRB (Physical Resource Block) is the smallest unit of resource allocation in LTE/5G, consisting of 12 subcarriers × 14 OFDM symbols = 84 resource elements. PRB utilization is a key KPI for capacity.
