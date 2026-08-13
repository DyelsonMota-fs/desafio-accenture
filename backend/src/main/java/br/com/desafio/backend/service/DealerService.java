package br.com.desafio.backend.service;

import br.com.desafio.backend.dto.DealerRequest;
import br.com.desafio.backend.dto.DealerResponse;
import br.com.desafio.backend.entity.Dealer;
import br.com.desafio.backend.repository.DealerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealerService {

    private final DealerRepository dealerRepository;

    public DealerService(DealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

    public List<DealerResponse> findAll() {
        return dealerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DealerResponse findById(Long id) {
        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concessionária não encontrada"));

        return toResponse(dealer);
    }

    private DealerResponse toResponse(Dealer dealer) {
        return new DealerResponse(
                dealer.getId(),
                dealer.getRazaoSocial(),
                dealer.getCnpj(),
                dealer.getEndereco()
        );
    }

    public DealerResponse create(DealerRequest request) {

        Dealer dealer = new Dealer();

        dealer.setRazaoSocial(request.razaoSocial());
        dealer.setCnpj(request.cnpj());
        dealer.setEndereco(request.endereco());

        Dealer savedDealer = dealerRepository.save(dealer);

        return toResponse(savedDealer);
    }

    public DealerResponse update(Long id, DealerRequest request) {

        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Concessionária não encontrada")
                );

        dealer.setRazaoSocial(request.razaoSocial());
        dealer.setCnpj(request.cnpj());
        dealer.setEndereco(request.endereco());

        Dealer updatedDealer = dealerRepository.save(dealer);

        return toResponse(updatedDealer);
    }

    public void delete(Long id) {

        if (!dealerRepository.existsById(id)) {
            throw new RuntimeException("Concessionária não encontrada");
        }

        dealerRepository.deleteById(id);
    }
}