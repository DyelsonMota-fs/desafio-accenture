import { zodResolver } from "@hookform/resolvers/zod";
import { CircleAlert, LoaderCircle, MapPin } from "lucide-react";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { ApiError } from "../../shared/api/client.ts";
import { formatCep, formatCnpj, onlyDigits } from "../../shared/formatters.ts";
import { dealerSchema, type DealerFormValues } from "./schema.ts";
import type { Dealer, DealerPayload } from "./types.ts";
import "../../shared/styles/forms.css";

type DealerFormProps = {
  dealer: Dealer | null;
  onSubmit: (payload: DealerPayload) => Promise<void>;
  onCancel: () => void;
};

function getDefaultValues(dealer: Dealer | null): DealerFormValues {
  return {
    razaoSocial: dealer?.razaoSocial ?? "",
    cnpj: dealer ? formatCnpj(dealer.cnpj) : "",
    cep: dealer ? formatCep(dealer.cep) : "",
    numero: dealer?.numero ?? "",
    logradouro: dealer?.logradouro ?? "",
    complemento: dealer?.complemento ?? "",
    bairro: dealer?.bairro ?? "",
    cidade: dealer?.cidade ?? "",
    estado: dealer?.estado ?? "",
  };
}

export function DealerForm({ dealer, onSubmit, onCancel }: DealerFormProps) {
  const [submitError, setSubmitError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    setError,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm<DealerFormValues>({
    resolver: zodResolver(dealerSchema),
    defaultValues: getDefaultValues(dealer),
  });

  useEffect(() => {
    reset(getDefaultValues(dealer));
    setSubmitError(null);
  }, [dealer, reset]);

  const submit = handleSubmit(async (values) => {
    setSubmitError(null);

    try {
      await onSubmit({
        razaoSocial: values.razaoSocial.trim(),
        cnpj: onlyDigits(values.cnpj),
        cep: onlyDigits(values.cep),
        numero: values.numero.trim(),
        logradouro: values.logradouro.trim() || null,
        complemento: values.complemento.trim() || null,
        bairro: values.bairro.trim() || null,
        cidade: values.cidade.trim() || null,
        estado: values.estado.trim().toUpperCase() || null,
      });
    } catch (error) {
      if (error instanceof ApiError && error.fieldErrors) {
        Object.entries(error.fieldErrors).forEach(([field, message]) => {
          if (field in getDefaultValues(dealer)) {
            setError(field as keyof DealerFormValues, {
              message,
            });
          }
        });
      }

      setSubmitError(
        error instanceof Error
          ? error.message
          : "Não foi possível salvar a concessionária.",
      );
    }
  });

  const cnpjRegistration = register("cnpj");
  const cepRegistration = register("cep");

  return (
    <form className="entity-form" onSubmit={submit} noValidate>
      {submitError && (
        <div className="form-alert" role="alert">
          <CircleAlert size={18} aria-hidden="true" />
          <span>{submitError}</span>
        </div>
      )}

      <label className="form-field">
        <span>Razão social</span>
        <input
          placeholder="Ex.: Vértice Recife Comércio de Veículos"
          autoFocus
          {...register("razaoSocial")}
        />
        {errors.razaoSocial && <small>{errors.razaoSocial.message}</small>}
      </label>

      <div className="form-grid form-grid--two">
        <label className="form-field">
          <span>CNPJ</span>
          <input
            inputMode="numeric"
            placeholder="00.000.000/0000-00"
            {...cnpjRegistration}
            onChange={(event) => {
              setValue("cnpj", formatCnpj(event.target.value), {
                shouldValidate: true,
              });
            }}
          />
          {errors.cnpj && <small>{errors.cnpj.message}</small>}
        </label>

        <label className="form-field">
          <span>CEP</span>
          <input
            inputMode="numeric"
            placeholder="00000-000"
            {...cepRegistration}
            onChange={(event) => {
              setValue("cep", formatCep(event.target.value), {
                shouldValidate: true,
              });
            }}
          />
          {errors.cep && <small>{errors.cep.message}</small>}
        </label>
      </div>

      {!dealer && (
        <div className="info-strip">
          <MapPin size={18} aria-hidden="true" />
          <span>
            Ao cadastrar, o endereço será preenchido automaticamente pelo
            ViaCEP.
          </span>
        </div>
      )}

      <div className="form-grid form-grid--address">
        <label className="form-field form-field--wide">
          <span>Logradouro</span>
          <input
            placeholder="Rua ou avenida"
            {...register("logradouro")}
            disabled={!dealer}
          />
          {errors.logradouro && <small>{errors.logradouro.message}</small>}
        </label>

        <label className="form-field">
          <span>Número</span>
          <input placeholder="Ex.: 120" {...register("numero")} />
          {errors.numero && <small>{errors.numero.message}</small>}
        </label>

        <label className="form-field">
          <span>Complemento</span>
          <input
            placeholder="Opcional"
            {...register("complemento")}
            disabled={!dealer}
          />
        </label>

        <label className="form-field">
          <span>Bairro</span>
          <input
            placeholder="Bairro"
            {...register("bairro")}
            disabled={!dealer}
          />
        </label>

        <label className="form-field">
          <span>Cidade</span>
          <input
            placeholder="Cidade"
            {...register("cidade")}
            disabled={!dealer}
          />
        </label>

        <label className="form-field">
          <span>UF</span>
          <input
            placeholder="PE"
            maxLength={2}
            {...register("estado")}
            disabled={!dealer}
          />
          {errors.estado && <small>{errors.estado.message}</small>}
        </label>
      </div>

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

          {dealer ? "Salvar alterações" : "Cadastrar concessionária"}
        </button>
      </div>
    </form>
  );
}
