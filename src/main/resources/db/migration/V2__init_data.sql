-- =====================================================
-- DPServiceCenterCRM — Test users
-- =====================================================

-- Insert test users with BCrypt-hashed passwords
-- Password 'Ike290692' hashed with BCryptPasswordEncoder
INSERT INTO users (
    service_center_id,
    created_at,
    username,
    password_hash,
    role,
    first_name,
    last_name,
    email,
    active,
    completed_orders_count,
    completed_works_count,
    total_revenue
) VALUES (
    1,
    CURRENT_TIMESTAMP,
    'ibigboss',
    '$2a$10$xDWOa4lG48DjEbbL6s7qduczGpn3ivFcI.9ywQZfwdmC6vwwCvtR.',
    'DIRECTOR',
    'Ильдар',
    'Бигбаев',
    'ibigboss@crm.local',
    TRUE,
    0,
    0,
    0.00
);
