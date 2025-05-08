import { useState } from "react";
import { useNavigate } from "react-router-dom";

const AppUserRegister = ({ onRegister }) => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    role: "PARTICIPANTE"
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (onRegister) {
      onRegister(formData);
    }
  };

  return (
    <div className="container">
      <form onSubmit={handleSubmit}>
        <label>Nome:</label>
        <input
          name="name"
          value={formData.name}
          onChange={handleChange}
          required
        />

        <label>Email:</label>
        <input
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          required
        />

        <label>Senha:</label>
        <input
          name="password"
          type="password"
          value={formData.password}
          onChange={handleChange}
          required
        />

        <label>Tipo de Usuário:</label>
        <select
          name="role"
          value={formData.role}
          onChange={handleChange}
          required
        >
          <option value="PARTICIPANTE">Participante</option>
          <option value="ORGANIZADOR">Organizador</option>
        </select>

        <button type="submit" className="resultado">
          Cadastrar
        </button>

        <button
          type="button"
          onClick={() => navigate("/perfil")}
          style={{
            backgroundColor: "#e0e0e0",
            color: "#333",
            padding: "10px",
            width: "100%",
            border: "none",
            borderRadius: "5px",
            marginTop: "10px",
            cursor: "pointer"
          }}
        >
          Voltar para Login
        </button>
      </form>
    </div>
  );
};

export default AppUserRegister;
