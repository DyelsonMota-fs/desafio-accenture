import { CheckCircle2, CircleAlert, X } from "lucide-react";
import {
  useCallback,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import { ToastContext, type ToastKind } from "./toast-context.ts";

type Toast = {
  id: number;
  message: string;
  kind: ToastKind;
};

export function ToastProvider({ children }: { children: ReactNode }) {
  const [toast, setToast] = useState<Toast | null>(null);

  const showToast = useCallback(
    (message: string, kind: ToastKind = "success") => {
      setToast({
        id: Date.now(),
        message,
        kind,
      });
    },
    [],
  );

  useEffect(() => {
    if (!toast) {
      return;
    }

    const timer = window.setTimeout(() => setToast(null), 4200);

    return () => window.clearTimeout(timer);
  }, [toast]);

  const value = useMemo(() => ({ showToast }), [showToast]);

  return (
    <ToastContext.Provider value={value}>
      {children}

      {toast && (
        <div
          className={`toast toast--${toast.kind}`}
          role="status"
          key={toast.id}
        >
          {toast.kind === "success" ? (
            <CheckCircle2 size={19} aria-hidden="true" />
          ) : (
            <CircleAlert size={19} aria-hidden="true" />
          )}

          <span>{toast.message}</span>

          <button
            type="button"
            onClick={() => setToast(null)}
            aria-label="Fechar aviso"
          >
            <X size={17} aria-hidden="true" />
          </button>
        </div>
      )}
    </ToastContext.Provider>
  );
}
