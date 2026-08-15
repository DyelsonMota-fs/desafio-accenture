import { Building2, CarFront, Menu, X } from "lucide-react";
import { useState } from "react";
import { NavLink, Outlet } from "react-router-dom";
import "./AppShell.css";

export function AppShell() {
  const [menuOpen, setMenuOpen] = useState(false);

  const closeMenu = () => setMenuOpen(false);

  return (
    <div className="app-shell">
      <header className="site-header">
        <div className="site-header__inner container">
          <NavLink
            className="brand"
            to="/vehicles"
            onClick={closeMenu}
            aria-label="Stellantis Motors"
          >
            <span className="brand__mark">S</span>

            <span className="brand__name">STELLANTIS</span>

            <span className="brand__suffix">MOTORS</span>
          </NavLink>

          <button
            className="menu-button"
            type="button"
            onClick={() => setMenuOpen((value) => !value)}
            aria-expanded={menuOpen}
            aria-label={menuOpen ? "Fechar menu" : "Abrir menu"}
          >
            {menuOpen ? <X size={21} /> : <Menu size={21} />}
          </button>

          <nav
            className={`site-nav ${menuOpen ? "site-nav--open" : ""}`}
            aria-label="Principal"
          >
            <NavLink to="/vehicles" onClick={closeMenu}>
              <CarFront size={17} aria-hidden="true" />
              Veículos
            </NavLink>

            <NavLink to="/dealers" onClick={closeMenu}>
              <Building2 size={17} aria-hidden="true" />
              Concessionárias
            </NavLink>
          </nav>

          <div className="header-status" title="Ambiente de gestão">
            <span />
            Gestão de estoque
          </div>
        </div>
      </header>

      <main className="main-content">
        <Outlet />
      </main>

      <footer className="site-footer">
        <div className="container site-footer__inner">
          <span>Stellantis Motors</span>

          <span>Inventário &amp; rede de concessionárias</span>
        </div>
      </footer>
    </div>
  );
}
