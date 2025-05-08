import { useState, useEffect } from "react";

const AppUserEdit = ({ user = {}, onSave }) => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    role: "PARTICIPANTE",
  });

  useEffect(() => {
    const storedUser = JSON.parse(localStorage.getItem("userData"));
    if (storedUser) {
      setFormData({
        name: storedUser.name || "",
        email: storedUser.email || "",
        password: "",
        role: storedUser.role || "PARTICIPANTE",
      });
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (onSave) onSave(formData);
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3 style={{ textAlign: "center", marginBottom: "20px" }}>Editar Informações</h3>

      <label>Nome:</label>
      <input name="name" value={formData.name} onChange={handleChange} required />

      <label>Email:</label>
      <input name="email" type="email" value={formData.email} onChange={handleChange} required />

      <label>Senha:</label>
      <input name="password" type="password" value={formData.password} onChange={handleChange} required />

      <label>Tipo de Usuário:</label>
      <select name="role" value={formData.role} onChange={handleChange} required>
        <option value="PARTICIPANTE">Participante</option>
        <option value="ORGANIZADOR">Organizador</option>
      </select>

      <button type="submit" className="resultado">Salvar Alterações</button>
    </form>
  );
};

export default AppUserEdit;
