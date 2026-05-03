package com.openran.copilot.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * RAN Intelligent Controller metadata.
 * Represents both Near-RT and Non-RT RIC instances.
 */
public class RICInfo {
    private String id;
    private String name;
    private String type; // "near-rt" or "non-rt"
    private String address;
    private int port;
    private String version;
    private LocalDateTime lastConnected;
    private Map<String, String> capabilities = new HashMap<>();
    private Map<String, String> supportedServiceModels = new HashMap<>();
    private boolean healthy;

    public RICInfo() {
    }

    public RICInfo(String name, String type, String address, int port) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.type = type;
        this.address = address;
        this.port = port;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(LocalDateTime lastConnected) {
        this.lastConnected = lastConnected;
    }

    public Map<String, String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, String> capabilities) {
        this.capabilities = capabilities;
    }

    public void addCapability(String key, String value) {
        this.capabilities.put(key, value);
    }

    public Map<String, String> getSupportedServiceModels() {
        return supportedServiceModels;
    }

    public void setSupportedServiceModels(Map<String, String> supportedServiceModels) {
        this.supportedServiceModels = supportedServiceModels;
    }

    public void addSupportedServiceModel(String model, String version) {
        this.supportedServiceModels.put(model, version);
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isNearRT() {
        return "near-rt".equals(type);
    }

    public boolean isNonRT() {
        return "non-rt".equals(type);
    }

    @Override
    public String toString() {
        return "RICInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", version='" + version + '\'' +
                ", healthy=" + healthy +
                '}';
    }
}
