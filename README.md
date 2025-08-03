# Tech Challenge – Sistema de Autoatendimento para Lanchonete

## Descrição

Este projeto foi desenvolvido como parte do **Tech Challenge da Fase 01**, com o objetivo de aplicar os conhecimentos adquiridos nas disciplinas do curso em um sistema backend completo. A proposta consiste em criar uma solução de autoatendimento para uma lanchonete em expansão, otimizando o fluxo de pedidos, desde a escolha dos produtos até a entrega ao cliente, com um painel administrativo para gestão do negócio.

## Objetivo

Implementar um sistema de autoatendimento de fast food que permita:
- Realização e acompanhamento de pedidos pelos clientes;
- Gestão de produtos, categorias e clientes por administradores;
- Monitoramento da preparação e entrega dos pedidos pela cozinha.

---

## Tecnologias Utilizadas

- **Linguagem:** Java
- **Framework:** Spring Boot
- **Arquitetura:** Hexagonal
- **Documentação de API:** Swagger
- **Gerenciamento de Dependências:** Maven
- **Banco de Dados:** MySQL
- **Containers:** Docker e Docker Compose
- **Orquestração Local:** Kubernetes (Minikube)

---

## Funcionalidades

### 🧾 Cliente
- Cadastro e identificação via CPF
- Montagem de pedido personalizado com:
  - Lanche
  - Acompanhamento
  - Bebida
  - Sobremesa
- Pagamento via QR Code do Mercado Pago (fake checkout)
- Acompanhamento do status do pedido:
  - Recebido
  - Em preparação
  - Pronto
  - Finalizado

### 🛠️ Administrativo
- Gestão de produtos (CRUD)
- Gestão de categorias fixas
- Gestão de clientes
- Acompanhamento de pedidos em tempo real

---

## Instalação do Projeto

> **Pré-requisitos**:
> - Ter o Docker e o Docker Compose instalados na máquina.
> - (Opcional) Para rodar no Kubernetes, ter o [Minikube](https://minikube.sigs.k8s.io/) instalado.

---

### 1. Clonar o Repositório

```bash
git clone git@github.com:samuelvinib/challenge-fiap.git
cd challenge-fiap
```

---

## Executando com Docker Compose

### Ambiente de Desenvolvimento

Permite hot-reload do código Java, facilitando testes e ajustes rápidos.

1. **Suba os containers em modo desenvolvimento (default):**

   ```bash
   docker compose up -d --build
   ```

2. **Acesse a aplicação:**

   [http://localhost:8080](http://localhost:8080)

---

### Ambiente de Produção

No ambiente de produção, a imagem é otimizada usando multi-stage build, sem incluir código-fonte local e sem ferramentas de desenvolvimento.

1. **Suba os containers para produção:**

   ```bash
   docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
   ```

2. **Acesse a aplicação:**

   [http://localhost:8080](http://localhost:8080)

---

## Executando com Minikube (Kubernetes Local)

> **Pré-requisitos**: Ter o Minikube instalado e em execução.

1. **Inicie o Minikube (caso ainda não esteja rodando):**

   ```bash
   minikube start
   ```

2. **Aplique os manifests do Kubernetes:**

   ```bash
   kubectl apply -f k8s/
   ```

3. **Descubra o IP do Minikube:**

   ```bash
   minikube ip
   ```

   > Exemplo de saída: `192.168.49.2`

4. **Acesse a aplicação pelo navegador:**

   ```
   http://<IP-DO-MINIKUBE>:30080/api/
   ```
   > Exemplo: [http://192.168.49.2:30080/api/](http://192.168.49.2:30080/api/)

5. **Acesse a documentação da API (Swagger UI):**

   ```
   http://<IP-DO-MINIKUBE>:30080/api/
   ```
   > Exemplo: [http://192.168.49.2:30080/api/](http://192.168.49.2:30080/api/)

---

## Recursos Adicionais

- **Segurança:** A API está protegida por API Key via Spring Security.
- **Documentação:** A API está documentada via Swagger.

---

## Dicas

- Caso não consiga acessar a aplicação no Minikube, verifique se o serviço está disponível rodando:
  ```bash
  kubectl get svc
  ```
  E confirme que o serviço `lanchonete-api-service` está com um `NodePort` (ex: 30080).
- Se tiver problemas de conexão, confira o selector do service e os endpoints com:
  ```bash
  kubectl get endpoints lanchonete-api-service
  ```
- Para acessar localmente via port-forward:
  ```bash
  kubectl port-forward svc/lanchonete-api-service 8080:80
  ```
  E acesse: [http://localhost:8080/api/](http://localhost:8080/api/)

---

## Documentação da API

Após iniciar a aplicação, acesse a documentação no navegador:

- **Docker Compose:**  
  [http://localhost:8080/api/](http://localhost:8080/api/)

- **Kubernetes/Minikube:**  
  [http://<IP-DO-MINIKUBE>:30080/api/](http://192.168.49.2:30080/api/)

---