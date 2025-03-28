<template>
  <div>
    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      :timeout="3000"
    >
      {{ snackbar.text }}
    </v-snackbar>

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
            <template #top>
              <v-toolbar flat>
                <v-toolbar-title>Lista de Clientes</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <v-btn color="primary" prepend-icon="mdi-plus" @click="dialog = true">
                  Novo Cliente
                </v-btn>
              </v-toolbar>
            </template>

            <template #item.actions="{ item }">
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
  dataCadastro: new Date().toISOString()
});
const defaultItem = ref<Cliente>({
  nome: '',
  documentoFiscal: '',
  telefone: '',
  endereco: '',
  dataCadastro: new Date().toISOString()
});

const snackbar = ref({
  show: false,
  text: '',
  color: 'error'
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
    console.log('Iniciando busca de clientes...');
    const response = await ClienteService.listar();
    console.log('Clientes encontrados:', response.data);
    clientes.value = response.data;
    console.log('Lista de clientes atualizada com sucesso');
  } catch (error: any) {
    console.error('Erro detalhado ao buscar clientes:', {
      mensagem: error.message,
      resposta: error.response?.data,
      status: error.response?.status
    });
    showSnackbar('Erro ao carregar lista de clientes', 'error');
  }
}

function editItem(item: Cliente) {
  console.log('Iniciando edição do cliente:', item);
  editedIndex.value = clientes.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  dialog.value = true;
  console.log('Cliente carregado para edição:', editedItem.value);
}

async function deleteItem(item: Cliente) {
  console.log('Iniciando processo de exclusão do cliente:', item);
  const index = clientes.value.indexOf(item);
  
  if (confirm('Tem certeza que deseja excluir este cliente?')) {
    try {
      if (item.id) {
        console.log('Enviando requisição de exclusão para o cliente ID:', item.id);
        await ClienteService.excluir(item.id);
        console.log('Cliente excluído com sucesso no backend');
        
        // Verifica se o cliente ainda existe na lista antes de tentar removê-lo
        if (index !== -1) {
          console.log('Removendo cliente da lista local, índice:', index);
          clientes.value.splice(index, 1);
          showSnackbar('Cliente excluído com sucesso!', 'success');
        } else {
          console.warn('Cliente não encontrado na lista local para remoção');
          showSnackbar('Cliente não encontrado na lista', 'warning');
        }
      } else {
        console.warn('Tentativa de excluir cliente sem ID');
        showSnackbar('Cliente inválido para exclusão', 'error');
      }
    } catch (error: any) {
      console.error('Erro detalhado ao excluir cliente:', {
        mensagem: error.message,
        resposta: error.response?.data,
        status: error.response?.status
      });
      showSnackbar(error.response?.data?.message || 'Erro ao excluir cliente', 'error');
    }
  } else {
    console.log('Operação de exclusão cancelada pelo usuário');
  }
}

function close() {
  console.log('Fechando diálogo de edição');
  dialog.value = false;
  editedItem.value = Object.assign({}, defaultItem.value);
  editedIndex.value = -1;
  console.log('Estado do formulário resetado');
}

function showSnackbar(text: string, color: string = 'error') {
  console.log('Exibindo notificação:', { text, color });
  snackbar.value = {
    show: true,
    text,
    color
  };
}

async function save() {
  console.log('Iniciando processo de salvamento:', editedItem.value);
  
  try {
    if (editedIndex.value > -1) {
      console.log('Modo de edição - Atualizando cliente existente');
      if (editedItem.value.id) {
        console.log('Enviando atualização para o cliente ID:', editedItem.value.id);
        await ClienteService.atualizar(editedItem.value.id, editedItem.value);
        console.log('Cliente atualizado com sucesso no backend');
        
        // Verifica se o cliente ainda existe na lista antes de atualizar
        if (editedIndex.value < clientes.value.length) {
          console.log('Atualizando cliente na lista local, índice:', editedIndex.value);
          Object.assign(clientes.value[editedIndex.value], editedItem.value);
          showSnackbar('Cliente atualizado com sucesso!', 'success');
        } else {
          console.warn('Cliente não encontrado na lista local para atualização');
          showSnackbar('Cliente não encontrado na lista', 'warning');
        }
      } else {
        console.warn('Tentativa de atualizar cliente sem ID');
        showSnackbar('Cliente inválido para atualização', 'error');
      }
    } else {
      console.log('Modo de criação - Criando novo cliente');
      const response = await ClienteService.criar(editedItem.value);
      console.log('Novo cliente criado:', response.data);
      clientes.value.push(response.data);
      showSnackbar('Cliente criado com sucesso!', 'success');
    }
    close();
  } catch (error: any) {
    console.error('Erro detalhado ao salvar cliente:', {
      mensagem: error.message,
      resposta: error.response?.data,
      status: error.response?.status
    });
    showSnackbar(error.response?.data?.message || 'Erro ao salvar cliente', 'error');
  }
}

onMounted(() => {
  console.log('Componente Clientes montado, iniciando carregamento...');
  fetchClientes();
});
</script> 