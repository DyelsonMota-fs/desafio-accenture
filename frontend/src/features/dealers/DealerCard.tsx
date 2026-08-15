import { CarFront, MapPin, Pencil, Trash2 } from "lucide-react";
import { formatCep, formatCnpj, getInitials } from "../../shared/formatters.ts";
import type { Dealer } from "./types.ts";
import "./DealerCard.css";

type DealerCardProps = {
  dealer: Dealer;
  vehicleCount: number;
  onEdit: () => void;
  onDelete: () => void;
};

export function DealerCard({
  dealer,
  vehicleCount,
  onEdit,
  onDelete,
}: DealerCardProps) {
  const address = [dealer.logradouro, dealer.numero, dealer.bairro]
    .filter(Boolean)
    .join(", ");

  const location = [dealer.cidade, dealer.estado].filter(Boolean).join(" — ");

  return (
    <article className="dealer-card">
      <header className="dealer-card__header">
        <div className="dealer-monogram" aria-hidden="true">
          {getInitials(dealer.razaoSocial)}
        </div>

        <div className="dealer-card__actions">
          <button
            className="icon-button"
            type="button"
            onClick={onEdit}
            aria-label={`Editar ${dealer.razaoSocial}`}
          >
            <Pencil size={17} aria-hidden="true" />
          </button>

          <button
            className="icon-button"
            type="button"
            onClick={onDelete}
            aria-label={`Excluir ${dealer.razaoSocial}`}
          >
            <Trash2 size={17} aria-hidden="true" />
          </button>
        </div>
      </header>

      <div className="dealer-card__title">
        <span>Concessionária #{String(dealer.id).padStart(2, "0")}</span>

        <h3>{dealer.razaoSocial}</h3>
        <p>{formatCnpj(dealer.cnpj)}</p>
      </div>

      <div className="dealer-card__details">
        <div>
          <MapPin size={18} aria-hidden="true" />

          <p>
            <strong>{address || "Endereço não informado"}</strong>
            <span>
              {location || formatCep(dealer.cep)} · {formatCep(dealer.cep)}
            </span>
          </p>
        </div>

        <div>
          <CarFront size={18} aria-hidden="true" />

          <p>
            <strong>
              {vehicleCount} {vehicleCount === 1 ? "veículo" : "veículos"}
            </strong>
            <span>associado(s) a esta unidade</span>
          </p>
        </div>
      </div>
    </article>
  );
}
