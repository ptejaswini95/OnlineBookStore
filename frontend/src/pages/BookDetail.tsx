import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Alert, Badge, Spinner } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import { Book } from '../types';
import apiService from '../services/api';

const BookDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [book, setBook] = useState<Book | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    if (id) {
      fetchBook(parseInt(id));
    }
  }, [id]);

  const fetchBook = async (bookId: number) => {
    try {
      setLoading(true);
      const fetchedBook = await apiService.getBook(bookId);
      setBook(fetchedBook);
    } catch (err) {
      setError('Failed to load book details');
      console.error('Error fetching book:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = () => {
    // TODO: Implement cart functionality
    alert('Add to cart functionality will be implemented');
  };

  const handleQuantityChange = (newQuantity: number) => {
    if (newQuantity >= 1 && newQuantity <= (book?.stockQuantity || 1)) {
      setQuantity(newQuantity);
    }
  };

  if (loading) {
    return (
      <Container className="text-center mt-5">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
      </Container>
    );
  }

  if (error || !book) {
    return (
      <Container>
        <Alert variant="danger">
          {error || 'Book not found'}
        </Alert>
        <Button onClick={() => navigate('/books')} variant="primary">
          Back to Books
        </Button>
      </Container>
    );
  }

  return (
    <Container>
      <Row>
        <Col>
          <Button 
            onClick={() => navigate('/books')} 
            variant="outline-secondary" 
            className="mb-3"
          >
            ← Back to Books
          </Button>
        </Col>
      </Row>

      <Row>
        <Col lg={8}>
          <Card>
            <Card.Body>
              <Row>
                <Col md={4}>
                  <div className="text-center mb-3">
                    <div 
                      className="bg-light rounded p-4 d-flex align-items-center justify-content-center"
                      style={{ height: '300px' }}
                    >
                      <span className="text-muted">📚</span>
                    </div>
                  </div>
                </Col>
                <Col md={8}>
                  <h1>{book.title}</h1>
                  <p className="text-muted h5">by {book.author}</p>
                  
                  <Badge bg="secondary" className="mb-3">{book.category}</Badge>
                  
                  <div className="mb-3">
                    <strong>ISBN:</strong> {book.isbn}
                  </div>
                  
                  <div className="mb-3">
                    <strong>Published:</strong> {new Date(book.publishedDate).toLocaleDateString()}
                  </div>
                  
                  <div className="mb-3">
                    <strong>Stock:</strong> {book.stockQuantity} available
                  </div>
                  
                  <div className="mb-4">
                    <h3 className="text-primary">${book.price}</h3>
                  </div>
                  
                  <div className="mb-4">
                    <h5>Description</h5>
                    <p>{book.description}</p>
                  </div>
                  
                  <div className="d-flex align-items-center gap-3">
                    <div className="d-flex align-items-center">
                      <Button
                        variant="outline-secondary"
                        size="sm"
                        onClick={() => handleQuantityChange(quantity - 1)}
                        disabled={quantity <= 1}
                      >
                        -
                      </Button>
                      <span className="mx-3">{quantity}</span>
                      <Button
                        variant="outline-secondary"
                        size="sm"
                        onClick={() => handleQuantityChange(quantity + 1)}
                        disabled={quantity >= book.stockQuantity}
                      >
                        +
                      </Button>
                    </div>
                    
                    <Button
                      variant="primary"
                      size="lg"
                      onClick={handleAddToCart}
                      disabled={book.stockQuantity === 0}
                    >
                      {book.stockQuantity === 0 ? 'Out of Stock' : 'Add to Cart'}
                    </Button>
                  </div>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
        
        <Col lg={4}>
          <Card>
            <Card.Header>
              <h5>Book Information</h5>
            </Card.Header>
            <Card.Body>
              <div className="mb-2">
                <strong>Category:</strong> {book.category}
              </div>
              <div className="mb-2">
                <strong>ISBN:</strong> {book.isbn}
              </div>
              <div className="mb-2">
                <strong>Published:</strong> {new Date(book.publishedDate).toLocaleDateString()}
              </div>
              <div className="mb-2">
                <strong>Stock:</strong> {book.stockQuantity} copies
              </div>
              <div>
                <strong>Price:</strong> ${book.price}
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default BookDetail; 