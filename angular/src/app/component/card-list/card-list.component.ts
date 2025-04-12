import { Component } from '@angular/core';
import { ApiService } from '../../service/api.service';
import { Movie } from '../../model/movie';
import { CardComponent } from "../card/card.component";
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs';
import { SearchResponse } from '../../model/search-response';

@Component({
  selector: 'app-card-list',
  imports: [CardComponent],
  templateUrl: './card-list.component.html',
  styleUrl: './card-list.component.css'
})
export class CardListComponent {
  movies: Movie[] = [];
  search!: string;
  adminSearch!:SearchResponse;

  constructor(private apiService: ApiService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params['search'] !== undefined) {
        if (this.apiService.getRole() == 'ROLE_USER') {
          this.apiService.searchMoviesByTitleInternal(params['search']).subscribe({
            next: (movies) => {
              this.movies = movies;
            },
            error: (err) => {
              this.movies = [];
            },
          });
        } else {
      
            this.apiService.searchMoviesByTitleExternal(params['search'])
              .subscribe({
                next: (response: SearchResponse) => {
                 
                  this.movies = response.search;
                },
                error: () => {
                  this.movies = [];
                },
              });
        
        }
      }
    });

    this.apiService.getAllForUser().subscribe({
      next: (movies) => {
        this.movies = movies;
      },
      error: (err) => {
        this.movies = [];
      },
    });
  }
}
