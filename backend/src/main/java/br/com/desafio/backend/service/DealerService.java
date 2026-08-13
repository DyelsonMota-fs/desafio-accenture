package br.com.desafio.backend.service;

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

    private DealerResponse toResponse(Dealer dealer) {
        return new DealerResponse(
                dealer.getId(),
                dealer.getRazaoSocial(),
                dealer.getCnpj(),
                dealer.getEndereco()
        );
    }
}