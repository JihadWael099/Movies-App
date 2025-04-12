import { Component, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
 
  router = inject(Router);
  apiService=inject(ApiService);

  searchControl = new FormControl('');

  
  

  logout() {
    this.apiService.logout();
    this.router.navigate(['/login']);
  }
  search() {
    let searchString = '';
    if (this.searchControl.value) {
       searchString = this.searchControl.value.trim();
    } 
      this.router.navigate([], {
        queryParams: { search: searchString }
      });
    
  }
}