import { create } from 'zustand';
import type { UserResponse } from '../types/api';

interface AuthState {
  token: string | null;
  user: UserResponse | null;
  isAuthenticated: boolean;
  setAuth: (token: string, user: UserResponse) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  token: localStorage.getItem('token'),
  user: JSON.parse(localStorage.getItem('user') || 'null'),
  isAuthenticated: !!localStorage.getItem('token'),

  setAuth: (_token: string, user: UserResponse) => {
    const simpleToken = `token_${user.username}_${user.role}`;
    localStorage.setItem('token', simpleToken);
    localStorage.setItem('user', JSON.stringify(user));
    set({ token: simpleToken, user, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    set({ token: null, user: null, isAuthenticated: false });
  },
}));
