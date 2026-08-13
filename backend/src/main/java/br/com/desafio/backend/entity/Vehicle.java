package br.com.desafio.backend.entity;

import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;

    @Enumerated(EnumType.STRING)
    private TipoCombustivel tipoCombustivel;

    private String cor;

    @ManyToOne
    private Dealer dealer;

}
