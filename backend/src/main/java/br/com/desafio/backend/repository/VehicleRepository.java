package br.com.desafio.backend.repository;

import br.com.desafio.backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByDealerId(Long dealerId);


}