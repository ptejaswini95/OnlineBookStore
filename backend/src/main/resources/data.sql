-- Sample data for Online Book Store

-- Insert sample users (password is 'password123' encoded with BCrypt)
INSERT INTO bookstore.users (username, email, password, role) VALUES
('admin', 'admin@bookstore.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN'),
('user1', 'user1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'USER'),
('user2', 'user2@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'USER')
ON CONFLICT (username) DO NOTHING;

-- Insert sample books
INSERT INTO bookstore.books (title, author, description, price, stock, isbn, category, published_date) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 'A story of the fabulously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.', 12.99, 50, '9780743273565', 'Fiction', '1925-04-10'),
('To Kill a Mockingbird', 'Harper Lee', 'The story of young Scout Finch and her father Atticus in a racially divided Alabama town.', 14.99, 45, '9780446310789', 'Fiction', '1960-07-11'),
('1984', 'George Orwell', 'A dystopian novel about totalitarianism and surveillance society.', 11.99, 60, '9780451524935', 'Science Fiction', '1949-06-08'),
('Pride and Prejudice', 'Jane Austen', 'A romantic novel of manners that follows the emotional development of Elizabeth Bennet.', 9.99, 40, '9780141439518', 'Romance', '1813-01-28'),
('The Hobbit', 'J.R.R. Tolkien', 'A fantasy novel about a hobbit who embarks on a quest to reclaim a dwarf kingdom.', 15.99, 35, '9780547928241', 'Fantasy', '1937-09-21'),
('The Catcher in the Rye', 'J.D. Salinger', 'A novel about teenage alienation and loss of innocence in post-World War II America.', 13.99, 30, '9780316769488', 'Fiction', '1951-07-16'),
('Lord of the Flies', 'William Golding', 'A novel about a group of British boys stranded on an uninhabited island.', 10.99, 25, '9780399501487', 'Fiction', '1954-09-17'),
('Animal Farm', 'George Orwell', 'An allegorical novella about a group of farm animals who rebel against their human farmer.', 8.99, 55, '9780451526342', 'Fiction', '1945-08-17'),
('The Alchemist', 'Paulo Coelho', 'A novel about a young Andalusian shepherd who dreams of finding a worldly treasure.', 12.99, 70, '9780062315007', 'Fiction', '1988-01-01'),
('Brave New World', 'Aldous Huxley', 'A dystopian novel about a futuristic society controlled by technology and conditioning.', 11.99, 40, '9780060850524', 'Science Fiction', '1932-01-01'),
('The Little Prince', 'Antoine de Saint-Exupéry', 'A poetic tale about a young prince who visits various planets in space.', 9.99, 65, '9780156013987', 'Fiction', '1943-04-06'),
('The Da Vinci Code', 'Dan Brown', 'A mystery thriller novel about a murder in the Louvre Museum and a religious mystery.', 16.99, 80, '9780307474278', 'Mystery', '2003-03-18'),
('Gone Girl', 'Gillian Flynn', 'A psychological thriller about a woman who disappears on her fifth wedding anniversary.', 14.99, 45, '9780307588364', 'Mystery', '2012-06-05'),
('The Hunger Games', 'Suzanne Collins', 'A dystopian novel about a teenage girl who volunteers to take her sister''s place in a deadly competition.', 13.99, 90, '9780439023481', 'Science Fiction', '2008-09-14'),
('The Fault in Our Stars', 'John Green', 'A novel about two teenagers who meet at a cancer support group and fall in love.', 12.99, 75, '9780525478812', 'Romance', '2012-01-10')
ON CONFLICT (isbn) DO NOTHING; 