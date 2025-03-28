import { createRouter, createWebHistory } from 'vue-router';
import Produtos from '../views/Produtos.vue';
import Clientes from '../views/Clientes.vue';
import Funcionarios from '../views/Funcionarios.vue';
import Fornecedores from '../views/Fornecedores.vue';
import Vendas from '../views/Vendas.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/produtos'
    },
    {
      path: '/produtos',
      name: 'produtos',
      component: Produtos
    },
    {
      path: '/clientes',
      name: 'clientes',
      component: Clientes
    },
    {
      path: '/funcionarios',
      name: 'funcionarios',
      component: Funcionarios
    },
    {
      path: '/fornecedores',
      name: 'fornecedores',
      component: Fornecedores
    },
    {
      path: '/vendas',
      name: 'vendas',
      component: Vendas
    }
  ]
});

export default router; 