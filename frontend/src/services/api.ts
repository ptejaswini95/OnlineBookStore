import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { Book, User, LoginRequest, LoginResponse, ApiResponse, Order } from '../types';

const API_BASE_URL = 'http://localhost:8081/api';

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add request interceptor to include auth token
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Add response interceptor to handle auth errors
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // Authentication
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response: AxiosResponse<LoginResponse> = await this.api.post('/auth/login', credentials);
    return response.data;
  }

  async register(userData: Partial<User> & { password: string }): Promise<ApiResponse<User>> {
    const response: AxiosResponse<ApiResponse<User>> = await this.api.post('/auth/register', userData);
    return response.data;
  }

  // Books
  async getBooks(): Promise<Book[]> {
    const response: AxiosResponse<Book[]> = await this.api.get('/books');
    return response.data;
  }

  async getBook(id: number): Promise<Book> {
    const response: AxiosResponse<Book> = await this.api.get(`/books/${id}`);
    return response.data;
  }

  async createBook(book: Book): Promise<Book> {
    const response: AxiosResponse<Book> = await this.api.post('/books', book);
    return response.data;
  }

  async updateBook(id: number, book: Book): Promise<Book> {
    const response: AxiosResponse<Book> = await this.api.put(`/books/${id}`, book);
    return response.data;
  }

  async deleteBook(id: number): Promise<void> {
    await this.api.delete(`/books/${id}`);
  }

  // Orders
  async getOrders(): Promise<Order[]> {
    const response: AxiosResponse<Order[]> = await this.api.get('/orders');
    return response.data;
  }

  async createOrder(order: Partial<Order>): Promise<Order> {
    const response: AxiosResponse<Order> = await this.api.post('/orders', order);
    return response.data;
  }

  async updateOrderStatus(id: number, status: string): Promise<Order> {
    const response: AxiosResponse<Order> = await this.api.patch(`/orders/${id}/status`, { status });
    return response.data;
  }

  // Users
  async getUsers(): Promise<User[]> {
    const response: AxiosResponse<User[]> = await this.api.get('/users');
    return response.data;
  }

  async getUserProfile(): Promise<User> {
    const response: AxiosResponse<User> = await this.api.get('/users/profile');
    return response.data;
  }

  async updateUserProfile(userData: Partial<User>): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put('/users/profile', userData);
    return response.data;
  }
}

export const apiService = new ApiService();
export default apiService; 