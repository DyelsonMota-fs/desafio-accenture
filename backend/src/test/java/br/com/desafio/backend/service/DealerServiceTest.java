package br.com.desafio.backend.service;

import br.com.desafio.backend.dto.DealerRequest;
import br.com.desafio.backend.dto.DealerResponse;
import br.com.desafio.backend.entity.Dealer;
import br.com.desafio.backend.exception.ResourceNotFoundException;
import br.com.desafio.backend.integration.viacep.ViaCepClient;
import br.com.desafio.backend.integration.viacep.ViaCepResponse;
import br.com.desafio.backend.repository.DealerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealerServiceTest {

    @Mock
    private DealerRepository dealerRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private DealerService dealerService;

    private Dealer dealer;
    private DealerRequest request;
    private ViaCepResponse viaCepResponse;

    @BeforeEach
    void setUp() {

        dealer = new Dealer();

        dealer.setRazaoSocial("Concessionária Teste");
        dealer.setCnpj("11.222.333/0001-81");
        dealer.setCep("01001-000");
        dealer.setLogradouro("Praça da Sé");
        dealer.setNumero("100");
        dealer.setComplemento("Sala 1");
        dealer.setBairro("Sé");
        dealer.setCidade("São Paulo");
        dealer.setEstado("SP");

        request = new DealerRequest(
                "Concessionária Teste",
                "11.222.333/0001-81",
                "01001-000",
                null,
                "100",
                null,
                null,
                null,
                null
        );

        viaCepResponse = new ViaCepResponse();

        viaCepResponse.setCep("01001-000");
        viaCepResponse.setLogradouro("Praça da Sé");
        viaCepResponse.setComplemento("lado ímpar");
        viaCepResponse.setBairro("Sé");
        viaCepResponse.setLocalidade("São Paulo");
        viaCepResponse.setUf("SP");
        viaCepResponse.setErro(false);
    }

    @Test
    void deveListarTodasAsConcessionarias() {

        when(dealerRepository.findAll())
                .thenReturn(List.of(dealer));

        List<DealerResponse> response =
                dealerService.findAll();

        assertEquals(1, response.size());
        assertEquals(
                "Concessionária Teste",
                response.get(0).razaoSocial()
        );

        verify(dealerRepository).findAll();
    }

    @Test
    void deveBuscarConcessionariaPorId() {

        when(dealerRepository.findById(1L))
                .thenReturn(Optional.of(dealer));

        DealerResponse response =
                dealerService.findById(1L);

        assertNotNull(response);
        assertEquals(
                "Concessionária Teste",
                response.razaoSocial()
        );
        assertEquals(
                "01001-000",
                response.cep()
        );

        verify(dealerRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoConcessionariaNaoExistir() {

        when(dealerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> dealerService.findById(1L)
        );

        verify(dealerRepository).findById(1L);
    }

    @Test
    void deveCriarConcessionariaConsultandoViaCep() {

        when(viaCepClient.buscarCep("01001-000"))
                .thenReturn(viaCepResponse);

        when(dealerRepository.save(any(Dealer.class)))
                .thenReturn(dealer);

        DealerResponse response =
                dealerService.create(request);

        assertNotNull(response);

        assertEquals(
                "Concessionária Teste",
                response.razaoSocial()
        );

        assertEquals(
                "Praça da Sé",
                response.logradouro()
        );

        assertEquals(
                "Sé",
                response.bairro()
        );

        assertEquals(
                "São Paulo",
                response.cidade()
        );

        assertEquals(
                "SP",
                response.estado()
        );

        verify(viaCepClient).buscarCep("01001-000");
        verify(dealerRepository).save(any(Dealer.class));
    }

    @Test
    void deveLancarExcecaoQuandoCepNaoForEncontrado() {

        viaCepResponse.setErro(true);

        when(viaCepClient.buscarCep("01001-000"))
                .thenReturn(viaCepResponse);

        assertThrows(
                ResourceNotFoundException.class,
                () -> dealerService.create(request)
        );

        verify(viaCepClient).buscarCep("01001-000");

        verify(
                dealerRepository,
                never()
        ).save(any(Dealer.class));
    }

    @Test
    void deveAtualizarConcessionaria() {

        when(dealerRepository.findById(1L))
                .thenReturn(Optional.of(dealer));

        when(dealerRepository.save(any(Dealer.class)))
                .thenReturn(dealer);

        DealerResponse response =
                dealerService.update(1L, request);

        assertNotNull(response);

        assertEquals(
                "Concessionária Teste",
                dealer.getRazaoSocial()
        );

        assertEquals(
                "11.222.333/0001-81",
                dealer.getCnpj()
        );

        verify(dealerRepository).findById(1L);
        verify(dealerRepository).save(dealer);
    }

    @Test
    void deveLancarExcecaoAoAtualizarConcessionariaInexistente() {

        when(dealerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> dealerService.update(1L, request)
        );

        verify(dealerRepository).findById(1L);

        verify(
                dealerRepository,
                never()
        ).save(any(Dealer.class));
    }

    @Test
    void deveExcluirConcessionaria() {

        when(dealerRepository.existsById(1L))
                .thenReturn(true);

        dealerService.delete(1L);

        verify(dealerRepository).existsById(1L);
        verify(dealerRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoExcluirConcessionariaInexistente() {

        when(dealerRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> dealerService.delete(1L)
        );

        verify(dealerRepository).existsById(1L);

        verify(
                dealerRepository,
                never()
        ).deleteById(1L);
    }
}