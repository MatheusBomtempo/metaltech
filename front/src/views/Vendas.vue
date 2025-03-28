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
                <v-select
                  v-model="editedItem.formaPagamento"
                  :items="formasPagamento"
                  label="Forma de Pagamento"
                ></v-select>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="editedItem.dataVenda"
                  label="Data da Venda"
                  type="datetime-local"
                ></v-text-field>
              </v-col>
            </v-row>

            <!-- Tabela de Itens -->
            <v-row>
              <v-col cols="12">
                <v-table>
                  <thead>
                    <tr>
                      <th>Produto</th>
                      <th>Quantidade</th>
                      <th>Preço Unitário</th>
                      <th>Subtotal</th>
                      <th>Ações</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in editedItem.itens" :key="index">
                      <td>
                        <v-select
                          v-model="item.produto"
                          :items="produtos"
                          item-title="nome"
                          item-value="id"
                          label="Produto"
                        ></v-select>
                      </td>
                      <td>
                        <v-text-field
                          v-model="item.quantidade"
                          type="number"
                          label="Quantidade"
                          @input="calcularSubtotal(index)"
                        ></v-text-field>
                      </td>
                      <td>
                        <v-text-field
                          v-model="item.precoUnitario"
                          type="number"
                          label="Preço Unitário"
                          @input="calcularSubtotal(index)"
                        ></v-text-field>
                      </td>
                      <td>{{ item.subtotal }}</td>
                      <td>
                        <v-btn
                          icon
                          color="error"
                          size="small"
                          @click="removerItem(index)"
                        >
                          <v-icon>mdi-delete</v-icon>
                        </v-btn>
                      </td>
                    </tr>
                  </tbody>
                  <tfoot>
                    <tr>
                      <td colspan="3" class="text-right"><strong>Total:</strong></td>
                      <td><strong>{{ totalVenda }}</strong></td>
                      <td>
                        <v-btn
                          color="primary"
                          size="small"
                          @click="adicionarItem"
                        >
                          Adicionar Item
                        </v-btn>
                      </td>
                    </tr>
                  </tfoot>
                </v-table>
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
  name: 'Vendas',
  data() {
    return {
      search: '',
      dialog: false,
      headers: [
        { title: 'Data', key: 'dataVenda' },
        { title: 'Cliente', key: 'cliente.nome' },
        { title: 'Funcionário', key: 'funcionario.nome' },
        { title: 'Total', key: 'valorTotal' },
        { title: 'Forma de Pagamento', key: 'formaPagamento' },
        { title: 'Ações', key: 'actions', sortable: false }
      ],
      vendas: [],
      clientes: [],
      funcionarios: [],
      produtos: [],
      formasPagamento: ['DINHEIRO', 'CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX'],
      editedIndex: -1,
      editedItem: {
        cliente: null,
        funcionario: null,
        dataVenda: '',
        valorTotal: 0,
        formaPagamento: '',
        itens: []
      },
      defaultItem: {
        cliente: null,
        funcionario: null,
        dataVenda: '',
        valorTotal: 0,
        formaPagamento: '',
        itens: []
      }
    }
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'Nova Venda' : 'Editar Venda'
    },
    totalVenda () {
      return this.editedItem.itens.reduce((total, item) => total + (item.subtotal || 0), 0)
    }
  },
  methods: {
    async fetchVendas() {
      try {
        const response = await axios.get('/api/vendas')
        this.vendas = response.data
      } catch (error) {
        console.error('Erro ao buscar vendas:', error)
      }
    },
    async fetchClientes() {
      try {
        const response = await axios.get('/api/clientes')
        this.clientes = response.data
      } catch (error) {
        console.error('Erro ao buscar clientes:', error)
      }
    },
    async fetchFuncionarios() {
      try {
        const response = await axios.get('/api/funcionarios')
        this.funcionarios = response.data
      } catch (error) {
        console.error('Erro ao buscar funcionários:', error)
      }
    },
    async fetchProdutos() {
      try {
        const response = await axios.get('/api/produtos')
        this.produtos = response.data
      } catch (error) {
        console.error('Erro ao buscar produtos:', error)
      }
    },
    editItem (item) {
      this.editedIndex = this.vendas.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    async deleteItem (item) {
      const index = this.vendas.indexOf(item)
      confirm('Tem certeza que deseja excluir esta venda?') && 
        await this.vendas.splice(index, 1)
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
        Object.assign(this.vendas[this.editedIndex], this.editedItem)
      } else {
        this.vendas.push(this.editedItem)
      }
      this.close()
    },
    adicionarItem() {
      this.editedItem.itens.push({
        produto: null,
        quantidade: 0,
        precoUnitario: 0,
        subtotal: 0
      })
    },
    removerItem(index) {
      this.editedItem.itens.splice(index, 1)
    },
    calcularSubtotal(index) {
      const item = this.editedItem.itens[index]
      item.subtotal = item.quantidade * item.precoUnitario
      this.editedItem.valorTotal = this.totalVenda
    }
  },
  created() {
    this.fetchVendas()
    this.fetchClientes()
    this.fetchFuncionarios()
    this.fetchProdutos()
  }
}
</script> 