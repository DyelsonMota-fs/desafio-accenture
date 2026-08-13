package br.com.desafio.backend.controller;

import br.com.desafio.backend.dto.DealerRequest;
import br.com.desafio.backend.dto.DealerResponse;
import br.com.desafio.backend.dto.VehicleResponse;
import br.com.desafio.backend.entity.Vehicle;
import br.com.desafio.backend.service.DealerService;
import br.com.desafio.backend.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer")
@Tag(
        name = "Dealers",
        description = "Operações de gerenciamento de concessionárias"
)
public class DealerController {

    private final DealerService dealerService;
    private final VehicleService vehicleService;

    public DealerController(DealerService dealerService, VehicleService vehiclService) {
        this.dealerService = dealerService;
        this.vehicleService = vehiclService;
    }

    @Operation(
            summary = "Lista todas as concessionárias",
            description = "Retorna todas as concessionárias cadastradas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Concessionárias encontradas com sucesso"
            )
    })
    @GetMapping
    public List<DealerResponse> findAll() {
        return dealerService.findAll();
    }

    @Operation(
            summary = "Busca uma concessionária por ID",
            description = "Retorna os dados de uma concessionária específica."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Concessionária encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Concessionária não encontrada"
            )
    })
    @GetMapping("/{id}")
    public DealerResponse findById(@PathVariable Long id) {
        return dealerService.findById(id);
    }

    @Operation(
            summary = "Cadastra uma concessionária",
            description = "Cadastra uma nova concessionária e consulta automaticamente o endereço através do ViaCEP."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Concessionária cadastrada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<DealerResponse> create(
            @Valid @RequestBody DealerRequest request
    ) {
        DealerResponse response = dealerService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Atualiza uma concessionária",
            description = "Atualiza os dados de uma concessionária existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Concessionária atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Concessionária não encontrada"
            )
    })
    @PutMapping("/{id}")
    public DealerResponse update(
            @PathVariable Long id,
            @Valid
            @RequestBody DealerRequest request
    ) {
        return dealerService.update(id, request);
    }

    @Operation(
            summary = "Exclui uma concessionária",
            description = "Remove uma concessionária pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Concessionária excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Concessionária não encontrada"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dealerService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Lista veículos de uma concessionária",
            description = "Retorna todos os veículos associados à concessionária informada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Veículos encontrados com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Concessionária não encontrada"
            )
    })
    @GetMapping("/{id}/vehicles")
    public List<VehicleResponse> findVehicles(@PathVariable Long id) {
        return vehicleService.findByDealerId(id);
    }
}