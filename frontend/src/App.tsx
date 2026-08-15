import { Navigate, Route, Routes } from "react-router-dom";
import { DealersPage } from "./features/dealers/DealersPage.tsx";
import { VehiclesPage } from "./features/vehicles/VehiclesPage.tsx";
import { AppShell } from "./shared/components/AppShell.tsx";
import "./shared/styles/pages.css";
import "./shared/styles/catalog.css";

function App() {
  return (
    <Routes>
      <Route element={<AppShell />}>
        <Route index element={<Navigate to="/vehicles" replace />} />

        <Route path="vehicles" element={<VehiclesPage />} />

        <Route path="dealers" element={<DealersPage />} />

        <Route path="*" element={<Navigate to="/vehicles" replace />} />
      </Route>
    </Routes>
  );
}

export default App;
