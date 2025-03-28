package com.metaltech.controller;

import com.metaltech.model.Venda;
import com.metaltech.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    
    @Autowired
    private VendaService service;

    @PostMapping
    public ResponseEntity<Venda> criar(@RequestBody Venda venda) {
        return ResponseEntity.ok(service.salvar(venda));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Venda>> buscarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(service.buscarPorPeriodo(inicio, fim));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venda>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.buscarPorCliente(clienteId));
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<Venda>> buscarPorFuncionario(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.buscarPorFuncionario(funcionarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 