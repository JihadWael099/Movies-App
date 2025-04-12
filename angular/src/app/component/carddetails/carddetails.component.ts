import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Movie } from '../../model/movie';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-carddetails',
  imports: [],
  templateUrl: './carddetails.component.html',
  styleUrl: './carddetails.component.css'
})
export class CarddetailsComponent {

  constructor(private activeRoute: ActivatedRoute, private apiService: ApiService) { }

  movieId!: string ;
  movie!: Movie;
  isLoading = true;
  error: string | null = null;
  ngOnInit() {
    this.movieId = this.activeRoute.snapshot.params['id'];
    if (this.movieId!) {
      this.apiService.getMovieByIdInternal(this.movieId).subscribe({
        next: (movie) => {
          this.movie = movie;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = err.message || 'Failed to load movie details';
          this.isLoading = false;
          console.error('API Error:', err);
        }
      });
    } else {
      this.error = 'No movie ID provided';
      this.isLoading = false;
    }
  }
}
