package br.com.desafio.backend.dto;

import br.com.desafio.backend.entity.TipoCombustivel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleRequest(

        @NotBlank(message = "Marca é obrigatória")
        String marca,

        @NotBlank(message = "Modelo é obrigatório")
        String modelo,

        @NotNull(message = "Tipo de combustível é obrigatório")
        TipoCombustivel tipoCombustivel,

        @NotBlank(message = "Cor é obrigatória")
        String cor,

        Long dealerId
) {
}