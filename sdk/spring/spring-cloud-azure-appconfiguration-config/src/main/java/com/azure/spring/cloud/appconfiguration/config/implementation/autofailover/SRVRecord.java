package com.azure.spring.cloud.appconfiguration.config.implementation.autofailover;

public class SRVRecord {

    private final int priority;

    private final int weight;

    private final int port;

    private final String target;
    
    public SRVRecord(String[] record) {
        this.priority = Integer.valueOf(record[0]);
        this.weight = Integer.valueOf(record[1]);
        this.port = Integer.valueOf(record[2]);
        this.target = record[3].substring(0, record[3].length() - 1);
    }

    public int getPriority() {
        return priority;
    }

    public int getWeight() {
        return weight;
    }

    public int getPort() {
        return port;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return String.format("Priority: %s, Weight %s, Port %s, Target %s", priority, weight, port, target);
    }
}
