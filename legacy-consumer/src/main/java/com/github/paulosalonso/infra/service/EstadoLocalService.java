package com.github.paulosalonso.infra.service;

import com.github.paulosalonso.domain.model.Estado;
import com.github.paulosalonso.domain.repository.EstadoRepository;
import com.github.paulosalonso.domain.service.EstadoService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Profile({ "default", "local" })
@Service
public class EstadoLocalService implements EstadoService {

    private EstadoRepository repository;

    public EstadoLocalService(EstadoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Estado> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<Estado> buscar(Long id) {
        return repository.findById(id);
    }
}
