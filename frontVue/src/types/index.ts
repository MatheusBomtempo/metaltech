export interface Produto {
  id?: number;
  codigo: string;
  nome: string;
  preco: number;
  quantidadeEstoque: number;
  categoria: string;
}

export interface Cliente {
  id?: number;
  nome: string;
  documentoFiscal: string;
  telefone: string;
  endereco: string;
  dataCadastro?: string;
}

export interface Funcionario {
  id?: number;
  nome: string;
  documentoFiscal: string;
  telefone: string;
  cargo: 'GERENTE' | 'REPRESENTANTE_VENDAS' | 'SOLDADOR' | 'OPERADOR_MAQUINA' | 'AUXILIAR_ADMINISTRATIVO';
  salario: number;
  dataContratacao: string;
  dataCadastro?: string;
}

export interface Fornecedor {
  id?: number;
  nomeEmpresa: string;
  cnpj: string;
  telefone: string;
  materiais: string[];
}

export interface ItemVenda {
  id?: number;
  produto: Produto;
  quantidade: number;
  precoUnitario: number;
  subtotal: number;
}

export interface Venda {
  id?: number;
  cliente: Cliente;
  funcionario: Funcionario;
  dataVenda: string;
  valorTotal: number;
  formaPagamento: 'DINHEIRO' | 'CARTAO_CREDITO' | 'CARTAO_DEBITO' | 'PIX';
  itens: ItemVenda[];
} 