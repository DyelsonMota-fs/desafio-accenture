import {
  CircleAlert,
  LoaderCircle,
  PackageOpen,
  RefreshCw,
} from "lucide-react";
import "./Feedback.css";

export function PageLoader({ label = "Carregando dados" }: { label?: string }) {
  return (
    <div className="feedback-card feedback-card--loading" role="status">
      <LoaderCircle className="spin" size={28} aria-hidden="true" />

      <strong>{label}</strong>
      <span>Isso deve levar apenas alguns segundos.</span>
    </div>
  );
}

export function ErrorState({
  message,
  onRetry,
}: {
  message: string;
  onRetry: () => void;
}) {
  return (
    <div className="feedback-card" role="alert">
      <CircleAlert size={30} aria-hidden="true" />

      <strong>Não foi possível carregar</strong>
      <span>{message}</span>

      <button
        className="button button--secondary"
        type="button"
        onClick={onRetry}
      >
        <RefreshCw size={16} aria-hidden="true" />
        Tentar novamente
      </button>
    </div>
  );
}

type EmptyStateProps = {
  title: string;
  description: string;
  actionLabel?: string;
  onAction?: () => void;
};

export function EmptyState({
  title,
  description,
  actionLabel,
  onAction,
}: EmptyStateProps) {
  return (
    <div className="feedback-card feedback-card--empty">
      <PackageOpen size={32} aria-hidden="true" />

      <strong>{title}</strong>
      <span>{description}</span>

      {actionLabel && onAction && (
        <button
          className="button button--primary"
          type="button"
          onClick={onAction}
        >
          {actionLabel}
        </button>
      )}
    </div>
  );
}
