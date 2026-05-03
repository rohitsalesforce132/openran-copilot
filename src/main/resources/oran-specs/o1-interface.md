# O1 Interface Specification

## Overview

The O1 interface connects O-RAN components (O-RU, O-DU, O-CU, RIC) to the Service Management and Orchestration (SMO) layer. It provides management and orchestration capabilities for configuration, fault, performance, and security management.

## Protocol Stack

### Transport
- HTTP/HTTPS (RESTful)
- NETCONF/YANG
- gRPC

### Security
- TLS 1.3
- Mutual authentication (mTLS)
- Certificate-based authorization

## Management Areas

### 1. Configuration Management

#### Configuration Data
- Cell configuration (PCI, bandwidth, frequency)
- Power settings
- Neighbor relations
- Feature toggles

#### Configuration Operations

**GET Configuration**
```http
GET /o1/v1/nodes/{nodeId}/config
```

**SET Configuration**
```http
PUT /o1/v1/nodes/{nodeId}/config
Content-Type: application/yang-data+json

{
  "cell-config": {
    "pci": 120,
    "dl-bandwidth": 100,
    "ul-bandwidth": 100,
    "dl-frequency": 367000,
    "ul-frequency": 215000
  }
}
```

**YANG Model Example**
```yang
module cell-config {
  namespace "urn:o-ran:cell-config";
  prefix cell;

  container cell-config {
    leaf pci {
      type uint16 {
        range "0..1007";
      }
      mandatory true;
    }

    leaf dl-bandwidth {
      type enumeration {
        enum 5 { value 5; }
        enum 10 { value 10; }
        enum 15 { value 15; }
        enum 20 { value 20; }
        enum 25 { value 25; }
        enum 30 { value 30; }
        enum 40 { value 40; }
        enum 50 { value 50; }
        enum 60 { value 60; }
        enum 80 { value 80; }
        enum 90 { value 90; }
        enum 100 { value 100; }
      }
      mandatory true;
    }
  }
}
```

### 2. Fault Management

#### Alarm Notifications

**Alarm Structure**
```json
{
  "alarmId": "ALM-001",
  "nodeId": "gNB-001",
  "alarmType": "COMMUNICATION_LOSS",
  "perceivedSeverity": "CRITICAL",
  "probableCause": "Loss of signal",
  "raisedTime": "2024-01-15T10:30:00Z",
  "additionalText": "Fronthaul link to O-RU-01 lost",
  "proposedRepairActions": [
    "Check fiber connection",
    "Verify O-RU power supply",
    "Review logs for errors"
  ]
}
```

#### Alarm Severity Levels
- **CRITICAL**: Service affecting, immediate action required
- **MAJOR**: Significant impact, action required soon
- **MINOR**: Minor impact, schedule for repair
- **WARNING**: Potential issue, monitor closely
- **INDETERMINATE**: Severity cannot be determined
- **CLEARED**: Alarm has been resolved

#### Alarm Subscription
```http
POST /o1/v1/alms/subscribe
{
  "nodeId": "gNB-001",
  "notificationUrl": "https://smo/o1/alarms/callback",
  "filter": {
    "severity": ["CRITICAL", "MAJOR"],
    "alarmTypes": ["COMMUNICATION_LOSS", "HARDWARE_FAILURE"]
  }
}
```

### 3. Performance Management

#### Performance Counters

**Counter Categories**
- **Resource Utilization**: PRB usage, CPU, memory
- **Traffic**: Throughput, packet loss, delay
- **Mobility**: Handover success, handover failures
- **Connection**: RRC setup success, connection drops
- **Quality**: SINR, RSRP, CQI

**Performance Data Response**
```json
{
  "nodeId": "gNB-001",
  "timestamp": "2024-01-15T10:00:00Z",
  "counters": {
    "RRU.PrbUsedDl": 75.5,
    "RRU.PrbUsedUl": 45.2,
    "DRB.UEThpDl": 25.3,
    "HO.ExecSuccess": 98.5,
    "RRC.ConnSuccess": 99.2
  }
}
```

#### Performance Data Retrieval
```http
GET /o1/v1/nodes/{nodeId}/pm?startTime=2024-01-15T09:00:00Z&endTime=2024-01-15T10:00:00Z&granularity=PT5M
```

**Granularity Options**
- PT1M: 1 minute
- PT5M: 5 minutes
- PT15M: 15 minutes
- PT1H: 1 hour

### 4. Software Management

#### Software Inventory
```http
GET /o1/v1/nodes/{nodeId}/software/inventory
```

**Response**
```json
{
  "nodeId": "gNB-001",
  "software": [
    {
      "component": "O-DU",
      "version": "2.1.0",
      "vendor": "VendorA",
      "installedDate": "2024-01-01T00:00:00Z"
    },
    {
      "component": "O-CU",
      "version": "1.5.2",
      "vendor": "VendorA",
      "installedDate": "2024-01-01T00:00:00Z"
    }
  ]
}
```

#### Software Upgrade
```http
POST /o1/v1/nodes/{nodeId}/software/upgrade
{
  "component": "O-DU",
  "targetVersion": "2.2.0",
  "packageUrl": "https://repo/sw/o-du-2.2.0.tar.gz",
  "scheduleTime": "2024-01-20T02:00:00Z",
  "upgradeType": "IN_PLACE"
}
```

#### Upgrade Types
- **IN_PLACE**: Upgrade in running system
- **ROLLING**: Upgrade with traffic migration
- **COLD**: Stop system, upgrade, restart

### 5. File Management

#### File Operations
- Upload configuration files
- Download logs
- Backup/restore
- Certificate management

**Upload File**
```http
POST /o1/v1/nodes/{nodeId}/files
Content-Type: multipart/form-data

file: configuration.yaml
path: /opt/config/
```

**Download File**
```http
GET /o1/v1/nodes/{nodeId}/files?path=/var/log/o-du.log
```

### 6. Security Management

#### Certificate Management

**Upload Certificate**
```http
POST /o1/v1/nodes/{nodeId}/security/certificates
{
  "type": "CLIENT",
  "certificate": "-----BEGIN CERTIFICATE-----...",
  "privateKey": "-----BEGIN PRIVATE KEY-----...",
  "caChain": "-----BEGIN CERTIFICATE-----..."
}
```

**List Certificates**
```http
GET /o1/v1/nodes/{nodeId}/security/certificates
```

#### Access Control

**Role Management**
```http
POST /o1/v1/users
{
  "username": "admin",
  "roles": ["OPERATOR", "CONFIGURATION_MANAGER"],
  "permissions": [
    "config:read",
    "config:write",
    "alarm:read"
  ]
}
```

## O1 and E2 Coordination

### Configuration Flow
1. SMO sends config via O1 to O-DU
2. O-DU applies configuration
3. O-DU notifies Near-RT RIC via E2 RIC Service Update
4. xApps adapt to new configuration

### Fault Flow
1. O-DU detects fault
2. O-DU sends alarm via O1 to SMO
3. SMO triggers remediation workflow
4. SMO may adjust policies via A1

### Performance Flow
1. SMO collects PM data via O1
2. SMO analyzes trends
3. SMO creates policies via A1
4. Near-RT RIC enforces policies

## Integration with SMO

### Service Orchestration
1. SMO creates network slice
2. SMO configures RAN components via O1
3. SMO provisions policies via A1
4. Near-RT RIC enforces via E2

### Closed-Loop Automation
1. SMO monitors KPIs via O1
2. AI/ML in SMO analyzes trends
3. SMO updates policies via A1
4. xApps execute via E2

### Lifecycle Management
1. Software upgrade scheduled via O1
2. Configuration backed up via O1
3. Nodes put in maintenance mode via O1
4. Traffic migrated via E2 control

## Performance Requirements

### Latency
- Configuration GET: < 100ms
- Configuration SET: < 500ms
- Alarm delivery: < 1s
- PM data retrieval: < 2s

### Throughput
- Support for 100+ concurrent operations
- 1000+ alarms/second
- 10,000+ PM counters/second

### Reliability
- 99.99% availability
- Automatic reconnection
- Message persistence for alarms

## Security Considerations

### Authentication
- Mutual TLS required for all connections
- Certificate rotation supported
- Session-based authentication

### Authorization
- RBAC (Role-Based Access Control)
- Fine-grained permissions
- Audit logging for all operations

### Integrity
- Message signing
- Configuration checksums
- Secure file transfer

### Confidentiality
- TLS encryption for data in transit
- Encrypted storage for sensitive data
- Key management integration

## Use Cases

### Use Case 1: Emergency Software Patch
1. Security vulnerability identified
2. SMO schedules patch deployment
3. O1 uploads patch to nodes
4. O1 coordinates rolling upgrade
5. E2 manages traffic migration
6. O1 verifies successful upgrade

### Use Case 2: Dynamic Capacity Expansion
1. Traffic spike detected via PM
2. SMO triggers capacity expansion
3. O1 configures new cell/carrier
4. E2 coordinates neighbor relations
5. O1 monitors performance

### Use Case 3: Self-Healing
1. Critical alarm received via O1
2. SMO triggers self-healing workflow
3. O1 collects diagnostics
4. SMO analyzes and identifies fix
5. O1 applies configuration change
6. E2 verifies improvement
