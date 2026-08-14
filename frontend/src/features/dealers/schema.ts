import { z } from "zod";
import { onlyDigits } from "../../shared/formatters.ts";

function calculateDigit(base: string, weights: number[]) {
  const sum = weights.reduce(
    (total, weight, index) => total + Number(base[index]) * weight,
    0,
  );

  const remainder = sum % 11;

  return remainder < 2 ? 0 : 11 - remainder;
}

export function isValidCnpj(value: string) {
  const digits = onlyDigits(value);

  if (digits.length !== 14 || /^(\d)\1+$/.test(digits)) {
    return false;
  }

  const first = calculateDigit(digits, [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);

  const second = calculateDigit(
    digits,
    [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2],
  );

  return first === Number(digits[12]) && second === Number(digits[13]);
}

export const dealerSchema = z.object({
  razaoSocial: z.string().trim().min(1, "Informe a razão social"),

  cnpj: z.string().refine(isValidCnpj, "Informe um CNPJ válido"),

  cep: z
    .string()
    .refine((value) => onlyDigits(value).length === 8, "Informe um CEP válido"),

  numero: z.string().trim().min(1, "Informe o número"),

  logradouro: z.string(),
  complemento: z.string(),
  bairro: z.string(),
  cidade: z.string(),

  estado: z.string().max(2, "Use a sigla do estado"),
});

export type DealerFormValues = z.infer<typeof dealerSchema>;
