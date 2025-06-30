import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Form, InputGroup, Alert, Badge } from 'react-bootstrap';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { Book } from '../types';
import apiService from '../services/api';

const Books: React.FC = () => {
  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const categories = ['Fiction', 'Non-Fiction', 'Science Fiction', 'Mystery', 'Romance', 'Biography', 'History', 'Technology'];

  useEffect(() => {
    const category = searchParams.get('category');
    if (category) {
      setSelectedCategory(category);
    }
  }, [searchParams]);

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      setLoading(true);
      const fetchedBooks = await apiService.getBooks();
      setBooks(fetchedBooks);
    } catch (err) {
      setError('Failed to load books');
      console.error('Error fetching books:', err);
    } finally {
      setLoading(false);
    }
  };

  const filteredBooks = books.filter(book => {
    const matchesSearch = book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         book.author.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         book.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = !selectedCategory || book.category === selectedCategory;
    return matchesSearch && matchesCategory;
  });

  const handleCategoryChange = (category: string) => {
    setSelectedCategory(category);
    if (category) {
      setSearchParams({ category });
    } else {
      setSearchParams({});
    }
  };

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
      <Row className="mb-4">
        <Col>
          <h1>Browse Books</h1>
          {error && <Alert variant="danger">{error}</Alert>}
        </Col>
      </Row>

      {/* Search and Filter */}
      <Row className="mb-4">
        <Col md={6}>
          <InputGroup>
            <Form.Control
              placeholder="Search books by title, author, or description..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <Button variant="outline-secondary">
              🔍 Search
            </Button>
          </InputGroup>
        </Col>
        <Col md={6}>
          <Form.Select
            value={selectedCategory}
            onChange={(e) => handleCategoryChange(e.target.value)}
          >
            <option value="">All Categories</option>
            {categories.map(category => (
              <option key={category} value={category}>{category}</option>
            ))}
          </Form.Select>
        </Col>
      </Row>

      {/* Results Count */}
      <Row className="mb-3">
        <Col>
          <p className="text-muted">
            Showing {filteredBooks.length} of {books.length} books
            {selectedCategory && ` in ${selectedCategory}`}
          </p>
        </Col>
      </Row>

      {/* Books Grid */}
      <Row xs={1} md={2} lg={3} xl={4} className="g-4">
        {filteredBooks.map((book) => (
          <Col key={book.id}>
            <Card className="h-100">
              <Card.Body>
                <Card.Title className="h6">{book.title}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">
                  by {book.author}
                </Card.Subtitle>
                <Badge bg="secondary" className="mb-2">{book.category}</Badge>
                <Card.Text className="small">
                  {book.description.length > 100
                    ? `${book.description.substring(0, 100)}...`
                    : book.description}
                </Card.Text>
                <div className="d-flex justify-content-between align-items-center">
                  <span className="h5 text-primary mb-0">${book.price}</span>
                  <div className="text-muted small">
                    Stock: {book.stockQuantity}
                  </div>
                </div>
              </Card.Body>
              <Card.Footer className="bg-transparent">
                <div className="d-grid gap-2">
                  <Button
                    variant="outline-primary"
                    size="sm"
                    onClick={() => navigate(`/books/${book.id}`)}
                  >
                    View Details
                  </Button>
                </div>
              </Card.Footer>
            </Card>
          </Col>
        ))}
      </Row>

      {filteredBooks.length === 0 && !loading && (
        <Row className="mt-5">
          <Col className="text-center">
            <Alert variant="info">
              No books found matching your criteria.
            </Alert>
          </Col>
        </Row>
      )}
    </Container>
  );
};

export default Books; 