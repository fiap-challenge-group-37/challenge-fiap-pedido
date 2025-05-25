package com.fiap.challenge.pedido.application.exception;

// Não é necessário @ResponseStatus aqui, pois será tratado pelo GlobalRestExceptionHandler
public class PedidoNaoEncontradoException extends RuntimeException {

    /**
     * Construtor que aceita uma mensagem de erro.
     * @param message A mensagem detalhando a exceção.
     */
    public PedidoNaoEncontradoException(String message) {
        super(message); // Chama o construtor da superclasse (RuntimeException) com a mensagem
    }

    /**
     * Construtor que aceita uma mensagem de erro e a causa original.
     * @param message A mensagem detalhando a exceção.
     * @param cause A causa original da exceção.
     */
    public PedidoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause); // Chama o construtor da superclasse com mensagem e causa
    }
}
