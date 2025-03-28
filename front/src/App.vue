<script>
export default {
  name: 'App',
  data() {
    return {
      drawer: true,
      menuItems: [
        { title: 'Produtos', icon: 'mdi-package-variant', path: '/produtos' },
        { title: 'Clientes', icon: 'mdi-account-group', path: '/clientes' },
        { title: 'Funcionários', icon: 'mdi-account-tie', path: '/funcionarios' },
        { title: 'Fornecedores', icon: 'mdi-truck', path: '/fornecedores' },
        { title: 'Vendas', icon: 'mdi-cart', path: '/vendas' }
      ]
    }
  }
}
</script>

<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" app>
      <v-list>
        <v-list-item
          v-for="item in menuItems"
          :key="item.title"
          :to="item.path"
          :prepend-icon="item.icon"
          :title="item.title"
          @click="drawer = false"
        />
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app>
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>MetalTech</v-toolbar-title>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
