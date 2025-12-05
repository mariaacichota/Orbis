# Se precisar ajustar o backend para gerar a pasta target
# OBS: comandos testados no Ubuntu 24.04 LTS
## Execute o comando abaixo para gerar o jar na raiz do projeto
cd backend
./mvnw clean package -DskipTests 
cd ..

## Se der problema na execução do mvnw verifique se está como executável com o seguinte comando
ls -la

## Se for preciso torná-lo executável use o comando a seguir
chmod +x mvnw


# Instrucoes para rodar o Docker com K8s
## Acesse o link abaixo e execute as etapas para o sistema operacional
https://docs.docker.com/desktop/setup/install/linux/ubuntu/

## Se tiver qualquer problema de virtualização, como kvm, por exemplo, confira na bios se está habilitada

## Com o Docker Desktop aberto (precisa estar em execução) instale o kubectl na máquina host. Link abaixo
https://kubernetes.io/pt-br/docs/tasks/tools/

## Valide a instalação com o comando
kubectl version --client

## Enquanto não visualizar a versão do kubectl não adianta prosseguir

## Instale o minikube seguindo os passos do seguinte link
https://minikube.sigs.k8s.io/docs/start/


## Valide com o seguinte comando
minikube version

## Construção das imagens localmente (esteja na raiz do projeto, pasta principal, Orbis)
minikube start
docker build -f docker/backend.Dockerfile -t orbis-backend:latest .
docker build -f docker/frontend.Dockerfile -t orbis-frontend:latest .
docker build -f docker/sql.Dockerfile -t orbis-postgres:latest .
docker build -f docker/ticket-service.Dockerfile -t ticket-service:latest .


## Enviar as imagens para o k8s
minikube image load orbis-backend:latest
minikube image load orbis-frontend:latest
minikube image load orbis-postgres:latest
minikube image load ticket-service:latest


## Aplicar os yamls
kubectl apply -f k8s/postgres-configmap.yaml
kubectl apply -f k8s/postgres-pvc.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml

kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/backend-service.yaml

kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/frontend-service.yaml

kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/mongo-service.yaml

kubectl apply -f k8s/ticket-service-configmap.yaml
kubectl apply -f k8s/ticket-service-deployment.yaml
kubectl apply -f k8s/ticket-service-service.yaml


## (Opcional) Inicie o tunnel para o LoadBalancer funcionar no cluster local (com NodePort não precisa)
minikube tunnel

## Ver a aplicação rodando
minikube service orbis-backend (apenas para utilizar o postman, o site não vai rodar com este comando)
minikube service orbis-frontend


## Ver réplicas
kubectl get deployment orbis-frontend
kubectl get deployment orbis-backend


## Teste no Postman
Escolha o método POST e pegue a porta que vai aparecer no serviço do backend
http://127.0.0.1:<PORTA_DO_BACKEND>/api/auth/sign-up
Body > Raw > JSON
{
  "name": "Teste",
  "email": "teste@gmail.com",
  "password": "Teste23!",
  "role": "PARTICIPANTE"
}


## Para conferir o Banco de Dados
kubectl exec -it $(kubectl get pod -l app=postgres -o name) -- psql -U postgres -d orbis
\dt para listar as tabelas
Execute a query
q para sair das tabelas
\q para voltar ao terminal


## Monitorar
kubectl get pods
kubectl get services
kubectl get all

## Log do backend em tempo real
kubectl logs -f -l app=orbis-backend

## Interromper
minikube stop

## Se precisar forcar um restart de imagem
kubectl rollout restart deployment orbis-frontend

## Se precisar deletar pod para testar replicas
kubectl delete pod <NOME_DO_POD>

## Se precisar deletar o servico inteiro
kubectl delete -f k8s/<NOME_DO_SERVICO>

## Para automatizar a execucao do K8s execute o seguinte comando (está na raiz do projeto)
./deploy-all.sh

## Se não reconhecer como executável tente
chmod +x deploy-all.sh
./deploy-all.sh


## Se der problema e precisar recriar do zero delete tudo com os seguintes comandos
kubectl delete --all deployments --all-namespaces
kubectl delete --all services --all-namespaces
kubectl delete --all configmaps --all-namespaces
kubectl delete --all pvc --all-namespaces
kubectl delete --all pods --all-namespaces
docker rmi -f orbis-backend orbis-frontend orbis-postgres
docker system prune -af --volumes
minikube delete
rm -rf ~/.docker


## Se precisar deletar e usar rollout
docker build -f docker/backend.Dockerfile -t orbis-backend .
minikube image load orbis-backend
kubectl rollout restart deployment orbis-backend

docker build -f docker/frontend.Dockerfile -t orbis-frontend .
minikube image load orbis-frontend
kubectl rollout restart deployment orbis-frontend

## Para verificar a versão deployada
kubectl get deployment orbis-frontend -o jsonpath="{.spec.template.spec.containers[0].image}{'\n'}"

## Subindo apenas a att de alguma imagem específica (ex: front)
docker build -t orbis-frontend:latest -f docker/frontend.Dockerfile .
minikube image load orbis-frontend:latest
kubectl delete pod -l app=orbis-frontend


## Conferir o nginx do front da imagem
kubectl exec -it $(kubectl get pods -l app=orbis-frontend -o jsonpath='{.items[0].metadata.name}') -- cat /etc/nginx/conf.d/default.conf