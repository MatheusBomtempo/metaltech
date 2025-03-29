<template>
  <div>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title>
            Funcionários
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
            :items="funcionarios"
            :search="search"
            class="elevation-1"
          >
            <template v-slot:top>
              <v-toolbar flat>
                <v-toolbar-title>Lista de Funcionários</v-toolbar-title>
                <v-divider class="mx-4" inset vertical></v-divider>
                <v-spacer></v-spacer>
                <v-btn color="primary" prepend-icon="mdi-plus" @click="dialog = true">
                  Novo Funcionário
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

    <!-- Dialog para adicionar/editar funcionário -->
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
                  label="CPF"
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
                  v-model="editedItem.cargo"
                  :items="['GERENTE', 'REPRESENTANTE_VENDAS', 'SOLDADOR', 'OPERADOR_MAQUINA', 'AUXILIAR_ADMINISTRATIVO']"
                  label="Cargo"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.salario"
                  label="Salário"
                  type="number"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.dataContratacao"
                  label="Data de Contratação"
                  type="date"
                ></v-text-field>
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
import { FuncionarioService } from '../services/api';
import type { Funcionario } from '../types';

const search = ref('');
const dialog = ref(false);
const funcionarios = ref<Funcionario[]>([]);
const editedIndex = ref(-1);
const editedItem = ref<Funcionario>({
  nome: '',
  documentoFiscal: '',
  telefone: '',
  cargo: 'AUXILIAR_ADMINISTRATIVO',
  salario: 0,
  dataContratacao: new Date().toISOString().split('T')[0],
  dataCadastro: ''
});
const defaultItem = ref<Funcionario>({
  nome: '',
  documentoFiscal: '',
  telefone: '',
  cargo: 'AUXILIAR_ADMINISTRATIVO',
  salario: 0,
  dataContratacao: new Date().toISOString().split('T')[0],
  dataCadastro: ''
});

const headers = [
  { title: 'Nome', key: 'nome' },
  { title: 'CPF', key: 'documentoFiscal' },
  { title: 'Telefone', key: 'telefone' },
  { title: 'Cargo', key: 'cargo' },
  { title: 'Salário', key: 'salario' },
  { title: 'Data Contratação', key: 'dataContratacao' },
  { title: 'Data Cadastro', key: 'dataCadastro' },
  { title: 'Ações', key: 'actions', sortable: false }
];

const formTitle = computed(() => {
  return editedIndex.value === -1 ? 'Novo Funcionário' : 'Editar Funcionário';
});

async function fetchFuncionarios() {
  try {
    const response = await FuncionarioService.listar();
    funcionarios.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar funcionários:', error);
  }
}

function editItem(item: Funcionario) {
  editedIndex.value = funcionarios.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  dialog.value = true;
}

async function deleteItem(item: Funcionario) {
  const index = funcionarios.value.indexOf(item);
  if (confirm('Tem certeza que deseja excluir este funcionário?')) {
    try {
      if (item.id) {
        await FuncionarioService.excluir(item.id);
      }
      funcionarios.value.splice(index, 1);
    } catch (error) {
      console.error('Erro ao excluir funcionário:', error);
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
    // Validações básicas
    if (!editedItem.value.nome?.trim()) {
      alert('Nome é obrigatório');
      return;
    }

    if (!editedItem.value.documentoFiscal?.trim()) {
      alert('Documento fiscal é obrigatório');
      return;
    }

    if (!editedItem.value.cargo) {
      alert('Cargo é obrigatório');
      return;
    }

    if (!editedItem.value.salario || editedItem.value.salario <= 0) {
      alert('Salário deve ser maior que zero');
      return;
    }

    if (editedIndex.value > -1) {
      if (editedItem.value.id) {
        console.log('Atualizando funcionário:', editedItem.value);
        const response = await FuncionarioService.atualizar(editedItem.value.id, editedItem.value);
        Object.assign(funcionarios.value[editedIndex.value], response.data);
        alert('Funcionário atualizado com sucesso');
      }
    } else {
      console.log('Criando novo funcionário:', editedItem.value);
      const response = await FuncionarioService.criar(editedItem.value);
      funcionarios.value.push(response.data);
      alert('Funcionário criado com sucesso');
    }
    close();
  } catch (error: any) {
    console.error('Erro ao salvar funcionário:', error);
    const errorMessage = error.response?.data?.message || 'Erro ao salvar funcionário';
    alert(errorMessage);
  }
}

onMounted(() => {
  fetchFuncionarios();
});
</script> 