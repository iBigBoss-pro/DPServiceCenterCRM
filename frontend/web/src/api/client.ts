import axios from 'axios';
import type { AuthResponse } from '../types/api';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - add auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  console.log('Interceptor - token:', token ? 'EXISTS' : 'NULL', 'URL:', config.url);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
    console.log('Interceptor - Authorization header set');
  }
  return config;
});

// Response interceptor - handle 401
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authApi = {
  login: async (username: string, password: string): Promise<AuthResponse> => {
    const response = await api.post('/api/v1/auth/login', { username, password });
    return response.data;
  },
};

// Clients API
export const clientsApi = {
  getAll: (serviceCenterId: number) =>
    api.get(`/api/v1/clients?serviceCenterId=${serviceCenterId}`),
  getById: (id: number, serviceCenterId: number) =>
    api.get(`/api/v1/clients/${id}?serviceCenterId=${serviceCenterId}`),
  create: (data: any, serviceCenterId: number) =>
    api.post(`/api/v1/clients?serviceCenterId=${serviceCenterId}`, data),
  update: (id: number, data: any, serviceCenterId: number) =>
    api.put(`/api/v1/clients/${id}?serviceCenterId=${serviceCenterId}`, data),
  delete: (id: number, serviceCenterId: number) =>
    api.delete(`/api/v1/clients/${id}?serviceCenterId=${serviceCenterId}`),
};

// Users API
export const usersApi = {
  getAll: () => api.get('/api/v1/users'),
  getById: (id: number) => api.get(`/api/v1/users/${id}`),
  getByUsername: (username: string) => api.get(`/api/v1/users/username/${username}`),
  create: (data: any, serviceCenterId: number) =>
    api.post(`/api/v1/users?serviceCenterId=${serviceCenterId}`, data),
  update: (id: number, data: any, serviceCenterId: number) =>
    api.put(`/api/v1/users/${id}?serviceCenterId=${serviceCenterId}`, data),
  delete: (id: number) => api.delete(`/api/v1/users/${id}`),
};

// Orders API
export const ordersApi = {
  getAll: (serviceCenterId: number) =>
    api.get(`/api/v1/orders?serviceCenterId=${serviceCenterId}`),
  getById: (id: number, serviceCenterId: number) =>
    api.get(`/api/v1/orders/${id}?serviceCenterId=${serviceCenterId}`),
  create: (data: any, serviceCenterId: number) =>
    api.post(`/api/v1/orders?serviceCenterId=${serviceCenterId}`, data),
  changeStatus: (id: number, data: any) =>
    api.post(`/api/v1/orders/${id}/status`, data),
  approve: (id: number, data: any) =>
    api.post(`/api/v1/orders/${id}/approve`, data),
  finalizePricing: (id: number, data: any) =>
    api.post(`/api/v1/orders/${id}/finalize-pricing`, data),
};

// Payments API
export const paymentsApi = {
  getAll: () => api.get('/api/v1/payments'),
  getById: (id: number) => api.get(`/api/v1/payments/${id}`),
  getByOrderId: (orderId: number) => api.get(`/api/v1/payments/order/${orderId}`),
  create: (data: any, serviceCenterId: number) =>
    api.post(`/api/v1/payments?serviceCenterId=${serviceCenterId}`, data),
};

// Parts API
export const partsApi = {
  getAll: () => api.get('/api/v1/parts'),
  getById: (id: number) => api.get(`/api/v1/parts/${id}`),
  create: (data: any, serviceCenterId: number) =>
    api.post(`/api/v1/parts?serviceCenterId=${serviceCenterId}`, data),
  update: (id: number, data: any, serviceCenterId: number) =>
    api.put(`/api/v1/parts/${id}?serviceCenterId=${serviceCenterId}`, data),
  delete: (id: number) => api.delete(`/api/v1/parts/${id}`),
  adjustStock: (id: number, data: any) =>
    api.post(`/api/v1/parts/${id}/stock`, data),
};

// Locations API
export const locationsApi = {
  getAll: () => api.get('/api/v1/locations'),
  getById: (id: number) => api.get(`/api/v1/locations/${id}`),
  create: (data: any, serviceCenterId: number) =>
    api.post(`/api/v1/locations?serviceCenterId=${serviceCenterId}`, data),
};

// Health API
export const healthApi = {
  check: () => api.get('/api/health'),
};

export default api;
