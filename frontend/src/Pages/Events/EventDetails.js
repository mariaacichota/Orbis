import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";

const EventDetails = () => {
  const { id } = useParams();
  const [event, setEvent] = useState(null);

  useEffect(() => {
    const mockEvents = JSON.parse(localStorage.getItem("mockEvents")) || [];
    const found = mockEvents.find(e => e.id === Number(id));
    setEvent(found);
  }, [id]);

  const handleAddToCart = () => {
    const currentCart = JSON.parse(localStorage.getItem("cart")) || [];
    localStorage.setItem("cart", JSON.stringify([...currentCart, event]));
    window.dispatchEvent(new CustomEvent("cartUpdated"));
    alert("Evento adicionado ao carrinho!");
  };

  if (!event) return <p className="texto">Evento não encontrado.</p>;

  return (
    <div className="container">
      <h2>{event.title}</h2>

      {event.image && (
        <img
          src={event.image}
          alt={event.title}
          style={{
            width: "100%",
            maxHeight: 300,
            objectFit: "cover",
            borderRadius: 8,
            marginBottom: 20
          }}
        />
      )}

      <p className="texto">{event.description}</p>
      <p><strong>Data:</strong> {event.date}</p>
      <p><strong>Horário:</strong> {event.startTime} às {event.endTime}</p>
      <p><strong>Local:</strong> {event.location}</p>
      <p><strong>Capacidade:</strong> {event.capacity} pessoas</p>
      <p><strong>Categoria:</strong> {event.category}</p>
      <p><strong>Preço:</strong> {event.price === 0 ? "Gratuito" : `R$ ${event.price.toFixed(2)}`}</p>

      <button className="resultado" onClick={handleAddToCart}>
        Adicionar ao carrinho
      </button>
    </div>
  );
};

export default EventDetails;
