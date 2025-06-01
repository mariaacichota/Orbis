import {
  ShoppingCartOutlined,
  UserOutlined,
  CalendarOutlined,
} from "@ant-design/icons";
import { Menu, Badge } from "antd";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

function AppSideMenu({ collapsed }) {
  const location = useLocation();
  const [selectedKeys, setSelectedKeys] = useState("/");
  const [cartCount, setCartCount] = useState(0);
  const navigate = useNavigate();

  const updateCartCount = () => {
    const cartItems = JSON.parse(localStorage.getItem("cart")) || [];
    setCartCount(cartItems.length);
  };

  useEffect(() => {
    setSelectedKeys(location.pathname);
    updateCartCount();

    const handler = () => updateCartCount();
    window.addEventListener("cartUpdated", handler);

    return () => {
      window.removeEventListener("cartUpdated", handler);
    };
  }, [location.pathname]);

  return (
      <div className="AppSideMenu responsive-menu">
        <Menu
            className="SideMenuVertical"
            mode="inline"
            inlineCollapsed={collapsed}
            onClick={(item) => navigate(item.key)}
            selectedKeys={[selectedKeys]}
            items={[
              {
                label: "Eventos",
                key: "/",
                icon: <CalendarOutlined />,
              },
              {
                label: "Perfil",
                key: "/perfil",
                icon: <UserOutlined />,
              },
              {
                label: "Carrinho",
                key: "/carrinho",
                icon: (
                    <Badge count={cartCount} size="small" offset={[0, 4]}>
                      <ShoppingCartOutlined />
                    </Badge>
                ),
              },
            ]}
        />
      </div>
  );
}

export default AppSideMenu;
