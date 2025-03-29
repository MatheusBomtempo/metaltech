package com.metaltech.controller;

import com.metaltech.model.Venda;
import com.metaltech.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    
    private static final Logger logger = LoggerFactory.getLogger(VendaController.class);
    
    @Autowired
    private VendaService service;

    @PostMapping
    public ResponseEntity<Venda> criar(@RequestBody Venda venda) {
        logger.info("Recebendo requisição para criar venda: {}", venda);
        Venda vendaSalva = service.salvar(venda);
        return ResponseEntity.ok(vendaSalva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        logger.info("Recebendo requisição para buscar venda ID: {}", id);
        Venda venda = service.buscarPorId(id);
        return ResponseEntity.ok(venda);
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        logger.info("Recebendo requisição para listar todas as vendas");
        List<Venda> vendas = service.listarTodas();
        return ResponseEntity.ok(vendas);
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

    @PutMapping("/{id}")
    public ResponseEntity<Venda> atualizar(@PathVariable Long id, @RequestBody Venda venda) {
        logger.info("Recebendo requisição para atualizar venda ID: {} com dados: {}", id, venda);
        try {
            // Verificar se a venda existe
            Venda vendaExistente = service.buscarPorId(id);
            if (vendaExistente == null) {
                logger.error("Venda não encontrada para atualização: {}", id);
                return ResponseEntity.notFound().build();
            }
            
            // Atualizar os dados
            venda.setId(id);
            Venda vendaAtualizada = service.salvar(venda);
            logger.info("Venda atualizada com sucesso: {}", vendaAtualizada);
            return ResponseEntity.ok(vendaAtualizada);
        } catch (Exception e) {
            logger.error("Erro ao atualizar venda: ", e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        logger.info("Recebendo requisição para deletar venda ID: {}", id);
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 