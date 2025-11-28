FROM node:18-alpine AS dev
WORKDIR /app
EXPOSE 8080
CMD ["npm", "run", "serve", "--", "--host", "0.0.0.0"]

FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine AS prod
WORKDIR /usr/share/nginx/html
COPY --from=build /app/dist ./
COPY nginx.conf /etc/nginx/conf.d/default.conf
