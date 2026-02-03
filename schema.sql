-- REVENTE Database Schema
-- Generated with Senior Technical Standards: UUIDs, Audit, Optimistic Locking, Strict Constraints.

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ENUMS for status management
CREATE TYPE ticket_status AS ENUM ('AVAILABLE', 'PENDING_PAYMENT', 'SOLD', 'VALIDATED');
CREATE TYPE offer_status AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED', 'EXPIRED');
CREATE TYPE transaction_status AS ENUM ('SUCCESS', 'FAILED');
CREATE TYPE event_status AS ENUM ('ACTIVE', 'PAST');
CREATE TYPE event_category AS ENUM ('CONCERT', 'SPORTS', 'THEATER');

-- 1. USERS
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email TEXT UNIQUE,
    password_hash TEXT, -- Nullable if using only Phone/Firebase auth initially
    full_name TEXT,
    dni VARCHAR(8) UNIQUE CHECK (dni IS NULL OR LENGTH(dni) = 8), -- Nullable for progressive profiling
    phone TEXT UNIQUE,
    firebase_uid TEXT UNIQUE,
    username TEXT UNIQUE, -- New field for profile
    bio TEXT, -- New field for profile
    profile_image_url TEXT, -- New field for profile
    avg_rating DECIMAL(3, 2) DEFAULT 0.00 CHECK (avg_rating >= 0 AND avg_rating <= 5),
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 2. EVENTS
CREATE TABLE events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    description TEXT,
    location TEXT NOT NULL,
    event_date TIMESTAMPTZ NOT NULL,
    category event_category NOT NULL,
    image_url TEXT,
    status event_status DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 3. TICKETS
CREATE TABLE tickets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    seller_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    section TEXT,
    row_seat TEXT,
    original_price DECIMAL(10, 2) NOT NULL CHECK (original_price >= 0),
    listing_price DECIMAL(10, 2) NOT NULL CHECK (listing_price >= 0),
    status ticket_status DEFAULT 'AVAILABLE',
    quantity INTEGER NOT NULL CHECK (quantity BETWEEN 1 AND 4),
    secure_storage_path TEXT, -- Encrypted path to file
    version INTEGER DEFAULT 0, -- Optimistic Locking
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Composite Index for Marketplace View Optimization
CREATE INDEX idx_tickets_market ON tickets (event_id, status, listing_price);

-- 4. OFFERS (Bids)
CREATE TABLE offers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticket_id UUID NOT NULL REFERENCES tickets(id) ON DELETE RESTRICT,
    bidder_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    amount_offered DECIMAL(10, 2) NOT NULL CHECK (amount_offered > 0),
    status offer_status DEFAULT 'PENDING',
    expires_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 5. TRANSACTIONS
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticket_id UUID NOT NULL REFERENCES tickets(id) ON DELETE RESTRICT,
    buyer_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    platform_fee DECIMAL(10, 2) NOT NULL CHECK (platform_fee >= 0),
    payment_gateway_ref TEXT,
    status transaction_status DEFAULT 'SUCCESS',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 6. REVIEWS
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL REFERENCES transactions(id) ON DELETE RESTRICT,
    reviewer_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    reviewed_user_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 7. NOTIFICATIONS
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    related_entity_id UUID,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- FUNCTION: Update updated_at column
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- TRIGGERS
CREATE TRIGGER trg_update_users BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_update_events BEFORE UPDATE ON events FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_update_tickets BEFORE UPDATE ON tickets FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_update_offers BEFORE UPDATE ON offers FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_update_transactions BEFORE UPDATE ON transactions FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_update_reviews BEFORE UPDATE ON reviews FOR EACH ROW EXECUTE FUNCTION update_timestamp();
