import { Routes } from '@angular/router';
import { CardListComponent } from './component/card-list/card-list.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { CarddetailsComponent } from './component/carddetails/carddetails.component';

export const routes: Routes = [
    {
        path: '',
        component: CardListComponent,
        title: 'movieApp'
      },
      {
        path: 'login',
        component: LoginComponent,
        title: 'Login'
      },
      {
        path: 'register',
        component: RegisterComponent,
        title: 'Register'
      },
      {
        path: 'movie-details/:id',
        component: CarddetailsComponent,
        title: 'movie details'
      },
      {
        path: 'movies/:title',
        component: CardListComponent,
        title: 'movie search'
      },
];
