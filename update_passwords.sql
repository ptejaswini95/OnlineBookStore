-- Update passwords to simple ones for testing
-- Run this script to update existing users with simple passwords

-- Note: All passwords below are BCrypt encoded versions of simple passwords

-- Update admin password to 'password123' (BCrypt encoded)
UPDATE bookstore.users 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa' 
WHERE username = 'admin';

-- Update user1 password to 'password123' (BCrypt encoded)
UPDATE bookstore.users 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa' 
WHERE username = 'user1';

-- Update user2 password to 'password123' (BCrypt encoded)
UPDATE bookstore.users 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa' 
WHERE username = 'user2';

-- Verify the updates
SELECT username, role FROM bookstore.users;

-- Display login credentials
SELECT 
    'Login Credentials:' as info,
    '' as username,
    '' as password
UNION ALL
SELECT 
    'Admin User:' as info,
    username as username,
    'password123' as password
FROM bookstore.users WHERE username = 'admin'
UNION ALL
SELECT 
    'Regular User 1:' as info,
    username as username,
    'password123' as password
FROM bookstore.users WHERE username = 'user1'
UNION ALL
SELECT 
    'Regular User 2:' as info,
    username as username,
    'password123' as password
FROM bookstore.users WHERE username = 'user2'; 