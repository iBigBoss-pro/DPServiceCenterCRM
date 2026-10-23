# DPServiceCenterCRM — Frontend

## Структура проекта

```
frontend/
├── web/                 # React веб-приложение (Vite + TypeScript)
└── mobile/              # React Native мобильное приложение (Expo) — в разработке
```

## Запуск веб-приложения

### Требования
- Node.js v20+
- npm 10+

### Установка зависимостей
```bash
cd frontend/web
npm install
```

### Запуск
```bash
npm run dev
```

Приложение откроется на http://localhost:5173

### Конфигурация
Скопируйте `.env.example` в `.env` и настройте:
```env
VITE_API_URL=http://localhost:8080
```

## Запуск мобильного приложения (в будущем)
```bash
cd frontend/mobile
npx create-expo-app . --template expo-template-blank-typescript
npm install
npx expo start
```

## Стек технологий

### Веб-приложение
- **React 19** + TypeScript
- **Vite** — сборщик
- **React Router DOM** — навигация
- **Zustand** — состояние (аутентификация)
- **React Query** — кэширование данных
- **Axios** — HTTP клиент

### API
- Spring Boot 3.3.5 REST API
- BCrypt аутентификация
- Role-based authorization (RECEIVER, MASTER, ADMIN, DIRECTOR)

## Функциональность

### Реализовано ✅
- [x] Страница логина
- [x] Аутентификация (JWT token)
- [x] Protected routes
- [x] Main layout с sidebar
- [x] Dashboard страница
- [x] API client с интерсепторами
- [x] CORS конфигурация

### В разработке 🚧
- [ ] Страница клиентов (CRUD)
- [ ] Страница заказов (CRUD + статусы)
- [ ] Страница платежей
- [ ] Страница запчастей
- [ ] Страница пользователей

## API Endpoints

Все endpoints описаны в `src/api/client.ts`.

### Auth
- `POST /api/v1/auth/login` — вход

### Clients
- `GET /api/v1/clients` — список клиентов
- `GET /api/v1/clients/:id` — клиент по ID
- `POST /api/v1/clients` — создать клиента
- `PUT /api/v1/clients/:id` — обновить клиента
- `DELETE /api/v1/clients/:id` — удалить клиента

### Orders
- `GET /api/v1/orders` — список заказов
- `GET /api/v1/orders/:id` — заказ по ID
- `POST /api/v1/orders` — создать заказ
- `POST /api/v1/orders/:id/status` — сменить статус
- `POST /api/v1/orders/:id/approve` — согласовать
- `POST /api/v1/orders/:id/finalize-pricing` — финализировать цену

### Payments
- `GET /api/v1/payments` — список платежей
- `GET /api/v1/payments/:id` — платеж по ID
- `GET /api/v1/payments/order/:orderId` — платежи по заказу
- `POST /api/v1/payments` — создать платеж

### Parts
- `GET /api/v1/parts` — список запчастей
- `GET /api/v1/parts/:id` — запчасть по ID
- `POST /api/v1/parts` — создать запчасть
- `PUT /api/v1/parts/:id` — обновить запчасть
- `DELETE /api/v1/parts/:id` — удалить запчасть
- `POST /api/v1/parts/:id/stock` — изменить остаток

### Users
- `GET /api/v1/users` — список пользователей
- `GET /api/v1/users/:id` — пользователь по ID
- `POST /api/v1/users` — создать пользователя
- `PUT /api/v1/users/:id` — обновить пользователя
- `DELETE /api/v1/users/:id` — удалить пользователя

### Locations
- `GET /api/v1/locations` — список точек (публично)
- `POST /api/v1/locations` — создать точку

### Health
- `GET /api/health` — проверка статуса (публично)
