package com.metaltech.controller;

import com.metaltech.model.Funcionario;
import com.metaltech.model.Cargo;
import com.metaltech.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController extends PessoaController<Funcionario, FuncionarioService> {
    
    @Autowired
    private FuncionarioService funcionarioService;

    @Override
    protected FuncionarioService getService() {
        return funcionarioService;
    }

    @GetMapping("/cargo/{cargo}")
    public ResponseEntity<List<Funcionario>> buscarPorCargo(@PathVariable Cargo cargo) {
        return ResponseEntity.ok(funcionarioService.buscarPorCargo(cargo));
    }
} 