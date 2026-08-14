export type Dealer = {
  id: number;
  razaoSocial: string;
  cnpj: string;
  cep: string;
  logradouro: string | null;
  numero: string;
  complemento: string | null;
  bairro: string | null;
  cidade: string | null;
  estado: string | null;
};

export type DealerPayload = Omit<Dealer, "id">;
