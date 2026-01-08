# language: pt
Funcionalidade: Gestão de Pedidos
  Como um cliente da lanchonete
  Eu quero criar e acompanhar meus pedidos
  Para que eu possa receber minha comida

  Cenario: Criar um pedido com sucesso
    Dado que existem produtos disponíveis
    Quando eu criar um pedido com 2 itens
    Entao o pedido deve ser criado com sucesso
    E o status do pedido deve ser "RECEBIDO"

  Cenario: Atualizar status do pedido
    Dado que existe um pedido com status "RECEBIDO"
    Quando eu atualizar o status para "EM_PREPARACAO"
    Entao o status do pedido deve ser "EM_PREPARACAO"

  Cenario: Listar pedidos na fila
    Dado que existem 3 pedidos não finalizados
    Quando eu listar os pedidos
    Entao devo receber uma lista com pelo menos 3 pedidos