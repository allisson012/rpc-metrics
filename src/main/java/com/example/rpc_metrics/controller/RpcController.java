package com.example.rpc_metrics.controller;

import com.example.rpc_metrics.client.RmiClient;
import com.example.rpc_metrics.rmi.MetricsData;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RpcController {

    private final RmiClient rmiClient;

    public RpcController(RmiClient rmiClient) {
        this.rmiClient = rmiClient;
    }

    @GetMapping("/rpc/metrics")
    public Map<String, Object> getRpcMetrics() {
        MetricsData data = rmiClient.getLatestMetrics();

        Map<String, Object> response = new HashMap<>();

        if (data == null) {
            response.put("status", "aguardando primeiro poll...");
            return response;
        }

        response.put("cpuUsage",        data.getCpuUsage());
        response.put("memoryUsage",     data.getMemoryUsage());
        response.put("diskUsage",       data.getDiskUsage());
        response.put("timestamp",       data.getTimestamp());
        response.put("serverId",        data.getServerId());
        response.put("rpcCallCount",    rmiClient.getPollCount());
        response.put("latencyMs",       rmiClient.getLastLatencyMs());
        response.put("avgLatencyMs",    rmiClient.getAvgLatencyMs());
        response.put("connectionType",  "nova conexão TCP por chamada (RMI)");

        return response;
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("rpcTotalCalls", rmiClient.getPollCount());
        status.put("rpcAvgLatency", rmiClient.getAvgLatencyMs());
        status.put("rmiPort",       1099);
        status.put("restPort",      8081);
        return status;
    }
}
