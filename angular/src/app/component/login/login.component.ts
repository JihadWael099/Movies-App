import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ApiService} from '../../service/api.service'; 
import { Router } from '@angular/router';
import { LoginDto } from '../../model/login-dto';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  formValues: LoginDto = {
    username: '',  
    password: ''
  };
  isLoading = false;      
  loginError = '';      
  
  constructor(private apiService: ApiService ,
    private router: Router 
  ) {}

  handleFormSubmit(form: NgForm) {
    if (form.valid) {
      this.onLogin();
    }
  }

  onLogin() {
    this.isLoading = true;
    this.loginError = ''; 
    this.apiService.login(this.formValues).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Login success!', response);
        
        localStorage.setItem('role', response.role);
        this.router.navigate(['/movies']);
      },
      error: (err) => {
        this.isLoading = false;
        this.loginError = 'Login failed. Check your credentials.'; 
        console.error('Login error:', err);
      }
    });
  }
}