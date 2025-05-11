import "./App.css";
import AppFooter from "./components/AppFooter";
import AppHeader from "./components/AppHeader";
import AppContent from "./components/AppContent";
import AppSideMenu from "./components/AppSideMenu";
import { useState } from "react";

function App() {
    const [collapsed, setCollapsed] = useState(false);

    return (
        <div className="App">
            <AppHeader
                collapsed={collapsed}
                onToggleSidebar={() => setCollapsed(!collapsed)}
            />
            <div className="SideMenuContent">
                {!collapsed && <AppSideMenu collapsed={collapsed} />}
                <AppContent />
            </div>
            <AppFooter />
        </div>
    );
}

export default App;
