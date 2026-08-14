package br.com.desafio.backend.dto;

import br.com.desafio.backend.entity.TipoCombustivel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record VehicleRequest(

        @NotBlank(message = "Marca é obrigatória")
        String marca,

        @NotBlank(message = "Modelo é obrigatório")
        String modelo,

        @NotNull(message = "Tipo de combustível é obrigatório")
        TipoCombustivel tipoCombustivel,

        @NotBlank(message = "Cor é obrigatória")
        String cor,

        @Min(value = 1886, message = "Ano do veículo inválido")
        Integer ano,

        @Size(max = 17, message = "Chassi deve possuir no máximo 17 caracteres")
        String chassi,

        @Positive(message = "Valor deve ser maior que zero")
        BigDecimal valor,

        String imagemUrl,

        Long dealerId
) {
}