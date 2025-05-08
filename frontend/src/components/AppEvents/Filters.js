import { useState, useEffect } from "react";
import { Input, Select, DatePicker, Space, Button, Form, Card, TimePicker } from "antd";

const { RangePicker } = DatePicker;

const Filters = ({ onApplyFilters, onResetFilters, initialValues }) => {
  const [form] = Form.useForm();
  
  // Atualiza o formulário quando os valores iniciais mudam
  useEffect(() => {
    if (initialValues) {
      form.setFieldsValue({
        search: initialValues.search || "",
        region: initialValues.region || undefined,
        category: initialValues.category || undefined,
        dateRange: initialValues.dateRange || null
      });
    }
  }, [initialValues, form]);
  
  const handleFilter = () => {
    const values = form.getFieldsValue();
    onApplyFilters(values);
  };
  
  const handleReset = () => {
    form.resetFields();
    onResetFilters();
  };
  
  return (
    <Card className="shadow-sm">
      <Form
        form={form}
        layout="vertical"
        onFinish={handleFilter}
        initialValues={{
          search: "",
          region: undefined,
          category: undefined,
          dateRange: null
        }}
      >
        <Space wrap align="end" size="large" style={{ width: "100%" }}>
          <Form.Item name="search" style={{ marginBottom: 0, minWidth: 220 }}>
            <Input.Search 
              placeholder="Buscar por nome do evento" 
              allowClear
            />
          </Form.Item>
          
          <Form.Item name="category" style={{ marginBottom: 0, minWidth: 160 }}>
            <Select
              placeholder="Categoria"
              allowClear
              style={{ width: '100%' }}
            >
              <Select.Option value="TECHNOLOGY">Tecnologia</Select.Option>
              <Select.Option value="MUSIC">Música</Select.Option>
              <Select.Option value="EDUCATION">Educação</Select.Option>
              <Select.Option value="BUSINESS">Negócios</Select.Option>
              <Select.Option value="SPORTS">Esportes</Select.Option>
              <Select.Option value="HEALTH">Saúde</Select.Option>
              <Select.Option value="ART">Arte</Select.Option>
            </Select>
          </Form.Item>
          
          <Form.Item name="region" style={{ marginBottom: 0, minWidth: 160 }}>
            <Select
              placeholder="Região"
              allowClear
              style={{ width: '100%' }}
            >
              <Select.Option value="north">Norte</Select.Option>
              <Select.Option value="south">Sul</Select.Option>
              <Select.Option value="southeast">Sudeste</Select.Option>
              <Select.Option value="midwest">Centro-Oeste</Select.Option>
              <Select.Option value="northeast">Nordeste</Select.Option>
            </Select>
          </Form.Item>
          
          <Form.Item name="dateRange" style={{ marginBottom: 0 }}>
            <RangePicker
              format="DD/MM/YYYY"
              placeholder={["Data início", "Data fim"]}
            />
          </Form.Item>
          
          <Form.Item style={{ marginBottom: 0 }}>
            <Space>
              <Button type="primary" htmlType="submit">
                Aplicar Filtros
              </Button>
              <Button onClick={handleReset}>
                Limpar
              </Button>
            </Space>
          </Form.Item>
        </Space>
      </Form>
    </Card>
  );
};

export default Filters;