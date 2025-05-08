import { Navigate } from "react-router-dom";

const PrivateRoute = ({ children }) => {
  const isAuthenticated = !!localStorage.getItem("userData");
  return isAuthenticated ? children : <Navigate to="/perfil" replace />;
};

export default PrivateRoute;
