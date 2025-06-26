import { useState } from "react";

const FormCreateEvent = () => {
  const [form, setForm] = useState({
    title: "",
    description: "",
    date: "",
    time: "",
    location: "",
    maxTickets: "",
    organizerId: localStorage.getItem("userId") || "",
    price: ""
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const today = new Date().toISOString().split("T")[0];
    if (form.date < today) {
      setError("A data do evento não pode ser anterior a hoje.");
      return;
    }

    if (parseFloat(form.price) < 0) {
      setError("O preço não pode ser negativo.");
      return;
    }

    if (parseInt(form.maxTickets) <= 0) {
      setError("A quantidade máxima de ingressos deve ser maior que zero.");
      return;
    }

    if (parseInt(form.organizerId) < 0) {
      setError("O ID do organizador deve ser maior ou igual a zero.");
      return;
    }

    setError("");

    const payload = {
      title: form.title,
      description: form.description,
      date: form.date,
      startTime: form.time,
      endTime: form.time,
      location: form.location,
      capacity: parseInt(form.maxTickets),
      organizerId: parseInt(form.organizerId),
      category: "Outro",
      price: parseFloat(form.price),
      image: ""
    };

    const token = localStorage.getItem("token");

    try {
      const response = await fetch("http://localhost:8080/events", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        alert("Evento cadastrado com sucesso!");
        setForm({
          title: "",
          description: "",
          date: "",
          time: "",
          location: "",
          maxTickets: "",
          organizerId: localStorage.getItem("userId") || "",
          price: ""
        });
      } else {
        alert("Erro ao cadastrar evento.");
      }
    } catch (err) {
      alert("Erro inesperado.");
      console.error(err);
    }
  };

  return (
    <form className="container" onSubmit={handleSubmit}>
      <h2>Cadastrar Evento</h2>

      <label>Título</label>
      <input type="text" name="title" required value={form.title} onChange={handleChange} />

      <label>Descrição</label>
      <input type="text" name="description" required value={form.description} onChange={handleChange} />

      <label>Data</label>
      <input type="date" name="date" required value={form.date} onChange={handleChange} />

      <label>Horário</label>
      <input type="time" name="time" required value={form.time} onChange={handleChange} />

      <label>Local</label>
      <input type="text" name="location" required value={form.location} onChange={handleChange} />

      <label>Quantidade Máxima de Ingressos</label>
      <input type="number" name="maxTickets" required value={form.maxTickets} onChange={handleChange} />

      <label>ID do Organizador</label>
      <input type="number" name="organizerId" required value={form.organizerId} onChange={handleChange} />

      <label>Preço (R$)</label>
      <input type="number" name="price" step="0.01" value={form.price} onChange={handleChange} />

      {error && <span style={{ color: "red", marginBottom: "10px" }}>{error}</span>}

      <button className="resultado" type="submit">Cadastrar Evento</button>
    </form>
  );
};

export default FormCreateEvent;
