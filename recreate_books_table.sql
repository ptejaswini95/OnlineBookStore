-- Drop and recreate the books table with correct schema
-- This ensures the table has all required columns

-- 1. Drop the existing books table
DROP TABLE IF EXISTS bookstore.books CASCADE;

-- 2. Create the books table with correct schema
CREATE TABLE bookstore.books (
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

-- 3. Insert sample books with all required fields
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
('The Fault in Our Stars', 'John Green', 'A novel about two teenagers who meet at a cancer support group and fall in love.', 12.99, 75, '9780525478812', 'Romance', '2012-01-10');

-- 4. Create indexes for better performance
CREATE INDEX idx_books_title ON bookstore.books(title);
CREATE INDEX idx_books_author ON bookstore.books(author);
CREATE INDEX idx_books_category ON bookstore.books(category);

-- 5. Verify the table structure and data
SELECT 'Table created successfully!' as status;
SELECT COUNT(*) as total_books FROM bookstore.books;
SELECT title, author, category, published_date FROM bookstore.books LIMIT 3; 