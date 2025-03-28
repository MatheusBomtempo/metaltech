<template>
  <div>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title>
            Clientes
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
            :items="clientes"
            :search="search"
            class="elevation-1"
          >
            <template v-slot:top>
              <v-toolbar flat>
                <v-toolbar-title>Lista de Clientes</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <v-btn color="primary" prepend-icon="mdi-plus" @click="dialog = true">
                  Novo Cliente
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

    <!-- Dialog para adicionar/editar cliente -->
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
                  v-model="editedItem.nome"
                  label="Nome"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.documentoFiscal"
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
                <v-textarea
                  v-model="editedItem.endereco"
                  label="Endereço"
                ></v-textarea>
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
import { ClienteService } from '../services/api';
import type { Cliente } from '../types';

const search = ref('');
const dialog = ref(false);
const clientes = ref<Cliente[]>([]);
const editedIndex = ref(-1);
const editedItem = ref<Cliente>({
  nome: '',
  documentoFiscal: '',
  telefone: '',
  endereco: '',
  dataCadastro: new Date()
});
const defaultItem = ref<Cliente>({
  nome: '',
  documentoFiscal: '',
  telefone: '',
  endereco: '',
  dataCadastro: new Date()
});

const headers = [
  { title: 'Nome', key: 'nome' },
  { title: 'CNPJ', key: 'documentoFiscal' },
  { title: 'Telefone', key: 'telefone' },
  { title: 'Endereço', key: 'endereco' },
  { title: 'Data Cadastro', key: 'dataCadastro' },
  { title: 'Ações', key: 'actions', sortable: false }
];

const formTitle = computed(() => {
  return editedIndex.value === -1 ? 'Novo Cliente' : 'Editar Cliente';
});

async function fetchClientes() {
  try {
    const response = await ClienteService.listar();
    clientes.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar clientes:', error);
  }
}

function editItem(item: Cliente) {
  editedIndex.value = clientes.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  dialog.value = true;
}

async function deleteItem(item: Cliente) {
  const index = clientes.value.indexOf(item);
  if (confirm('Tem certeza que deseja excluir este cliente?')) {
    try {
      if (item.id) {
        await ClienteService.excluir(item.id);
      }
      clientes.value.splice(index, 1);
    } catch (error) {
      console.error('Erro ao excluir cliente:', error);
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
        await ClienteService.atualizar(editedItem.value.id, editedItem.value);
      }
      Object.assign(clientes.value[editedIndex.value], editedItem.value);
    } else {
      const response = await ClienteService.criar(editedItem.value);
      clientes.value.push(response.data);
    }
    close();
  } catch (error) {
    console.error('Erro ao salvar cliente:', error);
  }
}

onMounted(() => {
  fetchClientes();
});
</script> 