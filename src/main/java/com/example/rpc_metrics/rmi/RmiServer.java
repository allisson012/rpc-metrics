package com.example.rpc_metrics.rmi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Configuração do servidor RMI
 *
 * Equivalente ao @GrpcService no gRPC
 *
 * DIFERENÇA:
 * Aqui tudo é manual (porta, registro, nome do serviço)
 */
@Configuration
public class RmiServer {

    @Bean
    public Registry rmiRegistry(MetricsServiceImpl metricsService) throws Exception {

        // Cria o registry na porta padrão do RMI (1099)
        Registry registry = LocateRegistry.createRegistry(1099);

        // Registra o serviço com um nome
        registry.rebind("MetricsService", metricsService);

        System.out.println("[RMI] Servidor rodando na porta 1099");
        System.out.println("[RMI] Serviço registrado como 'MetricsService'");

        return registry;
    }
}