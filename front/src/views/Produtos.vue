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

<script>
import axios from 'axios'

export default {
  name: 'Produtos',
  data() {
    return {
      search: '',
      dialog: false,
      headers: [
        { title: 'Código', key: 'codigo' },
        { title: 'Nome', key: 'nome' },
        { title: 'Preço', key: 'preco' },
        { title: 'Estoque', key: 'quantidadeEstoque' },
        { title: 'Categoria', key: 'categoria' },
        { title: 'Ações', key: 'actions', sortable: false }
      ],
      produtos: [],
      categorias: ['Tubos', 'Parafusos', 'Chapas', 'Porcas', 'Arruelas', 'Rebites'],
      editedIndex: -1,
      editedItem: {
        codigo: '',
        nome: '',
        preco: 0,
        quantidadeEstoque: 0,
        categoria: ''
      },
      defaultItem: {
        codigo: '',
        nome: '',
        preco: 0,
        quantidadeEstoque: 0,
        categoria: ''
      }
    }
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'Novo Produto' : 'Editar Produto'
    }
  },
  methods: {
    async fetchProdutos() {
      try {
        const response = await axios.get('/api/produtos')
        this.produtos = response.data
      } catch (error) {
        console.error('Erro ao buscar produtos:', error)
      }
    },
    editItem (item) {
      this.editedIndex = this.produtos.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    async deleteItem (item) {
      const index = this.produtos.indexOf(item)
      confirm('Tem certeza que deseja excluir este produto?') && 
        await this.produtos.splice(index, 1)
    },
    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },
    async save () {
      if (this.editedIndex > -1) {
        Object.assign(this.produtos[this.editedIndex], this.editedItem)
      } else {
        this.produtos.push(this.editedItem)
      }
      this.close()
    }
  },
  created() {
    this.fetchProdutos()
  }
}
</script> 