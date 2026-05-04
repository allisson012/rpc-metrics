package com.example.rpc_metrics.rmi;

import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 * Implementação do serviço RMI
 *
 * Equivalente ao MetricsServiceImpl do gRPC
 */
@Component
public class MetricsServiceImpl extends UnicastRemoteObject implements MetricsService {

    private final Random random = new Random();

    private double cpuBase  = 45.0;
    private double memBase  = 60.0;

    public MetricsServiceImpl() throws RemoteException {
        super();
    }

    /**
     * Método chamado remotamente pelo cliente
     */
    @Override
    public MetricsData getMetrics() throws RemoteException {

        System.out.println("[RMI] Chamada recebida — executa e encerra");

        boolean spike = random.nextDouble() < 0.08;

        cpuBase = spike
                ? 85 + random.nextDouble() * 14
                : Math.min(99, Math.max(5, cpuBase + (random.nextDouble() * 20 - 10)));

        memBase = Math.min(99, Math.max(20, memBase + (random.nextDouble() * 10 - 5)));


        double disk = 30 + random.nextDouble() * 25;

        if (spike) {
            System.out.println("[RMI] *** PICO DE CPU: "
                    + String.format("%.1f", cpuBase) + "% ***");
        }

        // Retorna os dados (objeto será serializado e enviado pela rede)
        return new MetricsData(
                Math.round(cpuBase * 10.0) / 10.0,
                Math.round(memBase * 10.0) / 10.0,
                Math.round(disk    * 10.0) / 10.0,
                System.currentTimeMillis(),
                "rpc-server-01"
        );
    }
}