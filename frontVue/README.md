# Metaltech - Frontend

Frontend do sistema de gestão Metaltech, desenvolvido com Vue 3, TypeScript e Vuetify.

## Tecnologias Utilizadas

- Vue 3
- TypeScript
- Vuetify 3
- Vue Router
- Axios
- Vite

## Requisitos

- Node.js 18 ou superior
- npm ou yarn

## Instalação

1. Clone o repositório
2. Instale as dependências:

```bash
npm install
# ou
yarn install
```

## Desenvolvimento

Para iniciar o servidor de desenvolvimento:

```bash
npm run dev
# ou
yarn dev
```

O servidor estará disponível em `http://localhost:5173`

## Build

Para criar uma build de produção:

```bash
npm run build
# ou
yarn build
```

## Lint

Para executar o linting:

```bash
npm run lint
# ou
yarn lint
```

## Estrutura do Projeto

```
src/
├── assets/        # Recursos estáticos
├── components/    # Componentes reutilizáveis
├── plugins/       # Plugins (Vuetify, etc)
├── router/        # Configuração de rotas
├── services/      # Serviços de API
├── types/         # Definições de tipos TypeScript
├── views/         # Componentes de página
├── App.vue        # Componente raiz
└── main.ts        # Ponto de entrada
```

## Funcionalidades

- Gestão de Produtos
- Gestão de Clientes
- Gestão de Funcionários
- Gestão de Fornecedores
- Gestão de Vendas
- Relatórios
- Interface responsiva
- Tema claro/escuro

## Integração com Backend

O frontend se comunica com o backend através da API REST em `http://localhost:8080/api`.

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes. 