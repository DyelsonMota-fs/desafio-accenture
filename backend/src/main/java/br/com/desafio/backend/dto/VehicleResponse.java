package br.com.desafio.backend.dto;

import br.com.desafio.backend.entity.TipoCombustivel;

import java.math.BigDecimal;

public record VehicleResponse(
        Long id,
        String marca,
        String modelo,
        TipoCombustivel tipoCombustivel,
        String cor,
        Integer ano,
        String chassi,
        BigDecimal valor,
        String imagemUrl,
        Long dealerId
) {
}