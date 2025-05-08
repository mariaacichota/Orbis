import "./App.css";
import AppFooter from "./components/AppFooter";
import AppHeader from "./components/AppHeader";
import AppContent from "./components/AppContent";
import AppSideMenu from "./components/AppSideMenu";

function App() {
  return (
    <div className="App">
      <AppHeader />
      <div className="SideMenuContent">
        <AppSideMenu></AppSideMenu>
        <AppContent></AppContent>
      </div>
      <AppFooter />
    </div>
  );
}
export default App;



