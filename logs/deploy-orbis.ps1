# Script de Deploy Automatizado - Projeto Orbis
# Autor: Luisa

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "      ORBIS - DEPLOY AUTOMATIZADO     " -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan

# --- 1. CONFIGURAÇÃO DE LOGS (Loki + Grafana) ---
Write-Host "`n[1/7] Instalando Monitoramento (Grafana + Loki)..." -ForegroundColor Yellow
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
helm upgrade --install loki-stack grafana/loki-stack `
  --namespace monitoring `
  --create-namespace `
  --set grafana.enabled=true `
  --set prometheus.enabled=false `
  --set promtail.enabled=true

# --- 2. COMPILAÇÃO JAVA (Maven) ---
Write-Host "`n[2/7] Compilando Microsservicos (Java)..." -ForegroundColor Yellow

# Função para compilar e voltar
function Build-Service ($folder) {
    Write-Host "   -> Compilando $folder..." -ForegroundColor Gray
    Push-Location $folder
    ./mvnw.cmd clean package -DskipTests
    if ($LASTEXITCODE -ne 0) { Write-Error "Falha ao compilar $folder"; exit 1 }
    Pop-Location
}

Build-Service "ticket-service"
Build-Service "event-service"
Build-Service "orbis-login"

# --- 3. DOCKER ENVIRONMENT ---
Write-Host "`n[3/7] Conectando ao Docker do Minikube..." -ForegroundColor Yellow
minikube docker-env | Invoke-Expression

# --- 4. BUILD DAS IMAGENS DOCKER ---
Write-Host "`n[4/7] Criando Imagens Docker..." -ForegroundColor Yellow
docker build -f docker/ticket-service.Dockerfile -t ticket-service:latest .
docker build -f docker/event-service.Dockerfile -t event-service:latest .
docker build -f docker/orbis-login.Dockerfile -t orbis-login:latest .

# --- 5. INFRAESTRUTURA (Bancos e Mensageria) ---
Write-Host "`n[5/7] Subindo Infraestrutura..." -ForegroundColor Yellow
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/mongo-service.yaml
# Se tiver Postgres e RabbitMQ, descomente abaixo:
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml
kubectl apply -f k8s/rabbitmq-deployment.yaml
kubectl apply -f k8s/rabbitmq-service.yaml

Write-Host "   ... Aguardando bancos de dados iniciarem..." -ForegroundColor Gray
# O comando wait evita que os apps quebrem ao tentar conectar num banco desligado
kubectl wait --for=condition=ready pod -l app=mongo --timeout=120s

# --- 6. MICROSSERVIÇOS ---
Write-Host "`n[6/7] Subindo Microsservicos..." -ForegroundColor Yellow
kubectl apply -f k8s/ticket-service-deployment.yaml
kubectl apply -f k8s/event-service-deployment.yaml
kubectl apply -f k8s/orbis-login-deployment.yaml

Write-Host "   ... Aguardando apps iniciarem..." -ForegroundColor Gray
kubectl wait --for=condition=ready pod -l app=ticket-service --timeout=180s

# --- 7. FINALIZAÇÃO ---
Write-Host "`n[7/7] STATUS FINAL:" -ForegroundColor Green
kubectl get pods

Write-Host "`n======================================" -ForegroundColor Cyan
Write-Host "DEPLOY CONCLUIDO COM SUCESSO! 🚀" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host "📊 GRAFANA:"
Write-Host "   1. Pegue a senha: kubectl get secret --namespace monitoring loki-stack-grafana -o jsonpath='{.data.admin-password}' | base64 --decode ; echo"
Write-Host "   2. Libere o acesso: kubectl port-forward --namespace monitoring svc/loki-stack-grafana 3000:80"
Write-Host "   3. Acesse: http://localhost:3000"
Write-Host "======================================" -ForegroundColor Cyan