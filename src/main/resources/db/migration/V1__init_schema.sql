-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    email VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255),
    full_name VARCHAR(255),
    dni VARCHAR(8) UNIQUE,
    phone VARCHAR(255) UNIQUE NOT NULL, -- The ONLY mandatory business field
    firebase_uid VARCHAR(255) UNIQUE,
    avg_rating DECIMAL(19, 2),
    is_verified BOOLEAN DEFAULT FALSE,
    username VARCHAR(255) UNIQUE,
    bio TEXT,
    profile_image_url VARCHAR(255)
);

-- Events Table
CREATE TABLE IF NOT EXISTS events (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    category VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    image_url VARCHAR(255)
);

-- Tickets Table
CREATE TABLE IF NOT EXISTS tickets (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    event_id UUID NOT NULL REFERENCES events(id),
    seller_id UUID NOT NULL REFERENCES users(id),
    section VARCHAR(255) NOT NULL,
    row_seat VARCHAR(255) NOT NULL,
    original_price DECIMAL(19, 2) NOT NULL,
    listing_price DECIMAL(19, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    secure_storage_path VARCHAR(255),
    version BIGINT DEFAULT 0
);

-- Offers Table
CREATE TABLE IF NOT EXISTS offers (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    ticket_id UUID NOT NULL REFERENCES tickets(id),
    bidder_id UUID NOT NULL REFERENCES users(id),
    amount_offered DECIMAL(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    ticket_id UUID NOT NULL REFERENCES tickets(id),
    buyer_id UUID NOT NULL REFERENCES users(id),
    total_amount DECIMAL(19, 2) NOT NULL,
    platform_fee DECIMAL(19, 2) NOT NULL,
    payment_gateway_ref VARCHAR(255),
    status VARCHAR(50) NOT NULL
);

-- Reviews Table
CREATE TABLE IF NOT EXISTS reviews (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    transaction_id UUID NOT NULL REFERENCES transactions(id),
    reviewer_id UUID NOT NULL REFERENCES users(id),
    reviewed_user_id UUID NOT NULL REFERENCES users(id),
    rating INTEGER NOT NULL,
    comment TEXT
);

-- Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    user_id UUID NOT NULL REFERENCES users(id),
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE
);

-- Insert Sample Users
-- Password is 'password' hashed with BCrypt (standard/common placeholder hash if auth system uses it, or plain for dev if not yet fully integrated, but assuming standard Spring Security)
-- Note: UUIDs are hardcoded for relationship consistency

INSERT INTO users (id, created_at, updated_at, email, password_hash, full_name, dni, phone, firebase_uid, avg_rating, is_verified)
VALUES
('11111111-1111-1111-1111-111111111111', NOW(), NOW(), 'admin@revente.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Admin User', '00000001', '+51900000001', 'firebase-uid-1', 5.00, TRUE),
('22222222-2222-2222-2222-222222222222', NOW(), NOW(), 'juan.perez@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Juan Perez', '00000002', '+51900000002', 'firebase-uid-2', 4.50, TRUE);

-- Insert Sample Events
INSERT INTO events (id, created_at, updated_at, title, description, location, event_date, category, status, image_url)
VALUES
('33333333-3333-3333-3333-333333333333', NOW(), NOW(), 'Coldplay World Tour', 'Experience the magic of Coldplay live in Lima.', 'Estadio Nacional', NOW() + INTERVAL '30 days', 'CONCERT', 'ACTIVE', 'https://example.com/coldplay.jpg'),
('44444444-4444-4444-4444-444444444444', NOW(), NOW(), 'Copa Libertadores Final', 'The ultimate football match of the year.', 'Estadio Monumental', NOW() + INTERVAL '45 days', 'SPORTS', 'ACTIVE', 'https://example.com/libertadores.jpg'),
('55555555-5555-5555-5555-555555555555', NOW(), NOW(), 'Hamlet', 'A classic performance by the National Theater.', 'Teatro Municipal', NOW() + INTERVAL '15 days', 'THEATER', 'ACTIVE', 'https://example.com/hamlet.jpg');

-- Insert Sample Tickets
INSERT INTO tickets (id, created_at, updated_at, event_id, seller_id, section, row_seat, original_price, listing_price, quantity, status, version)
VALUES
('66666666-6666-6666-6666-666666666666', NOW(), NOW(), '33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', 'Campo A', 'N/A', 500.00, 450.00, 2, 'AVAILABLE', 0),
('77777777-7777-7777-7777-777777777777', NOW(), NOW(), '44444444-4444-4444-4444-444444444444', '22222222-2222-2222-2222-222222222222', 'Occidente', 'Row 5 Seat 20', 200.00, 300.00, 1, 'AVAILABLE', 0);
