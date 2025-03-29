# Sistema MetalTech

Sistema de gestão para metalúrgica desenvolvido com Spring Boot (backend) e Vue.js (frontend).

## Pré-requisitos

- Java JDK 17 ou superior
- Node.js 18 ou superior
- Maven
- MySQL 8.0 ou superior
- Git

## Configuração do Banco de Dados

1. Crie um banco de dados MySQL chamado `metaltech`
2. Execute o script SQL disponível em `src/main/resources/db/init.sql` para criar as tabelas necessárias

## Configuração do Backend

1. Navegue até a pasta do projeto:
```bash
cd metaltech
```

2. Configure as variáveis de ambiente no arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/metaltech
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

3. Compile o projeto com Maven:
```bash
mvn clean install
```

4. Execute o backend:
```bash
mvn spring-boot:run
```

O backend estará disponível em `http://localhost:8081`

## Configuração do Frontend

1. Navegue até a pasta do frontend:
```bash
cd frontVue
```

2. Instale as dependências:
```bash
npm install
```

3. Execute o servidor de desenvolvimento:
```bash
npm run dev
```

O frontend estará disponível em `http://localhost:5173`

## Estrutura do Projeto

### Backend
- `src/main/java/com/metaltech/`
  - `controller/`: Controladores REST
  - `model/`: Entidades do banco de dados
  - `repository/`: Repositórios JPA
  - `service/`: Lógica de negócios
  - `config/`: Configurações do Spring Boot

### Frontend
- `frontVue/`
  - `src/`
    - `components/`: Componentes Vue reutilizáveis
    - `views/`: Páginas da aplicação
    - `services/`: Serviços de API
    - `types/`: Definições de tipos TypeScript
    - `router/`: Configuração de rotas
    - `App.vue`: Componente principal
    - `main.ts`: Ponto de entrada da aplicação

## Funcionalidades

- Gestão de Produtos
- Gestão de Clientes
- Gestão de Funcionários
- Gestão de Fornecedores
- Gestão de Vendas
- Relatórios

## Desenvolvimento

### Backend
- Use o Spring Boot DevTools para hot-reload
- Os logs estão configurados em `src/main/resources/logback-spring.xml`
- As migrações do banco de dados são gerenciadas pelo Flyway

### Frontend
- Use o Vue DevTools para debug
- Os logs estão disponíveis no console do navegador
- O Vite fornece hot-reload durante o desenvolvimento

## Testes

### Backend
```bash
mvn test
```

### Frontend
```bash
npm run test
```

## Solução de Problemas

### CORS
Se encontrar erros de CORS, verifique:
1. Se o backend está rodando em `http://localhost:8081`
2. Se o frontend está rodando em `http://localhost:5173`
3. Se as configurações de CORS no `CorsConfig.java` estão corretas

### Banco de Dados
Se encontrar problemas com o banco de dados:
1. Verifique se o MySQL está rodando
2. Confirme as credenciais no `application.properties`
3. Verifique se o banco de dados foi criado corretamente

### Frontend
Se o frontend não se conectar ao backend:
1. Verifique se a URL base no `api.ts` está correta
2. Confirme se o backend está rodando
3. Verifique os logs no console do navegador

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Crie um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes. 