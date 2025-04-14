import { serverRoutes } from './../../app.routes.server';
import { Component } from '@angular/core';
import { ApiService } from '../../service/api.service';
import { Movie } from '../../model/movie';
import { CardComponent } from "../card/card.component";
import { ActivatedRoute } from '@angular/router';
import { SearchResponse } from '../../model/search-response';


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
  constructor(private apiService: ApiService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const searchTerm = params['search'];

      if (searchTerm !== undefined) {
     
        if (this.apiService.getRole() === 'USER') {
          console.log("user movies")
          this.apiService.searchMoviesByTitleInternal(searchTerm).subscribe({
            next: (movies) => {
              this.movies = movies;
            },
            error: () => {
              this.movies = [];
            }
          });
        } else {
          console.log("admin movies")
          this.apiService.searchMoviesByTitleExternal(searchTerm).subscribe({
            next: (movies) => {
              this.movies = [movies];
              console.log(movies);
            },
            error: () => {
              this.movies=[];
            }
          });
        }
      } else {
       

        console.log("All movies in database");
        this.apiService.getAllForUser().subscribe({
          next: (movies) => {
            this.movies = movies;
            this.allMovies=true;
          },
          error: () => {
            this.movies = [];
          }
        });
      }
    });
  }
}
