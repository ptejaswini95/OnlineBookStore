# Database Setup Guide for Online Book Store

## Prerequisites
- PostgreSQL installed and running
- Access to PostgreSQL as a superuser (postgres)

## Step 1: Create the Database
```bash
# Connect to PostgreSQL as superuser
psql -U postgres

# Create the database
CREATE DATABASE bookstore;

# Exit psql
\q
```

## Step 2: Run the Complete Setup Script
```bash
# Run the complete setup script
psql -U postgres -d bookstore -f setup_database.sql
```

## Step 3: Verify the Setup
```bash
# Connect to the bookstore database
psql -U postgres -d bookstore

# Check if tables were created
\dt bookstore.*

# Check if data was inserted
SELECT COUNT(*) FROM bookstore.users;
SELECT COUNT(*) FROM bookstore.books;

# View sample data
SELECT title, author, price, category FROM bookstore.books LIMIT 5;

# Exit psql
\q
```

## Step 4: Test the Application
1. Make sure the backend is running on port 8081
2. Start the frontend application
3. Navigate to http://localhost:3000
4. You should see the featured books loaded from the database

## Sample Login Credentials
- **Admin User:**
  - Username: `admin`
  - Password: `password`
  - Role: ADMIN

- **Regular Users:**
  - Username: `user1` or `user2`
  - Password: `password`
  - Role: USER

## Database Structure

### Tables Created:
1. **bookstore.users** - User accounts and authentication
2. **bookstore.books** - Book catalog with details

### Sample Data:
- **3 Users** (1 admin, 2 regular users)
- **15 Books** across various categories (Fiction, Science Fiction, Romance, Mystery, Fantasy)

## Troubleshooting

### If you get permission errors:
```bash
# Make sure you're connecting as the postgres user
psql -U postgres -h localhost
```

### If the database already exists:
```bash
# Drop and recreate (WARNING: This will delete all data)
DROP DATABASE IF EXISTS bookstore;
CREATE DATABASE bookstore;
```

### If tables already exist:
The script uses `IF NOT EXISTS` and `ON CONFLICT DO NOTHING`, so it's safe to run multiple times.

## Manual Step-by-Step (Alternative)

If you prefer to run commands manually:

```bash
# 1. Create database
psql -U postgres -c "CREATE DATABASE bookstore;"

# 2. Create schema
psql -U postgres -d bookstore -c "CREATE SCHEMA IF NOT EXISTS bookstore;"

# 3. Create enum type
psql -U postgres -d bookstore -c "CREATE TYPE bookstore.user_role AS ENUM ('USER', 'ADMIN');"

# 4. Create users table
psql -U postgres -d bookstore -c "
CREATE TABLE IF NOT EXISTS bookstore.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role bookstore.user_role DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);"

# 5. Create books table
psql -U postgres -d bookstore -c "
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
);"
```

Then run the data insertion commands from the `setup_database.sql` file. 