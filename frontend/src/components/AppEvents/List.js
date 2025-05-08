import { Row, Col, Card, Skeleton, Badge, Tooltip, Button, Tag } from "antd";
import { Link } from "react-router-dom";
import { 
  CalendarOutlined, 
  EnvironmentOutlined, 
  InfoCircleOutlined, 
  ClockCircleOutlined, 
  TeamOutlined,
  LaptopOutlined,
  SoundOutlined,
  BookOutlined,
  ShopOutlined,
  TrophyOutlined,
  MedicineBoxOutlined,
  PictureOutlined
} from "@ant-design/icons";

// Mapeamento de regiões
const regionNames = {
  north: "Norte",
  south: "Sul",
  southeast: "Sudeste",
  midwest: "Centro-Oeste",
  northeast: "Nordeste"
};

// Ícones e cores por categoria
const categoryIcons = {
  TECHNOLOGY: { icon: <LaptopOutlined />, color: "blue" },
  MUSIC: { icon: <SoundOutlined />, color: "purple" },
  EDUCATION: { icon: <BookOutlined />, color: "green" },
  BUSINESS: { icon: <ShopOutlined />, color: "gold" },
  SPORTS: { icon: <TrophyOutlined />, color: "cyan" },
  HEALTH: { icon: <MedicineBoxOutlined />, color: "red" },
  ART: { icon: <PictureOutlined />, color: "magenta" }
};

// Tradução das categorias
const categoryNames = {
  TECHNOLOGY: "Tecnologia",
  MUSIC: "Música",
  EDUCATION: "Educação",
  BUSINESS: "Negócios",
  SPORTS: "Esportes",
  HEALTH: "Saúde",
  ART: "Arte"
};

// Formatação de data
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('pt-BR');
};

const formatTime = (timeString) => timeString;

const List = ({ events, loading }) => {
  return (
    <Row gutter={[16, 16]}>
      {loading ? (
        Array(4).fill().map((_, index) => (
          <Col key={`skeleton-${index}`} xs={24} sm={12} md={8} lg={6}>
            <Card>
              <Skeleton active avatar paragraph={{ rows: 3 }} />
            </Card>
          </Col>
        ))
      ) : (
        events.map((event) => (
          <Col key={event.id} xs={24} sm={12} md={8} lg={6}>
            <Badge.Ribbon 
              text={event.featured ? "Destaque" : ""} 
              color="blue" 
              style={{ display: event.featured ? 'block' : 'none' }}
            >
              <Card 
                hoverable 
                cover={
                  <div style={{position: 'relative'}}>
                    <img 
                      alt={event.title} 
                      src={event.image} 
                      style={{ height: 180, objectFit: 'cover', width: '100%' }} 
                    />
                    <Tag 
                      icon={categoryIcons[event.category]?.icon}
                      color={categoryIcons[event.category]?.color}
                      style={{ 
                        position: 'absolute', 
                        bottom: '10px', 
                        right: '10px',
                        padding: '2px 8px'
                      }}
                    >
                      {categoryNames[event.category]}
                    </Tag>
                  </div>
                }
                actions={[
                  <Link to={`/eventos/${event.id}`} key="details">
                    <Button type="link">Ver detalhes</Button>
                  </Link>
                ]}
              >
                <Card.Meta 
                  title={event.title} 
                  description={
                    <>
                      <p>
                        <CalendarOutlined style={{ marginRight: 8 }} />
                        {formatDate(event.date)}
                      </p>
                      <p>
                        <ClockCircleOutlined style={{ marginRight: 8 }} />
                        {formatTime(event.startTime)} - {formatTime(event.endTime)}
                      </p>
                      <p>
                        <EnvironmentOutlined style={{ marginRight: 8 }} />
                        {event.location || regionNames[event.region]}
                      </p>
                      <p>
                        <TeamOutlined style={{ marginRight: 8 }} />
                        Capacidade: {event.capacity} pessoas
                      </p>
                      <Tooltip title={event.description}>
                        <p style={{ cursor: 'pointer', display: 'flex', alignItems: 'center' }}>
                          <InfoCircleOutlined style={{ marginRight: 8 }} />
                          <span style={{ 
                            whiteSpace: 'nowrap',
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            maxWidth: '200px'
                          }}>
                            {event.description}
                          </span>
                        </p>
                      </Tooltip>
                    </>
                  }
                />
              </Card>
            </Badge.Ribbon>
          </Col>
        ))
      )}
    </Row>
  );
};

export default List;
