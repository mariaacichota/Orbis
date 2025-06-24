import {
    BellFilled,
    MailOutlined,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
} from "@ant-design/icons";
import { Badge, Typography, Button } from "antd";

function AppHeader({ collapsed, onToggleSidebar }) {
    return (
        <div className="AppHeader" style={{ display: "flex", alignItems: "center", gap: 16 }}>
            <Button
                type="text"
                icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
                onClick={onToggleSidebar}
                style={{ fontSize: "18px" }}
            />
            <Typography.Title level={3} className="title" style={{ margin: 0 }}>
                Orbis - Gestão de Eventos
            </Typography.Title>
        </div>
    );
}

export default AppHeader;
