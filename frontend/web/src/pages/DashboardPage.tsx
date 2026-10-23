import { useQuery } from '@tanstack/react-query';
import { healthApi } from '../api/client';

export function DashboardPage() {
  const { data, isLoading } = useQuery({
    queryKey: ['health'],
    queryFn: healthApi.check,
  });

  return (
    <div>
      <h2 style={{ marginBottom: '20px', color: '#2d3748' }}>Панель управления</h2>

      <div style={{
        background: 'white',
        padding: '30px',
        borderRadius: '12px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
      }}>
        <h3 style={{ marginTop: 0 }}>Статус системы</h3>
        {isLoading ? (
          <p>Загрузка...</p>
        ) : data ? (
          <div style={{
            padding: '15px',
            background: '#c6f6d5',
            borderRadius: '8px',
            color: '#22543d',
          }}>
            <strong>Статус:</strong> {data.data.status}
            <br />
            <strong>Приложение:</strong> {data.data.application}
          </div>
        ) : (
          <p style={{ color: '#e53e3e' }}>Не удалось подключиться к серверу</p>
        )}
      </div>

      <div style={{ marginTop: '30px', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '20px' }}>
        <div style={{
          background: 'white',
          padding: '20px',
          borderRadius: '12px',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        }}>
          <h4 style={{ margin: '0 0 10px 0', color: '#667eea' }}>Клиенты</h4>
          <p style={{ fontSize: '32px', fontWeight: 700, margin: 0 }}>0</p>
        </div>

        <div style={{
          background: 'white',
          padding: '20px',
          borderRadius: '12px',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        }}>
          <h4 style={{ margin: '0 0 10px 0', color: '#48bb78' }}>Заказы</h4>
          <p style={{ fontSize: '32px', fontWeight: 700, margin: 0 }}>0</p>
        </div>

        <div style={{
          background: 'white',
          padding: '20px',
          borderRadius: '12px',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        }}>
          <h4 style={{ margin: '0 0 10px 0', color: '#ed8936' }}>Платежи</h4>
          <p style={{ fontSize: '32px', fontWeight: 700, margin: 0 }}>0</p>
        </div>
      </div>
    </div>
  );
}
