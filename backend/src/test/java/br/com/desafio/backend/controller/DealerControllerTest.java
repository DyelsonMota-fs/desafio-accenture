package br.com.desafio.backend.controller;

import br.com.desafio.backend.dto.DealerRequest;
import br.com.desafio.backend.dto.DealerResponse;
import br.com.desafio.backend.dto.VehicleResponse;
import br.com.desafio.backend.entity.TipoCombustivel;
import br.com.desafio.backend.exception.ResourceNotFoundException;
import br.com.desafio.backend.service.DealerService;
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

@WebMvcTest(DealerController.class)
class DealerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DealerService dealerService;

    @MockitoBean
    private VehicleService vehicleService;

    @Test
    void deveListarConcessionarias() throws Exception {

        DealerResponse response = new DealerResponse(
                1L,
                "Concessionária Teste",
                "11.222.333/0001-81",
                "01001-000",
                "Praça da Sé",
                "100",
                null,
                "Sé",
                "São Paulo",
                "SP"
        );

        when(dealerService.findAll())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/dealer"))
                .andExpect(status().isOk());

        verify(dealerService).findAll();
    }

    @Test
    void deveBuscarConcessionariaPorId() throws Exception {

        DealerResponse response = new DealerResponse(
                1L,
                "Concessionária Teste",
                "11.222.333/0001-81",
                "01001-000",
                "Praça da Sé",
                "100",
                null,
                "Sé",
                "São Paulo",
                "SP"
        );

        when(dealerService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/dealer/1"))
                .andExpect(status().isOk());

        verify(dealerService).findById(1L);
    }

    @Test
    void deveRetornar404QuandoConcessionariaNaoExistir()
            throws Exception {

        when(dealerService.findById(1L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Concessionária não encontrada"
                        )
                );

        mockMvc.perform(get("/dealer/1"))
                .andExpect(status().isNotFound());

        verify(dealerService).findById(1L);
    }

    @Test
    void deveCriarConcessionaria() throws Exception {

        DealerResponse response = new DealerResponse(
                1L,
                "Concessionária Teste",
                "11.222.333/0001-81",
                "01001-000",
                "Praça da Sé",
                "100",
                null,
                "Sé",
                "São Paulo",
                "SP"
        );

        when(dealerService.create(any(DealerRequest.class)))
                .thenReturn(response);

        String json = """
                {
                    "razaoSocial": "Concessionária Teste",
                    "cnpj": "11.222.333/0001-81",
                    "cep": "01001-000",
                    "numero": "100"
                }
                """;

        mockMvc.perform(
                        post("/dealer")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());

        verify(dealerService)
                .create(any(DealerRequest.class));
    }

    @Test
    void deveRetornar400QuandoDadosForemInvalidos()
            throws Exception {

        String json = """
                {
                    "razaoSocial": "",
                    "cnpj": "",
                    "cep": "",
                    "numero": ""
                }
                """;

        mockMvc.perform(
                        post("/dealer")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());

        verify(
                dealerService,
                never()
        ).create(any(DealerRequest.class));
    }

    @Test
    void deveAtualizarConcessionaria() throws Exception {

        DealerResponse response = new DealerResponse(
                1L,
                "Concessionária Atualizada",
                "11.222.333/0001-81",
                "01001-000",
                "Praça da Sé",
                "200",
                null,
                "Sé",
                "São Paulo",
                "SP"
        );

        when(dealerService.update(
                eq(1L),
                any(DealerRequest.class)
        )).thenReturn(response);

        String json = """
                {
                    "razaoSocial": "Concessionária Atualizada",
                    "cnpj": "11.222.333/0001-81",
                    "cep": "01001-000",
                    "numero": "200"
                }
                """;

        mockMvc.perform(
                        put("/dealer/1")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());

        verify(dealerService)
                .update(
                        eq(1L),
                        any(DealerRequest.class)
                );
    }

    @Test
    void deveExcluirConcessionaria() throws Exception {

        doNothing()
                .when(dealerService)
                .delete(1L);

        mockMvc.perform(delete("/dealer/1"))
                .andExpect(status().isNoContent());

        verify(dealerService).delete(1L);
    }

    @Test
    void deveListarVeiculosDaConcessionaria()
            throws Exception {

        VehicleResponse response = new VehicleResponse(
                1L,
                "Toyota",
                "Corolla",
                TipoCombustivel.FLEX,
                "Preto",
                1L
        );

        when(vehicleService.findByDealerId(1L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/dealer/1/vehicles"))
                .andExpect(status().isOk());

        verify(vehicleService)
                .findByDealerId(1L);
    }

    @Test
    void deveRetornar404QuandoConcessionariaNaoExistirAoBuscarVeiculos()
            throws Exception {

        when(vehicleService.findByDealerId(1L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Concessionária não encontrada"
                        )
                );

        mockMvc.perform(get("/dealer/1/vehicles"))
                .andExpect(status().isNotFound());

        verify(vehicleService)
                .findByDealerId(1L);
    }
}