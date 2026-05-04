package com.example.rpc_metrics.client;

import com.example.rpc_metrics.rmi.MetricsData;
import com.example.rpc_metrics.rmi.MetricsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Cliente RMI
 *
 * Equivalente ao cliente gRPC
 *
 * PRINCIPAL DIFERENÇA:
 * Não existe streaming → usamos polling (loop chamando o servidor)
 */
@Component
public class RmiClient {

    // Stub remoto (representa o servidor)
    private MetricsService stub;

    // Última métrica recebida
    private volatile MetricsData latestMetrics;

    // Contador de chamadas (polling)
    private volatile int pollCount = 0;

    private volatile long lastLatencyMs = 0;
    private long totalLatency = 0;

    @PostConstruct
    public void init() {

        // Thread para tentar conectar até o servidor estar disponível
        Thread connectThread = new Thread(() -> {

            while (true) {
                try {
                    System.out.println("[RMI CLIENT] Tentando conectar...");

                    // Conecta no registry
                    Registry registry = LocateRegistry.getRegistry("localhost", 1099);

                    // Busca o serviço pelo nome
                    stub = (MetricsService) registry.lookup("MetricsService");

                    System.out.println("[RMI CLIENT] Conectado!");
                    
                    // Inicia o polling
                    startPolling();
                    break;

                } catch (Exception e) {
                    System.out.println("[RMI CLIENT] Servidor não disponível, tentando novamente...");
                    try { Thread.sleep(1000); } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });

        connectThread.setDaemon(true);
        connectThread.start();
    }

    /**
     * Simula "tempo real" usando polling
     *
     * 
     * Cada chamada = nova conexão TCP
     */
    private void startPolling() {

        Thread pollingThread = new Thread(() -> {

            System.out.println("[RMI CLIENT] Iniciando polling...");

            while (!Thread.currentThread().isInterrupted()) {
                try {

                    long start = System.currentTimeMillis();

                    // Chamada remota (abre conexão, executa, fecha)
                    MetricsData data = stub.getMetrics();

                    lastLatencyMs = System.currentTimeMillis() - start;
                    totalLatency += lastLatencyMs;

                    pollCount++;
                    latestMetrics = data;

                    System.out.println("[RMI CLIENT] Poll #" + pollCount
                            + " — CPU: " + String.format("%.1f", data.getCpuUsage()) + "%"
                            + " — latência: " + lastLatencyMs + "ms");

                    Thread.sleep(2000);

                } catch (Exception e) {
                    System.err.println("[RMI CLIENT] Erro: " + e.getMessage());
                }
            }
        });

        pollingThread.setDaemon(true);
        pollingThread.start();
    }

    public MetricsData getLatestMetrics() { return latestMetrics; }
    public int getPollCount()             { return pollCount; }
    public long getLastLatencyMs()        { return lastLatencyMs; }
    public long getAvgLatencyMs() {
    return pollCount > 0 ? totalLatency / pollCount : 0;
    }
}