# O-RAN E2 Interface

## Overview

The E2 interface connects the Near-RT RIC to the O-DU and O-CU, enabling intelligent control over RAN functions. It is the primary interface for xApp interactions with the radio network.

## Protocol Stack

### Lower Layers
- **Transport**: SCTP
- **Network**: IP
- **Data Link**: Ethernet

### Application Layer
- **E2AP (E2 Application Protocol)**: E2-AP PDU definitions
- **RAN Functions**: Service-specific protocol stacks

## E2-AP Procedures

### 1. E2 Setup
- Initiates connection between RIC and RAN node
- Negotiates supported RAN functions
- Establishes global RIC ID

**Procedure Flow:**
1. RIC → RAN Node: E2 SETUP REQUEST
2. RAN Node → RIC: E2 SETUP RESPONSE

### 2. RIC Service Update
- Adds or removes supported RAN functions
- Updates RAN function capabilities
- Dynamically reconfigures E2 connection

### 3. E2 Reset
- Resets E2 connection state
- Clears all subscriptions and controls
- Used for error recovery

### 4. Error Indication
- Reports protocol errors
- Indicates procedure failures
- Provides error codes for troubleshooting

## RAN Function Indication/Control

### Indication (RAN → RIC)
- Reports measurements and events
- Sends unsolicited notifications
- Provides RAN status updates

### Control (RIC → RAN)
- Controls RAN parameters
- Modifies RAN behavior
- Triggers RAN actions

## Service Models

### KPM Service Model (E2SM-KPM)
**Purpose:** Key Performance Measurement reporting

**RIC Style:**
- **Style 1**: Periodic reporting
- **Style 2**: Event-triggered reporting
- **Style 3**: On-demand reporting

**Key Elements:**
- Measurement definitions
- Reporting configuration
- Measurement data format

**Supported Metrics:**
- Cell throughput (UL/DL)
- User latency
- Dropped call rate
- Handover success rate
- PRB utilization
- RSRP/RSRQ/SINR

### RC Service Model (E2SM-RC)
**Purpose:** Radio Resource Control

**Control Actions:**
- Cell ON/OFF
- Power adjustment
- Antenna tilt control
- Handover parameter modification
- Cell individual offset

**Conflict Resolution:**
- Priority-based arbitration
- Last-writer-wins for same parameter
- Conflict notification to xApps

### EH2 Service Model (E2SM-EH2)
**Purpose:** Enhanced Handover

**Events:**
- A3 event (neighbour better serving cell)
- A4 event (neighbour becomes better than threshold)
- A5 event (serving cell becomes worse than threshold, neighbour better)

**Control Actions:**
- HO parameter adjustment
- TTT (Time to Trigger) modification
- Hysteresis tuning

## Message Formats

### E2-AP PDU Structure
```
E2AP-PDU {
    initiating-message | successful-outcome | unsuccessful-outcome | ...
}
```

### E2 Setup Request
```
 initiating-message {
    procedureCode: id-E2Setup,
    criticality: reject,
    value: {
        globalRIC-ID,
        ranFunctions-List,
        ...
    }
}
```

### RIC Indication
```
 successful-outcome {
    procedureCode: id-RICindication,
    criticality: ignore,
    value: {
        ranFunction-ID,
        ricRequest-ID,
        ranFunction-IE,
        ...
    }
}
```

## Security

### Security Modes
- **Mode 1**: No protection (not recommended for production)
- **Mode 2**: Integrity protection
- **Mode 3**: Integrity + confidentiality
- **Mode 4**: Full protection with mutual authentication

### Key Management
- Pre-shared keys for lab environments
- Certificate-based authentication for production
- Key rotation supported

## Performance

### Latency Requirements
- E2 Setup: < 100ms
- Indication delivery: < 5ms
- Control execution: < 10ms

### Throughput
- Supports 1000+ indications per second
- Handles 100+ concurrent subscriptions
- Processes 10M+ messages per day

## Configuration

### SCTP Parameters
- Max streams: 10
- Max retransmissions: 4
- Timeout: 5 seconds

### E2-AP Timers
- T1 retransmission timer: 1s
- T2 response timer: 2s
- T3 guard timer: 10s

## Troubleshooting

### Common Issues

#### 1. E2 Setup Failure
- Check RIC ID configuration
- Verify RAN function compatibility
- Review SCTP association status

#### 2. Subscription Rejection
- Verify service model support
- Check resource availability
- Review xApp permissions

#### 3. Control Action Failure
- Check conflict resolution status
- Verify parameter validity
- Review RAN node state
