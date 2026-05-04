package com.example.rpc_metrics.rmi;

import java.io.Serializable;

/**
 * Classe que representa os dados trafegados no RMI
 *
 * Equivalente ao MetricsResponse do gRPC
 *
 * 
 * No RMI usamos serialização Java nativa (Serializable),
 * diferente do gRPC que usa Protobuf (mais leve e eficiente)
 */
public class MetricsData implements Serializable {

    private static final long serialVersionUID = 1L;

    private double cpuUsage;
    private double memoryUsage;
    private double diskUsage;
    private long   timestamp;
    private String serverId;

    public MetricsData() {}

    public MetricsData(double cpuUsage, double memoryUsage,
                       double diskUsage, long timestamp, String serverId) {
        this.cpuUsage    = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.diskUsage   = diskUsage;
        this.timestamp   = timestamp;
        this.serverId    = serverId;
    }

    public double getCpuUsage()    { return cpuUsage; }
    public double getMemoryUsage() { return memoryUsage; }
    public double getDiskUsage()   { return diskUsage; }
    public long   getTimestamp()   { return timestamp; }
    public String getServerId()    { return serverId; }
}