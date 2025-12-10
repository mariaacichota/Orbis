## 📊 Como rodar os Logs (Grafana + Loki)

Para ver os logs centralizados, você precisa do **Helm** instalado.

1. **Instale o Helm** (se não tiver):
   - Windows: `choco install kubernetes-helm`
   - Ou baixe em: https://helm.sh/docs/intro/install/

2. **Instale o Monitoramento**:
   - Rode o arquivo `instalar-logs.bat` que está na raiz do projeto.

3. **Acesse**:
   - **Passo 1: Pegue a senha de admin** (Copie e cole no terminal):
     ```powershell
     kubectl get secret --namespace monitoring loki-stack-grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo
     ```
     *(Se estiver no PowerShell e o comando acima der erro, tente este:)*
     ```powershell
     kubectl get secret --namespace monitoring loki-stack-grafana -o jsonpath="{.data.admin-password}" | ForEach-Object { [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($_)) }
     ```

   - **Passo 2: Libere o acesso ao site**:
     ```powershell
     kubectl port-forward --namespace monitoring svc/loki-stack-grafana 3000:80
     ```

   - **Passo 3: Abra o navegador**:
     - Link: http://localhost:3000
     - Usuário: `admin`
     - Senha: (A que você pegou no Passo 1)
