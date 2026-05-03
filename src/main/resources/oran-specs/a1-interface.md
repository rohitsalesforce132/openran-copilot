# O-RAN A1 Interface

## Overview

The A1 interface connects the Non-RT RIC to the Near-RT RIC, enabling policy-based, intent-driven control of RAN functions. It operates with latency requirements > 1 second.

## Purpose

### Policy Distribution
- Distribute policies from Non-RT RIC to Near-RT RIC
- Enable intent-based control
- Support policy versioning and rollback

### Guidance and Enrichment
- Provide ML model parameters to xApps
- Supply external data (weather, events)
- Enable cross-domain coordination

### Performance Management
- Set performance targets (KPI thresholds)
- Define optimization goals
- Establish SLA parameters

## A1 Policy Types

### 1. Handover Policy
Defines handover behavior and optimization targets.

**Parameters:**
- HO success rate target: > 98%
- HO failure penalty weight: 0.8
- Ping-pong HO prevention: enabled
- Min HO time: 300ms

**Example:**
```json
{
  "policyType": "handover",
  "targets": {
    "hoSuccessRate": 0.98,
    "hoLatency": 50
  },
  "constraints": {
    "minHoInterval": 300
  }
}
```

### 2. Load Balancing Policy
Defines traffic distribution goals.

**Parameters:**
- Max cell load: 80%
- Load imbalance threshold: 20%
- Preferred frequency: 3.5 GHz

**Example:**
```json
{
  "policyType": "loadBalancing",
  "targets": {
    "maxCellLoad": 0.8,
    "loadBalanceThreshold": 0.2
  }
}
```

### 3. Power Saving Policy
Defines energy efficiency targets.

**Parameters:**
- Min cells ON: 60%
- Sleep mode duration: 10 min
- Wake-up time: 1 min

### 4. QoS Policy
Defines quality of service requirements.

**Parameters:**
- Min throughput: 10 Mbps
- Max latency: 50 ms
- Min reliability: 99.9%

## A1 Interface Procedures

### 1. Policy Creation
- Non-RT RIC creates new policy
- Assigns unique policy ID
- Validates policy parameters
- Sends to Near-RT RIC

### 2. Policy Update
- Modify existing policy
- Increment policy version
- Provide update reason
- Request acknowledgment

### 3. Policy Delete
- Remove active policy
- Graceful deactivation
- Fallback to default behavior

### 4. Policy Query
- Retrieve active policies
- Get policy status
- Check policy compliance

## Message Format

### Policy Object
```json
{
  "policyID": "policy-001",
  "policyType": "handover",
  "version": "1.0",
  "status": "active",
  "created": "2026-05-03T10:00:00Z",
  "parameters": {
    "hoSuccessRate": 0.98,
    "hoLatency": 50
  },
  "constraints": {
    "minHoInterval": 300
  }
}
```

## A1 Enrichment

### Enrichment Data Types

#### 1. External Data
- Weather conditions (rain, temperature)
- Event schedules (concerts, sports)
- Traffic predictions (holiday, rush hour)

#### 2. ML Model Parameters
- Model weights
- Inference thresholds
- Feature importance

#### 3. Network Context
- Neighbor cell status
- Backhaul capacity
- Core network load

## Security

### Authentication
- Mutual TLS between RICs
- Token-based access
- API key authentication

### Authorization
- Policy create/update/delete permissions
- Role-based access control
- Audit logging

## Performance

### Latency
- Policy deployment: < 1 second
- Policy acknowledgment: < 500ms
- Policy query: < 100ms

### Throughput
- Supports 1000+ active policies
- Handles 100+ policy updates per hour
- Processes 10K+ policy queries per day

## Integration Points

### With Non-RT RIC rApps
- rApps generate policies
- ML models determine optimal parameters
- Policy lifecycle management

### With Near-RT RIC xApps
- xApps receive policies
- xApps enforce policy constraints
- xApps report policy compliance

### With SMO
- SMO triggers policy changes
- SMO monitors policy effectiveness
- SMO manages policy lifecycle

## Use Cases

### 1. Intent-Based Optimization
**Scenario:** Operator wants to optimize for energy efficiency

**A1 Policy:**
```json
{
  "policyType": "powerSaving",
  "intent": "Minimize energy consumption",
  "parameters": {
    "minCellsOn": 0.6,
    "sleepModeDuration": 600
  }
}
```

**xApp Action:**
- Monitor traffic load
- Put low-traffic cells to sleep
- Wake up cells on demand

### 2. SLA Enforcement
**Scenario:** Enterprise customer requires 99.99% availability

**A1 Policy:**
```json
{
  "policyType": "sla",
  "intent": "Enforce enterprise SLA",
  "parameters": {
    "availability": 0.9999,
    "priority": "high"
  }
}
```

**xApp Action:**
- Prioritize enterprise traffic
- Reserve resources
- Trigger failover on degradation

### 3. Event-Driven Scaling
**Scenario:** Large event expected at stadium

**A1 Enrichment:**
- Event schedule: "Stadium event, 7 PM, 50K attendees"
- Traffic prediction: 5x normal

**A1 Policy:**
```json
{
  "policyType": "capacity",
  "parameters": {
    "targetCapacity": "5x",
    "scalingEnabled": true
  }
}
```

**xApp Action:**
- Pre-activate additional cells
- Adjust power levels
- Enable load balancing
