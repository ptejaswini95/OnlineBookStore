-- Create database (run this separately as superuser)
-- CREATE DATABASE bookstore;

-- Connect to the database
\c bookstore;

-- Create schemas
CREATE SCHEMA IF NOT EXISTS bookstore;
CREATE SCHEMA IF NOT EXISTS security;

-- Set search path
SET search_path TO bookstore, security, public;

-- Create enum types
CREATE TYPE bookstore.user_role AS ENUM ('USER', 'ADMIN');
CREATE TYPE bookstore.order_status AS ENUM ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED');
CREATE TYPE bookstore.payment_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED');
CREATE TYPE bookstore.payment_method AS ENUM ('CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL');

-- Create tables in bookstore schema
CREATE TABLE IF NOT EXISTS bookstore.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role bookstore.user_role DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookstore.books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    isbn VARCHAR(13) UNIQUE,
    category VARCHAR(50) NOT NULL,
    published_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookstore.orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES bookstore.users(id),
    total_amount DECIMAL(10,2) NOT NULL,
    status bookstore.order_status DEFAULT 'PENDING',
    payment_method bookstore.payment_method NOT NULL,
    payment_status bookstore.payment_status DEFAULT 'PENDING',
    shipping_address JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id),
    book_id BIGINT REFERENCES books(id),
    quantity INTEGER NOT NULL,
    price_at_time DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create stored procedures

-- Procedure to create a new order with transaction
CREATE OR REPLACE PROCEDURE create_order(
    p_user_id BIGINT,
    p_items JSONB,
    p_shipping_address JSONB,
    p_payment_method bookstore.payment_method,
    OUT p_order_id BIGINT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_total_amount DECIMAL(10,2) := 0;
    v_item JSONB;
    v_book_id BIGINT;
    v_quantity INTEGER;
    v_price DECIMAL(10,2);
BEGIN
    -- Start transaction
    BEGIN
        -- Create order
        INSERT INTO orders (user_id, total_amount, shipping_address, payment_method)
        VALUES (p_user_id, 0, p_shipping_address, p_payment_method)
        RETURNING id INTO p_order_id;

        -- Process each item
        FOR v_item IN SELECT * FROM jsonb_array_elements(p_items)
        LOOP
            v_book_id := (v_item->>'bookId')::BIGINT;
            v_quantity := (v_item->>'quantity')::INTEGER;
            
            -- Get book price and update stock
            SELECT price INTO v_price FROM books WHERE id = v_book_id FOR UPDATE;
            
            -- Add order item
            INSERT INTO order_items (order_id, book_id, quantity, price_at_time)
            VALUES (p_order_id, v_book_id, v_quantity, v_price);
            
            -- Update total amount
            v_total_amount := v_total_amount + (v_price * v_quantity);
            
            -- Update book stock
            UPDATE books 
            SET stock = stock - v_quantity
            WHERE id = v_book_id;
        END LOOP;
        
        -- Update order total amount
        UPDATE orders 
        SET total_amount = v_total_amount
        WHERE id = p_order_id;
        
        -- Commit transaction
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback transaction on error
            ROLLBACK;
            RAISE;
    END;
END;
$$;

-- Procedure to update book stock
CREATE OR REPLACE PROCEDURE update_book_stock(
    p_book_id BIGINT,
    p_quantity INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE books
    SET stock = stock + p_quantity,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = p_book_id;
END;
$$;

-- Procedure to get user orders with details
CREATE OR REPLACE FUNCTION get_user_orders(p_user_id BIGINT)
RETURNS TABLE (
    order_id BIGINT,
    total_amount DECIMAL(10,2),
    status bookstore.order_status,
    payment_status bookstore.payment_status,
    created_at TIMESTAMP,
    items JSONB
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        o.id,
        o.total_amount,
        o.status,
        o.payment_status,
        o.created_at,
        COALESCE(
            jsonb_agg(
                jsonb_build_object(
                    'bookId', oi.book_id,
                    'quantity', oi.quantity,
                    'price', oi.price_at_time
                )
            ) FILTER (WHERE oi.id IS NOT NULL),
            '[]'::jsonb
        ) as items
    FROM orders o
    LEFT JOIN order_items oi ON o.id = oi.order_id
    WHERE o.user_id = p_user_id
    GROUP BY o.id, o.total_amount, o.status, o.payment_status, o.created_at
    ORDER BY o.created_at DESC;
END;
$$ LANGUAGE plpgsql;

-- Create indexes
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_book_id ON order_items(book_id); 