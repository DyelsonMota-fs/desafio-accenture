package br.com.desafio.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Dealer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String endereco;

    @OneToMany(mappedBy = "dealer")
    private List<Vehicle> vehicles;

}
