package br.com.desafio.backend.dto;

import br.com.desafio.backend.entity.TipoCombustivel;

public record VehicleRequest(
        String marca,
        String modelo,
        TipoCombustivel tipoCombustivel,
        String cor,
        Long dealerId
) {
}