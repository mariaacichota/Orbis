import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";
import { useEffect, useState } from "react";
import AppCheckout from "../../components/AppCheckout";

// Chave pública para teste
const stripePromise = loadStripe("pk_test_51RMISM0195wzPBqffmamw3GXJrW3gCZ8CwXjVeHlzO0AboJSFt1M3vikVuoCfag3oGBcfSen3u5QoSx5FJliEvYr006iv6RZtF");

function Cart() {
  const [cartItems, setCartItems] = useState([]);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    const stored = JSON.parse(localStorage.getItem("cart")) || [];
    setCartItems(stored);
    const sum = stored.reduce((acc, item) => acc + (item.price || 0), 0);
    setTotal(sum);
  }, []);

  const handleRemove = (indexToRemove) => {
    const updatedCart = cartItems.filter((_, i) => i !== indexToRemove);
    localStorage.setItem("cart", JSON.stringify(updatedCart));
    setCartItems(updatedCart);
    const sum = updatedCart.reduce((acc, item) => acc + (item.price || 0), 0);
    setTotal(sum);
    window.dispatchEvent(new CustomEvent("cartUpdated"));
  };
  

  return (
    <div className="container">
      <h2>Carrinho</h2>

      {cartItems.length === 0 ? (
        <p className="texto">Seu carrinho está vazio.</p>
      ) : (
        <div style={{ marginBottom: 30 }}>
          {cartItems.map((item, index) => (
            <div key={index} style={{ 
              border: "1px solid #ddd", 
              padding: 15, 
              marginBottom: 10, 
              borderRadius: 8, 
              display: "flex", 
              justifyContent: "space-between",
              alignItems: "center"
            }}>
              <div>
                <p><strong>{item.title}</strong></p>
                <p>{item.date} — {item.startTime} às {item.endTime}</p>
                <p>{item.price === 0 ? "Gratuito" : `R$ ${item.price.toFixed(2)}`}</p>
              </div>
              <button
                onClick={() => handleRemove(index)}
                style={{ background: "#ff4d4f", color: "#fff", border: "none", padding: "5px 10px", borderRadius: 5 }}
              >
                Remover
              </button>
            </div>
          ))}

          <h3>Total: {total === 0 ? "Gratuito" : `R$ ${total.toFixed(2)}`}</h3>
        </div>
      )}

      <Elements stripe={stripePromise}>
        <AppCheckout />
      </Elements>
    </div>
  );
}

export default Cart;
