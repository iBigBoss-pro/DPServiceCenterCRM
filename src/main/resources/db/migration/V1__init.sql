-- =====================================================
-- DPServiceCenterCRM — Initial schema
-- =====================================================

-- service_centers
CREATE TABLE service_centers (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    name                    VARCHAR(500) NOT NULL,
    legal_name              VARCHAR(500) NOT NULL,
    inn                     VARCHAR(50) NOT NULL UNIQUE,
    ogrn                    VARCHAR(50),
    legal_address           VARCHAR(1000),
    actual_address          VARCHAR(1000),
    phone                   VARCHAR(50),
    email                   VARCHAR(255),
    director_name           VARCHAR(255),
    logo_attachment_id      BIGINT,
    last_order_number       BIGINT NOT NULL DEFAULT 0,
    last_document_number    BIGINT NOT NULL DEFAULT 0,
    last_order_year         INT NOT NULL DEFAULT 0,
    last_document_year      INT NOT NULL DEFAULT 0,
    regular_orders_threshold INT NOT NULL DEFAULT 3,
    vip_profit_threshold_rub BIGINT NOT NULL DEFAULT 30000
);

CREATE INDEX idx_service_centers_inn ON service_centers(inn);

-- locations
CREATE TABLE locations (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    name                    VARCHAR(500) NOT NULL,
    address                 VARCHAR(1000),
    phone                   VARCHAR(50),
    working_hours           VARCHAR(500),
    active                  BOOLEAN NOT NULL DEFAULT TRUE,
    default_location        BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_locations_sc ON locations(service_center_id);

-- users
CREATE TABLE users (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    username                VARCHAR(100) NOT NULL UNIQUE,
    password_hash           VARCHAR(500) NOT NULL,
    role                    VARCHAR(50) NOT NULL,
    first_name              VARCHAR(255) NOT NULL,
    last_name               VARCHAR(255),
    email                   VARCHAR(255) NOT NULL,
    location_id             BIGINT,
    active                  BOOLEAN NOT NULL DEFAULT TRUE,
    completed_orders_count  INT NOT NULL DEFAULT 0,
    completed_works_count   INT NOT NULL DEFAULT 0,
    total_revenue           DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    last_completed_at       TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_sc ON users(service_center_id);

-- clients
CREATE TABLE clients (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    first_name              VARCHAR(255) NOT NULL,
    last_name               VARCHAR(255),
    patronymic              VARCHAR(255),
    phone_number            VARCHAR(50),
    email                   VARCHAR(255),
    telegram_chat_id        VARCHAR(255),
    notify_by_sms           BOOLEAN NOT NULL DEFAULT FALSE,
    notify_by_email         BOOLEAN NOT NULL DEFAULT FALSE,
    notify_by_telegram      BOOLEAN NOT NULL DEFAULT FALSE,
    notify_by_push          BOOLEAN NOT NULL DEFAULT FALSE,
    preferred_channel       VARCHAR(50),
    status                  VARCHAR(50) NOT NULL,
    status_manual_override  BOOLEAN NOT NULL DEFAULT FALSE,
    important               BOOLEAN NOT NULL DEFAULT FALSE,
    importance_note         VARCHAR(1000),
    discount_percent        DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    total_orders            INT NOT NULL DEFAULT 0,
    total_spent             DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    total_profit            DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    first_visit_at          TIMESTAMP WITH TIME ZONE,
    last_visit_at           TIMESTAMP WITH TIME ZONE,
    note                    VARCHAR(2000)
);

CREATE INDEX idx_clients_sc ON clients(service_center_id);
CREATE INDEX idx_clients_phone ON clients(phone_number);
CREATE INDEX idx_clients_email ON clients(email);

-- orders
CREATE TABLE orders (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    order_number            VARCHAR(50) NOT NULL UNIQUE,
    public_token            VARCHAR(100) NOT NULL UNIQUE,
    client_id               BIGINT NOT NULL,
    master_id               BIGINT,
    receiver_id             BIGINT NOT NULL,
    location_id             BIGINT NOT NULL,
    status                  VARCHAR(50) NOT NULL,
    accepted_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    must_be_finished_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    finished_at             TIMESTAMP WITH TIME ZONE,
    pick_up                 TIMESTAMP WITH TIME ZONE,
    device_type             VARCHAR(50) NOT NULL,
    manufacturer            VARCHAR(255) NOT NULL,
    model                   VARCHAR(255) NOT NULL,
    serial_number           VARCHAR(255) NOT NULL,
    malfunction             VARCHAR(500) NOT NULL,
    malfunction_description VARCHAR(2000),
    accessories             VARCHAR(500),
    condition_on_acceptance VARCHAR(1000),
    locked                  BOOLEAN NOT NULL DEFAULT FALSE,
    password_provided       BOOLEAN NOT NULL DEFAULT FALSE,
    photos_taken            BOOLEAN NOT NULL DEFAULT FALSE,
    price_estimate          DECIMAL(15,2),
    final_price             DECIMAL(15,2),
    diagnostics_price       DECIMAL(15,2),
    cost_price              DECIMAL(15,2),
    profit                  DECIMAL(15,2),
    client_agreed_to_diagnostics BOOLEAN NOT NULL DEFAULT FALSE,
    client_agreed_to_password_use   BOOLEAN NOT NULL DEFAULT FALSE,
    storage_deadline_days   INT NOT NULL,
    approved_price          DECIMAL(15,2),
    approved_deadline       TIMESTAMP WITH TIME ZONE,
    approved_at             TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_orders_number ON orders(order_number);
CREATE INDEX idx_orders_token ON orders(public_token);
CREATE INDEX idx_orders_client ON orders(client_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_sc ON orders(service_center_id);

ALTER TABLE orders ADD CONSTRAINT fk_orders_client FOREIGN KEY (client_id) REFERENCES clients(id);

-- order_works
CREATE TABLE order_works (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    order_id                BIGINT NOT NULL,
    name                    VARCHAR(500) NOT NULL,
    client_price            DECIMAL(15,2) NOT NULL,
    quantity                INT NOT NULL,
    executor_type           VARCHAR(50) NOT NULL,
    master_id               BIGINT,
    external_master_name    VARCHAR(255),
    external_master_contact VARCHAR(255),
    cost_price              DECIMAL(15,2) NOT NULL,
    paid_to_executor        DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    comment                 VARCHAR(1000)
);

CREATE INDEX idx_order_works_order ON order_works(order_id);

ALTER TABLE order_works ADD CONSTRAINT fk_order_works_order FOREIGN KEY (order_id) REFERENCES orders(id);

-- order_events
CREATE TABLE order_events (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    order_id                BIGINT NOT NULL,
    timestamp               TIMESTAMP WITH TIME ZONE NOT NULL,
    old_status              VARCHAR(50) NOT NULL,
    new_status              VARCHAR(50) NOT NULL,
    user_id                 BIGINT NOT NULL,
    status_comment          VARCHAR(1000),
    notification_id         BIGINT
);

CREATE INDEX idx_order_events_order ON order_events(order_id);

-- payments
CREATE TABLE payments (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    direction               VARCHAR(50) NOT NULL,
    order_id                BIGINT,
    expense_category        VARCHAR(50),
    order_work_id           BIGINT,
    recipient_name          VARCHAR(255),
    recipient_contact       VARCHAR(255),
    supplier_id             BIGINT,
    amount                  DECIMAL(15,2) NOT NULL,
    method                  VARCHAR(50) NOT NULL,
    paid_at                 TIMESTAMP WITH TIME ZONE NOT NULL,
    accepted_by_user_id     BIGINT NOT NULL,
    comment                 VARCHAR(1000)
);

CREATE INDEX idx_payments_order ON payments(order_id);
CREATE INDEX idx_payments_sc ON payments(service_center_id);

-- parts
CREATE TABLE parts (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    name                    VARCHAR(500) NOT NULL,
    sku                     VARCHAR(100) NOT NULL,
    purchase_price          DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    sale_price              DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    quantity_in_stock       INT NOT NULL DEFAULT 0,
    min_stock               INT NOT NULL DEFAULT 0,
    location_id             BIGINT,
    active                  BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_parts_sku ON parts(sku);
CREATE INDEX idx_parts_sc ON parts(service_center_id);

-- stock_movements
CREATE TABLE stock_movements (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    part_id                 BIGINT NOT NULL,
    order_id                BIGINT,
    type                    VARCHAR(50) NOT NULL,
    quantity                INT NOT NULL,
    purchase_price          DECIMAL(15,2) NOT NULL,
    client_price            DECIMAL(15,2),
    user_id                 BIGINT NOT NULL,
    moved_at                TIMESTAMP WITH TIME ZONE NOT NULL,
    comment                 VARCHAR(1000)
);

CREATE INDEX idx_stock_movements_part ON stock_movements(part_id);
CREATE INDEX idx_stock_movements_order ON stock_movements(order_id);

-- documents
CREATE TABLE documents (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    order_id                BIGINT NOT NULL,
    type                    VARCHAR(50) NOT NULL,
    document_number         VARCHAR(50) NOT NULL,
    template_id             BIGINT NOT NULL,
    template_version        INT NOT NULL,
    body_html               TEXT NOT NULL,
    pdf_path                VARCHAR(1000),
    status                  VARCHAR(50) NOT NULL,
    printed_at              TIMESTAMP WITH TIME ZONE,
    printed_by_user_id      BIGINT,
    signed_at               TIMESTAMP WITH TIME ZONE,
    signed_by_user_id       BIGINT,
    signed_scan_attachment_id BIGINT,
    voided_by_user_id       BIGINT,
    voided_at               TIMESTAMP WITH TIME ZONE,
    void_reason             VARCHAR(1000),
    copy_number             INT NOT NULL DEFAULT 1,
    next_copy_number        INT NOT NULL DEFAULT 2
);

CREATE INDEX idx_documents_order ON documents(order_id);

-- document_templates
CREATE TABLE document_templates (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    code                    VARCHAR(100) NOT NULL,
    version                 INT NOT NULL,
    language                VARCHAR(10) NOT NULL,
    name                    VARCHAR(500) NOT NULL,
    body_html               TEXT NOT NULL,
    active                  BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_doc_template_code_version UNIQUE (code, version)
);

CREATE INDEX idx_doc_templates_code ON document_templates(code);

-- notifications
CREATE TABLE notifications (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    client_id               BIGINT NOT NULL,
    order_id                BIGINT,
    template_id             BIGINT,
    template_version        INT NOT NULL DEFAULT 0,
    channel                 VARCHAR(50) NOT NULL,
    status                  VARCHAR(50) NOT NULL,
    recipient               VARCHAR(500) NOT NULL,
    subject                 VARCHAR(500),
    body                    TEXT NOT NULL,
    sent_at                 TIMESTAMP WITH TIME ZONE,
    delivered_at            TIMESTAMP WITH TIME ZONE,
    read_at                 TIMESTAMP WITH TIME ZONE,
    error_message           VARCHAR(2000),
    triggered_by_user_id    BIGINT
);

CREATE INDEX idx_notifications_order ON notifications(order_id);
CREATE INDEX idx_notifications_client ON notifications(client_id);
CREATE INDEX idx_notifications_status ON notifications(status);

-- notification_templates
CREATE TABLE notification_templates (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    code                    VARCHAR(100) NOT NULL,
    version                 INT NOT NULL,
    language                VARCHAR(10) NOT NULL,
    channel                 VARCHAR(50) NOT NULL,
    subject                 VARCHAR(500),
    body                    TEXT NOT NULL,
    active                  BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_notif_template_code_version UNIQUE (code, version)
);

CREATE INDEX idx_notif_templates_code ON notification_templates(code);

-- attachments
CREATE TABLE attachments (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    owner_type              VARCHAR(50) NOT NULL,
    owner_id                BIGINT NOT NULL,
    type                    VARCHAR(50) NOT NULL,
    file_name               VARCHAR(500) NOT NULL,
    storage_key             VARCHAR(1000) NOT NULL,
    mime_type               VARCHAR(200),
    size_bytes              BIGINT NOT NULL,
    sha256                  VARCHAR(64),
    uploaded_by_user_id     BIGINT,
    uploaded_at             TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_attachments_owner ON attachments(owner_type, owner_id);

-- warranties
CREATE TABLE warranties (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    order_id                BIGINT NOT NULL UNIQUE,
    months                  INT,
    valid_until             TIMESTAMP WITH TIME ZONE,
    note                    VARCHAR(1000),
    issued_by_user_id       BIGINT NOT NULL,
    issued_at               TIMESTAMP WITH TIME ZONE NOT NULL,
    warranty_document_id    BIGINT
);

CREATE INDEX idx_warranties_order ON warranties(order_id);

-- approvals
CREATE TABLE approvals (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    order_id                BIGINT NOT NULL,
    proposed_price          DECIMAL(15,2),
    proposed_deadline       TIMESTAMP WITH TIME ZONE,
    reason                  VARCHAR(1000),
    status                  VARCHAR(50) NOT NULL,
    sent_channel            VARCHAR(50),
    sent_at                 TIMESTAMP WITH TIME ZONE,
    responded_at            TIMESTAMP WITH TIME ZONE,
    responded_channel       VARCHAR(50),
    comment                 VARCHAR(1000),
    created_by_user_id      BIGINT NOT NULL,
    expires_at              TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_approvals_order ON approvals(order_id);

-- price_list_items
CREATE TABLE price_list_items (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_center_id       BIGINT NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    entity_version          BIGINT NOT NULL DEFAULT 0,
    name                    VARCHAR(500) NOT NULL,
    category                VARCHAR(255),
    price                   DECIMAL(15,2) NOT NULL,
    active                  BOOLEAN NOT NULL DEFAULT TRUE,
    location_id             BIGINT
);

CREATE INDEX idx_price_list_category ON price_list_items(category);
