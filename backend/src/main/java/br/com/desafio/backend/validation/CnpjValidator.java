package br.com.desafio.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidator implements ConstraintValidator<Cnpj, String> {

    @Override
    public boolean isValid(
            String cnpj,
            ConstraintValidatorContext context
    ) {
        if (cnpj == null || cnpj.isBlank()) {
            return true;
        }

        return validarCnpj(cnpj);
    }

    private boolean validarCnpj(String cnpj) {

        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14) {
            return false;
        }

        if (cnpj.chars().distinct().count() == 1) {
            return false;
        }

        int[] pesosPrimeiroDigito = {
                5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2
        };

        int[] pesosSegundoDigito = {
                6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2
        };

        int primeiroDigito = calcularDigito(
                cnpj,
                pesosPrimeiroDigito
        );

        int segundoDigito = calcularDigito(
                cnpj,
                pesosSegundoDigito
        );

        return primeiroDigito == Character.getNumericValue(cnpj.charAt(12))
                && segundoDigito == Character.getNumericValue(cnpj.charAt(13));
    }

    private int calcularDigito(String cnpj, int[] pesos) {

        int soma = 0;

        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i))
                    * pesos[i];
        }

        int resto = soma % 11;

        return resto < 2 ? 0 : 11 - resto;
    }
}