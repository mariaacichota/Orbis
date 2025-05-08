import { Typography } from "antd";
import { Link } from "react-router-dom";

function AppFooter() {
  return (
    <div className="AppFooter">
      <Link to="/termos">
        <Typography.Text underline style={{ cursor: "pointer" }}>
          Termos de Uso
        </Typography.Text>
      </Link>

      <p className="dark:text-gray-200 text-gray-700 text-center m-20">
        © Projeto de Bloco - Orbi - Gestão de Eventos
      </p>

      <Link to="/politica">
        <Typography.Text underline style={{ cursor: "pointer" }}>
          Política de Privacidade
        </Typography.Text>
      </Link>
    </div>
  );
}

export default AppFooter;
