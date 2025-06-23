// export const getUsuarios = () => {
//     return fetch("http://localhost:8080/users").then((res) => res.json());
//   };
  
//   export const getEventos = () => {
//     return fetch("http://localhost:8080/events").then((res) => res.json());
//   };
  
//   export const getEventoPorId = (id) => {
//     return fetch(`http://localhost:8080/events/${id}`).then((res) => res.json());
//   };
  
//   export const criarEvento = (evento) => {
//     return fetch("http://localhost:8080/events", {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/json",
//       },
//       body: JSON.stringify(evento),
//     }).then((res) => res.json());
//   };
  