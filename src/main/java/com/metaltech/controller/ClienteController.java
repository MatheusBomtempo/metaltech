package com.metaltech.controller;

import com.metaltech.model.Cliente;
import com.metaltech.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends PessoaController<Cliente, ClienteService> {
    
    @Autowired
    private ClienteService clienteService;

    @Override
    protected ClienteService getService() {
        return clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        cliente.setId(id);
        return ResponseEntity.ok(clienteService.atualizar(cliente));
    }
} 