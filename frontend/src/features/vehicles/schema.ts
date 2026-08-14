import { z } from "zod";
import { fuelTypes } from "./types.ts";

export const vehicleSchema = z.object({
  marca: z.string().trim().min(1, "Informe a marca"),

  modelo: z.string().trim().min(1, "Informe o modelo"),

  tipoCombustivel: z.enum(fuelTypes, {
    message: "Selecione o combustível",
  }),

  cor: z.string().trim().min(1, "Informe a cor"),

  ano: z
    .string()
    .refine(
      (value) => !value || (/^\d{4}$/.test(value) && Number(value) >= 1886),
      "Informe um ano válido a partir de 1886",
    ),

  chassi: z
    .string()
    .trim()
    .max(17, "O chassi deve possuir no máximo 17 caracteres"),

  valor: z
    .string()
    .refine(
      (value) => !value || (!Number.isNaN(Number(value)) && Number(value) > 0),
      "O valor deve ser maior que zero",
    ),

  imagemUrl: z
    .string()
    .trim()
    .refine((value) => !value || URL.canParse(value), "Informe uma URL válida"),

  dealerId: z.string(),
});

export type VehicleFormValues = z.infer<typeof vehicleSchema>;
