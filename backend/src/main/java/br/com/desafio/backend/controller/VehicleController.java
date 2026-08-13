package br.com.desafio.backend.controller;

import br.com.desafio.backend.dto.VehicleRequest;
import br.com.desafio.backend.dto.VehicleResponse;
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
@RequestMapping("/vehicles")
@Tag(
        name = "Vehicles",
        description = "Operações de gerenciamento de veículos"
)
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(
            summary = "Lista todos os veículos",
            description = "Retorna todos os veículos cadastrados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Veículos encontrados com sucesso"
            )
    })
    @GetMapping
    public List<VehicleResponse> findAll() {
        return vehicleService.findAll();
    }

    @Operation(
            summary = "Busca um veículo por ID",
            description = "Retorna os dados de um veículo específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Veículo encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo não encontrado"
            )
    })
    @GetMapping("/{id}")
    public VehicleResponse findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @Operation(
            summary = "Cadastra um veículo",
            description = "Cria um novo veículo e opcionalmente associa a uma concessionária."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Veículo cadastrado com sucesso"
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
    @PostMapping
    public ResponseEntity<VehicleResponse> create(
            @Valid @RequestBody VehicleRequest request
    ) {
        VehicleResponse response = vehicleService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Atualiza um veículo",
            description = "Atualiza os dados de um veículo existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Veículo atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo ou concessionária não encontrada"
            )
    })
    @PutMapping("/{id}")
    public VehicleResponse update(
            @PathVariable Long id,
            @Valid
            @RequestBody VehicleRequest request
    ) {
        return vehicleService.update(id, request);
    }

    @Operation(
            summary = "Exclui um veículo",
            description = "Remove um veículo cadastrado pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Veículo excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo não encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);

        return ResponseEntity.noContent().build();
    }
}