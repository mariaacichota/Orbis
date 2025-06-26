FROM node:18 as build
WORKDIR /app
COPY ../frontend/package*.json ./
RUN npm install
COPY ../frontend/ .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY ../frontend/nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
