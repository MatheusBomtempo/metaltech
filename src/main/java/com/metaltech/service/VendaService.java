package com.metaltech.service;

import com.metaltech.model.Venda;
import com.metaltech.model.ItemVenda;
import com.metaltech.model.Produto;
import com.metaltech.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class VendaService {
    
    private static final Logger logger = LoggerFactory.getLogger(VendaService.class);
    
    @Autowired
    private VendaRepository repository;

    @Autowired
    private ProdutoService produtoService;

    @Transactional
    public Venda salvar(Venda venda) {
        try {
            logger.info("Iniciando salvamento da venda: {}", venda);
            
            // Validar se o funcionário existe e é um representante de vendas
            if (venda.getFuncionario() == null) {
                logger.error("Funcionário é obrigatório para a venda");
                throw new IllegalArgumentException("Funcionário é obrigatório para a venda");
            }

            // Validar se há itens na venda
            if (venda.getItens() == null || venda.getItens().isEmpty()) {
                logger.error("A venda deve conter pelo menos um item");
                throw new IllegalArgumentException("A venda deve conter pelo menos um item");
            }

            // Validar data da venda
            if (venda.getDataVenda() == null) {
                venda.setDataVenda(LocalDateTime.now());
            } else if (venda.getDataVenda().isAfter(LocalDateTime.now())) {
                logger.error("A data da venda não pode ser futura");
                throw new IllegalArgumentException("A data da venda não pode ser futura");
            }

            // Validar estoque e atualizar preços unitários
            for (ItemVenda item : venda.getItens()) {
                logger.info("Processando item da venda: {}", item);
                Produto produto = produtoService.buscarPorId(item.getProduto().getId());
                item.setProduto(produto);
                item.setPrecoUnitario(produto.getPreco());
                item.setVenda(venda);
            }

            Venda vendaSalva = repository.save(venda);
            logger.info("Venda salva com sucesso: {}", vendaSalva);
            return vendaSalva;
        } catch (Exception e) {
            logger.error("Erro ao salvar venda: ", e);
            throw e;
        }
    }

    public Venda buscarPorId(Long id) {
        try {
            logger.info("Buscando venda por ID: {}", id);
            Venda venda = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));
            logger.info("Venda encontrada: {}", venda);
            return venda;
        } catch (Exception e) {
            logger.error("Erro ao buscar venda por ID: ", e);
            throw e;
        }
    }

    public List<Venda> listarTodas() {
        try {
            logger.info("Listando todas as vendas");
            List<Venda> vendas = repository.findAll();
            logger.info("Total de vendas encontradas: {}", vendas.size());
            return vendas;
        } catch (Exception e) {
            logger.error("Erro ao listar vendas: ", e);
            throw e;
        }
    }

    public List<Venda> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        try {
            logger.info("Buscando vendas por período: {} até {}", inicio, fim);
            List<Venda> vendas = repository.findByDataVendaBetween(inicio, fim);
            logger.info("Total de vendas encontradas no período: {}", vendas.size());
            return vendas;
        } catch (Exception e) {
            logger.error("Erro ao buscar vendas por período: ", e);
            throw e;
        }
    }

    public List<Venda> buscarPorCliente(Long clienteId) {
        try {
            logger.info("Buscando vendas por cliente ID: {}", clienteId);
            List<Venda> vendas = repository.findByClienteId(clienteId);
            logger.info("Total de vendas encontradas para o cliente: {}", vendas.size());
            return vendas;
        } catch (Exception e) {
            logger.error("Erro ao buscar vendas por cliente: ", e);
            throw e;
        }
    }

    public List<Venda> buscarPorFuncionario(Long funcionarioId) {
        try {
            logger.info("Buscando vendas por funcionário ID: {}", funcionarioId);
            List<Venda> vendas = repository.findByFuncionarioId(funcionarioId);
            logger.info("Total de vendas encontradas para o funcionário: {}", vendas.size());
            return vendas;
        } catch (Exception e) {
            logger.error("Erro ao buscar vendas por funcionário: ", e);
            throw e;
        }
    }

    @Transactional
    public void deletar(Long id) {
        try {
            logger.info("Iniciando exclusão da venda ID: {}", id);
            if (!repository.existsById(id)) {
                logger.error("Venda não encontrada para exclusão: {}", id);
                throw new IllegalArgumentException("Venda não encontrada");
            }
            repository.deleteById(id);
            logger.info("Venda excluída com sucesso: {}", id);
        } catch (Exception e) {
            logger.error("Erro ao excluir venda: ", e);
            throw e;
        }
    }
} 