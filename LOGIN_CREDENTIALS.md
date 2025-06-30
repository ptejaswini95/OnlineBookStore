# Online Book Store - Login Credentials & Troubleshooting

## 🔑 Login Credentials

### Admin User
- **Username:** `admin`
- **Password:** `password123`
- **Role:** ADMIN
- **Email:** admin@bookstore.com

### Regular Users
- **Username:** `user1`
- **Password:** `password123`
- **Role:** USER
- **Email:** user1@example.com

- **Username:** `user2`
- **Password:** `password123`
- **Role:** USER
- **Email:** user2@example.com

## 🚀 Quick Setup Steps

### 1. Update Passwords in Database
```bash
# Run the password update script
psql -U postgres -d bookstore -f update_passwords.sql
```

### 2. Restart Backend (to apply security changes)
```bash
# Stop the current backend
pkill -f "OnlineBookStoreApplication"

# Start the backend again
cd backend && mvn spring-boot:run
```

### 3. Test the Application
1. **Frontend:** http://localhost:3000
2. **Backend API:** http://localhost:8081
3. **Swagger Docs:** http://localhost:8081/swagger-ui.html

## 🔧 Troubleshooting

### Issue: "Failed to load featured books"
**Solution:** The backend needs to be restarted to apply the security configuration changes that allow public access to `/api/books/**`.

### Issue: Can't connect to database
**Solution:** Make sure PostgreSQL is running and the `bookstore` database exists with the correct schema.

### Issue: Login fails
**Solution:** Use the simple passwords listed above. All users now have the password `password123`.

## 📊 Database Status Check
```bash
# Check if database has data
psql -U postgres -d bookstore -c "SELECT COUNT(*) FROM bookstore.books;"
psql -U postgres -d bookstore -c "SELECT COUNT(*) FROM bookstore.users;"
```

## 🎯 Expected Results
- ✅ Frontend loads featured books without authentication
- ✅ Users can login with simple passwords
- ✅ Admin can access admin features
- ✅ Regular users can browse books and place orders

## 🔒 Security Notes
- These are **test passwords** for development only
- In production, use strong, unique passwords
- The BCrypt hash used: `$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa`
- This hash corresponds to the password: `password123` 