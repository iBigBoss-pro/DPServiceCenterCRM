import { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { clientsApi } from '../api/client';
import type { ClientResponse } from '../types/api';

interface ClientFormProps {
  client?: ClientResponse | null;
  serviceCenterId: number;
  onSave: () => void;
  onCancel: () => void;
}

function ClientForm({ client, serviceCenterId, onSave, onCancel }: ClientFormProps) {
  const [formData, setFormData] = useState({
    lastName: '',
    firstName: '',
    patronymic: '',
    phoneNumber: '',
    email: '',
    telegramChatId: '',
    status: 'NEW',
    important: false,
    importanceNote: '',
    note: '',
    discountPercent: 0,
  });

  // Обновляем форму при изменении клиента (режим редактирования)
  useEffect(() => {
    if (client) {
      setFormData({
        lastName: client.lastName || '',
        firstName: client.firstName || '',
        patronymic: client.patronymic || '',
        phoneNumber: client.phoneNumber || '',
        email: client.email || '',
        telegramChatId: client.telegramChatId || '',
        status: client.status || 'NEW',
        important: client.important || false,
        importanceNote: client.importanceNote || '',
        note: client.note || '',
        discountPercent: client.discountPercent || 0,
      });
    } else {
      setFormData({
        lastName: '',
        firstName: '',
        patronymic: '',
        phoneNumber: '',
        email: '',
        telegramChatId: '',
        status: 'NEW',
        important: false,
        importanceNote: '',
        note: '',
        discountPercent: 0,
      });
    }
  }, [client]);

  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: (data: typeof formData) => clientsApi.create(data, serviceCenterId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients', serviceCenterId] });
      onSave();
    },
  });

  const updateMutation = useMutation({
    mutationFn: (data: typeof formData) => clientsApi.update(client!.id, data, serviceCenterId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients', serviceCenterId] });
      onSave();
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (client) {
      updateMutation.mutate(formData);
    } else {
      createMutation.mutate(formData);
    }
  };

  const handleChange = (field: string, value: string | number | boolean) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const inputStyle: React.CSSProperties = {
    width: '100%',
    padding: '10px',
    border: '1px solid #ddd',
    borderRadius: '8px',
    fontSize: '14px',
    boxSizing: 'border-box',
  };

  const labelStyle: React.CSSProperties = {
    display: 'block',
    marginBottom: '6px',
    fontWeight: 500,
    fontSize: '14px',
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
      <h3 style={{ margin: 0 }}>{client ? 'Редактировать' : 'Новый'}</h3>

      <div>
        <label style={labelStyle}>Фамилия *</label>
        <input style={inputStyle} required value={formData.lastName}
          onChange={e => handleChange('lastName', e.target.value)} />
      </div>

      <div>
        <label style={labelStyle}>Имя *</label>
        <input style={inputStyle} required value={formData.firstName}
          onChange={e => handleChange('firstName', e.target.value)} />
      </div>

      <div>
        <label style={labelStyle}>Отчество</label>
        <input style={inputStyle} value={formData.patronymic}
          onChange={e => handleChange('patronymic', e.target.value)} />
      </div>

      <div>
        <label style={labelStyle}>Телефон</label>
        <input style={inputStyle} type="tel" value={formData.phoneNumber}
          onChange={e => handleChange('phoneNumber', e.target.value)} />
      </div>

      <div>
        <label style={labelStyle}>Email</label>
        <input style={inputStyle} type="email" value={formData.email}
          onChange={e => handleChange('email', e.target.value)} />
      </div>

      <div>
        <label style={labelStyle}>Telegram Chat ID</label>
        <input style={inputStyle} value={formData.telegramChatId}
          onChange={e => handleChange('telegramChatId', e.target.value)} />
      </div>

      <div>
        <label style={labelStyle}>Статус</label>
        <select style={inputStyle} value={formData.status}
          onChange={e => handleChange('status', e.target.value)}>
          <option value="NEW">Новый</option>
          <option value="REGULAR">Обычный</option>
          <option value="VIP">VIP</option>
          <option value="PROBLEM">Проблемный</option>
          <option value="BLACKLIST">Чёрный список</option>
          <option value="INACTIVE">Неактивный</option>
        </select>
      </div>

      <div>
        <label style={labelStyle}>Скидка (%)</label>
        <input style={inputStyle} type="number" min="0" max="100" value={formData.discountPercent}
          onChange={e => handleChange('discountPercent', Math.min(100, Math.max(0, Number(e.target.value))))} />
      </div>

      <div>
        <label style={labelStyle}>Заметка о клиенте</label>
        <textarea style={{...inputStyle, minHeight: '60px', resize: 'vertical'}}
          value={formData.note}
          onChange={e => handleChange('note', e.target.value)}
          placeholder="Общая заметка о клиенте..." />
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
        <input type="checkbox" id="important" checked={formData.important}
          onChange={e => handleChange('important', e.target.checked)} />
        <label htmlFor="important">Важный клиент</label>
      </div>

      {formData.important && (
        <div>
          <label style={labelStyle}>Причина важности</label>
          <input style={inputStyle} required value={formData.importanceNote}
            onChange={e => handleChange('importanceNote', e.target.value)}
            placeholder="Почему клиент важен?" />
        </div>
      )}

      <div style={{ display: 'flex', gap: '12px', marginTop: '8px' }}>
        <button type="submit" disabled={createMutation.isPending || updateMutation.isPending}
          style={{
            flex: 1, padding: '12px', background: '#667eea', color: 'white',
            border: 'none', borderRadius: '8px', fontSize: '14px', fontWeight: 600,
            cursor: (createMutation.isPending || updateMutation.isPending) ? 'not-allowed' : 'pointer',
          }}>
          {(createMutation.isPending || updateMutation.isPending) ? 'Сохранение...' : 'Сохранить'}
        </button>
        <button type="button" onClick={onCancel}
          style={{
            flex: 1, padding: '12px', background: '#f1f1f1', color: '#333',
            border: 'none', borderRadius: '8px', fontSize: '14px', cursor: 'pointer',
          }}>
          Отмена
        </button>
      </div>
    </form>
  );
}

export function ClientsPage() {
  const queryClient = useQueryClient();
  const [showForm, setShowForm] = useState(false);
  const [editingClient, setEditingClient] = useState<ClientResponse | null>(null);
  const [serviceCenterId] = useState(1);

  const { data: clients, isLoading } = useQuery({
    queryKey: ['clients', serviceCenterId],
    queryFn: () => clientsApi.getAll(serviceCenterId).then(res => res.data),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => clientsApi.delete(id, serviceCenterId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clients', serviceCenterId] });
    },
  });

  const handleEdit = (client: ClientResponse) => {
    setEditingClient(client);
    setShowForm(true);
  };

  const handleDelete = (id: number) => {
    if (confirm('Удалить клиента?')) {
      deleteMutation.mutate(id);
    }
  };

  const handleSaveComplete = () => {
    setShowForm(false);
    setEditingClient(null);
  };

  const cardStyle: React.CSSProperties = {
    background: 'white',
    borderRadius: '12px',
    padding: '20px',
    marginBottom: '12px',
    boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
  };

  const buttonStyle = (bg: string): React.CSSProperties => ({
    padding: '8px 16px',
    background: bg,
    color: 'white',
    border: 'none',
    borderRadius: '6px',
    fontSize: '13px',
    cursor: 'pointer',
  });

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h2 style={{ margin: 0 }}>Клиенты</h2>
        <button onClick={() => { setShowForm(true); setEditingClient(null); }}
          style={{ ...buttonStyle('#667eea'), padding: '10px 20px', fontSize: '14px' }}>
          + Добавить клиента
        </button>
      </div>

      {showForm && (
        <div style={{
          background: 'white',
          borderRadius: '12px',
          padding: '24px',
          marginBottom: '20px',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
        }}>
          <ClientForm
            client={editingClient}
            serviceCenterId={serviceCenterId}
            onSave={handleSaveComplete}
            onCancel={() => { setShowForm(false); setEditingClient(null); }}
          />
        </div>
      )}

      {isLoading && <p>Загрузка...</p>}

      {!isLoading && (!clients || clients.length === 0) && (
        <div style={{
          textAlign: 'center', padding: '60px 20px', color: '#999',
        }}>
          <p style={{ fontSize: '48px', margin: '0 0 16px' }}>👥</p>
          <p style={{ fontSize: '18px' }}>Клиентов пока нет</p>
          <p>Нажмите «Добавить клиента» чтобы создать первого</p>
        </div>
      )}

      {clients && clients.map((client: ClientResponse) => (
        <div key={client.id} style={cardStyle}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
            <div>
              <h3 style={{ margin: '0 0 8px', fontSize: '18px' }}>
                {client.lastName} {client.firstName}
                {client.patronymic && <span style={{ color: '#666' }}> {client.patronymic}</span>}
              </h3>
              <div style={{ fontSize: '14px', color: '#666', marginBottom: '4px' }}>
                {client.phoneNumber && `📞 ${client.phoneNumber}`}
              </div>
              <div style={{ fontSize: '14px', color: '#666', marginBottom: '4px' }}>
                {client.email && `✉ ${client.email}`}
              </div>
              {client.telegramChatId && (
                <div style={{ fontSize: '14px', color: '#666', marginBottom: '4px' }}>
                  ✈ Telegram: {client.telegramChatId}
                </div>
              )}
              <div style={{ display: 'flex', gap: '12px', marginTop: '8px', fontSize: '13px' }}>
                <span style={{
                  padding: '4px 10px',
                  background: client.status === 'ACTIVE' ? '#e6f9e6' : '#f1f1f1',
                  color: client.status === 'ACTIVE' ? '#2d7d2d' : '#666',
                  borderRadius: '12px',
                }}>
                  {client.status === 'ACTIVE' ? 'Активный' : 'Неактивный'}
                </span>
                {client.important && (
                  <span style={{
                    padding: '4px 10px',
                    background: '#fff3cd',
                    color: '#856404',
                    borderRadius: '12px',
                  }}>
                    ⭐ Важный
                  </span>
                )}
                {client.discountPercent > 0 && (
                  <span style={{
                    padding: '4px 10px',
                    background: '#e7f3ff',
                    color: '#004085',
                    borderRadius: '12px',
                  }}>
                    Скидка: {client.discountPercent}%
                  </span>
                )}
              </div>
            </div>
            <div style={{ display: 'flex', gap: '8px' }}>
              <button onClick={() => handleEdit(client)} style={buttonStyle('#667eea')}>
                ✏️ Редактировать
              </button>
              <button onClick={() => handleDelete(client.id)} style={buttonStyle('#e53e3e')}>
                🗑 Удалить
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}
