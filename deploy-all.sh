#!/bin/bash

echo "🔧 Iniciando Minikube..."
minikube start --driver=docker

echo "⬇️ Baixando imagens necessárias..."
docker pull consul:1.15.4
docker pull mongo:latest

echo "Gerando JARs dos microserviços..."

cd ticket-service
mvn clean package -DskipTests || { echo "❌ Erro ao gerar ticket-service.jar"; exit 1; }
cd ..

cd event-service
mvn clean package -DskipTests || { echo "❌ Erro ao gerar event-service.jar"; exit 1; }
cd ..

cd orbis-login
mvn clean package -DskipTests || { echo "❌ Erro ao gerar orbis-login.jar"; exit 1; }
cd ..

cd orbis-gateway
mvn clean package -DskipTests || { echo "❌ Erro ao gerar orbis-gateway.jar"; exit 1; }
cd ..

echo "Buildando imagens Docker..."

docker build -f docker/ticket-service.Dockerfile -t ticket-service:latest .
docker build -f docker/event-service.Dockerfile -t event-service:latest .
docker build -f docker/orbis-login.Dockerfile -t orbis-login:latest .
docker build -f docker/orbis-gateway.Dockerfile -t orbis-gateway:latest .
docker build -f docker/postgres.Dockerfile -t orbis-postgres:latest .

echo "Carregando imagens no Minikube..."

minikube image load ticket-service:latest
minikube image load event-service:latest
minikube image load orbis-login:latest
minikube image load orbis-gateway:latest
minikube image load orbis-postgres:latest
minikube image load mongo:latest
minikube image load consul:1.15.4

echo "Aplicando YAMLs no Kubernetes..."

kubectl apply -f k8s/consul-deployment.yaml
kubectl apply -f k8s/consul-service.yaml

kubectl apply -f k8s/postgres-configmap.yaml
kubectl apply -f k8s/postgres-pvc.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml

kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/mongo-service.yaml

kubectl apply -f k8s/rabbitmq-configmap.yaml
kubectl apply -f k8s/rabbitmq-pvc.yaml
kubectl apply -f k8s/rabbitmq-deployment.yaml
kubectl apply -f k8s/rabbitmq-service.yaml

kubectl apply -f k8s/ticket-service-configmap.yaml
kubectl apply -f k8s/ticket-service-deployment.yaml
kubectl apply -f k8s/ticket-service-service.yaml

kubectl apply -f k8s/event-service-deployment.yaml
kubectl apply -f k8s/event-service-service.yaml

kubectl apply -f k8s/orbis-login-deployment.yaml
kubectl apply -f k8s/orbis-login-service.yaml

kubectl apply -f k8s/orbis-gateway-deployment.yaml
kubectl apply -f k8s/orbis-gateway-service.yaml

echo "Aguardando gateway subir..."
kubectl wait --for=condition=available deployment/orbis-gateway --timeout=90s

echo "Acesse via Minikube:"
minikube service orbis-gateway