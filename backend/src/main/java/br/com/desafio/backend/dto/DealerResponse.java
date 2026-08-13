package br.com.desafio.backend.dto;

public record DealerResponse(
        Long id,
        String razaoSocial,
        String cnpj,
        String endereco
) {
}