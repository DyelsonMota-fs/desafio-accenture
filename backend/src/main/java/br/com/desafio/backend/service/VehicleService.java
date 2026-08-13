package br.com.desafio.backend.service;


import br.com.desafio.backend.dto.VehicleRequest;
import br.com.desafio.backend.dto.VehicleResponse;
import br.com.desafio.backend.entity.Vehicle;
import br.com.desafio.backend.repository.DealerRepository;
import br.com.desafio.backend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          DealerRepository dealerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.dealerRepository = dealerRepository;
    }

    public VehicleResponse create(VehicleRequest request) {

        Vehicle vehicle = new Vehicle();

        vehicle.setMarca(request.marca());
        vehicle.setModelo(request.modelo());
        vehicle.setTipoCombustivel(request.tipoCombustivel());
        vehicle.setCor(request.cor());

        if (request.dealerId() != null) {
            vehicle.setDealer(
                    dealerRepository.findById(request.dealerId())
                            .orElseThrow(() ->
                                    new RuntimeException("Concessionária não encontrada")
                            )
            );
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return toResponse(savedVehicle);
    }

    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getMarca(),
                vehicle.getModelo(),
                vehicle.getTipoCombustivel(),
                vehicle.getCor(),
                vehicle.getDealer() != null ? vehicle.getDealer().getId() : null
        );
    }

    public VehicleResponse findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        return toResponse(vehicle);
    }

    public VehicleResponse update(Long id, VehicleRequest request) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        vehicle.setMarca(request.marca());
        vehicle.setModelo(request.modelo());
        vehicle.setTipoCombustivel(request.tipoCombustivel());
        vehicle.setCor(request.cor());

        if (request.dealerId() != null) {
            vehicle.setDealer(
                    dealerRepository.findById(request.dealerId())
                            .orElseThrow(() ->
                                    new RuntimeException("Concessionária não encontrada")
                            )
            );
        } else {
            vehicle.setDealer(null);
        }

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return toResponse(updatedVehicle);
    }

    public void delete(Long id) {

        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado");
        }

        vehicleRepository.deleteById(id);
    }
}