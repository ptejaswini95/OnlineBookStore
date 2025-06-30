-- Fix the books table schema by adding missing columns
-- This script adds the category and published_date columns that the application expects

-- 1. Add the missing category column
ALTER TABLE bookstore.books 
ADD COLUMN IF NOT EXISTS category VARCHAR(50);

-- 2. Add the missing published_date column
ALTER TABLE bookstore.books 
ADD COLUMN IF NOT EXISTS published_date DATE;

-- 3. Update existing books with category and published_date
UPDATE bookstore.books SET 
    category = 'Fiction',
    published_date = '1925-04-10'
WHERE title = 'The Great Gatsby';

UPDATE bookstore.books SET 
    category = 'Fiction',
    published_date = '1960-07-11'
WHERE title = 'To Kill a Mockingbird';

UPDATE bookstore.books SET 
    category = 'Science Fiction',
    published_date = '1949-06-08'
WHERE title = '1984';

UPDATE bookstore.books SET 
    category = 'Romance',
    published_date = '1813-01-28'
WHERE title = 'Pride and Prejudice';

UPDATE bookstore.books SET 
    category = 'Fantasy',
    published_date = '1937-09-21'
WHERE title = 'The Hobbit';

UPDATE bookstore.books SET 
    category = 'Fiction',
    published_date = '1951-07-16'
WHERE title = 'The Catcher in the Rye';

UPDATE bookstore.books SET 
    category = 'Fiction',
    published_date = '1954-09-17'
WHERE title = 'Lord of the Flies';

UPDATE bookstore.books SET 
    category = 'Fiction',
    published_date = '1945-08-17'
WHERE title = 'Animal Farm';

UPDATE bookstore.books SET 
    category = 'Fiction',
    published_date = '1988-01-01'
WHERE title = 'The Alchemist';

UPDATE bookstore.books SET 
    category = 'Science Fiction',
    published_date = '1932-01-01'
WHERE title = 'Brave New World';

UPDATE bookstore.books SET 
    category = 'Fiction',
    published_date = '1943-04-06'
WHERE title = 'The Little Prince';

UPDATE bookstore.books SET 
    category = 'Mystery',
    published_date = '2003-03-18'
WHERE title = 'The Da Vinci Code';

UPDATE bookstore.books SET 
    category = 'Mystery',
    published_date = '2012-06-05'
WHERE title = 'Gone Girl';

UPDATE bookstore.books SET 
    category = 'Science Fiction',
    published_date = '2008-09-14'
WHERE title = 'The Hunger Games';

UPDATE bookstore.books SET 
    category = 'Romance',
    published_date = '2012-01-10'
WHERE title = 'The Fault in Our Stars';

-- 4. Make category column NOT NULL after updating all records
ALTER TABLE bookstore.books 
ALTER COLUMN category SET NOT NULL;

-- 5. Make published_date column NOT NULL after updating all records
ALTER TABLE bookstore.books 
ALTER COLUMN published_date SET NOT NULL;

-- 6. Verify the changes
SELECT title, author, category, published_date FROM bookstore.books LIMIT 5;

-- 7. Show table structure
\d bookstore.books 