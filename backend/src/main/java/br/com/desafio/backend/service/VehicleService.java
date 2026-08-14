package br.com.desafio.backend.service;

import br.com.desafio.backend.dto.VehicleRequest;
import br.com.desafio.backend.dto.VehicleResponse;
import br.com.desafio.backend.entity.Dealer;
import br.com.desafio.backend.entity.Vehicle;
import br.com.desafio.backend.exception.ResourceNotFoundException;
import br.com.desafio.backend.repository.DealerRepository;
import br.com.desafio.backend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;

    public VehicleService(
            VehicleRepository vehicleRepository,
            DealerRepository dealerRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.dealerRepository = dealerRepository;
    }

    public VehicleResponse create(VehicleRequest request) {

        Vehicle vehicle = new Vehicle();

        applyRequestToVehicle(vehicle, request);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return toResponse(savedVehicle);
    }

    public List<VehicleResponse> findAll() {

        return vehicleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VehicleResponse findById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Veículo não encontrado")
                );

        return toResponse(vehicle);
    }

    public VehicleResponse update(Long id, VehicleRequest request) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Veículo não encontrado")
                );

        applyRequestToVehicle(vehicle, request);

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return toResponse(updatedVehicle);
    }

    public void delete(Long id) {

        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo não encontrado");
        }

        vehicleRepository.deleteById(id);
    }

    public List<VehicleResponse> findByDealerId(Long dealerId) {

        if (!dealerRepository.existsById(dealerId)) {
            throw new ResourceNotFoundException(
                    "Concessionária não encontrada"
            );
        }

        return vehicleRepository.findByDealerId(dealerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void applyRequestToVehicle(
            Vehicle vehicle,
            VehicleRequest request
    ) {

        vehicle.setMarca(request.marca());
        vehicle.setModelo(request.modelo());
        vehicle.setTipoCombustivel(request.tipoCombustivel());
        vehicle.setCor(request.cor());

        vehicle.setAno(request.ano());
        vehicle.setChassi(request.chassi());
        vehicle.setValor(request.valor());
        vehicle.setImagemUrl(request.imagemUrl());

        vehicle.setDealer(resolveDealer(request.dealerId()));
    }

    private Dealer resolveDealer(Long dealerId) {

        if (dealerId == null) {
            return null;
        }

        return dealerRepository.findById(dealerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Concessionária não encontrada"
                        )
                );
    }

    private VehicleResponse toResponse(Vehicle vehicle) {

        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getMarca(),
                vehicle.getModelo(),
                vehicle.getTipoCombustivel(),
                vehicle.getCor(),
                vehicle.getAno(),
                vehicle.getChassi(),
                vehicle.getValor(),
                vehicle.getImagemUrl(),
                vehicle.getDealer() != null
                        ? vehicle.getDealer().getId()
                        : null
        );
    }
}