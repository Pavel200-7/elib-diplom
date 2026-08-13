# СТЕЙДЖ 1: Сборка
FROM node:22-alpine AS builder
WORKDIR /app

ARG VITE_API_BASE_URL
ARG VITE_AUTH_URL

ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
ENV VITE_AUTH_URL=$VITE_AUTH_URL

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# СТЕЙДЖ 2: Запуск (используем serve)
FROM node:20-alpine

# Устанавливаем serve глобально
RUN npm install -g serve

WORKDIR /app

COPY --from=builder /app/dist ./dist

RUN addgroup -S appgroup && adduser -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

EXPOSE 5173

# Healthcheck для serve
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:5173/ || exit 1

CMD ["serve", "-s", "dist", "-l", "5173"]