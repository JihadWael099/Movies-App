import { Movie } from './../../model/movie';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-card',
  imports: [],
  templateUrl: './card.component.html',
  styleUrl: './card.component.css'
})
export class CardComponent {
  
  @Input() movie!:Movie;

  userRole: any;
  constructor(private router:Router, private api:ApiService){
    this.userRole = this.api.getRole();
  }

  handleDetailsItem(id:string){
    this.router.navigate(['/movie-details',id]);
  }

  

  


  handleAddItem(id: string) {
    this.api.getMovieByIdExternal(id).subscribe(
      (movie) => {
        this.api.addMovie(movie).subscribe(
          (response) => {
            console.log('Movie added successfully', response);
          },
          (error) => {
    
            console.error('Error adding movie', error);
          }
        );
      },
      (error) => {
        console.error('Error fetching movie details', error);
      }
    );
  }
  handleDeleteItem(id:string){
    this.router.navigate(['/movie-add',id]);
  }


}
