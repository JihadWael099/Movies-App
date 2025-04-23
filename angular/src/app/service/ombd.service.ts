import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable } from 'rxjs';
import { Movie } from '../model/movie';
import { Movieresponse } from '../model/movieresponse';

@Injectable({
  providedIn: 'root'
})
export class OmbdService {
  
  private  ombd_key = "cbf45b19";
  private  url="http://www.omdbapi.com/";
  constructor(private http: HttpClient) {}

  getMovieByTitle(title: string): Observable<Movieresponse> {
    return this.http.get<Movieresponse>(`${this.url}?t=${title}&apikey=${this.ombd_key}`);
  }

  searchMoviesWithPagination(title: string, page: number): Observable<Movieresponse[]> {
    const searchUrl = `${this.url}?s=${title}&page=${page}&apikey=${this.ombd_key}`;
    return this.http.get<any>(searchUrl).pipe(
      map((response: { Search: any; }) => response.Search), 
      catchError(this.handleError) 
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    throw new Error('Error fetching data from OMDB API');
  }

  getMovieById(imdbId: string): Observable<Movieresponse> {
    const url = `${this.url}?i=${imdbId}&apikey=${this.ombd_key}`;
    return this.http.get<Movieresponse>(url).pipe(
      catchError(this.handleError) 
    );
  }



}
