import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import { Movie } from '../model/movie';
import { LoginDto } from '../model/login-dto';
import { AuthResponse } from '../model/auth-response';
import { RegisterDto } from '../model/register-dto';
import { SearchResponse } from '../model/search-response';


const baseUrl = 'http://localhost:8083/api/v1/movies';
const authUrl = 'http://localhost:8083/auth';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) {}


  isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof window.localStorage !== 'undefined';
  }
  private createHeaders(): HttpHeaders {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    if (this.isBrowser()) {
      const token = localStorage.getItem('jwt_token');
      console.log('Retrieved token from localStorage:', token); 
      if (token) {
        headers = headers.append('Authorization', `Bearer ${token}`);
      }
    }
    console.log('Headers:', {
      'Content-Type': headers.get('Content-Type'),
      'Authorization': headers.get('Authorization')
    });
    return headers;
  }
  login(credentials: LoginDto): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${authUrl}/login`, credentials).pipe(
      map((response) => {
        if (this.isBrowser()) {
          localStorage.setItem('jwt_token', response.token);
        }
        return response;
      }),
      catchError(this.handleError)
    );
  }

  register(userData: RegisterDto): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${authUrl}/register`, userData).pipe(
      catchError(this.handleError)
    );
  }

  getAllForUser(): Observable<Movie[]> {
    return this.http.get<Movie[]>(baseUrl, { headers: this.createHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  searchMoviesByTitleExternal(title: string): Observable<SearchResponse> {
    const url = `${baseUrl}/external/title?title=${title}`;

    return this.http.get<SearchResponse>(url, { headers: this.createHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  searchMoviesByTitleInternal(title: string): Observable<Movie[]> {
    const url = `${baseUrl}/internal/search?title=${encodeURIComponent(title)}`;
    return this.http.get<Movie[]>(url, { headers: this.createHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  searchMoviesWithPagination(title: string, page: number): Observable<Movie[]> {
    const url = `${baseUrl}/external/search?title=${title}&page=${page}`;
    return this.http.get<Movie[]>(url, { headers: this.createHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  addMovie(movie: Movie): Observable<Movie> {
    const url = `${baseUrl}/add`;
    return this.http.post<Movie>(url, movie, { headers: this.createHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  getMovieByIdInternal(imdbId: string): Observable<Movie> {
    const url = `${baseUrl}/internal/id?id=${imdbId}`;
    return this.http.get<Movie>(url, { headers: this.createHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  getMovieByIdExternal(imdbId: string): Observable<Movie> {
    const url = `${baseUrl}/external/id?id=${imdbId}`;
    return this.http.get<Movie>(url, { headers: this.createHeaders() }).pipe(
      catchError(this.handleError)
    );
  }

  isLoggedIn(): boolean {
    return this.isBrowser() && !!localStorage.getItem('jwt_token');
  }

  getRole(): string | null {
    return this.isBrowser() ? localStorage.getItem('role') : null;
  }

  hasRole(role: string): boolean {
    return this.getRole() === role;
  }

  logout() {
    if (this.isBrowser()) {
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('role');
    }
  }

  private handleError(error: any) {
    console.error('API Error:', error);
    let errorMessage = 'An unknown error occurred';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client error: ${error.error.message}`;
    } else if (error.status === 401) {
      errorMessage = 'Session expired. Please login again.';
    } else if (error.status === 403) {
      errorMessage = 'You don\'t have permission for this action';
    } else if (error.error?.message) {
      errorMessage = error.error.message;
    } else {
      errorMessage = `Server error: ${error.status} ${error.statusText}`;
    }
    
    return throwError(() => new Error(errorMessage));
  }
}
