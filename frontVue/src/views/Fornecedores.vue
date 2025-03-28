<template>
  <div>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title>
            Fornecedores
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
            :items="fornecedores"
            :search="search"
            class="elevation-1"
          >
            <template v-slot:top>
              <v-toolbar flat>
                <v-toolbar-title>Lista de Fornecedores</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <v-btn color="primary" prepend-icon="mdi-plus" @click="dialog = true">
                  Novo Fornecedor
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

    <!-- Dialog para adicionar/editar fornecedor -->
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
                  v-model="editedItem.nomeEmpresa"
                  label="Nome da Empresa"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.cnpj"
                  label="CNPJ"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.telefone"
                  label="Telefone"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-select
                  v-model="editedItem.materiais"
                  :items="tiposMateriais"
                  label="Materiais"
                  multiple
                  chips
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
import { FornecedorService } from '../services/api';
import type { Fornecedor } from '../types';

const search = ref('');
const dialog = ref(false);
const fornecedores = ref<Fornecedor[]>([]);
const tiposMateriais = [
  'Tubos',
  'Parafusos',
  'Chapas',
  'Porcas',
  'Arruelas',
  'Rebites'
];
const editedIndex = ref(-1);
const editedItem = ref<Fornecedor>({
  nomeEmpresa: '',
  cnpj: '',
  telefone: '',
  materiais: []
});
const defaultItem = ref<Fornecedor>({
  nomeEmpresa: '',
  cnpj: '',
  telefone: '',
  materiais: []
});

const headers = [
  { title: 'Nome da Empresa', key: 'nomeEmpresa' },
  { title: 'CNPJ', key: 'cnpj' },
  { title: 'Telefone', key: 'telefone' },
  { title: 'Materiais', key: 'materiais' },
  { title: 'Ações', key: 'actions', sortable: false }
];

const formTitle = computed(() => {
  return editedIndex.value === -1 ? 'Novo Fornecedor' : 'Editar Fornecedor';
});

async function fetchFornecedores() {
  try {
    const response = await FornecedorService.listar();
    fornecedores.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar fornecedores:', error);
  }
}

function editItem(item: Fornecedor) {
  editedIndex.value = fornecedores.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  dialog.value = true;
}

async function deleteItem(item: Fornecedor) {
  const index = fornecedores.value.indexOf(item);
  if (confirm('Tem certeza que deseja excluir este fornecedor?')) {
    try {
      if (item.id) {
        await FornecedorService.excluir(item.id);
      }
      fornecedores.value.splice(index, 1);
    } catch (error) {
      console.error('Erro ao excluir fornecedor:', error);
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
        await FornecedorService.atualizar(editedItem.value.id, editedItem.value);
      }
      Object.assign(fornecedores.value[editedIndex.value], editedItem.value);
    } else {
      const response = await FornecedorService.criar(editedItem.value);
      fornecedores.value.push(response.data);
    }
    close();
  } catch (error) {
    console.error('Erro ao salvar fornecedor:', error);
  }
}

onMounted(() => {
  fetchFornecedores();
});
</script> 