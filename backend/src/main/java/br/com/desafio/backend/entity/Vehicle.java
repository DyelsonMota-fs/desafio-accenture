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

    private String imagemUrl;

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

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

}
