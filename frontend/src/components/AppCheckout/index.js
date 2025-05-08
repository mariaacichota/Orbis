import { useState } from "react";
import { CardElement, useStripe, useElements } from "@stripe/react-stripe-js";

const AppCheckout = () => {
  const stripe = useStripe();
  const elements = useElements();
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!stripe || !elements) return;

    const { error, paymentMethod } = await stripe.createPaymentMethod({
      type: "card",
      card: elements.getElement(CardElement),
    });

    if (error) {
      setMessage(error.message);
    } else {
      setMessage("Pagamento simulado com sucesso! (modo teste)");
      console.log("PaymentMethod:", paymentMethod);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3 style={{ textAlign: "center", marginBottom: "20px" }}>
        Checkout de Pagamento
      </h3>

      <p className="texto">
        Use o cartão de teste: <strong>4242 4242 4242 4242</strong><br />
        Data: <strong>12/34</strong> – CVC: <strong>123</strong>
      </p>

      <div style={{ backgroundColor: "#fff", padding: "10px", borderRadius: "6px", marginBottom: "15px", border: "1px solid #ccc" }}>
        <CardElement options={{ hidePostalCode: true }} />
      </div>

      <button
        type="submit"
        disabled={!stripe}
        className="resultado"
      >
        Pagar (Simulação)
      </button>

      {message && <p className="texto" style={{ marginTop: "12px" }}>{message}</p>}
    </form>
  );
};

export default AppCheckout;
