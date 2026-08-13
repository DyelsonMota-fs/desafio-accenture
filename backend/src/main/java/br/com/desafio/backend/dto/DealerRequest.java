package br.com.desafio.backend.dto;

import br.com.desafio.backend.validation.Cep;
import br.com.desafio.backend.validation.Cnpj;
import jakarta.validation.constraints.NotBlank;

public record DealerRequest(

        @NotBlank(message = "Razão social é obrigatória")
        String razaoSocial,

        @NotBlank(message = "CNPJ é obrigatório")
        @Cnpj
        String cnpj,

        @NotBlank(message = "CEP é obrigatório")
        @Cep
        String cep,

        String logradouro,

        @NotBlank(message = "Número é obrigatório")
        String numero,

        String complemento,

        String bairro,

        String cidade,

        String estado
) {
}