import React from "react";
import { useNavigate } from "react-router-dom";
import AppUserEdit from "../../components/AppUsers/AppUserEdit";

const EditUser = () => {
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");

  const initialData = {
    userName: localStorage.getItem("userName") || "",
    email: localStorage.getItem("email") || "",
    role: localStorage.getItem("role") || "PARTICIPANTE",
  };

  const handleSave = async (formData) => {
    try {
      const response = await fetch(`http://localhost:8080/users/${userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: token, 
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao atualizar usuário");
      }

      const updatedUser = await response.json();

      localStorage.setItem("userName", updatedUser.name);
      localStorage.setItem("email", updatedUser.email); 
      localStorage.setItem("role", updatedUser.role);    
      localStorage.setItem("userData", JSON.stringify(updatedUser));

      alert("Usuário atualizado com sucesso!");
      navigate("/logado");

    } catch (err) {
      alert("Erro: " + err.message);
    }
  };

  return <AppUserEdit onSave={handleSave} initialData={initialData} />;
};

export default EditUser;
