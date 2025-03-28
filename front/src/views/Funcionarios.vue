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
                  :items="cargos"
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

<script>
import axios from 'axios'

export default {
  name: 'Funcionarios',
  data() {
    return {
      search: '',
      dialog: false,
      headers: [
        { title: 'Nome', key: 'nome' },
        { title: 'CPF', key: 'documentoFiscal' },
        { title: 'Telefone', key: 'telefone' },
        { title: 'Cargo', key: 'cargo' },
        { title: 'Salário', key: 'salario' },
        { title: 'Data Contratação', key: 'dataContratacao' },
        { title: 'Ações', key: 'actions', sortable: false }
      ],
      funcionarios: [],
      cargos: ['GERENTE', 'REPRESENTANTE_VENDAS', 'SOLDADOR', 'OPERADOR_MAQUINA', 'AUXILIAR_ADMINISTRATIVO'],
      editedIndex: -1,
      editedItem: {
        nome: '',
        documentoFiscal: '',
        telefone: '',
        cargo: '',
        salario: 0,
        dataContratacao: ''
      },
      defaultItem: {
        nome: '',
        documentoFiscal: '',
        telefone: '',
        cargo: '',
        salario: 0,
        dataContratacao: ''
      }
    }
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'Novo Funcionário' : 'Editar Funcionário'
    }
  },
  methods: {
    async fetchFuncionarios() {
      try {
        const response = await axios.get('/api/funcionarios')
        this.funcionarios = response.data
      } catch (error) {
        console.error('Erro ao buscar funcionários:', error)
      }
    },
    editItem (item) {
      this.editedIndex = this.funcionarios.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    async deleteItem (item) {
      const index = this.funcionarios.indexOf(item)
      confirm('Tem certeza que deseja excluir este funcionário?') && 
        await this.funcionarios.splice(index, 1)
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
        Object.assign(this.funcionarios[this.editedIndex], this.editedItem)
      } else {
        this.funcionarios.push(this.editedItem)
      }
      this.close()
    }
  },
  created() {
    this.fetchFuncionarios()
  }
}
</script> 