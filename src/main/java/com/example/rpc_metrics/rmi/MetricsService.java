package com.example.rpc_metrics.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface do serviço RMI (contrato)
 *
 * Equivalente ao arquivo .proto no gRPC
 *
 * 
 * - Só funciona em Java
 * - Não existe streaming
 * - Toda chamada é única (abre, executa e fecha)
 */
public interface MetricsService extends Remote {

    /**
     * Retorna uma métrica do servidor
     *
     * IMPORTANTE:
     *
     * Para simular "tempo real", o cliente precisa chamar isso várias vezes (polling)
     */
    MetricsData getMetrics() throws RemoteException;

}