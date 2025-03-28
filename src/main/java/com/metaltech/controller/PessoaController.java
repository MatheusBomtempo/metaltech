package com.metaltech.controller;

import com.metaltech.model.Pessoa;
import com.metaltech.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pessoas")
public abstract class PessoaController<T extends Pessoa, S extends PessoaService<T, ?>> {
    
    @Autowired
    protected S service;

    protected abstract S getService();

    @PostMapping
    public ResponseEntity<T> criar(@RequestBody T pessoa) {
        return ResponseEntity.ok(getService().salvar(pessoa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(getService().buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        getService().deletar(id);
        return ResponseEntity.noContent().build();
    }
} 