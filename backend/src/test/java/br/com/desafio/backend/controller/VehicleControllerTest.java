package br.com.desafio.backend.controller;

import br.com.desafio.backend.dto.VehicleRequest;
import br.com.desafio.backend.dto.VehicleResponse;
import br.com.desafio.backend.entity.TipoCombustivel;
import br.com.desafio.backend.exception.ResourceNotFoundException;
import br.com.desafio.backend.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @Test
    void deveListarVeiculos() throws Exception {

        VehicleResponse response = new VehicleResponse(
                1L,
                "Toyota",
                "Corolla",
                TipoCombustivel.FLEX,
                "Preto",
                null
        );

        when(vehicleService.findAll())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk());

        verify(vehicleService).findAll();
    }

    @Test
    void deveBuscarVeiculoPorId() throws Exception {

        VehicleResponse response = new VehicleResponse(
                1L,
                "Toyota",
                "Corolla",
                TipoCombustivel.FLEX,
                "Preto",
                null
        );

        when(vehicleService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk());

        verify(vehicleService).findById(1L);
    }

    @Test
    void deveRetornar404QuandoVeiculoNaoExistir() throws Exception {

        when(vehicleService.findById(1L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Veículo não encontrado"
                        )
                );

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isNotFound());

        verify(vehicleService).findById(1L);
    }

    @Test
    void deveCriarVeiculo() throws Exception {

        VehicleResponse response = new VehicleResponse(
                1L,
                "Toyota",
                "Corolla",
                TipoCombustivel.FLEX,
                "Preto",
                null
        );

        when(vehicleService.create(any(VehicleRequest.class)))
                .thenReturn(response);

        String json = """
                {
                    "marca": "Toyota",
                    "modelo": "Corolla",
                    "tipoCombustivel": "FLEX",
                    "cor": "Preto"
                }
                """;

        mockMvc.perform(
                        post("/vehicles")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());

        verify(vehicleService)
                .create(any(VehicleRequest.class));
    }

    @Test
    void deveRetornar400QuandoDadosForemInvalidos() throws Exception {

        String json = """
                {
                    "marca": "",
                    "modelo": "",
                    "tipoCombustivel": null,
                    "cor": ""
                }
                """;

        mockMvc.perform(
                        post("/vehicles")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());

        verify(vehicleService, never())
                .create(any(VehicleRequest.class));
    }

    @Test
    void deveAtualizarVeiculo() throws Exception {

        VehicleResponse response = new VehicleResponse(
                1L,
                "Honda",
                "Civic",
                TipoCombustivel.FLEX,
                "Branco",
                null
        );

        when(vehicleService.update(
                eq(1L),
                any(VehicleRequest.class)
        )).thenReturn(response);

        String json = """
                {
                    "marca": "Honda",
                    "modelo": "Civic",
                    "tipoCombustivel": "FLEX",
                    "cor": "Branco"
                }
                """;

        mockMvc.perform(
                        put("/vehicles/1")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());

        verify(vehicleService)
                .update(eq(1L), any(VehicleRequest.class));
    }

    @Test
    void deveExcluirVeiculo() throws Exception {

        doNothing()
                .when(vehicleService)
                .delete(1L);

        mockMvc.perform(delete("/vehicles/1"))
                .andExpect(status().isNoContent());

        verify(vehicleService).delete(1L);
    }
}