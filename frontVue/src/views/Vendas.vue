<template>
  <div>
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
          >
            <template v-slot:top>
              <v-toolbar flat>
                <v-toolbar-title>Lista de Vendas</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <v-btn color="primary" prepend-icon="mdi-plus" @click="dialog = true">
                  Nova Venda
                </v-btn>
              </v-toolbar>
            </template>

            <template v-slot:item.actions="{ item }">
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
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-select
                  v-model="editedItem.funcionario"
                  :items="funcionarios"
                  item-title="nome"
                  item-value="id"
                  label="Funcionário"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.dataVenda"
                  label="Data da Venda"
                  type="date"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-select
                  v-model="editedItem.formaPagamento"
                  :items="formasPagamento"
                  label="Forma de Pagamento"
                ></v-select>
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
                    <template v-slot:item.actions="{ item }">
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
                          @change="updateItemPrice"
                        ></v-select>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model="newItem.quantidade"
                          label="Quantidade"
                          type="number"
                          @input="calculateSubtotal"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model="newItem.precoUnitario"
                          label="Preço Unitário"
                          type="number"
                          readonly
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model="newItem.subtotal"
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

const search = ref('');
const dialog = ref(false);
const itemDialog = ref(false);
const vendas = ref<Venda[]>([]);
const clientes = ref<Cliente[]>([]);
const funcionarios = ref<Funcionario[]>([]);
const produtos = ref<Produto[]>([]);
const formasPagamento = ['DINHEIRO', 'CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX', 'TRANSFERENCIA'];

const editedIndex = ref(-1);
const editedItem = ref<Venda>({
  cliente: null,
  funcionario: null,
  dataVenda: '',
  valorTotal: 0,
  formaPagamento: '',
  itens: []
});

const defaultItem = ref<Venda>({
  cliente: null,
  funcionario: null,
  dataVenda: '',
  valorTotal: 0,
  formaPagamento: '',
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

async function fetchVendas() {
  try {
    const response = await VendaService.listar();
    vendas.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar vendas:', error);
  }
}

async function fetchClientes() {
  try {
    const response = await ClienteService.listar();
    clientes.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar clientes:', error);
  }
}

async function fetchFuncionarios() {
  try {
    const response = await FuncionarioService.listar();
    funcionarios.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar funcionários:', error);
  }
}

async function fetchProdutos() {
  try {
    const response = await ProdutoService.listar();
    produtos.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar produtos:', error);
  }
}

function editItem(item: Venda) {
  editedIndex.value = vendas.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  dialog.value = true;
}

async function deleteItem(item: Venda) {
  const index = vendas.value.indexOf(item);
  if (confirm('Tem certeza que deseja excluir esta venda?')) {
    try {
      if (item.id) {
        await VendaService.excluir(item.id);
      }
      vendas.value.splice(index, 1);
    } catch (error) {
      console.error('Erro ao excluir venda:', error);
    }
  }
}

function close() {
  dialog.value = false;
  editedItem.value = Object.assign({}, defaultItem.value);
  editedIndex.value = -1;
}

function addItem() {
  itemDialog.value = true;
}

function removeItem(item: ItemVenda) {
  const index = editedItem.value.itens.indexOf(item);
  editedItem.value.itens.splice(index, 1);
  calculateTotal();
}

function updateItemPrice() {
  const produto = produtos.value.find(p => p.id === newItem.value.produto);
  if (produto) {
    newItem.value.precoUnitario = produto.preco;
    calculateSubtotal();
  }
}

function calculateSubtotal() {
  newItem.value.subtotal = newItem.value.quantidade * newItem.value.precoUnitario;
}

function calculateTotal() {
  editedItem.value.valorTotal = editedItem.value.itens.reduce(
    (total, item) => total + item.subtotal,
    0
  );
}

function saveItem() {
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
  try {
    if (editedIndex.value > -1) {
      if (editedItem.value.id) {
        await VendaService.atualizar(editedItem.value.id, editedItem.value);
      }
      Object.assign(vendas.value[editedIndex.value], editedItem.value);
    } else {
      const response = await VendaService.criar(editedItem.value);
      vendas.value.push(response.data);
    }
    close();
  } catch (error) {
    console.error('Erro ao salvar venda:', error);
  }
}

onMounted(() => {
  fetchVendas();
  fetchClientes();
  fetchFuncionarios();
  fetchProdutos();
});
</script> 