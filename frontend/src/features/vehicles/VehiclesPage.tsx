import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  ChevronDown,
  CircleAlert,
  LoaderCircle,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import { useMemo, useState } from "react";
import { dealerKeys, listDealers } from "../dealers/api.ts";
import { ApiError } from "../../shared/api/client.ts";
import {
  EmptyState,
  ErrorState,
  PageLoader,
} from "../../shared/components/Feedback.tsx";
import { Modal } from "../../shared/components/Modal.tsx";
import { useToast } from "../../shared/components/toast-context.ts";
import {
  createVehicle,
  deleteVehicle,
  listVehicles,
  updateVehicle,
  vehicleKeys,
} from "./api.ts";
import { VehicleCard } from "./VehicleCard.tsx";
import { VehicleForm } from "./VehicleForm.tsx";
import type { Vehicle, VehiclePayload } from "./types.ts";
import "./VehiclesPage.css";

export function VehiclesPage() {
  const queryClient = useQueryClient();
  const { showToast } = useToast();

  const [search, setSearch] = useState("");
  const [dealerFilter, setDealerFilter] = useState("all");
  const [formOpen, setFormOpen] = useState(false);
  const [editingVehicle, setEditingVehicle] = useState<Vehicle | null>(null);
  const [vehicleToDelete, setVehicleToDelete] = useState<Vehicle | null>(null);

  const vehiclesQuery = useQuery({
    queryKey: vehicleKeys.all,
    queryFn: listVehicles,
  });

  const dealersQuery = useQuery({
    queryKey: dealerKeys.all,
    queryFn: listDealers,
  });

  const saveMutation = useMutation({
    mutationFn: ({
      vehicle,
      payload,
    }: {
      vehicle: Vehicle | null;
      payload: VehiclePayload;
    }) => {
      return vehicle
        ? updateVehicle(vehicle.id, payload)
        : createVehicle(payload);
    },

    onSuccess: async (_, variables) => {
      await queryClient.invalidateQueries({
        queryKey: vehicleKeys.all,
      });

      showToast(
        variables.vehicle
          ? "Veículo atualizado com sucesso."
          : "Veículo cadastrado com sucesso.",
      );
    },
  });

  const deleteMutation = useMutation({
    mutationFn: deleteVehicle,

    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: vehicleKeys.all,
      });

      setVehicleToDelete(null);
      showToast("Veículo removido do estoque.");
    },
  });

  const dealerMap = useMemo(() => {
    return new Map(
      (dealersQuery.data ?? []).map((dealer) => [dealer.id, dealer]),
    );
  }, [dealersQuery.data]);

  const filteredVehicles = useMemo(() => {
    const term = search.trim().toLocaleLowerCase("pt-BR");

    return (vehiclesQuery.data ?? []).filter((vehicle) => {
      const searchableContent = [
        vehicle.marca,
        vehicle.modelo,
        vehicle.cor,
        vehicle.ano ?? "",
        vehicle.chassi ?? "",
      ]
        .join(" ")
        .toLocaleLowerCase("pt-BR");

      const matchesSearch =
        term.length === 0 || searchableContent.includes(term);

      const matchesDealer =
        dealerFilter === "all" ||
        (dealerFilter === "none"
          ? vehicle.dealerId === null
          : vehicle.dealerId === Number(dealerFilter));

      return matchesSearch && matchesDealer;
    });
  }, [dealerFilter, search, vehiclesQuery.data]);

  const associatedCount = (vehiclesQuery.data ?? []).filter(
    (vehicle) => vehicle.dealerId !== null,
  ).length;

  const openCreate = () => {
    saveMutation.reset();
    setEditingVehicle(null);
    setFormOpen(true);
  };

  const openEdit = (vehicle: Vehicle) => {
    saveMutation.reset();
    setEditingVehicle(vehicle);
    setFormOpen(true);
  };

  const closeForm = () => {
    if (saveMutation.isPending) {
      return;
    }

    setFormOpen(false);
  };

  const handleSave = async (payload: VehiclePayload) => {
    await saveMutation.mutateAsync({
      vehicle: editingVehicle,
      payload,
    });

    setFormOpen(false);
  };

  const loadError = vehiclesQuery.error ?? dealersQuery.error;

  return (
    <>
      <section className="page-hero page-hero--dark">
        <div className="container page-hero__inner">
          <div className="page-hero__copy">
            <span className="eyebrow eyebrow--light">Gestão de inventário</span>

            <h1>Veículos em exposição.</h1>

            <p>
              Organize o estoque, mantenha os dados atualizados e distribua cada
              modelo pela rede.
            </p>
          </div>

          <div className="page-hero__summary" aria-label="Resumo do estoque">
            <div>
              <strong>
                {String(vehiclesQuery.data?.length ?? 0).padStart(2, "0")}
              </strong>
              <span>veículos no estoque</span>
            </div>

            <div>
              <strong>{String(associatedCount).padStart(2, "0")}</strong>
              <span>em concessionárias</span>
            </div>
          </div>
        </div>
      </section>

      <section className="page-section container">
        <header className="section-heading">
          <div>
            <span className="eyebrow">Catálogo</span>
            <h2>Estoque atual</h2>
            <p>Consulte, edite e gerencie os veículos cadastrados.</p>
          </div>

          <button
            className="button button--primary"
            type="button"
            onClick={openCreate}
          >
            <Plus size={18} aria-hidden="true" />
            Novo veículo
          </button>
        </header>

        <div className="toolbar">
          <label className="search-field">
            <Search size={18} aria-hidden="true" />

            <input
              type="search"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              placeholder="Buscar por marca, modelo ou cor"
              aria-label="Buscar veículos"
            />
          </label>

          <label className="select-field">
            <select
              value={dealerFilter}
              onChange={(event) => setDealerFilter(event.target.value)}
              aria-label="Filtrar por concessionária"
            >
              <option value="all">Todas as concessionárias</option>
              <option value="none">Sem concessionária</option>

              {(dealersQuery.data ?? []).map((dealer) => (
                <option key={dealer.id} value={dealer.id}>
                  {dealer.razaoSocial}
                </option>
              ))}
            </select>

            <ChevronDown size={17} aria-hidden="true" />
          </label>

          <span className="result-count">
            {filteredVehicles.length} resultado(s)
          </span>
        </div>

        {vehiclesQuery.isLoading || dealersQuery.isLoading ? (
          <PageLoader label="Preparando o estoque" />
        ) : loadError ? (
          <ErrorState
            message={
              loadError instanceof Error
                ? loadError.message
                : "Falha inesperada."
            }
            onRetry={() => {
              void vehiclesQuery.refetch();
              void dealersQuery.refetch();
            }}
          />
        ) : filteredVehicles.length === 0 ? (
          <EmptyState
            title={
              vehiclesQuery.data?.length
                ? "Nenhum veículo encontrado"
                : "O estoque está vazio"
            }
            description={
              vehiclesQuery.data?.length
                ? "Altere os filtros para visualizar outros modelos."
                : "Cadastre o primeiro veículo para começar a organizar o inventário."
            }
            actionLabel={
              vehiclesQuery.data?.length ? undefined : "Cadastrar veículo"
            }
            onAction={vehiclesQuery.data?.length ? undefined : openCreate}
          />
        ) : (
          <div className="vehicle-grid">
            {filteredVehicles.map((vehicle) => (
              <VehicleCard
                key={vehicle.id}
                vehicle={vehicle}
                dealer={
                  vehicle.dealerId ? dealerMap.get(vehicle.dealerId) : undefined
                }
                onEdit={() => openEdit(vehicle)}
                onDelete={() => {
                  deleteMutation.reset();
                  setVehicleToDelete(vehicle);
                }}
              />
            ))}
          </div>
        )}
      </section>

      <Modal
        open={formOpen}
        onClose={closeForm}
        title={editingVehicle ? "Editar veículo" : "Novo veículo"}
        description="Preencha os dados que serão exibidos no catálogo da rede."
      >
        <VehicleForm
          vehicle={editingVehicle}
          dealers={dealersQuery.data ?? []}
          onSubmit={handleSave}
          onCancel={closeForm}
        />
      </Modal>

      <Modal
        open={Boolean(vehicleToDelete)}
        onClose={() => {
          if (!deleteMutation.isPending) {
            setVehicleToDelete(null);
          }
        }}
        title="Remover veículo"
        description="Esta ação não poderá ser desfeita."
      >
        {vehicleToDelete && (
          <div className="confirm-content">
            <div className="confirm-icon">
              <Trash2 size={24} aria-hidden="true" />
            </div>

            <p>
              Deseja remover{" "}
              <strong>
                {vehicleToDelete.marca} {vehicleToDelete.modelo}
              </strong>{" "}
              do estoque?
            </p>

            {deleteMutation.error && (
              <div className="form-alert" role="alert">
                <CircleAlert size={18} aria-hidden="true" />
                <span>
                  {deleteMutation.error instanceof ApiError
                    ? deleteMutation.error.message
                    : "Não foi possível excluir o veículo."}
                </span>
              </div>
            )}

            <div className="form-actions">
              <button
                className="button button--ghost"
                type="button"
                onClick={() => setVehicleToDelete(null)}
                disabled={deleteMutation.isPending}
              >
                Cancelar
              </button>

              <button
                className="button button--danger"
                type="button"
                onClick={() => deleteMutation.mutate(vehicleToDelete.id)}
                disabled={deleteMutation.isPending}
              >
                {deleteMutation.isPending && (
                  <LoaderCircle className="spin" size={17} aria-hidden="true" />
                )}
                Excluir veículo
              </button>
            </div>
          </div>
        )}
      </Modal>
    </>
  );
}
