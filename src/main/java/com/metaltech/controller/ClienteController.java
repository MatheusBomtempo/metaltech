package com.metaltech.controller;

import com.metaltech.model.Cliente;
import com.metaltech.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends PessoaController<Cliente, ClienteService> {
    
    @Autowired
    private ClienteService clienteService;

    @Override
    protected ClienteService getService() {
        return clienteService;
    }
} 