# Сборка фронта
FROM node:20 AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

# Nginx для выдачи статики + прокси
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Кладём билд
COPY --from=build /app/dist ./

# Кладём конфиг nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf


#FROM node:18-alpine
#WORKDIR /app
#EXPOSE 8080
#CMD ["npm", "run", "serve", "--", "--host", "0.0.0.0"]