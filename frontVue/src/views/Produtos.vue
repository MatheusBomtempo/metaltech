<template>
  <div>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title>
            Produtos
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
            :items="produtos"
            :search="search"
            class="elevation-1"
          >
            <template v-slot:top>
              <v-toolbar flat>
                <v-toolbar-title>Lista de Produtos</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <v-btn color="primary" prepend-icon="mdi-plus" @click="dialog = true">
                  Novo Produto
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

    <!-- Dialog para adicionar/editar produto -->
    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title>
          <span class="text-h5">{{ formTitle }}</span>
        </v-card-title>

        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.codigo"
                  label="Código"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.nome"
                  label="Nome"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.preco"
                  label="Preço"
                  type="number"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.quantidadeEstoque"
                  label="Quantidade em Estoque"
                  type="number"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-select
                  v-model="editedItem.categoria"
                  :items="categorias"
                  label="Categoria"
                ></v-select>
              </v-col>
            </v-row>
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
import { ProdutoService } from '../services/api';
import type { Produto } from '../types';

const search = ref('');
const dialog = ref(false);
const produtos = ref<Produto[]>([]);
const categorias = ['Tubos', 'Parafusos', 'Chapas', 'Porcas', 'Arruelas', 'Rebites'];
const editedIndex = ref(-1);
const editedItem = ref<Produto>({
  codigo: '',
  nome: '',
  preco: 0,
  quantidadeEstoque: 0,
  categoria: ''
});
const defaultItem = ref<Produto>({
  codigo: '',
  nome: '',
  preco: 0,
  quantidadeEstoque: 0,
  categoria: ''
});

const headers = [
  { title: 'Código', key: 'codigo' },
  { title: 'Nome', key: 'nome' },
  { title: 'Preço', key: 'preco' },
  { title: 'Estoque', key: 'quantidadeEstoque' },
  { title: 'Categoria', key: 'categoria' },
  { title: 'Ações', key: 'actions', sortable: false }
];

const formTitle = computed(() => {
  return editedIndex.value === -1 ? 'Novo Produto' : 'Editar Produto';
});

async function fetchProdutos() {
  try {
    const response = await ProdutoService.listar();
    produtos.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar produtos:', error);
  }
}

function editItem(item: Produto) {
  editedIndex.value = produtos.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  dialog.value = true;
}

async function deleteItem(item: Produto) {
  const index = produtos.value.indexOf(item);
  if (confirm('Tem certeza que deseja excluir este produto?')) {
    try {
      if (item.id) {
        await ProdutoService.excluir(item.id);
      }
      produtos.value.splice(index, 1);
    } catch (error) {
      console.error('Erro ao excluir produto:', error);
    }
  }
}

function close() {
  dialog.value = false;
  editedItem.value = Object.assign({}, defaultItem.value);
  editedIndex.value = -1;
}

async function save() {
  try {
    if (editedIndex.value > -1) {
      if (editedItem.value.id) {
        await ProdutoService.atualizar(editedItem.value.id, editedItem.value);
      }
      Object.assign(produtos.value[editedIndex.value], editedItem.value);
    } else {
      const response = await ProdutoService.criar(editedItem.value);
      produtos.value.push(response.data);
    }
    close();
  } catch (error) {
    console.error('Erro ao salvar produto:', error);
  }
}

onMounted(() => {
  fetchProdutos();
});
</script> 