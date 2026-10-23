import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

export function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const logout = useAuthStore((state) => state.logout);
  const user = useAuthStore((state) => state.user);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navItems = [
    { path: '/', label: 'Главная' },
    { path: '/clients', label: 'Клиенты' },
    { path: '/orders', label: 'Заказы' },
    { path: '/payments', label: 'Платежи' },
    { path: '/parts', label: 'Запчасти' },
    { path: '/users', label: 'Пользователи' },
  ];

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      {/* Sidebar */}
      <aside style={{
        width: '250px',
        background: '#1a202c',
        color: 'white',
        display: 'flex',
        flexDirection: 'column',
      }}>
        <div style={{
          padding: '20px',
          borderBottom: '1px solid #2d3748',
        }}>
          <h2 style={{ margin: 0, fontSize: '18px' }}>CRM System</h2>
        </div>

        <nav style={{ flex: 1, padding: '20px 0' }}>
          {navItems.map((item) => (
            <a
              key={item.path}
              href={item.path}
              style={{
                display: 'block',
                padding: '12px 20px',
                color: location.pathname === item.path ? '#667eea' : '#a0aec0',
                textDecoration: 'none',
                background: location.pathname === item.path ? 'rgba(102,126,234,0.1)' : 'transparent',
                borderLeft: location.pathname === item.path ? '3px solid #667eea' : '3px solid transparent',
                fontWeight: location.pathname === item.path ? 600 : 400,
              }}
            >
              {item.label}
            </a>
          ))}
        </nav>

        <div style={{
          padding: '20px',
          borderTop: '1px solid #2d3748',
        }}>
          <div style={{ marginBottom: '10px', fontSize: '14px' }}>
            {user?.firstName} {user?.lastName}
          </div>
          <div style={{ fontSize: '12px', color: '#a0aec0', marginBottom: '10px' }}>
            {user?.role}
          </div>
          <button
            onClick={handleLogout}
            style={{
              width: '100%',
              padding: '8px',
              background: '#e53e3e',
              color: 'white',
              border: 'none',
              borderRadius: '6px',
              cursor: 'pointer',
            }}
          >
            Выйти
          </button>
        </div>
      </aside>

      {/* Main content */}
      <main style={{ flex: 1, background: '#f7fafc' }}>
        <header style={{
          background: 'white',
          padding: '20px 30px',
          borderBottom: '1px solid #e2e8f0',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}>
          <h1 style={{ margin: 0, fontSize: '24px', color: '#2d3748' }}>
            {navItems.find((item) => item.path === location.pathname)?.label || 'Главная'}
          </h1>
        </header>
        <div style={{ padding: '30px' }}>
          <Outlet />
        </div>
      </main>
    </div>
  );
}
