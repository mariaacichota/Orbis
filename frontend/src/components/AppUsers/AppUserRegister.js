import { useState } from "react";
import { useNavigate } from "react-router-dom";

const AppUserRegister = () => {
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

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("/api/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao registrar.");
      }

      alert("Usuário cadastrado com sucesso!");
      navigate("/");
    } catch (err) {
      alert("Erro: " + err.message);
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
      </form>
    </div>
  );
};

export default AppUserRegister;
