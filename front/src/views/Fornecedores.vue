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
                  :items="tiposMaterial"
                  label="Tipos de Material"
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

<script>
import axios from 'axios'

export default {
  name: 'Fornecedores',
  data() {
    return {
      search: '',
      dialog: false,
      headers: [
        { title: 'Nome da Empresa', key: 'nomeEmpresa' },
        { title: 'CNPJ', key: 'cnpj' },
        { title: 'Telefone', key: 'telefone' },
        { title: 'Materiais', key: 'materiais' },
        { title: 'Ações', key: 'actions', sortable: false }
      ],
      fornecedores: [],
      tiposMaterial: ['Aço Inox', 'Aço Carbono', 'Aço Especial', 'Aço Galvanizado', 'Aço Corten', 'Parafusos', 'Porcas', 'Arruelas', 'Rebites', 'Tubos de Aço'],
      editedIndex: -1,
      editedItem: {
        nomeEmpresa: '',
        cnpj: '',
        telefone: '',
        materiais: []
      },
      defaultItem: {
        nomeEmpresa: '',
        cnpj: '',
        telefone: '',
        materiais: []
      }
    }
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'Novo Fornecedor' : 'Editar Fornecedor'
    }
  },
  methods: {
    async fetchFornecedores() {
      try {
        const response = await axios.get('/api/fornecedores')
        this.fornecedores = response.data
      } catch (error) {
        console.error('Erro ao buscar fornecedores:', error)
      }
    },
    editItem (item) {
      this.editedIndex = this.fornecedores.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    async deleteItem (item) {
      const index = this.fornecedores.indexOf(item)
      confirm('Tem certeza que deseja excluir este fornecedor?') && 
        await this.fornecedores.splice(index, 1)
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
        Object.assign(this.fornecedores[this.editedIndex], this.editedItem)
      } else {
        this.fornecedores.push(this.editedItem)
      }
      this.close()
    }
  },
  created() {
    this.fetchFornecedores()
  }
}
</script> 