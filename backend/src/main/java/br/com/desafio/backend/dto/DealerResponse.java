package br.com.desafio.backend.dto;

public record DealerResponse(
        Long id,
        String razaoSocial,
        String cnpj,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
) {
}