package br.com.desafio.backend.service;

import br.com.desafio.backend.dto.DealerRequest;
import br.com.desafio.backend.dto.DealerResponse;
import br.com.desafio.backend.entity.Dealer;
import br.com.desafio.backend.exception.ResourceNotFoundException;
import br.com.desafio.backend.integration.viacep.ViaCepClient;
import br.com.desafio.backend.integration.viacep.ViaCepResponse;
import br.com.desafio.backend.repository.DealerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealerService {

    private final DealerRepository dealerRepository;
    private final ViaCepClient viaCepClient;

    public DealerService(DealerRepository dealerRepository, ViaCepClient viaCepClient) {
        this.dealerRepository = dealerRepository;
        this.viaCepClient = viaCepClient;
    }

    public List<DealerResponse> findAll() {
        return dealerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DealerResponse findById(Long id) {
        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concessionária não encontrada"));

        return toResponse(dealer);
    }

    private DealerResponse toResponse(Dealer dealer) {
        return new DealerResponse(
                dealer.getId(),
                dealer.getRazaoSocial(),
                dealer.getCnpj(),
                dealer.getCep(),
                dealer.getLogradouro(),
                dealer.getNumero(),
                dealer.getComplemento(),
                dealer.getBairro(),
                dealer.getCidade(),
                dealer.getEstado()
        );
    }

    public DealerResponse create(DealerRequest request) {

        ViaCepResponse endereco = viaCepClient.buscarCep(request.cep());

        if (Boolean.TRUE.equals(endereco.getErro())) {
            throw new ResourceNotFoundException("CEP não encontrado");
        }

        Dealer dealer = new Dealer();

        dealer.setRazaoSocial(request.razaoSocial());
        dealer.setCnpj(request.cnpj());
        dealer.setCep(endereco.getCep());
        dealer.setLogradouro(endereco.getLogradouro());
        dealer.setNumero(request.numero());
        dealer.setComplemento(endereco.getComplemento());
        dealer.setBairro(endereco.getBairro());
        dealer.setCidade(endereco.getLocalidade());
        dealer.setEstado(endereco.getUf());

        Dealer savedDealer = dealerRepository.save(dealer);

        return toResponse(savedDealer);
    }

    public DealerResponse update(Long id, DealerRequest request) {

        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Concessionária não encontrada")
                );

        dealer.setRazaoSocial(request.razaoSocial());
        dealer.setCnpj(request.cnpj());
        dealer.setCep(request.cep());
        dealer.setLogradouro(request.logradouro());
        dealer.setNumero(request.numero());
        dealer.setComplemento(request.complemento());
        dealer.setBairro(request.bairro());
        dealer.setCidade(request.cidade());
        dealer.setEstado(request.estado());

        Dealer updatedDealer = dealerRepository.save(dealer);

        return toResponse(updatedDealer);
    }

    public void delete(Long id) {

        if (!dealerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Concessionária não encontrada");
        }

        dealerRepository.deleteById(id);
    }
}