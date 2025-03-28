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

const snackbar = ref({
  show: false,
  text: '',
  color: 'error'
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
    console.log('Iniciando busca de produtos...');
    const response = await ProdutoService.listar();
    console.log('Produtos encontrados:', response.data);
    produtos.value = response.data;
    console.log('Lista de produtos atualizada com sucesso');
  } catch (error) {
    console.error('Erro ao buscar produtos:', error);
    showSnackbar('Erro ao carregar lista de produtos', 'error');
  }
}

function editItem(item: Produto) {
  console.log('Iniciando edição do produto:', item);
  editedIndex.value = produtos.value.indexOf(item);
  editedItem.value = Object.assign({}, item);
  dialog.value = true;
  console.log('Produto carregado para edição:', editedItem.value);
}

async function deleteItem(item: Produto) {
  console.log('Iniciando processo de exclusão do produto:', item);
  const index = produtos.value.indexOf(item);
  
  if (confirm('Tem certeza que deseja excluir este produto?')) {
    try {
      if (item.id) {
        console.log('Enviando requisição de exclusão para o produto ID:', item.id);
        await ProdutoService.excluir(item.id);
        console.log('Produto excluído com sucesso no backend');
        
        // Verifica se o produto ainda existe na lista antes de tentar removê-lo
        if (index !== -1) {
          console.log('Removendo produto da lista local, índice:', index);
          produtos.value.splice(index, 1);
          showSnackbar('Produto excluído com sucesso!', 'success');
        } else {
          console.warn('Produto não encontrado na lista local para remoção');
          showSnackbar('Produto não encontrado na lista', 'warning');
        }
      } else {
        console.warn('Tentativa de excluir produto sem ID');
        showSnackbar('Produto inválido para exclusão', 'error');
      }
    } catch (error: any) {
      console.error('Erro detalhado ao excluir produto:', {
        mensagem: error.message,
        resposta: error.response?.data,
        status: error.response?.status
      });
      showSnackbar(error.response?.data?.message || 'Erro ao excluir produto', 'error');
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
      console.log('Modo de edição - Atualizando produto existente');
      if (editedItem.value.id) {
        console.log('Enviando atualização para o produto ID:', editedItem.value.id);
        await ProdutoService.atualizar(editedItem.value.id, editedItem.value);
        console.log('Produto atualizado com sucesso no backend');
        
        // Verifica se o produto ainda existe na lista antes de atualizar
        if (editedIndex.value < produtos.value.length) {
          console.log('Atualizando produto na lista local, índice:', editedIndex.value);
          Object.assign(produtos.value[editedIndex.value], editedItem.value);
          showSnackbar('Produto atualizado com sucesso!', 'success');
        } else {
          console.warn('Produto não encontrado na lista local para atualização');
          showSnackbar('Produto não encontrado na lista', 'warning');
        }
      } else {
        console.warn('Tentativa de atualizar produto sem ID');
        showSnackbar('Produto inválido para atualização', 'error');
      }
    } else {
      console.log('Modo de criação - Criando novo produto');
      const response = await ProdutoService.criar(editedItem.value);
      console.log('Novo produto criado:', response.data);
      produtos.value.push(response.data);
      showSnackbar('Produto criado com sucesso!', 'success');
    }
    close();
  } catch (error: any) {
    console.error('Erro detalhado ao salvar produto:', {
      mensagem: error.message,
      resposta: error.response?.data,
      status: error.response?.status
    });
    showSnackbar(error.response?.data?.message || 'Erro ao salvar produto', 'error');
  }
}

onMounted(() => {
  console.log('Componente Produtos montado, iniciando carregamento...');
  fetchProdutos();
});
</script> 