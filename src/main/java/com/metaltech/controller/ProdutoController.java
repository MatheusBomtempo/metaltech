package com.metaltech.controller;

import com.metaltech.model.Produto;
import com.metaltech.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService service;

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        return ResponseEntity.ok(service.salvar(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        return ResponseEntity.ok(service.atualizar(produto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    @GetMapping("/baixo-estoque")
    public ResponseEntity<List<Produto>> buscarProdutosBaixoEstoque() {
        return ResponseEntity.ok(service.buscarProdutosBaixoEstoque());
    }

    @PutMapping("/{id}/estoque")
    public ResponseEntity<Void> atualizarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        service.atualizarEstoque(id, quantidade);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 