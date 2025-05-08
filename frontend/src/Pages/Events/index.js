import { useState, useEffect } from "react";
import { Space, Typography, message, Empty, Button } from "antd";
import { useNavigate } from "react-router-dom";
import Filters from "../../components/AppEvents/Filters";
import List from "../../components/AppEvents/List";
import {
  aisummit,
  artexhibition,
  healthexpo,
  musicfestival,
  roboticsfair,
  scienceweek,
  startupday,
  techconference,
  webdevconference,
  sportschampionship
} from "../../assets";

const { Title } = Typography;

const allEvents = [
  {
    id: 1,
    title: "Tech Conference 2025",
    date: "2025-06-15",
    startTime: "09:00",
    endTime: "18:00",
    category: "TECHNOLOGY",
    statusEvent: "ACTIVE",
    region: "southeast",
    location: "Centro de Convenções, São Paulo",
    capacity: 1000,
    featured: true,
    image: techconference,
    description: "Conferência de tecnologia com palestrantes renomados da área de desenvolvimento e inovação",
    price: 150.00
  },
  {
    id: 2,
    title: "AI Summit",
    date: "2025-07-10",
    startTime: "10:00",
    endTime: "17:00",
    category: "TECHNOLOGY",
    statusEvent: "ACTIVE",
    region: "south",
    location: "Centro Empresarial, Porto Alegre",
    capacity: 800,
    featured: true,
    image: aisummit,
    description: "Evento sobre inteligência artificial e machine learning com workshops e painéis de discussão",
    price: 200.00
  },
  {
    id: 3,
    title: "Web Dev Conference",
    date: "2025-07-25",
    startTime: "08:30",
    endTime: "19:00",
    category: "TECHNOLOGY",
    statusEvent: "ACTIVE",
    region: "northeast",
    location: "Teatro Municipal, Recife",
    capacity: 600,
    featured: true,
    image: webdevconference,
    description: "Conferência para desenvolvedores web com foco em novas tecnologias e frameworks",
    price: 120.00
  },
  {
    id: 4,
    title: "Startup Day",
    date: "2025-08-01",
    startTime: "13:00",
    endTime: "21:00",
    category: "BUSINESS",
    statusEvent: "ACTIVE",
    region: "southeast",
    location: "Parque Tecnológico, Rio de Janeiro",
    capacity: 450,
    featured: true,
    image: startupday,
    description: "Evento para startups e empreendedores com pitches e networking",
    price: 90.00
  },
  {
    id: 5,
    title: "Robotics Fair",
    date: "2025-08-15",
    startTime: "10:00",
    endTime: "18:00",
    category: "TECHNOLOGY",
    statusEvent: "ACTIVE",
    region: "midwest",
    location: "Centro de Exposições, Brasília",
    capacity: 1200,
    featured: true,
    image: roboticsfair,
    description: "Feira de robótica e automação com demonstrações e competições",
    price: 0.00
  },
  {
    id: 6,
    title: "Science Week",
    date: "2025-09-01",
    startTime: "08:00",
    endTime: "17:00",
    category: "EDUCATION",
    statusEvent: "ACTIVE",
    region: "north",
    location: "Universidade Federal, Manaus",
    capacity: 300,
    featured: false,
    image: scienceweek,
    description: "Semana de ciência e tecnologia com palestras e experimentos",
    price: 0.00
  },
  {
    id: 7,
    title: "Health Expo",
    date: "2025-09-20",
    startTime: "09:00",
    endTime: "16:00",
    category: "HEALTH",
    statusEvent: "ACTIVE",
    region: "northeast",
    location: "Centro de Convenções, Salvador",
    capacity: 750,
    featured: false,
    image: healthexpo,
    description: "Exposição sobre saúde e bem-estar com profissionais da área",
    price: 30.00
  },
  {
    id: 8,
    title: "Music Festival",
    date: "2025-10-05",
    startTime: "16:00",
    endTime: "23:00",
    category: "MUSIC",
    statusEvent: "ACTIVE",
    region: "south",
    location: "Parque da Cidade, Curitiba",
    capacity: 5000,
    featured: false,
    image: musicfestival,
    description: "Festival de música com artistas nacionais e internacionais",
    price: 250.00
  },
  {
    id: 9,
    title: "Art Exhibition",
    date: "2025-10-12",
    startTime: "10:00",
    endTime: "20:00",
    category: "ART",
    statusEvent: "ACTIVE",
    region: "southeast",
    location: "Museu de Arte Moderna, São Paulo",
    capacity: 200,
    featured: false,
    image: artexhibition,
    description: "Exposição de arte contemporânea com artistas emergentes",
    price: 0.00
  },
  {
    id: 10,
    title: "Sports Championship",
    date: "2025-11-05",
    startTime: "14:00",
    endTime: "18:00",
    category: "SPORTS",
    statusEvent: "ACTIVE",
    region: "south",
    location: "Ginásio Municipal, Florianópolis",
    capacity: 3000,
    featured: false,
    image: sportschampionship,
    description: "Campeonato esportivo com diversas modalidades",
    price: 40.00
  }
];

const Events = () => {
  const navigate = useNavigate();

  const [events, setEvents] = useState(allEvents);
  const [featuredEvents, setFeaturedEvents] = useState([]);
  const [filters, setFilters] = useState({
    search: "",
    region: null,
    dateRange: null
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const featuredOnly = allEvents.filter(event => event.featured);
    setFeaturedEvents(featuredOnly);
    localStorage.setItem("mockEvents", JSON.stringify(allEvents)); // <- Agora dentro do useEffect corretamente
  }, []);

  const applyFilters = (filterValues) => {
    setLoading(true);
    setTimeout(() => {
      let filteredEvents = [...allEvents];

      if (filterValues.search) {
        filteredEvents = filteredEvents.filter(event =>
          event.title.toLowerCase().includes(filterValues.search.toLowerCase())
        );
      }

      if (filterValues.region) {
        filteredEvents = filteredEvents.filter(event =>
          event.region === filterValues.region
        );
      }

      if (filterValues.category) {
        filteredEvents = filteredEvents.filter(event =>
          event.category === filterValues.category
        );
      }

      if (filterValues.dateRange && filterValues.dateRange[0] && filterValues.dateRange[1]) {
        const startDate = filterValues.dateRange[0].format('YYYY-MM-DD');
        const endDate = filterValues.dateRange[1].format('YYYY-MM-DD');

        filteredEvents = filteredEvents.filter(event =>
          event.date >= startDate && event.date <= endDate
        );
      }

      setEvents(filteredEvents);
      setFilters(filterValues);
      setLoading(false);

      if (filteredEvents.length === 0) {
        message.info('Nenhum evento encontrado com os filtros selecionados');
      }
    }, 500);
  };

  const resetFilters = () => {
    setEvents(allEvents);
    setFilters({
      search: "",
      region: null,
      dateRange: null
    });
  };

  return (
    <Space direction="vertical" size="large" style={{ width: "100%" }}>
      <Title level={2}>Eventos Disponíveis</Title>

      <Button
        type="primary"
        style={{ backgroundColor: "#4caf50", borderColor: "#4caf50", width: 180 }}
        onClick={() => navigate("/eventos/novo")}
      >
        Criar Evento
      </Button>

      <Filters
        onApplyFilters={applyFilters}
        onResetFilters={resetFilters}
        initialValues={filters}
      />

      <div>
        <Title level={4}>Todos os Eventos ({events.length})</Title>
        {events.length > 0 ? (
          <List events={events} loading={loading} />
        ) : (
          <Empty
            description="Nenhum evento encontrado"
            style={{ margin: '40px 0' }}
          />
        )}
      </div>
    </Space>
  );
};

export default Events;
