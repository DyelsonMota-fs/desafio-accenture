export type FieldErrors = Record<string, string>;

export class ApiError extends Error {
  status: number;
  fieldErrors?: FieldErrors;

  constructor(message: string, status: number, fieldErrors?: FieldErrors) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

const API_URL = (
  import.meta.env.VITE_API_URL ?? "http://localhost:8080"
).replace(/\/$/, "");

async function parseResponse(response: Response) {
  if (response.status === 204) {
    return undefined;
  }

  const contentType = response.headers.get("content-type") ?? "";

  if (contentType.includes("application/json")) {
    return response.json();
  }

  return response.text();
}

export async function apiRequest<T>(
  path: string,
  init?: RequestInit,
): Promise<T> {
  let response: Response;

  try {
    response = await fetch(`${API_URL}${path}`, {
      ...init,
      headers: {
        "Content-Type": "application/json",
        ...init?.headers,
      },
    });
  } catch {
    throw new ApiError(
      "Não foi possível conectar ao servidor. Verifique se o backend está em execução.",
      0,
    );
  }

  const data = await parseResponse(response);

  if (!response.ok) {
    if (typeof data === "string" && data) {
      throw new ApiError(data, response.status);
    }

    if (data && typeof data === "object") {
      const fieldErrors = data as FieldErrors;

      const firstMessage = Object.values(fieldErrors).find(
        (value): value is string => typeof value === "string",
      );

      throw new ApiError(
        firstMessage ?? "Não foi possível concluir a operação.",
        response.status,
        fieldErrors,
      );
    }

    throw new ApiError(
      "Não foi possível concluir a operação.",
      response.status,
    );
  }

  return data as T;
}
