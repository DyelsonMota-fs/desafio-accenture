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


    public Long getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public TipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public String getCor() {
        return cor;
    }

    public Dealer getDealer() {
        return dealer;
    }

}
