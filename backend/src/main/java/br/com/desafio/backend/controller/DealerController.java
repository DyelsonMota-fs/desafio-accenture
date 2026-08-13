package br.com.desafio.backend.controller;

import br.com.desafio.backend.dto.DealerRequest;
import br.com.desafio.backend.dto.DealerResponse;
import br.com.desafio.backend.dto.VehicleResponse;
import br.com.desafio.backend.entity.Vehicle;
import br.com.desafio.backend.service.DealerService;
import br.com.desafio.backend.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer")
public class DealerController {

    private final DealerService dealerService;
    private final VehicleService vehicleService;

    public DealerController(DealerService dealerService, VehicleService vehiclService) {
        this.dealerService = dealerService;
        this.vehicleService = vehiclService;
    }

    @GetMapping
    public List<DealerResponse> findAll() {
        return dealerService.findAll();
    }

    @GetMapping("/{id}")
    public DealerResponse findById(@PathVariable Long id) {
        return dealerService.findById(id);
    }

    @PostMapping
    public DealerResponse create(@RequestBody DealerRequest request) {
        return dealerService.create(request);
    }

    @PutMapping("/{id}")
    public DealerResponse update(
            @PathVariable Long id,
            @RequestBody DealerRequest request
    ) {
        return dealerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        dealerService.delete(id);
    }

    @GetMapping("/{id}/vehicles")
    public List<VehicleResponse> findVehicles(@PathVariable Long id) {
        return vehicleService.findByDealerId(id);
    }
}