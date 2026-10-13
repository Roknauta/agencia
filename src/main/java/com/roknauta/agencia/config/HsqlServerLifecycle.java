package com.roknauta.agencia.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.hsqldb.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HsqlServerLifecycle {

    private final boolean enabled;
    private final int port;
    private final Path databasePath;
    private final String databaseName;
    private Server server;

    public HsqlServerLifecycle(@Value("${agencia.hsql.enabled:true}") boolean enabled,
        @Value("${agencia.hsql.port:9061}") int port, @Value("${agencia.hsql.database-path}") String databasePath,
        @Value("${agencia.hsql.database-name:agencia}") String databaseName) {
        this.enabled = enabled;
        this.port = port;
        this.databasePath = Path.of(databasePath).toAbsolutePath().normalize();
        this.databaseName = databaseName;
    }

    @PostConstruct
    public void start() {
        if (!enabled) {
            return;
        }

        try {
            Path parent = databasePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            server = new Server();
            server.setPort(port);
            server.setDatabaseName(0, databaseName);
            server.setDatabasePath(0, "file:" + databasePath);
            server.setSilent(true);
            server.setNoSystemExit(true);
            server.start();
            server.checkRunning(true);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível preparar o diretório do HSQLDB: " + databasePath, e);
        }
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class StartupOrderConfiguration {

        @Bean
        static BeanFactoryPostProcessor hsqlBeforeDataSource() {
            return (ConfigurableListableBeanFactory beanFactory) -> {
                if (beanFactory.containsBeanDefinition("dataSource")) {
                    var dataSource = beanFactory.getBeanDefinition("dataSource");
                    String[] dependencies = dataSource.getDependsOn();
                    if (dependencies == null) {
                        dataSource.setDependsOn("hsqlServerLifecycle");
                    } else if (!Arrays.asList(dependencies).contains("hsqlServerLifecycle")) {
                        String[] updated = Arrays.copyOf(dependencies, dependencies.length + 1);
                        updated[dependencies.length] = "hsqlServerLifecycle";
                        dataSource.setDependsOn(updated);
                    }
                }
            };
        }
    }
}
