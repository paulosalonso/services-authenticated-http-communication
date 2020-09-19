package com.github.paulosalonso.api.controller;

import com.github.paulosalonso.domain.model.Estado;
import com.github.paulosalonso.domain.repository.EstadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    private EstadoRepository repository;

    public EstadoController(EstadoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Estado> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Estado buscar(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado salvar(@RequestBody Estado estado) {
        return repository.save(estado);
    }

    @PutMapping("/{id}")
    public Estado atualizar(@PathVariable Long id, @RequestBody Estado estado) {
        Estado estadoBanco = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        estadoBanco.setName(estado.getName());

        return repository.save(estadoBanco);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        repository.deleteById(id);
    }

}
