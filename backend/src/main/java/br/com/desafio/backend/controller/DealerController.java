package br.com.desafio.backend.controller;

import br.com.desafio.backend.dto.DealerRequest;
import br.com.desafio.backend.dto.DealerResponse;
import br.com.desafio.backend.service.DealerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer")
public class DealerController {

    private final DealerService dealerService;

    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
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
}