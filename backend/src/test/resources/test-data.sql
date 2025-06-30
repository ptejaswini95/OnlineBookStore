-- Test Users
INSERT INTO users (username, email, password, role, created_at, updated_at)
VALUES ('admin', 'admin@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (username, email, password, role, created_at, updated_at)
VALUES ('user', 'user@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Test Books
INSERT INTO books (title, author, description, price, stock, isbn, created_at, updated_at)
VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', 'A story of the fabulously wealthy Jay Gatsby', 19.99, 10, '9780743273565', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO books (title, author, description, price, stock, isbn, created_at, updated_at)
VALUES ('To Kill a Mockingbird', 'Harper Lee', 'The story of racial injustice and the loss of innocence', 15.99, 5, '9780446310789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 