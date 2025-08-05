# Tech Challenge – Sistema de Autoatendimento para Lanchonete

## Descrição

Este projeto foi desenvolvido como parte do **Tech Challenge da Fase 01**, com o objetivo de aplicar os conhecimentos adquiridos nas disciplinas do curso em um sistema backend completo. A proposta consiste em criar uma solução de autoatendimento para uma lanchonete em expansão, otimizando o fluxo de pedidos — desde a escolha dos produtos até a entrega ao cliente —, incluindo um painel administrativo para gestão do negócio.

## Objetivo

Implementar um sistema de autoatendimento para fast food que permita:
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
- **Orquestração Local:** Kubernetes (Kind)

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
> - (Opcional) Para rodar no Kubernetes, ter o [Kind](https://kind.sigs.k8s.io/) instalado (recomendado).

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

## Executando com Kubernetes Local (Kind RECOMENDADO)

> **Recomendado para padronizar ambientes de teste.**

### 1. Crie o cluster Kind com mapeamento de porta para NodePort:

Crie o cluster:

```bash
kind create cluster --name desafio-fiap --config kind-config.yaml
```

### 2. Construa a imagem Docker e carregue para o Kind:

```bash
docker build -t lanchonete_app:latest -f docker/Dockerfile .
kind load docker-image lanchonete_app:latest --name desafio-fiap
```

### 3. Aplique os manifests do Kubernetes:

```bash
kubectl apply -f k8s/
```

### 4. Acesse a aplicação

- [http://localhost:30080/api/](http://localhost:30080/api/)

Se preferir usar port-forward:

```bash
kubectl port-forward svc/lanchonete-api-service 8080:80
```
Acesse: [http://localhost:8080/api/](http://localhost:8080/api/)

> **Se a porta 8080 estiver ocupada, use por exemplo:**
> `kubectl port-forward svc/lanchonete-api-service 8081:80`  
> E acesse [http://localhost:8081/api/](http://localhost:8081/api/)

### 5. Para remover o cluster Kind ao finalizar:

```bash
kind delete cluster --name desafio-fiap
```

---

## Recursos Adicionais

- **Segurança:** A API está protegida por API Key via Spring Security.
- **Documentação:** A API está documentada via Swagger.

---

## Dicas

- Caso não consiga acessar a aplicação no Kind, verifique se o serviço está disponível rodando:
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
  [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

- **Kubernetes/Kind:**  
  [http://localhost:30080/api/swagger-ui/index.html](http://localhost:30080/api/swagger-ui/index.html)

---