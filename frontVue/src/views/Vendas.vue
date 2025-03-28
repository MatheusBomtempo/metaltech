<template>
  <div>
    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="3000">
      {{ snackbar.text }}
    </v-snackbar>

    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title>
            Vendas
            <v-spacer></v-spacer>
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Buscar"
              single-line
              hide-details
            ></v-text-field>
          </v-card-title>

          <v-data-table
            :headers="headers"
            :items="vendas"
            :search="search"
            class="elevation-1"
            :loading="loading"
            :items-per-page="10"
            :no-data-text="'Nenhuma venda encontrada'"
          >
            <template #top>
              <v-toolbar flat>
                <v-toolbar-title>Lista de Vendas</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <v-btn color="primary" prepend-icon="mdi-plus" @click="dialog = true">
                  Nova Venda
                </v-btn>
              </v-toolbar>
            </template>

            <template #[`item.cliente.nome`]="{ item }">
              {{ item.cliente?.nome || 'N/A' }}
            </template>

            <template #[`item.funcionario.nome`]="{ item }">
              {{ item.funcionario?.nome || 'N/A' }}
            </template>

            <template #[`item.valorTotal`]="{ item }">
              R$ {{ Number(item.valorTotal).toFixed(2) }}
            </template>

            <template #[`item.dataVenda`]="{ item }">
              {{ new Date(item.dataVenda).toLocaleString() }}
            </template>

            <template #[`item.actions`]="{ item }">
              <v-icon size="small" class="me-2" @click="editItem(item)">
                mdi-pencil
              </v-icon>
              <v-icon size="small" @click="deleteItem(item)">
                mdi-delete
              </v-icon>
            </template>
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>

    <!-- Dialog para adicionar/editar venda -->
    <v-dialog v-model="dialog" max-width="800px">
      <v-card>
        <v-card-title>
          <span class="text-h5">{{ formTitle }}</span>
        </v-card-title>

        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-select
                  v-model="editedItem.cliente"
                  :items="clientes"
                  item-title="nome"
                  item-value="id"
                  label="Cliente"
                  return-object
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-select
                  v-model="editedItem.funcionario"
                  :items="funcionarios"
                  item-title="nome"
                  item-value="id"
                  label="Funcionário"
                  return-object
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-select
                  v-model="editedItem.formaPagamento"
                  :items="formasPagamento"
                  label="Forma de Pagamento"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.dataVenda"
                  label="Data da Venda"
                  type="datetime-local"
                ></v-text-field>
              </v-col>
            </v-row>

            <!-- Lista de itens da venda -->
            <v-row>
              <v-col cols="12">
                <v-card>
                  <v-card-title>
                    Itens da Venda
                    <v-spacer></v-spacer>
                    <v-btn color="primary" prepend-icon="mdi-plus" @click="addItem">
                      Adicionar Item
                    </v-btn>
                  </v-card-title>

                  <v-data-table
                    :headers="itemHeaders"
                    :items="editedItem.itens"
                    class="elevation-1"
                  >
                    <template #[`item.actions`]="{ item }">
                      <v-icon size="small" @click="removeItem(item)">
                        mdi-delete
                      </v-icon>
                    </template>
                  </v-data-table>
                </v-card>
              </v-col>
            </v-row>

            <!-- Dialog para adicionar item -->
            <v-dialog v-model="itemDialog" max-width="500px">
              <v-card>
                <v-card-title>
                  <span class="text-h5">Adicionar Item</span>
                </v-card-title>

                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12">
                        <v-select
                          v-model="newItem.produto"
                          :items="produtos"
                          item-title="nome"
                          item-value="id"
                          label="Produto"
                          return-object
                          @update:model-value="updateItemPrice"
                        ></v-select>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model.number="newItem.quantidade"
                          label="Quantidade"
                          type="number"
                          @input="calculateSubtotal"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model.number="newItem.precoUnitario"
                          label="Preço Unitário"
                          type="number"
                          readonly
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model.number="newItem.subtotal"
                          label="Subtotal"
                          type="number"
                          readonly
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue-darken-1" variant="text" @click="itemDialog = false">
                    Cancelar
                  </v-btn>
                  <v-btn color="blue-darken-1" variant="text" @click="saveItem">
                    Adicionar
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue-darken-1" variant="text" @click="close">Cancelar</v-btn>
          <v-btn color="blue-darken-1" variant="text" @click="save">Salvar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { VendaService, ClienteService, FuncionarioService, ProdutoService } from '../services/api';
import type { Venda, ItemVenda, Cliente, Funcionario, Produto } from '../types';

type FormaPagamento = 'DINHEIRO' | 'CARTAO_CREDITO' | 'CARTAO_DEBITO' | 'PIX';

const search = ref('');
const dialog = ref(false);
const itemDialog = ref(false);
const vendas = ref<Venda[]>([]);
const clientes = ref<Cliente[]>([]);
const funcionarios = ref<Funcionario[]>([]);
const produtos = ref<Produto[]>([]);
const formasPagamento: FormaPagamento[] = ['DINHEIRO', 'CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX'];

// Snackbar para notificações
const snackbar = ref({
  show: false,
  text: '',
  color: 'success'
});

const editedIndex = ref(-1);
const editedItem = ref<Venda>({
  cliente: null,
  funcionario: null,
  dataVenda: '',
  valorTotal: 0,
  formaPagamento: 'DINHEIRO' as FormaPagamento,
  itens: []
});

const defaultItem = ref<Venda>({
  cliente: null,
  funcionario: null,
  dataVenda: '',
  valorTotal: 0,
  formaPagamento: 'DINHEIRO' as FormaPagamento,
  itens: []
});

const newItem = ref<ItemVenda>({
  produto: null,
  quantidade: 0,
  precoUnitario: 0,
  subtotal: 0
});

const headers = [
  { title: 'Cliente', key: 'cliente.nome' },
  { title: 'Funcionário', key: 'funcionario.nome' },
  { title: 'Data', key: 'dataVenda' },
  { title: 'Valor Total', key: 'valorTotal' },
  { title: 'Forma de Pagamento', key: 'formaPagamento' },
  { title: 'Ações', key: 'actions', sortable: false }
];

const itemHeaders = [
  { title: 'Produto', key: 'produto.nome' },
  { title: 'Quantidade', key: 'quantidade' },
  { title: 'Preço Unitário', key: 'precoUnitario' },
  { title: 'Subtotal', key: 'subtotal' },
  { title: 'Ações', key: 'actions', sortable: false }
];

const formTitle = computed(() => {
  return editedIndex.value === -1 ? 'Nova Venda' : 'Editar Venda';
});

function showSnackbar(text: string, color: string = 'success') {
  snackbar.value = {
    show: true,
    text,
    color
  };
}

// Adicionar estado de loading
const loading = ref(false);

async function fetchVendas() {
  loading.value = true;
  console.log('Iniciando busca de vendas...');
  try {
    const response = await VendaService.listar();
    console.log('Resposta completa da API:', response);
    console.log('Dados brutos recebidos:', response.data);
    
    if (!response.data) {
      console.error('Resposta da API não contém dados');
      showSnackbar('Erro: Dados não recebidos da API', 'error');
      vendas.value = [];
      return;
    }

    // Verificar se os dados estão no formato esperado
    if (typeof response.data === 'string') {
      console.error('Dados recebidos como string:', response.data);
      try {
        response.data = JSON.parse(response.data);
      } catch (e) {
        console.error('Erro ao fazer parse dos dados:', e);
        showSnackbar('Erro: Formato de dados inválido', 'error');
        vendas.value = [];
        return;
      }
    }

    const vendasProcessadas = Array.isArray(response.data) 
      ? response.data.map(venda => {
          console.log('Processando venda individual:', venda);
          return {
            ...venda,
            cliente: venda.cliente || null,
            funcionario: venda.funcionario || null,
            dataVenda: venda.dataVenda || '',
            valorTotal: Number(venda.valorTotal) || 0,
            formaPagamento: venda.formaPagamento || 'DINHEIRO',
            itens: Array.isArray(venda.itens) ? venda.itens : []
          };
        })
      : [];

    console.log('Vendas processadas para exibição:', vendasProcessadas);
    vendas.value = vendasProcessadas;

    if (vendasProcessadas.length === 0) {
      console.log('Nenhuma venda encontrada');
      showSnackbar('Nenhuma venda encontrada', 'info');
    }
  } catch (error: any) {
    console.error('Erro detalhado ao buscar vendas:', {
      mensagem: error.message,
      resposta: error.response?.data,
      status: error.response?.status,
      stack: error.stack
    });
    showSnackbar('Erro ao buscar vendas', 'error');
    vendas.value = [];
  } finally {
    loading.value = false;
  }
}

async function fetchClientes() {
  console.log('Iniciando busca de clientes...');
  try {
    const response = await ClienteService.listar();
    console.log('Clientes recebidos:', response.data);
    clientes.value = Array.isArray(response.data) 
      ? response.data.map(cliente => ({
          ...cliente,
          nome: cliente.nome || '',
          documentoFiscal: cliente.documentoFiscal || '',
          telefone: cliente.telefone || '',
          endereco: cliente.endereco || ''
        }))
      : [];
  } catch (error) {
    console.error('Erro ao buscar clientes:', error);
    showSnackbar('Erro ao buscar clientes', 'error');
    clientes.value = [];
  }
}

async function fetchFuncionarios() {
  console.log('Iniciando busca de funcionários...');
  try {
    const response = await FuncionarioService.listar();
    console.log('Funcionários recebidos:', response.data);
    funcionarios.value = Array.isArray(response.data) 
      ? response.data.map(funcionario => ({
          ...funcionario,
          nome: funcionario.nome || '',
          cargo: funcionario.cargo || 'AUXILIAR_ADMINISTRATIVO',
          telefone: funcionario.telefone || '',
          salario: Number(funcionario.salario) || 0,
          dataContratacao: funcionario.dataContratacao || new Date().toISOString()
        }))
      : [];
  } catch (error) {
    console.error('Erro ao buscar funcionários:', error);
    showSnackbar('Erro ao buscar funcionários', 'error');
    funcionarios.value = [];
  }
}

async function fetchProdutos() {
  console.log('Iniciando busca de produtos...');
  try {
    const response = await ProdutoService.listar();
    console.log('Produtos recebidos:', response.data);
    produtos.value = Array.isArray(response.data) 
      ? response.data.map(produto => ({
          ...produto,
          codigo: produto.codigo || '',
          nome: produto.nome || '',
          preco: Number(produto.preco) || 0,
          quantidadeEstoque: Number(produto.quantidadeEstoque) || 0,
          categoria: produto.categoria || ''
        }))
      : [];
  } catch (error) {
    console.error('Erro ao buscar produtos:', error);
    showSnackbar('Erro ao buscar produtos', 'error');
    produtos.value = [];
  }
}

function editItem(item: Venda) {
  console.log('Editando venda:', item);
  editedIndex.value = vendas.value.indexOf(item);
  editedItem.value = JSON.parse(JSON.stringify(item)); // Deep clone para evitar referências
  dialog.value = true;
}

async function deleteItem(item: Venda) {
  console.log('Iniciando exclusão da venda:', item);
  const index = vendas.value.indexOf(item);
  if (confirm('Tem certeza que deseja excluir esta venda?')) {
    try {
      if (item.id) {
        await VendaService.excluir(item.id);
        console.log('Venda excluída com sucesso');
        vendas.value.splice(index, 1);
        showSnackbar('Venda excluída com sucesso');
      }
    } catch (error) {
      console.error('Erro ao excluir venda:', error);
      showSnackbar('Erro ao excluir venda', 'error');
    }
  }
}

function close() {
  console.log('Fechando diálogo');
  dialog.value = false;
  editedItem.value = Object.assign({}, defaultItem.value);
  editedIndex.value = -1;
}

function addItem() {
  console.log('Adicionando novo item');
  itemDialog.value = true;
}

function removeItem(item: ItemVenda) {
  console.log('Removendo item:', item);
  const index = editedItem.value.itens.indexOf(item);
  editedItem.value.itens.splice(index, 1);
  calculateTotal();
}

function updateItemPrice() {
  console.log('Atualizando preço do item, produto selecionado:', newItem.value.produto);
  if (newItem.value.produto) {
    newItem.value.precoUnitario = newItem.value.produto.preco;
    calculateSubtotal();
  }
}

function calculateSubtotal() {
  newItem.value.subtotal = newItem.value.quantidade * newItem.value.precoUnitario;
  console.log('Subtotal calculado:', newItem.value.subtotal);
}

function calculateTotal() {
  editedItem.value.valorTotal = editedItem.value.itens.reduce(
    (total, item) => total + item.subtotal,
    0
  );
  console.log('Total calculado:', editedItem.value.valorTotal);
}

function saveItem() {
  console.log('Salvando item:', newItem.value);
  editedItem.value.itens.push({ ...newItem.value });
  calculateTotal();
  itemDialog.value = false;
  newItem.value = {
    produto: null,
    quantidade: 0,
    precoUnitario: 0,
    subtotal: 0
  };
}

async function save() {
  console.log('Salvando venda:', editedItem.value);
  try {
    if (editedIndex.value > -1) {
      if (editedItem.value.id) {
        const response = await VendaService.atualizar(editedItem.value.id, editedItem.value);
        console.log('Venda atualizada:', response.data);
        Object.assign(vendas.value[editedIndex.value], response.data);
        showSnackbar('Venda atualizada com sucesso');
      }
    } else {
      const response = await VendaService.criar(editedItem.value);
      console.log('Venda criada:', response.data);
      vendas.value.push(response.data);
      showSnackbar('Venda criada com sucesso');
    }
    close();
  } catch (error) {
    console.error('Erro ao salvar venda:', error);
    showSnackbar('Erro ao salvar venda', 'error');
  }
}

onMounted(() => {
  console.log('Componente Vendas montado');
  fetchVendas();
  fetchClientes();
  fetchFuncionarios();
  fetchProdutos();
});
</script> 