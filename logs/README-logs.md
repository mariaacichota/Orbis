## 📊 Como rodar os Logs (Grafana + Loki)

Para ver os logs centralizados, você precisa do **Helm** instalado.

1. **Instale o Helm** (se não tiver):
   - Windows: `choco install kubernetes-helm`
   - Ou baixe em: https://helm.sh/docs/intro/install/

2. **Instale o Monitoramento**:
   - Rode o arquivo `instalar-logs.bat` que está na raiz do projeto.

3. **Acesse**:
   - Pegue a senha com o comando que o script mostrar.
   - Faça o port-forward: `kubectl port-forward --namespace monitoring svc/loki-stack-grafana 3000:80`
   - Abra http://localhost:3000