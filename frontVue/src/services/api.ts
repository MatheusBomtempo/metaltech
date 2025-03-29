import axios from 'axios';
import type { Produto, Cliente, Funcionario, Fornecedor, Venda, ItemVenda } from '../types';

const api = axios.create({
  baseURL: 'http://localhost:8081/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Interceptor para tratamento de erros
api.interceptors.response.use(
  response => response,
  error => {
    console.error('Erro na requisição:', {
      url: error.config?.url,
      method: error.config?.method,
      status: error.response?.status,
      data: error.response?.data,
      message: error.message,
      stack: error.stack
    });
    return Promise.reject(error);
  }
);

// Serviço de Produtos
export const ProdutoService = {
  listar: () => api.get<Produto[]>('/produtos'),
  buscarPorId: (id: number) => api.get<Produto>(`/produtos/${id}`),
  criar: (produto: Produto) => api.post<Produto>('/produtos', produto),
  atualizar: (id: number, produto: Produto) => api.put<Produto>(`/produtos/${id}`, produto),
  excluir: (id: number) => api.delete(`/produtos/${id}`)
};

// Serviço de Clientes
export const ClienteService = {
  listar: () => api.get<Cliente[]>('/clientes'),
  buscarPorId: (id: number) => api.get<Cliente>(`/clientes/${id}`),
  criar: (cliente: Cliente) => api.post<Cliente>('/clientes', cliente),
  atualizar: (id: number, cliente: Cliente) => api.put<Cliente>(`/clientes/${id}`, cliente),
  excluir: (id: number) => api.delete(`/clientes/${id}`)
};

// Serviço de Funcionários
export const FuncionarioService = {
  listar: () => api.get<Funcionario[]>('/funcionarios'),
  buscarPorId: (id: number) => api.get<Funcionario>(`/funcionarios/${id}`),
  async criar(funcionario: Funcionario) {
    try {
      console.log('Enviando dados do funcionário:', funcionario);
      const response = await api.post('/funcionarios', {
        ...funcionario,
        salario: Number(funcionario.salario),
        dataContratacao: funcionario.dataContratacao || new Date().toISOString().split('T')[0]
      });
      return response;
    } catch (error) {
      console.error('Erro ao criar funcionário:', error);
      throw error;
    }
  },
  async atualizar(id: number, funcionario: Funcionario) {
    try {
      console.log('Enviando dados do funcionário para atualização:', funcionario);
      const response = await api.put(`/funcionarios/${id}`, {
        ...funcionario,
        salario: Number(funcionario.salario),
        dataContratacao: funcionario.dataContratacao || new Date().toISOString().split('T')[0]
      });
      return response;
    } catch (error) {
      console.error('Erro ao atualizar funcionário:', error);
      throw error;
    }
  },
  excluir: (id: number) => api.delete(`/funcionarios/${id}`)
};

// Serviço de Fornecedores
export const FornecedorService = {
  listar: () => api.get<Fornecedor[]>('/fornecedores'),
  buscarPorId: (id: number) => api.get<Fornecedor>(`/fornecedores/${id}`),
  criar: (fornecedor: Fornecedor) => api.post<Fornecedor>('/fornecedores', fornecedor),
  atualizar: (id: number, fornecedor: Fornecedor) => api.put<Fornecedor>(`/fornecedores/${id}`, fornecedor),
  excluir: (id: number) => api.delete(`/fornecedores/${id}`)
};

// Serviço de Vendas
export const VendaService = {
  listar: async () => {
    try {
      const response = await api.get('/vendas');
      return response.data;
    } catch (error) {
      console.error('Erro ao listar vendas:', error);
      throw error;
    }
  },
  buscarPorId: async (id: number) => {
    try {
      const response = await api.get(`/vendas/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Erro ao buscar venda ${id}:`, error);
      throw error;
    }
  },
  criar: async (venda: Venda) => {
    try {
      console.log('Enviando venda para criação:', venda);
      const response = await api.post('/vendas', venda);
      console.log('Resposta da criação:', response.data);
      return response.data;
    } catch (error) {
      console.error('Erro ao criar venda:', error);
      throw error;
    }
  },
  atualizar: async (id: number, venda: Venda) => {
    try {
      console.log('Enviando venda para atualização:', venda);
      const response = await api.put(`/vendas/${id}`, venda);
      console.log('Resposta da atualização:', response.data);
      return response.data;
    } catch (error) {
      console.error(`Erro ao atualizar venda ${id}:`, error);
      throw error;
    }
  },
  excluir: async (id: number) => {
    try {
      await api.delete(`/vendas/${id}`);
    } catch (error) {
      console.error(`Erro ao excluir venda ${id}:`, error);
      throw error;
    }
  }
}; 