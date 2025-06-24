#!/bin/bash

echo "Limpando e gerando .jar do backend..."
cd backend
./mvnw clean package -DskipTests || { echo "❌ Erro ao gerar o .jar"; exit 1; }
cd ..

echo "Buildando imagens Docker..."
minikube start
docker build -f docker/backend.Dockerfile -t orbis-backend:latest .
docker build -f docker/frontend.Dockerfile -t orbis-frontend:latest .
docker build -f docker/sql.Dockerfile -t orbis-postgres:latest .

echo "Carregando imagens no Minikube..."
minikube image load orbis-backend:latest
minikube image load orbis-frontend:latest
minikube image load orbis-postgres:latest

echo "☸Aplicando arquivos do Kubernetes..."
kubectl apply -f k8s/postgres-configmap.yaml
kubectl apply -f k8s/postgres-pvc.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/backend-service.yaml
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/frontend-service.yaml


echo "Aguardando frontend subir..."
kubectl wait --for=condition=available deployment/orbis-frontend --timeout=90s

echo "Acesse a aplicação:"
minikube service orbis-frontend
