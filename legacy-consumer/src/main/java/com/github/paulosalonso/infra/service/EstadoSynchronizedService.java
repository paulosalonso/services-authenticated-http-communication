package com.github.paulosalonso.infra.service;

import com.github.paulosalonso.client.LegacyApiClient;
import com.github.paulosalonso.core.Properties;
import com.github.paulosalonso.domain.model.Estado;
import com.github.paulosalonso.domain.repository.EstadoRepository;
import com.github.paulosalonso.domain.service.EstadoService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Profile("sync")
@Service
public class EstadoSynchronizedService implements EstadoService {

    private EstadoRepository repository;
    private LegacyApiClient client;
    private Properties properties;

    public EstadoSynchronizedService(EstadoRepository repository, LegacyApiClient client, Properties properties) {
        this.repository = repository;
        this.client = client;
        this.properties = properties;
    }

    @Override
    public List<Estado> listar() {
        List<Estado> estadosLocal = repository.findAll();

        if (!properties.legacyApi.autoSync) {
            List<Estado> estadosSincronizados = sincronizar(estadosLocal);
            estadosLocal.addAll(estadosSincronizados);
        }

        return estadosLocal;
    }

    @Override
    public Optional<Estado> buscar(Long id) {
        if (properties.legacyApi.autoSync) {
            return repository.findById(id);
        }

        return listar().stream()
                .filter(estado -> estado.getId().equals(id))
                .findAny();
    }

    protected List<Estado> sincronizar(List<Estado> estadosLocal) {
        List<Estado> estadosLegacy = client.getEstados();
        List<Long> idsLocal = estadosLocal.stream().map(Estado::getId).collect(toList());

        return estadosLegacy.stream()
                .filter(estado -> !idsLocal.contains(estado.getId()))
                .map(repository::save)
                .collect(toList());
    }
}
