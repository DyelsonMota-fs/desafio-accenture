package br.com.desafio.backend.dto;

public record DealerRequest(
        String razaoSocial,
        String cnpj,
        String endereco
) {
}