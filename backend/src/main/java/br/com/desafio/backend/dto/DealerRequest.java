package br.com.desafio.backend.dto;

public record DealerRequest(
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