import React from "react";
import { useNavigate } from "react-router-dom";
import AppUserLogin from "../../components/AppUsers/AppUserLogin";

const Login = () => {
  const navigate = useNavigate();

  const handleLogin = (formData) => {
    fetch("/api/auth/sign-in", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData)
    })
      .then(async (res) => {
        if (!res.ok) {
          const err = await res.json();
          throw new Error(err.message || "Erro ao fazer login");
        }
        return res.json();
      })
      .then((data) => {
        localStorage.setItem("token", data.token);
        localStorage.setItem("userName", data.name);
        localStorage.setItem("userId", data.id);
        alert("Login realizado com sucesso!");
        navigate("/logado");
      })
      .catch((err) => {
        alert("Erro: " + err.message);
      });
  };

  return (
    <>
      <AppUserLogin onLogin={handleLogin} />
    </>
  );
};

export default Login;
