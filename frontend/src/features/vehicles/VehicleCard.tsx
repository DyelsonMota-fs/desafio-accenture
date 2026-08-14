import {
  Building2,
  CalendarDays,
  CarFront,
  Fuel,
  Palette,
  Pencil,
  Trash2,
} from "lucide-react";
import { useEffect, useState } from "react";
import type { Dealer } from "../dealers/types.ts";
import { formatCurrency } from "../../shared/formatters.ts";
import { fuelLabels, type Vehicle } from "./types.ts";
import "./VehicleCard.css";

type VehicleCardProps = {
  vehicle: Vehicle;
  dealer?: Dealer;
  onEdit: () => void;
  onDelete: () => void;
};

export function VehicleCard({
  vehicle,
  dealer,
  onEdit,
  onDelete,
}: VehicleCardProps) {
  const [imageFailed, setImageFailed] = useState(false);

  useEffect(() => {
    setImageFailed(false);
  }, [vehicle.imagemUrl]);

  const showImage = Boolean(vehicle.imagemUrl) && !imageFailed;

  return (
    <article className="vehicle-card">
      <div
        className={`vehicle-card__visual ${
          showImage ? "vehicle-card__visual--image" : ""
        }`}
      >
        {showImage ? (
          <img
            src={vehicle.imagemUrl ?? undefined}
            alt={`${vehicle.marca} ${vehicle.modelo}`}
            loading="lazy"
            onError={() => setImageFailed(true)}
          />
        ) : (
          <div className="vehicle-placeholder" aria-hidden="true">
            <span>{vehicle.marca}</span>
            <CarFront size={68} strokeWidth={1.15} />
          </div>
        )}

        <span className="vehicle-card__id">
          #{String(vehicle.id).padStart(3, "0")}
        </span>

        <div className="vehicle-card__actions">
          <button
            className="icon-button icon-button--light"
            type="button"
            onClick={onEdit}
            aria-label={`Editar ${vehicle.modelo}`}
          >
            <Pencil size={17} aria-hidden="true" />
          </button>

          <button
            className="icon-button icon-button--light"
            type="button"
            onClick={onDelete}
            aria-label={`Excluir ${vehicle.modelo}`}
          >
            <Trash2 size={17} aria-hidden="true" />
          </button>
        </div>
      </div>

      <div className="vehicle-card__body">
        <span className="vehicle-card__brand">{vehicle.marca}</span>

        <h3>{vehicle.modelo}</h3>

        <strong className="vehicle-card__price">
          {vehicle.valor ? formatCurrency(vehicle.valor) : "Valor sob consulta"}
        </strong>

        <div className="vehicle-specs">
          <span>
            <Fuel size={15} aria-hidden="true" />
            {fuelLabels[vehicle.tipoCombustivel]}
          </span>

          <span>
            <Palette size={15} aria-hidden="true" />
            {vehicle.cor}
          </span>

          {vehicle.ano && (
            <span>
              <CalendarDays size={15} aria-hidden="true" />
              {vehicle.ano}
            </span>
          )}
        </div>

        {vehicle.chassi && (
          <div className="vehicle-card__chassis">
            <span>Chassi</span>
            <code>{vehicle.chassi}</code>
          </div>
        )}

        <div className="vehicle-card__dealer">
          <Building2 size={15} aria-hidden="true" />
          <span>{dealer?.razaoSocial ?? "Sem concessionária"}</span>
        </div>
      </div>
    </article>
  );
}
