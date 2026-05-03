# Context Development Lifecycle

## Overview

Context is a first-class artifact, just like code. It has a lifecycle: **Generate → Evaluate → Distribute → Observe**, supported by team practices that make context a shared, repeatable, and improvable part of software delivery.

For a RAG-powered system like OpenRAN Copilot, context IS the product. The quality of answers, code generation, and troubleshooting directly depends on the quality of the indexed O-RAN specifications.

## The Four Phases

```
┌───────────┐     ┌───────────┐     ┌─────────────┐     ┌───────────┐
│ GENERATE  │────►│ EVALUATE  │────►│ DISTRIBUTE   │────►│ OBSERVE   │
│           │     │           │     │              │     │           │
│ Ingest &  │     │ Check     │     │ Retrieve &   │     │ Track     │
│ chunk     │     │ quality   │     │ generate     │     │ relevance │
└───────────┘     └───────────┘     └─────────────┘     └───────────┘
       ▲                                                        │
       └────────────── Feedback loop ──────────────────────────┘
```

### 1. GENERATE — Create Context

Produce the context that feeds the RAG pipeline and AI assistants.

| Source | What Gets Generated | Example |
|--------|-------------------|---------|
| O-RAN Specifications | Chunked spec sections with embeddings | `oran-specs/*.md` → vector store |
| xApp Templates | Code templates with parameter descriptions | xApp template registry |
| Incident Playbooks | Troubleshooting steps mapped to symptoms | Diagnosis flow library |
| Architecture Decisions | ADRs, interface contracts, design rationale | `copilot-instructions.md` |
| Engineer Queries | Conversation history, feedback signals | Session logs with ratings |

**Chunking strategy:**
- Split by spec section (not fixed-size chunks) — preserves semantic boundaries
- Max 512 tokens per chunk, 50-token overlap for cross-section context
- Each chunk tagged with metadata: spec name, section number, topic tags
- Re-chunk when specs are updated

### 2. EVALUATE — Quality Check Context

Measure whether indexed context produces correct, relevant answers.

| Check | Question | How to Measure |
|-------|----------|----------------|
| **Retrieval relevance** | Are top-K chunks actually relevant? | Human rating of retrieved chunks (sample) |
| **Answer quality** | Does the generated answer cite the right specs? | Compare answer against spec source |
| **Coverage** | Can the system answer questions across all spec areas? | Test question battery per spec |
| **Freshness** | Are specs current with latest O-RAN releases? | Compare indexed version vs published version |
| **Chunk quality** | Are chunks too large, too small, or split mid-concept? | Review chunk boundaries manually |

**Evaluation pipeline:**
1. Create golden Q&A dataset (50+ questions with known-correct answers)
2. Run RAG retrieval → measure precision@5 and recall@5
3. Generate answers → measure factual accuracy against spec
4. Track over time → regression when specs are re-indexed

### 3. DISTRIBUTE — Get Context Where It's Needed

Deliver context through the right channel for each use case.

```
┌─────────────────────────────────────────────────────┐
│              Context (Vector Store + Specs)           │
└──────────┬──────────┬──────────┬──────────┬──────────┘
           │          │          │          │
      ┌────▼───┐ ┌───▼────┐ ┌──▼───┐ ┌───▼────┐
      │Chat    │ │Code    │ │Troub │ │TMF     │
      │Q&A API │ │Gen API │ │API   │ │Catalog │
      └────────┘ └────────┘ └──────┘ └────────┘
```

**Distribution patterns:**
- **Pull (on-demand):** Chat Q&A retrieves relevant chunks per query — most common pattern
- **Pre-computed:** xApp templates pre-indexed for fast code generation
- **Event-driven:** Troubleshooting triggered by engineer report → context injected
- **Catalog-sync:** TMF620 product catalog syncs with xApp template registry

### 4. OBSERVE — Monitor Context in Production

Track how context performs and feed back improvements.

| Metric | What It Measures | Target |
|--------|-----------------|--------|
| Retrieval hit rate | % queries with relevant top-5 results | > 90% |
| Answer satisfaction | Thumbs up/down on responses | > 80% positive |
| Spec coverage | % of spec topics that have successful Q&A | > 85% |
| Hallucination rate | % answers with fabricated spec references | < 5% |
| Chunk staleness | Days since spec was last re-indexed | < 14 days |
| Code generation success | % generated xApps that compile | > 70% |

---

## Team Practices

### Context as Code
Store O-RAN spec summaries and chunk configurations in version control.
- Spec files in `resources/oran-specs/` — versioned and reviewed
- Chunking config in `application.properties` — versioned
- Golden Q&A dataset in `tests/` — versioned

### Context Reviews
Review context changes like code changes.
- New O-RAN spec version released? Review chunk boundaries.
- Retrieval quality dropped? Review embedding model version.
- New topic area added? Review coverage and chunking.

### Context Ownership
Every piece of context has an owner who keeps it fresh.
- O-RAN spec library → Standards & architecture team
- xApp templates → RAN engineering team
- Troubleshooting playbooks → SRE team
- Golden Q&A dataset → QA team

### Context Debt
Track stale/missing context like technical debt.
- "O-RAN O2 interface spec not indexed yet" = context debt
- "E2 service model chunks split mid-message-flow" = context debt
- "xApp templates don't cover rApps" = context debt
- "Troubleshooting patterns not updated for v5.0 specs" = context debt

### Context Rituals
Build context maintenance into team ceremonies.
- **Sprint planning:** Review context debt, prioritize spec indexing
- **Post-release:** Re-index specs when O-RAN publishes new versions
- **Weekly:** Review answer satisfaction scores and hallucination reports
- **Monthly:** Full golden Q&A regression test, update coverage metrics

---

## Why This Matters for RAG Systems

RAG systems are only as good as their indexed context. The classic failure modes:

| Failure | Symptom | Root Cause | Fix |
|---------|---------|-----------|-----|
| Stale context | Answer cites deprecated O-RAN spec | Spec updated but not re-indexed | Auto-reindex on spec publish |
| Missing context | "I don't have information about that" | Spec section not chunked/indexed | Coverage audit, add missing sections |
| Wrong context | Answer about E2 when asked about A1 | Embedding similarity picks wrong chunks | Improve chunking, add metadata filters |
| Hallucination | Cites non-existent spec section | LLM generates beyond retrieved context | Grounding prompts, citation requirements |

---

## Interview Answer

> "In OpenRAN Copilot, context IS the product — it's a RAG system where answer quality depends entirely on indexed spec quality. We generate context by chunking O-RAN specifications into semantically meaningful sections and embedding them. We evaluate through a golden Q&A dataset that measures retrieval precision and factual accuracy. We distribute via pull-based RAG retrieval for chat, pre-computed templates for code generation, and event-driven context injection for troubleshooting. We observe retrieval hit rates, answer satisfaction, and hallucination rates. The critical insight for RAG systems: you need a context lifecycle, not just a one-time indexing job. Specs change, chunk boundaries need adjustment, and embedding models improve — without active context management, the system degrades silently."
