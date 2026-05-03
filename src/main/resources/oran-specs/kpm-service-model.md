# KPM Service Model Specification

## Overview

KPM (Key Performance Measurement) is a service model defined by O-RAN for monitoring RAN Key Performance Indicators (KPIs) in real-time. It is the most commonly used service model for xApp development.

## Purpose

The KPM service model enables:
- Real-time KPI monitoring
- Performance trend analysis
- Threshold-based alerting
- Historical data collection

## RAN Function ID

- **RAN Function ID**: 1 (standardized by O-RAN)

## Subscription Format

### KPM Subscription Request

```json
{
  "ranFunctionId": 1,
  "subscriptionDetails": {
    "eventTrigger": {
      "conditionType": "PERIODIC",
      "reportIntervalMs": 1000
    },
    "filter": {
      "actionType": "REPORT",
      "cellId": [1, 2, 3],
      "ueId": [],
      "indicators": [
        "RRU.PrbUsedDl",
        "RRU.PrbUsedUl",
        "DRB.UEThpDl",
        "DRB.UEThpUl",
        "HO.ExecSuccess"
      ]
    }
  }
}
```

### Subscription Parameters

#### eventTrigger.conditionType
- **PERIODIC**: Regular interval reporting
- **EVENT_TRIGGERED**: Report when threshold crossed
- **ON_DEMAND**: One-time report

#### eventTrigger.reportIntervalMs
- Reporting interval in milliseconds
- Typical values: 100ms to 10000ms
- Shorter intervals = higher overhead

#### filter.cellId
- List of cell IDs to monitor
- Empty list = all cells

#### filter.ueId
- List of UE IDs to monitor
- Empty list = all UEs

#### filter.indicators
- List of KPI indicators to include
- See indicator definitions below

## Standard Indicators

### Radio Resource Utilization (RRU)

#### RRU.PrbUsedDl
- **Name**: Downlink PRB Usage
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of Physical Resource Blocks used in downlink
- **Formula**: (Used PRBs / Total PRBs) × 100
- **Typical Use**: Capacity monitoring, load balancing

#### RRU.PrbUsedUl
- **Name**: Uplink PRB Usage
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of Physical Resource Blocks used in uplink
- **Formula**: (Used PRBs / Total PRBs) × 100
- **Typical Use**: Uplink capacity planning

#### RRU.TotalPrbDl
- **Name**: Total Downlink PRBs
- **Type**: Integer
- **Unit**: Count
- **Description**: Total number of PRBs available in downlink

#### RRU.TotalPrbUl
- **Name**: Total Uplink PRBs
- **Type**: Integer
- **Unit**: Count
- **Description**: Total number of PRBs available in uplink

### Data Radio Bearer (DRB)

#### DRB.UEThpDl
- **Name**: Downlink UE Throughput
- **Type**: Float
- **Unit**: Mbps
- **Description**: Average downlink throughput per UE
- **Measurement**: Over reporting interval
- **Typical Use**: QoE monitoring

#### DRB.UEThpUl
- **Name**: Uplink UE Throughput
- **Type**: Float
- **Unit**: Mbps
- **Description**: Average uplink throughput per UE
- **Measurement**: Over reporting interval

#### DRB.PacketLossDl
- **Name**: Downlink Packet Loss Rate
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of packets lost in downlink

#### DRB.PacketLossUl
- **Name**: Uplink Packet Loss Rate
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of packets lost in uplink

### Handover (HO)

#### HO.ExecSuccess
- **Name**: Handover Execution Success Rate
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of handovers that completed successfully
- **Formula**: (Successful HOs / Total HOs attempted) × 100
- **Typical Use**: Mobility optimization

#### HO.ExecFailure
- **Name**: Handover Execution Failure Rate
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of handovers that failed
- **Formula**: (Failed HOs / Total HOs attempted) × 100

#### HO.PreparationSuccess
- **Name**: Handover Preparation Success Rate
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of handover preparations that succeeded

### RRC Connection

#### RRC.ConnEstablished
- **Name**: RRC Connected UEs
- **Type**: Integer
- **Unit**: Count
- **Description**: Number of UEs in RRC CONNECTED state

#### RRC.ConnSuccess
- **Name**: RRC Connection Success Rate
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of RRC connection attempts that succeeded

#### RRC.ConnFailure
- **Name**: RRC Connection Failure Rate
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of RRC connection attempts that failed

### Call Drops

#### CALL.DroppedRate
- **Name**: Call Drop Rate
- **Type**: Percentage (0-100)
- **Unit**: %
- **Description**: Percentage of calls that were dropped
- **Formula**: (Dropped Calls / Total Calls) × 100
- **Typical Use**: Quality monitoring

#### CALL.DropCount
- **Name**: Call Drop Count
- **Type**: Integer
- **Unit**: Count
- **Description**: Number of dropped calls in reporting interval

## Indication Format

### KPM Indication Message

```json
{
  "subscriptionId": 12345,
  "indicationType": "REPORT",
  "timestamp": 1699012345678,
  "cellMeasurements": [
    {
      "cellId": 1,
      "indicators": {
        "RRU.PrbUsedDl": 75.5,
        "RRU.PrbUsedUl": 45.2,
        "DRB.UEThpDl": 25.3,
        "DRB.UEThpUl": 8.1,
        "HO.ExecSuccess": 98.5
      }
    },
    {
      "cellId": 2,
      "indicators": {
        "RRU.PrbUsedDl": 82.1,
        "RRU.PrbUsedUl": 38.7,
        "DRB.UEThpDl": 22.8,
        "DRB.UEThpUl": 7.5,
        "HO.ExecSuccess": 97.2
      }
    }
  ]
}
```

## Threshold-Based Reporting

### Event Trigger Configuration

```json
{
  "eventTrigger": {
    "conditionType": "EVENT_TRIGGERED",
    "thresholds": [
      {
        "indicator": "RRU.PrbUsedDl",
        "operator": "GREATER_THAN",
        "value": 80.0,
        "hysteresis": 2.0
      },
      {
        "indicator": "HO.ExecSuccess",
        "operator": "LESS_THAN",
        "value": 95.0,
        "hysteresis": 1.0
      }
    ]
  }
}
```

### Threshold Operators
- **GREATER_THAN**: Report when value > threshold
- **LESS_THAN**: Report when value < threshold
- **GREATER_EQUAL**: Report when value >= threshold
- **LESS_EQUAL**: Report when value <= threshold
- **EQUAL**: Report when value == threshold

### Hysteresis
- Prevents rapid threshold crossing reports
- Value must cross threshold + hysteresis
- Typical: 1-5% of threshold value

## Use Case Examples

### Use Case 1: Load Balancing xApp

**Subscription**:
```json
{
  "indicators": ["RRU.PrbUsedDl", "RRU.PrbUsedUl"],
  "reportIntervalMs": 1000
}
```

**Logic**:
1. Monitor PRB usage across cells
2. Identify overloaded cells (>80%)
3. Identify underloaded cells (<50%)
4. Steer UEs to underloaded cells via handover parameter adjustment

### Use Case 2: Handover Optimization xApp

**Subscription**:
```json
{
  "indicators": ["HO.ExecSuccess", "HO.ExecFailure"],
  "reportIntervalMs": 1000
}
```

**Logic**:
1. Monitor handover success rate
2. If success rate < 95%, analyze failure causes
3. Adjust A3 offset and TTT parameters
4. Monitor improvement

### Use Case 3: QoE Monitoring xApp

**Subscription**:
```json
{
  "indicators": ["DRB.UEThpDl", "DRB.UEThpUl", "CALL.DroppedRate"],
  "reportIntervalMs": 5000
}
```

**Logic**:
1. Monitor UE throughput and drop rate
2. Identify cells with poor QoE
3. Generate alerts for investigation
4. Correlate with other KPIs for root cause analysis

## Performance Considerations

### Reporting Interval Selection
- **100-500ms**: Real-time control (e.g., fast handover)
- **1000-5000ms**: Standard monitoring
- **10000ms+**: Trend analysis, capacity planning

### Indicator Selection
- More indicators = higher message size
- More indicators = higher processing overhead
- Select only needed indicators

### Cell/UE Filtering
- Filter to relevant cells/UEs
- Reduces message size and processing
- Improves scalability

## Integration with xApps

### Java xApp Example

```java
// Subscribe to KPM
KPMSubscription subscription = new KPMSubscription();
subscription.addIndicator("RRU.PrbUsedDl");
subscription.addIndicator("HO.ExecSuccess");
subscription.setReportingIntervalMs(1000);

e2Client.subscribe(subscription, this::onKPMIndication);

// Handle KPM indication
private void onKPMIndication(Indication indication) {
    KPMIndication kpm = (KPMIndication) indication;
    for (CellMeasurement cm : kpm.getCellMeasurements()) {
        double prbUsage = cm.getIndicatorValue("RRU.PrbUsedDl");
        double hoSuccess = cm.getIndicatorValue("HO.ExecSuccess");

        if (prbUsage > 80.0) {
            log.warn("High PRB usage on cell {}: {}", cm.getCellId(), prbUsage);
            // Take action
        }
    }
}
```

## Limitations

- No historical data storage (use external time-series DB)
- No custom indicator definitions (must use standard indicators)
- Limited aggregation (per-cell, per-UE only)
- No statistical functions (use external analytics)
