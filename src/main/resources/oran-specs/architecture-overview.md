# O-RAN Architecture Overview

## O-RAN Alliance Architecture

The O-RAN Alliance defines an open, intelligent, and interoperable RAN architecture that disaggregates the traditional RAN components and introduces intelligence through AI/ML.

## Key Components

### 1. O-DU (Open Distributed Unit)
- The O-DU is responsible for real-time Layer 2 (L2) functions and Layer 1 (L1) partial functions
- Implements real-time protocol stack processing (RLC, MAC, partial PHY)
- Connects to O-CU via F1 interface and to Near-RT RIC via E2 interface
- Supports both split 7.2 and split 8 configurations

### 2. O-CU (Open Centralized Unit)
- The O-CU is split into O-CU-CP (Control Plane) and O-CU-UP (User Plane)
- O-CU-CP handles RRC and PDCP-CP
- O-CU-UP handles PDCP-UP
- Connects to O-DU via F1 interface and to 5GC via NG interface
- Implements non-real-time control functions

### 3. Near-RT RIC (Near Real-Time RAN Intelligent Controller)
- Operates with sub-10ms latency
- Hosts xApps (RAN Intelligent Applications)
- Provides control loop functions for optimization
- Connects to O-DU/O-CU via E2 interface
- Runs on edge computing platforms

### 4. Non-RT RIC (Non Real-Time RAN Intelligent Controller)
- Operates with latency > 1 second
- Hosts rApps (RAN Applications)
- Provides AI/ML model training and policy management
- Connects to Near-RT RIC via A1 interface
- Integrates with SMO (Service Management and Orchestration)

### 5. O-RU (Open Radio Unit)
- The O-RU handles RF and L1 baseband functions
- Implements analog/digital conversion, beamforming
- Connects to O-DU via Open Fronthaul interface (eCPRI, RoE)
- Supports multiple fronthaul variants (7-2x, 7-3)

## Interfaces

### E2 Interface
- Between Near-RT RIC and O-DU/O-CU
- Supports control signaling and data reporting
- Enables xApp control over RAN functions
- Defines service models for KPM, RC, EH2

### A1 Interface
- Between Non-RT RIC and Near-RT RIC
- Used for policy distribution and guidance
- Supports policy creation, modification, deletion
- Enables intent-based control

### O1 Interface
- Between SMO and O-RAN components
- Provides management, configuration, and fault reporting
- Based on Netconf/YANG standards
- Supports lifecycle management

### F1 Interface
- Between O-CU and O-DU
- Separated into F1-C (control) and F1-U (user)
- Based on 3GPP specifications
- Supports split 7.2 architecture

### Open Fronthaul
- Between O-DU and O-RU
- Defines eCPRI and RoE transport
- Supports various split options (7-2x, 7-3)
- Enables multi-vendor interoperability

## Service Models

### KPM (Key Performance Measurement)
- Defines KPI reporting format
- Supports periodic and event-driven reporting
- Configurable measurement intervals

### RC (Radio Resource Control)
- Enables xApps to control radio parameters
- Supports cell-level and UE-level control
- Provides conflict resolution

### EH2 (Enhanced Handover)
- Optimizes handover performance
- Supports A3/A4/A5 events
- Enables predictive handover

## AI/ML Integration

### xApps (Near-RT RIC Applications)
- Run on Near-RT RIC
- Implement real-time control loops
- Use ML models for optimization
- Examples: Traffic prediction, Load balancing, HO optimization

### rApps (Non-RT RIC Applications)
- Run on Non-RT RIC
- Train ML models
- Generate policies for xApps
- Examples: Capacity planning, SLA optimization

## Benefits

1. **Openness**: Multi-vendor interoperability
2. **Intelligence**: AI/ML-driven optimization
3. **Flexibility**: Software-defined RAN functions
4. **Scalability**: Cloud-native architecture
5. **Cost Efficiency**: Disaggregated procurement
