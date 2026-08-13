package br.com.desafio.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<Cep, String> {

    @Override
    public boolean isValid(
            String cep,
            ConstraintValidatorContext context
    ) {
        if (cep == null || cep.isBlank()) {
            return true;
        }

        String cepLimpo = cep.replaceAll("\\D", "");

        return cepLimpo.matches("\\d{8}");
    }
}