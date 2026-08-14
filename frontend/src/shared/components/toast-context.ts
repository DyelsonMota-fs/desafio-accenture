import { createContext, useContext } from "react";

export type ToastKind = "success" | "error";

type ToastContextValue = {
  showToast: (message: string, kind?: ToastKind) => void;
};

export const ToastContext = createContext<ToastContextValue | null>(null);

export function useToast() {
  const context = useContext(ToastContext);

  if (!context) {
    throw new Error("useToast deve ser usado dentro de ToastProvider");
  }

  return context;
}
