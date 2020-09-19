package com.github.paulosalonso.domain.service;

import com.github.paulosalonso.domain.model.Estado;

import java.util.List;
import java.util.Optional;

public interface EstadoService {

    List<Estado> listar();
    Optional<Estado> buscar(Long id);
}
