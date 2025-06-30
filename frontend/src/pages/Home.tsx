import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Alert } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { Book } from '../types';
import apiService from '../services/api';

const Home: React.FC = () => {
  const [featuredBooks, setFeaturedBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchFeaturedBooks = async () => {
      try {
        const books = await apiService.getBooks();
        // Get first 6 books as featured
        setFeaturedBooks(books.slice(0, 6));
      } catch (err) {
        setError('Failed to load featured books');
        console.error('Error fetching books:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchFeaturedBooks();
  }, []);

  if (loading) {
    return (
      <Container className="text-center mt-5">
        <div className="spinner-border" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </Container>
    );
  }

  return (
    <Container>
      {/* Hero Section */}
      <Row className="mb-5">
        <Col>
          <div className="text-center py-5 bg-light rounded">
            <h1 className="display-4">Welcome to Online Book Store</h1>
            <p className="lead">
              Discover thousands of books across all genres. From bestsellers to hidden gems,
              find your next favorite read with us.
            </p>
            <Button 
              variant="primary" 
              size="lg"
              onClick={() => navigate('/books')}
            >
              Browse All Books
            </Button>
          </div>
        </Col>
      </Row>

      {/* Featured Books */}
      <Row className="mb-4">
        <Col>
          <h2>Featured Books</h2>
          {error && <Alert variant="danger">{error}</Alert>}
        </Col>
      </Row>

      <Row xs={1} md={2} lg={3} className="g-4 mb-5">
        {featuredBooks.map((book) => (
          <Col key={book.id}>
            <Card className="h-100">
              <Card.Body>
                <Card.Title>{book.title}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">
                  by {book.author}
                </Card.Subtitle>
                <Card.Text>
                  {book.description.length > 100
                    ? `${book.description.substring(0, 100)}...`
                    : book.description}
                </Card.Text>
                <div className="d-flex justify-content-between align-items-center">
                  <span className="h5 text-primary">${book.price}</span>
                  <Button
                    variant="outline-primary"
                    size="sm"
                    onClick={() => navigate(`/books/${book.id}`)}
                  >
                    View Details
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {/* Categories Section */}
      <Row className="mb-5">
        <Col>
          <h2>Browse by Category</h2>
          <Row className="g-3">
            {['Fiction', 'Non-Fiction', 'Science Fiction', 'Mystery', 'Romance', 'Biography'].map((category) => (
              <Col xs={6} md={4} lg={2} key={category}>
                <Card className="text-center">
                  <Card.Body>
                    <Card.Title className="h6">{category}</Card.Title>
                    <Button
                      variant="outline-secondary"
                      size="sm"
                      onClick={() => navigate(`/books?category=${category}`)}
                    >
                      Browse
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </Col>
      </Row>
    </Container>
  );
};

export default Home; 