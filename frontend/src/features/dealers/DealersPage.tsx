import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  Building2,
  CircleAlert,
  LoaderCircle,
  Plus,
  Search,
  ShieldAlert,
  Trash2,
} from "lucide-react";
import { useMemo, useState } from "react";
import { listVehicles, vehicleKeys } from "../vehicles/api.ts";
import { ApiError } from "../../shared/api/client.ts";
import {
  EmptyState,
  ErrorState,
  PageLoader,
} from "../../shared/components/Feedback.tsx";
import { Modal } from "../../shared/components/Modal.tsx";
import { useToast } from "../../shared/components/toast-context.ts";
import {
  createDealer,
  dealerKeys,
  deleteDealer,
  listDealers,
  updateDealer,
} from "./api.ts";
import { DealerCard } from "./DealerCard.tsx";
import { DealerForm } from "./DealerForm.tsx";
import type { Dealer, DealerPayload } from "./types.ts";
import "./DealersPage.css";

export function DealersPage() {
  const queryClient = useQueryClient();
  const { showToast } = useToast();

  const [search, setSearch] = useState("");
  const [formOpen, setFormOpen] = useState(false);
  const [editingDealer, setEditingDealer] = useState<Dealer | null>(null);
  const [dealerToDelete, setDealerToDelete] = useState<Dealer | null>(null);

  const dealersQuery = useQuery({
    queryKey: dealerKeys.all,
    queryFn: listDealers,
  });

  const vehiclesQuery = useQuery({
    queryKey: vehicleKeys.all,
    queryFn: listVehicles,
  });

  const saveMutation = useMutation({
    mutationFn: ({
      dealer,
      payload,
    }: {
      dealer: Dealer | null;
      payload: DealerPayload;
    }) => {
      return dealer ? updateDealer(dealer.id, payload) : createDealer(payload);
    },

    onSuccess: async (_, variables) => {
      await queryClient.invalidateQueries({
        queryKey: dealerKeys.all,
      });

      showToast(
        variables.dealer
          ? "Concessionária atualizada com sucesso."
          : "Concessionária cadastrada com sucesso.",
      );
    },
  });

  const deleteMutation = useMutation({
    mutationFn: deleteDealer,

    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: dealerKeys.all,
      });

      setDealerToDelete(null);
      showToast("Concessionária removida da rede.");
    },
  });

  const vehicleCounts = useMemo(() => {
    const counts = new Map<number, number>();

    for (const vehicle of vehiclesQuery.data ?? []) {
      if (vehicle.dealerId !== null) {
        const currentCount = counts.get(vehicle.dealerId) ?? 0;
        counts.set(vehicle.dealerId, currentCount + 1);
      }
    }

    return counts;
  }, [vehiclesQuery.data]);

  const filteredDealers = useMemo(() => {
    const term = search.trim().toLocaleLowerCase("pt-BR");

    if (!term) {
      return dealersQuery.data ?? [];
    }

    return (dealersQuery.data ?? []).filter((dealer) => {
      const searchableContent = [
        dealer.razaoSocial,
        dealer.cnpj,
        dealer.cidade ?? "",
        dealer.estado ?? "",
      ]
        .join(" ")
        .toLocaleLowerCase("pt-BR");

      return searchableContent.includes(term);
    });
  }, [dealersQuery.data, search]);

  const openCreate = () => {
    saveMutation.reset();
    setEditingDealer(null);
    setFormOpen(true);
  };

  const openEdit = (dealer: Dealer) => {
    saveMutation.reset();
    setEditingDealer(dealer);
    setFormOpen(true);
  };

  const closeForm = () => {
    if (saveMutation.isPending) {
      return;
    }

    setFormOpen(false);
  };

  const handleSave = async (payload: DealerPayload) => {
    await saveMutation.mutateAsync({
      dealer: editingDealer,
      payload,
    });

    setFormOpen(false);
  };

  const associatedVehicles = dealerToDelete
    ? (vehicleCounts.get(dealerToDelete.id) ?? 0)
    : 0;

  const loadError = dealersQuery.error ?? vehiclesQuery.error;

  return (
    <>
      <section className="page-hero page-hero--paper">
        <div className="container page-hero__inner">
          <div className="page-hero__copy">
            <span className="eyebrow">Nossa rede</span>
            <h1>Presença que aproxima.</h1>
            <p>
              Gerencie as unidades da rede e acompanhe a distribuição do
              inventário.
            </p>
          </div>

          <div
            className="network-seal"
            aria-label={`${dealersQuery.data?.length ?? 0} unidades na rede`}
          >
            <Building2 size={30} strokeWidth={1.4} aria-hidden="true" />

            <strong>
              {String(dealersQuery.data?.length ?? 0).padStart(2, "0")}
            </strong>

            <span>unidades</span>
          </div>
        </div>
      </section>

      <section className="page-section container">
        <header className="section-heading">
          <div>
            <span className="eyebrow">Concessionárias</span>
            <h2>Unidades cadastradas</h2>
            <p>Endereços, documentação e inventário de cada ponto da rede.</p>
          </div>

          <button
            className="button button--primary"
            type="button"
            onClick={openCreate}
          >
            <Plus size={18} aria-hidden="true" />
            Nova concessionária
          </button>
        </header>

        <div className="toolbar toolbar--dealers">
          <label className="search-field">
            <Search size={18} aria-hidden="true" />

            <input
              type="search"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              placeholder="Buscar por nome, CNPJ ou cidade"
              aria-label="Buscar concessionárias"
            />
          </label>

          <span className="result-count">
            {filteredDealers.length} unidade(s)
          </span>
        </div>

        {dealersQuery.isLoading || vehiclesQuery.isLoading ? (
          <PageLoader label="Carregando a rede" />
        ) : loadError ? (
          <ErrorState
            message={
              loadError instanceof Error
                ? loadError.message
                : "Falha inesperada."
            }
            onRetry={() => {
              void dealersQuery.refetch();
              void vehiclesQuery.refetch();
            }}
          />
        ) : filteredDealers.length === 0 ? (
          <EmptyState
            title={
              dealersQuery.data?.length
                ? "Nenhuma unidade encontrada"
                : "Sua rede ainda está vazia"
            }
            description={
              dealersQuery.data?.length
                ? "Altere a busca para visualizar outras concessionárias."
                : "Cadastre a primeira concessionária para começar a distribuir o estoque."
            }
            actionLabel={
              dealersQuery.data?.length ? undefined : "Cadastrar concessionária"
            }
            onAction={dealersQuery.data?.length ? undefined : openCreate}
          />
        ) : (
          <div className="dealer-grid">
            {filteredDealers.map((dealer) => (
              <DealerCard
                key={dealer.id}
                dealer={dealer}
                vehicleCount={vehicleCounts.get(dealer.id) ?? 0}
                onEdit={() => openEdit(dealer)}
                onDelete={() => {
                  deleteMutation.reset();
                  setDealerToDelete(dealer);
                }}
              />
            ))}
          </div>
        )}
      </section>

      <Modal
        open={formOpen}
        onClose={closeForm}
        title={editingDealer ? "Editar concessionária" : "Nova concessionária"}
        description="Mantenha os dados cadastrais e o endereço da unidade atualizados."
        size="large"
      >
        <DealerForm
          dealer={editingDealer}
          onSubmit={handleSave}
          onCancel={closeForm}
        />
      </Modal>

      <Modal
        open={Boolean(dealerToDelete)}
        onClose={() => {
          if (!deleteMutation.isPending) {
            setDealerToDelete(null);
          }
        }}
        title="Remover concessionária"
        description="Confira o inventário da unidade antes de continuar."
      >
        {dealerToDelete && (
          <div className="confirm-content">
            <div
              className={`confirm-icon ${
                associatedVehicles ? "confirm-icon--warning" : ""
              }`}
            >
              {associatedVehicles ? (
                <ShieldAlert size={24} aria-hidden="true" />
              ) : (
                <Trash2 size={24} aria-hidden="true" />
              )}
            </div>

            {associatedVehicles ? (
              <p>
                Esta unidade possui{" "}
                <strong>{associatedVehicles} veículo(s) associado(s)</strong>.
                Edite esses veículos e remova a associação antes de excluir a
                concessionária.
              </p>
            ) : (
              <p>
                Deseja remover <strong>{dealerToDelete.razaoSocial}</strong> da
                rede?
              </p>
            )}

            {deleteMutation.error && (
              <div className="form-alert" role="alert">
                <CircleAlert size={18} aria-hidden="true" />
                <span>
                  {deleteMutation.error instanceof ApiError
                    ? deleteMutation.error.message
                    : "Não foi possível excluir a concessionária."}
                </span>
              </div>
            )}

            <div className="form-actions">
              <button
                className="button button--ghost"
                type="button"
                onClick={() => setDealerToDelete(null)}
                disabled={deleteMutation.isPending}
              >
                {associatedVehicles ? "Entendi" : "Cancelar"}
              </button>

              {!associatedVehicles && (
                <button
                  className="button button--danger"
                  type="button"
                  onClick={() => {
                    deleteMutation.mutate(dealerToDelete.id);
                  }}
                  disabled={deleteMutation.isPending}
                >
                  {deleteMutation.isPending && (
                    <LoaderCircle
                      className="spin"
                      size={17}
                      aria-hidden="true"
                    />
                  )}
                  Excluir concessionária
                </button>
              )}
            </div>
          </div>
        )}
      </Modal>
    </>
  );
}
