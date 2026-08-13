package br.com.desafio.backend.repository;

import br.com.desafio.backend.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealerRepository extends JpaRepository<Dealer, Long> {
}