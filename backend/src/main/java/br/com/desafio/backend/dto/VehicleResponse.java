package br.com.desafio.backend.dto;

import br.com.desafio.backend.entity.TipoCombustivel;

public record VehicleResponse(
        Long id,
        String marca,
        String modelo,
        TipoCombustivel tipoCombustivel,
        String cor,
        String imagemUrl,
        Long dealerId
) {
}