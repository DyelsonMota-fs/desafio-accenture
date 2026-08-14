export const fuelTypes = [
  "GASOLINA",
  "ETANOL",
  "FLEX",
  "DIESEL",
  "ELETRICO",
  "HIBRIDO",
] as const;

export type FuelType = (typeof fuelTypes)[number];

export type Vehicle = {
  id: number;
  marca: string;
  modelo: string;
  tipoCombustivel: FuelType;
  cor: string;
  ano: number | null;
  chassi: string | null;
  valor: number | null;
  imagemUrl: string | null;
  dealerId: number | null;
};

export type VehiclePayload = {
  marca: string;
  modelo: string;
  tipoCombustivel: FuelType;
  cor: string;
  ano: number | null;
  chassi: string | null;
  valor: number | null;
  imagemUrl: string | null;
  dealerId: number | null;
};

export const fuelLabels: Record<FuelType, string> = {
  GASOLINA: "Gasolina",
  ETANOL: "Etanol",
  FLEX: "Flex",
  DIESEL: "Diesel",
  ELETRICO: "Elétrico",
  HIBRIDO: "Híbrido",
};
