import React from "react";

const Terms = () => {
  return (
    <div className="container">
      <h2>Termos de Uso</h2>
      <p className="texto">
        Bem-vindo(a) ao Orbis! Ao utilizar este sistema, você concorda com os seguintes termos:
      </p>
      <ul className="texto">
        <li>Você deve fornecer informações verdadeiras ao se cadastrar.</li>
        <li>O uso indevido da plataforma poderá resultar na suspensão da conta.</li>
        <li>Todos os pagamentos em modo de teste não resultam em cobranças reais.</li>
        <li>A Orbis não compartilha seus dados pessoais com terceiros sem consentimento.</li>
        <li>Esses termos podem ser atualizados sem aviso prévio.</li>
      </ul>
      <p className="texto">
        Caso tenha dúvidas, entre em contato com nosso suporte.
      </p>
    </div>
  );
};

export default Terms;
