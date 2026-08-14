import { apiRequest } from "../../shared/api/client.ts";
import type { Vehicle, VehiclePayload } from "./types.ts";

export const vehicleKeys = {
  all: ["vehicles"] as const,
};

export function listVehicles() {
  return apiRequest<Vehicle[]>("/vehicles");
}

export function createVehicle(payload: VehiclePayload) {
  return apiRequest<Vehicle>("/vehicles", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function updateVehicle(id: number, payload: VehiclePayload) {
  return apiRequest<Vehicle>(`/vehicles/${id}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export function deleteVehicle(id: number) {
  return apiRequest<void>(`/vehicles/${id}`, {
    method: "DELETE",
  });
}
