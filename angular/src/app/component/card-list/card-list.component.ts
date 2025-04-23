import { serverRoutes } from './../../app.routes.server';
import { Component } from '@angular/core';
import { ApiService } from '../../service/api.service';
import { Movie } from '../../model/movie';
import { CardComponent } from "../card/card.component";
import { ActivatedRoute, Router } from '@angular/router';
import { SearchResponse } from '../../model/search-response';
import { OmbdService } from '../../service/ombd.service';


@Component({
  selector: 'app-card-list',
  standalone: true,
  imports: [CardComponent],
  templateUrl: './card-list.component.html',
  styleUrl: './card-list.component.css'
})
export class CardListComponent {
  movies: Movie[] = [];
  allMovies:Boolean=false;
  
  constructor(private apiService: ApiService, private route: ActivatedRoute,private ombd:OmbdService,private router: Router) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const searchTerm = params['search'];
  
      if (searchTerm !== undefined) {
        if (this.apiService.getRole() === 'USER') {
          this.apiService.searchMoviesByTitleInternal(searchTerm).subscribe({
            next: (movies) => {
              this.movies = movies;
              if (this.movies.length === 0) {
                this.router.navigate(['/not-found']);
              }
            },
            error: () => {
              this.movies = [];
              this.router.navigate(['/not-found']);
            }
          });
        } else {
          this.ombd.getMovieByTitle(searchTerm).subscribe({
            next: (movies) => {
              this.movies = [this.apiService.transformMovieResponse(movies)];
              if (this.movies.length === 0) {
                this.router.navigate(['/not-found']);
              }
            },
            error: () => {
              this.movies = [];
              this.router.navigate(['/not-found']);
            }
          });
        }
      } else {
        this.apiService.getAllForUser().subscribe({
          next: (movies) => {
            this.movies = movies;
            this.allMovies = true;
          },
          error: () => {
            this.movies = [];
            this.router.navigate(['/not-found']);
          }
        });
      }
    });
  }
  
}
