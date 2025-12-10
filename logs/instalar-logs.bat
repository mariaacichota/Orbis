@echo off
echo ===================================================
echo 🚀 INSTALANDO MONITORAMENTO (GRAFANA + LOKI)
echo ===================================================

echo [1/3] Adicionando repositorio do Grafana...
call helm repo add grafana https://grafana.github.io/helm-charts
call helm repo update

echo [2/3] Instalando a Stack no Minikube...
call helm upgrade --install loki-stack grafana/loki-stack ^
  --namespace monitoring ^
  --create-namespace ^
  --set grafana.enabled=true ^
  --set prometheus.enabled=false ^
  --set promtail.enabled=true

echo.
echo ===================================================
echo ✅ SUCESSO! O Grafana foi instalado.
echo ===================================================
echo.
echo Para pegar a senha de admin, rode no PowerShell:
echo kubectl get secret --namespace monitoring loki-stack-grafana -o jsonpath="{.data.admin-password}" ^| base64 --decode ; echo
echo.
echo Para acessar o painel, rode:
echo kubectl port-forward --namespace monitoring svc/loki-stack-grafana 3000:80
echo.
echo (Acesse http://localhost:3000)
pause