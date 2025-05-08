import { Routes, Route } from "react-router-dom";
import Profile from "../../Pages/Users";
import Register from "../../Pages/Users/register";
import Cart from "../../Pages/Cart";
import Events from "../../Pages/Events";
import EventCreate from "../../Pages/Events/EventCreate";
import Logged from "../../Pages/Users/logged";
import Edit from "../../Pages/Users/edit";
import Terms from "../../Pages/Utils/terms";
import Politics from "../../Pages/Utils/politics";
import PrivateRoute from "./PrivateRoute";
import EventDetails from "../../Pages/Events/EventDetails";

function AppRoutes() {
  return (
    <Routes>
      <Route path="/perfil" element={<Profile />} />
      <Route path="/cadastro" element={<Register />} />
      <Route path="/carrinho" element={<Cart />} />
      <Route path="/termos" element={<Terms />} />
      <Route path="/politica" element={<Politics />} />
      <Route path="/eventos/:id" element={<EventDetails />} />

      <Route
        path="/eventos/novo"
        element={
          <PrivateRoute>
            <EventCreate />
          </PrivateRoute>
        }
      />
      <Route
        path="/logado"
        element={
          <PrivateRoute>
            <Logged />
          </PrivateRoute>
        }
      />
      <Route
        path="/editar"
        element={
          <PrivateRoute>
            <Edit />
          </PrivateRoute>
        }
      />

      <Route path="/" element={<Events />} />
    </Routes>
  );
}

export default AppRoutes;
