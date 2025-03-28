import axios from 'axios';
import type { Produto, Cliente, Funcionario, Fornecedor, Venda } from '../types';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

export const ProdutoService = {
  listar: () => api.get<Produto[]>('/produtos'),
  buscarPorId: (id: number) => api.get<Produto>(`/produtos/${id}`),
  criar: (produto: Produto) => api.post<Produto>('/produtos', produto),
  atualizar: (id: number, produto: Produto) => api.put<Produto>(`/produtos/${id}`, produto),
  excluir: (id: number) => api.delete(`/produtos/${id}`)
};

export const ClienteService = {
  listar: () => api.get<Cliente[]>('/clientes'),
  buscarPorId: (id: number) => api.get<Cliente>(`/clientes/${id}`),
  criar: (cliente: Cliente) => api.post<Cliente>('/clientes', cliente),
  atualizar: (id: number, cliente: Cliente) => api.put<Cliente>(`/clientes/${id}`, cliente),
  excluir: (id: number) => api.delete(`/clientes/${id}`)
};

export const FuncionarioService = {
  listar: () => api.get<Funcionario[]>('/funcionarios'),
  buscarPorId: (id: number) => api.get<Funcionario>(`/funcionarios/${id}`),
  criar: (funcionario: Funcionario) => api.post<Funcionario>('/funcionarios', funcionario),
  atualizar: (id: number, funcionario: Funcionario) => api.put<Funcionario>(`/funcionarios/${id}`, funcionario),
  excluir: (id: number) => api.delete(`/funcionarios/${id}`)
};

export const FornecedorService = {
  listar: () => api.get<Fornecedor[]>('/fornecedores'),
  buscarPorId: (id: number) => api.get<Fornecedor>(`/fornecedores/${id}`),
  criar: (fornecedor: Fornecedor) => api.post<Fornecedor>('/fornecedores', fornecedor),
  atualizar: (id: number, fornecedor: Fornecedor) => api.put<Fornecedor>(`/fornecedores/${id}`, fornecedor),
  excluir: (id: number) => api.delete(`/fornecedores/${id}`)
};

export const VendaService = {
  listar: () => api.get<Venda[]>('/vendas'),
  buscarPorId: (id: number) => api.get<Venda>(`/vendas/${id}`),
  criar: (venda: Venda) => api.post<Venda>('/vendas', venda),
  atualizar: (id: number, venda: Venda) => api.put<Venda>(`/vendas/${id}`, venda),
  excluir: (id: number) => api.delete(`/vendas/${id}`)
}; 