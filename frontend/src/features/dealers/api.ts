import { apiRequest } from "../../shared/api/client.ts";
import type { Dealer, DealerPayload } from "./types.ts";

export const dealerKeys = {
  all: ["dealers"] as const,
};

export function listDealers() {
  return apiRequest<Dealer[]>("/dealer");
}

export function createDealer(payload: DealerPayload) {
  return apiRequest<Dealer>("/dealer", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function updateDealer(id: number, payload: DealerPayload) {
  return apiRequest<Dealer>(`/dealer/${id}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export function deleteDealer(id: number) {
  return apiRequest<void>(`/dealer/${id}`, {
    method: "DELETE",
  });
}
