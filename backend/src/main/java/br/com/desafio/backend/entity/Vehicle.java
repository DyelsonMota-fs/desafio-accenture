package br.com.desafio.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

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

    private Integer ano;

    @Column(length = 17)
    private String chassi;

    @Column(precision = 12, scale = 2)
    private BigDecimal valor;

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

    public Integer getAno() {
        return ano;
    }

    public String getChassi() {
        return chassi;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getImagemUrl() {
        return imagemUrl;
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

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }
}