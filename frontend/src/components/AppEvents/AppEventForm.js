import React, { useState } from "react";

const AppEventForm = () => {
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    date: "",
    time: "",
    location: "",
    maxTickets: "",
    organizerId: ""
  });

  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/events", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          ...formData,
          maxTickets: parseInt(formData.maxTickets),
          organizerId: parseInt(formData.organizerId)
        })
      });

      if (!response.ok) {
        const err = await response.json();
        throw new Error(err.message || "Erro ao criar evento.");
      }

      setMessage("Evento criado com sucesso!");
      setFormData({
        title: "",
        description: "",
        date: "",
        time: "",
        location: "",
        maxTickets: "",
        organizerId: ""
      });
    } catch (error) {
      setMessage("Erro: " + error.message);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "10px", maxWidth: "400px" }}>
      <input name="title" placeholder="Título" value={formData.title} onChange={handleChange} required />
      <textarea name="description" placeholder="Descrição" value={formData.description} onChange={handleChange} required />
      <input name="date" type="date" value={formData.date} onChange={handleChange} required />
      <input name="time" type="time" value={formData.time} onChange={handleChange} required />
      <input name="location" placeholder="Local" value={formData.location} onChange={handleChange} required />
      <input name="maxTickets" type="number" placeholder="Capacidade Máxima" value={formData.maxTickets} onChange={handleChange} required />
      <input name="organizerId" type="number" placeholder="ID do Organizador" value={formData.organizerId} onChange={handleChange} required />
      <button type="submit">Criar Evento</button>
      {message && <p>{message}</p>}
    </form>
  );
};

export default AppEventForm;
