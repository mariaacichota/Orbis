import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const AppUserLogin = () => {

  const [formData, setFormData] = useState({
    email: "",
    password: ""
  });

  const navigate = useNavigate();

  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (userId) {
      navigate("/logado");
    }
  }, [navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("/api/auth/sign-in", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao fazer login.");
      }

      const user = await response.json();

      localStorage.setItem("userName", user.name);
      localStorage.setItem("userId", user.id);
      localStorage.setItem("role", user.role);
      localStorage.setItem("email", user.emailAddress);
      localStorage.setItem("token", user.token);
      localStorage.setItem("userData", JSON.stringify(user));

      navigate("/logado");
    } catch (err) {
      alert("Erro: " + err.message);
    }
  };

  const handleCadastro = () => {
    navigate("/cadastro");
  };

  return (
    <div className="container">
      <h2 className="texto">Login</h2>
      <form onSubmit={handleSubmit}>
        <label>Email:</label>
        <input
          name="email"
          value={formData.email}
          onChange={handleChange}
          required
          type="email"
        />

        <label>Senha:</label>
        <input
          name="password"
          value={formData.password}
          onChange={handleChange}
          required
          type="password"
        />

        <button type="submit" className="resultado">Entrar</button>
        <button type="button" onClick={handleCadastro} className="resultado" style={{ marginTop: "10px" }}>
          Cadastrar
        </button>
        <div style={{ marginTop: "15px", textAlign: "center" }}>
          <button
            type="button"
            onClick={() => {
              alert("Funcionalidade 'Esqueci minha senha' será implementada em breve.");
            }}
            style={{ 
              color: "#007bff", 
              textDecoration: "underline",
              background: "none",
              border: "none",
              cursor: "pointer",
              font: "inherit"
            }}
          >
            Esqueci minha senha
          </button>
        </div>
      </form>
    </div>
  );
};

export default AppUserLogin;
