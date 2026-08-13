package br.com.desafio.backend.service;

import br.com.desafio.backend.dto.VehicleRequest;
import br.com.desafio.backend.dto.VehicleResponse;
import br.com.desafio.backend.entity.Dealer;
import br.com.desafio.backend.entity.TipoCombustivel;
import br.com.desafio.backend.entity.Vehicle;
import br.com.desafio.backend.exception.ResourceNotFoundException;
import br.com.desafio.backend.repository.DealerRepository;
import br.com.desafio.backend.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DealerRepository dealerRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Dealer dealer;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {

        dealer = new Dealer();
        dealer.setRazaoSocial("Concessionária Teste");
        dealer.setCnpj("11.222.333/0001-81");

        vehicle = new Vehicle();
        vehicle.setMarca("Toyota");
        vehicle.setModelo("Corolla");
        vehicle.setTipoCombustivel(TipoCombustivel.FLEX);
        vehicle.setCor("Preto");
        vehicle.setDealer(dealer);
    }

    @Test
    void deveCriarVeiculo() {

        VehicleRequest request = new VehicleRequest(
                "Toyota",
                "Corolla",
                TipoCombustivel.FLEX,
                "Preto",
                1L
        );

        when(dealerRepository.findById(1L))
                .thenReturn(Optional.of(dealer));

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(vehicle);

        VehicleResponse response = vehicleService.create(request);

        assertNotNull(response);
        assertEquals("Toyota", response.marca());
        assertEquals("Corolla", response.modelo());
        assertEquals(TipoCombustivel.FLEX, response.tipoCombustivel());
        assertEquals("Preto", response.cor());

        verify(dealerRepository).findById(1L);
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void deveBuscarVeiculoPorId() {

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        VehicleResponse response = vehicleService.findById(1L);

        assertNotNull(response);
        assertEquals("Toyota", response.marca());
        assertEquals("Corolla", response.modelo());

        verify(vehicleRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoVeiculoNaoExistir() {

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService.findById(1L)
        );

        verify(vehicleRepository).findById(1L);
    }

    @Test
    void deveListarTodosOsVeiculos() {

        when(vehicleRepository.findAll())
                .thenReturn(List.of(vehicle));

        List<VehicleResponse> response = vehicleService.findAll();

        assertEquals(1, response.size());
        assertEquals("Toyota", response.get(0).marca());

        verify(vehicleRepository).findAll();
    }

    @Test
    void deveAtualizarVeiculo() {

        VehicleRequest request = new VehicleRequest(
                "Honda",
                "Civic",
                TipoCombustivel.FLEX,
                "Branco",
                1L
        );

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(dealerRepository.findById(1L))
                .thenReturn(Optional.of(dealer));

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(vehicle);

        VehicleResponse response =
                vehicleService.update(1L, request);

        assertNotNull(response);
        assertEquals("Honda", vehicle.getMarca());
        assertEquals("Civic", vehicle.getModelo());
        assertEquals("Branco", vehicle.getCor());

        verify(vehicleRepository).findById(1L);
        verify(dealerRepository).findById(1L);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void deveExcluirVeiculo() {

        when(vehicleRepository.existsById(1L))
                .thenReturn(true);

        vehicleService.delete(1L);

        verify(vehicleRepository).existsById(1L);
        verify(vehicleRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoExcluirVeiculoInexistente() {

        when(vehicleRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService.delete(1L)
        );

        verify(vehicleRepository).existsById(1L);
        verify(vehicleRepository, never()).deleteById(1L);
    }

    @Test
    void deveListarVeiculosPorConcessionaria() {

        when(dealerRepository.existsById(1L))
                .thenReturn(true);

        when(vehicleRepository.findByDealerId(1L))
                .thenReturn(List.of(vehicle));

        List<VehicleResponse> response =
                vehicleService.findByDealerId(1L);

        assertEquals(1, response.size());
        assertEquals("Toyota", response.get(0).marca());

        verify(dealerRepository).existsById(1L);
        verify(vehicleRepository).findByDealerId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoConcessionariaNaoExistir() {

        when(dealerRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService.findByDealerId(1L)
        );

        verify(dealerRepository).existsById(1L);
        verify(vehicleRepository, never())
                .findByDealerId(1L);
    }
}