export interface Book {
  id?: number;
  title: string;
  author: string;
  isbn: string;
  price: number;
  description: string;
  category: string;
  stockQuantity: number;
  publishedDate: string;
  imageUrl?: string;
}

export interface User {
  id?: number;
  username: string;
  email: string;
  role: 'USER' | 'ADMIN';
  firstName?: string;
  lastName?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
}

export interface CartItem {
  book: Book;
  quantity: number;
}

export interface Order {
  id?: number;
  userId: number;
  items: CartItem[];
  totalAmount: number;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED';
  orderDate: string;
} 