import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, Image, LoaderCircle } from "lucide-react";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import type { Dealer } from "../dealers/types.ts";
import { ApiError } from "../../shared/api/client.ts";
import { vehicleSchema, type VehicleFormValues } from "./schema.ts";
import {
  fuelLabels,
  fuelTypes,
  type Vehicle,
  type VehiclePayload,
} from "./types.ts";
import "../../shared/styles/forms.css";

type VehicleFormProps = {
  vehicle: Vehicle | null;
  dealers: Dealer[];
  onSubmit: (payload: VehiclePayload) => Promise<void>;
  onCancel: () => void;
};

function getDefaultValues(vehicle: Vehicle | null): VehicleFormValues {
  return {
    marca: vehicle?.marca ?? "",
    modelo: vehicle?.modelo ?? "",
    tipoCombustivel: vehicle?.tipoCombustivel ?? "FLEX",
    cor: vehicle?.cor ?? "",
    ano: vehicle?.ano ? String(vehicle.ano) : "",
    chassi: vehicle?.chassi ?? "",
    valor: vehicle?.valor ? String(vehicle.valor) : "",
    imagemUrl: vehicle?.imagemUrl ?? "",
    dealerId: vehicle?.dealerId ? String(vehicle.dealerId) : "",
  };
}

export function VehicleForm({
  vehicle,
  dealers,
  onSubmit,
  onCancel,
}: VehicleFormProps) {
  const [submitError, setSubmitError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<VehicleFormValues>({
    resolver: zodResolver(vehicleSchema),
    defaultValues: getDefaultValues(vehicle),
  });

  useEffect(() => {
    reset(getDefaultValues(vehicle));
    setSubmitError(null);
  }, [reset, vehicle]);

  const submit = handleSubmit(async (values) => {
    setSubmitError(null);

    try {
      await onSubmit({
        marca: values.marca.trim(),
        modelo: values.modelo.trim(),
        tipoCombustivel: values.tipoCombustivel,
        cor: values.cor.trim(),
        ano: values.ano ? Number(values.ano) : null,
        chassi: values.chassi.trim().toUpperCase() || null,
        valor: values.valor ? Number(values.valor) : null,
        imagemUrl: values.imagemUrl.trim() || null,
        dealerId: values.dealerId ? Number(values.dealerId) : null,
      });
    } catch (error) {
      if (error instanceof ApiError && error.fieldErrors) {
        Object.entries(error.fieldErrors).forEach(([field, message]) => {
          if (field in getDefaultValues(vehicle)) {
            setError(field as keyof VehicleFormValues, { message });
          }
        });
      }

      setSubmitError(
        error instanceof Error
          ? error.message
          : "Não foi possível salvar o veículo.",
      );
    }
  });

  return (
    <form className="entity-form" onSubmit={submit} noValidate>
      {submitError && (
        <div className="form-alert" role="alert">
          <CircleAlert size={18} aria-hidden="true" />
          <span>{submitError}</span>
        </div>
      )}

      <div className="form-grid form-grid--two">
        <label className="form-field">
          <span>Marca</span>
          <input placeholder="Ex.: Toyota" autoFocus {...register("marca")} />
          {errors.marca && <small>{errors.marca.message}</small>}
        </label>

        <label className="form-field">
          <span>Modelo</span>
          <input placeholder="Ex.: Corolla" {...register("modelo")} />
          {errors.modelo && <small>{errors.modelo.message}</small>}
        </label>

        <label className="form-field">
          <span>Combustível</span>
          <select {...register("tipoCombustivel")}>
            {fuelTypes.map((fuel) => (
              <option value={fuel} key={fuel}>
                {fuelLabels[fuel]}
              </option>
            ))}
          </select>
          {errors.tipoCombustivel && (
            <small>{errors.tipoCombustivel.message}</small>
          )}
        </label>

        <label className="form-field">
          <span>Cor</span>
          <input placeholder="Ex.: Preto" {...register("cor")} />
          {errors.cor && <small>{errors.cor.message}</small>}
        </label>
      </div>

      <div className="form-grid form-grid--three">
        <label className="form-field">
          <span>
            Ano <i>opcional</i>
          </span>
          <input
            type="number"
            inputMode="numeric"
            min="1886"
            step="1"
            placeholder="Ex.: 2024"
            {...register("ano")}
          />
          {errors.ano && <small>{errors.ano.message}</small>}
        </label>

        <label className="form-field">
          <span>
            Chassi <i>opcional</i>
          </span>
          <input
            maxLength={17}
            placeholder="Até 17 caracteres"
            className="text-uppercase"
            {...register("chassi")}
          />
          {errors.chassi && <small>{errors.chassi.message}</small>}
        </label>

        <label className="form-field">
          <span>
            Valor <i>opcional</i>
          </span>

          <div className="currency-field">
            <span>R$</span>
            <input
              type="number"
              inputMode="decimal"
              min="0.01"
              step="0.01"
              placeholder="0,00"
              {...register("valor")}
            />
          </div>

          {errors.valor && <small>{errors.valor.message}</small>}
        </label>
      </div>

      <label className="form-field">
        <span>Concessionária</span>
        <select {...register("dealerId")}>
          <option value="">Sem associação</option>

          {dealers.map((dealer) => (
            <option value={dealer.id} key={dealer.id}>
              {dealer.razaoSocial}
            </option>
          ))}
        </select>

        <em>Você pode alterar a associação a qualquer momento.</em>
        {errors.dealerId && <small>{errors.dealerId.message}</small>}
      </label>

      <label className="form-field form-field--icon">
        <span>
          Imagem do veículo <i>opcional</i>
        </span>

        <div>
          <Image size={18} aria-hidden="true" />
          <input
            placeholder="https://exemplo.com/veiculo.jpg"
            {...register("imagemUrl")}
          />
        </div>

        {errors.imagemUrl && <small>{errors.imagemUrl.message}</small>}
      </label>

      <div className="form-actions">
        <button
          className="button button--ghost"
          type="button"
          onClick={onCancel}
          disabled={isSubmitting}
        >
          Cancelar
        </button>

        <button
          className="button button--primary"
          type="submit"
          disabled={isSubmitting}
        >
          {isSubmitting && (
            <LoaderCircle className="spin" size={17} aria-hidden="true" />
          )}

          {vehicle ? "Salvar alterações" : "Cadastrar veículo"}
        </button>
      </div>
    </form>
  );
}
