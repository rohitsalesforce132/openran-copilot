# Deployment Strategy — Cloud, Edge, or On-Prem?

## TL;DR: Cloud-Native with Edge Caching

**Index in the cloud. Cache at the edge. LLM inference serverless.**

OpenRAN Copilot is a human-in-the-loop tool — engineers query specs and generate code during planning, not during real-time operations. This means the latency budget is seconds, not milliseconds, making cloud deployment the right choice.

## Architecture

```
Engineer's Browser / IDE         Cloud (AWS/Azure/GCP)
┌──────────────────────────┐   ┌──────────────────────────┐
│ Chat interface             │   │ RAG Service (Quarkus)     │
│ xApp code generation UI    │──│ Vector Store + Embeddings │
│ Troubleshooting dashboard  │   │ LLM Inference (LangChain) │
│                            │   │ O-RAN Spec Knowledge Base │
│                            │   │ TMF620/622/688 APIs       │
└──────────────────────────┘   └──────────────────────────┘
                                           │
                                    Optional: Edge Cache
                                   ┌──────────────────────────┐
                                   │ Cached spec embeddings    │
                                   │ Pre-generated xApp templates│
                                   │ Local troubleshooting rules│
                                   └──────────────────────────┘
```

## Why Cloud-Native?

### 1. Latency Budget is Generous
Engineers ask questions like "How do I implement a KPM xApp?" — they expect an answer in 2-5 seconds, not 10 milliseconds. Cloud-hosted LLM inference with a good model (GPT-4o-mini or Claude Haiku) delivers this easily.

### 2. Embedding Storage is Large
The O-RAN specification library (8 specs, each 200-500 lines of detailed content, plus chunked embeddings) is 50-100MB in vector form. This grows as more specs are indexed. Cloud storage is cheap and elastic.

### 3. LLM Compute is Expensive at Edge
Running a capable LLM (even a 7B parameter model) at every site requires dedicated GPU hardware. For a tool engineers use intermittently, this is a poor ROI. Serverless LLM endpoints (pay-per-token) are far more economical.

### 4. Knowledge Base Updates are Centralized
When O-RAN publishes new spec versions, the knowledge base needs re-indexing. This is a batch job — ingest new specs → chunk → embed → update vector store. Cloud-only operation.

### 5. Data Sensitivity is Lower
O-RAN specifications are public documents. The queries engineers ask ("how does E2 interface work?") aren't sensitive. No data sovereignty concerns for this workload.

## OpenRAN Copilot Specific Deployment

| Component | Where | Why |
|-----------|-------|-----|
| RAG Service (Quarkus) | Cloud | Human-in-the-loop, seconds latency OK |
| Vector Store | Cloud | 50-100MB, grows with spec updates |
| LLM Inference | Cloud (serverless) | Pay-per-token, bursty usage |
| O-RAN Spec Knowledge Base | Cloud | Public docs, centralized updates |
| TMF620/622/688 APIs | Cloud | Product catalog/ordering for xApp templates |
| Chat History / Sessions | Cloud (DB) | Persistent conversations |
| Code Generation | Cloud | LLM-powered, not real-time |
| **Optional Edge Cache** | On-Prem DC | Cached embeddings for offline use |

## Optional: Edge Deployment for Air-Gapped Environments

Some operators run fully air-gapped networks (no internet connectivity for security). For these:

1. **Pre-indexed knowledge base:** Export vector store as a snapshot
2. **Smaller LLM:** Use Ollama with a quantized model (Llama 3.1 8B Q4) on-prem
3. **Quarkus native image:** Sub-100ms cold start for the RAG service
4. **Periodic sync:** Sneakernet or secure file transfer for spec updates

This is a real requirement — tier-1 carriers often have separate management networks.

## Scaling Strategy

- **Serverless LLM:** Auto-scale based on concurrent chat sessions
- **Vector Store:** In-memory with periodic snapshots to persistent storage
- **Quarkus:** Horizontal scaling with session affinity for conversations
- **Cost optimization:** Cache frequent queries, pre-compute common xApp templates

## Interview Answer

> "OpenRAN Copilot is cloud-native by design — it's a human-in-the-loop tool where engineers query O-RAN specs and generate xApp code, so the latency budget is seconds, not milliseconds. The RAG service with the vector store and LLM inference all run in the cloud on serverless infrastructure. But I also designed an optional edge deployment path using Ollama with quantized models and Quarkus native images for air-gapped operator environments where even the management network has no internet connectivity. The key insight is matching the deployment to the human interaction pattern — you don't need edge inference when the user is a human reading a response."
