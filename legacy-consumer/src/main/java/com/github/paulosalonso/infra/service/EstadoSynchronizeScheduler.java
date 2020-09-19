package com.github.paulosalonso.infra.service;

import com.github.paulosalonso.domain.model.Estado;
import com.github.paulosalonso.domain.repository.EstadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("sync")
@Component
@ConditionalOnProperty(name = "com.github.paulosalonso.legacy-api.auto-sync", havingValue = "true")
public class EstadoSynchronizeScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadoSynchronizeScheduler.class);

    private final EstadoRepository estadoRepository;
    private final EstadoSynchronizedService estadoService;

    public EstadoSynchronizeScheduler(EstadoRepository estadoRepository, EstadoSynchronizedService estadoService) {
        this.estadoRepository = estadoRepository;
        this.estadoService = estadoService;
    }

    @Async
    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void sincronizar() {
        LOGGER.info("Iniciando sincronização de estados");

        List<Estado> estadosSincronizados = estadoService.sincronizar(estadoRepository.findAll());

        if (estadosSincronizados.isEmpty()) {
            LOGGER.info("Não existem estados para sincronizar");
        } else {
            LOGGER.info("{} estado(s) sincronizado(s)", estadosSincronizados.size());
        }

    }
}
