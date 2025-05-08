import { BellFilled, MailOutlined } from "@ant-design/icons";
import { Badge, Drawer, Image, List, Space, Typography } from "antd";
import { useEffect, useState } from "react";


function AppHeader(){

    return(
        <div className="AppHeader">        

        <Typography.Title className="title">Orbi - Gestão de Eventos</Typography.Title>
        
        </div>        
    );
}

export default AppHeader;