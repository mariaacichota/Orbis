import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const Logged = () => {
  const navigate = useNavigate();

  const [userName, setUserName] = useState("");
  const [role, setRole] = useState("");
  const [userId, setUserId] = useState("");
  const [email, setEmail] = useState("");

  useEffect(() => {

    setUserName(localStorage.getItem("userName"));
    setRole(localStorage.getItem("role"));
    setUserId(localStorage.getItem("userId"));
    setEmail(localStorage.getItem("email"));
  }, []);

  fetch(`/api/users/${storedId}`, {
      method: "GET",
      mode: "cors",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    })
      .then(res => res.json())
      .then(data => {
        setUserName(data.nome || data.name || data.fullName || "");
        localStorage.setItem("userName", data.nome || data.name || data.fullName || "");
      })
      .catch(err => console.error("Erro:", err));
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("userName");
    localStorage.removeItem("userId");
    localStorage.removeItem("role");
    localStorage.removeItem("token");
    navigate("/perfil");
  };

  const handleEdit = () => {
    navigate("/editar-usuario", {
      state: { userName, role, userId, email },
    });
  };

  const handleDelete = () => {
    if (!userId) return alert("ID do usuário não encontrado.");
    if (!window.confirm("Tem certeza que deseja excluir sua conta?")) return;
    const token = localStorage.getItem("token");

    fetch(`/api/users/${userId}`, {
      method: "DELETE",
      headers: {
        Authorization: `${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erro ao deletar conta");
        alert("Conta deletada com sucesso.");
        handleLogout();
      })
      .catch((err) => alert("Erro: " + err.message));
  };

  const handleBuyTicket = () => {
    navigate("/eventos/novo");
  };

  return (
    <div>
      <div className="container">
        <h2>{userName ? `Bem-vindo(a), ${userName}!` : "Bem-vindo(a)!"}</h2>
        <p className="texto">Tipo de Usuário: {role}</p>
        <button className="resultado" onClick={handleLogout}>Sair</button>
        <button className="resultado" onClick={handleEdit}>Editar Informações</button>
        <button className="resultado" onClick={handleDelete}>Deletar Conta</button>
        <button className="resultado" onClick={handleBuyTicket}>Comprar Ticket</button>
      </div>
    </div>
  );
};

export default Logged;