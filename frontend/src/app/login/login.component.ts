import { Component} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(private http: HttpClient, private router: Router) {}

  blobPAth = 'assets/blob.svg';
  googlePath = 'assets/google.svg'
  showPassword: boolean = false;
  showSignUp: boolean = false;

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }
  toggleSignup(): void {
    this.showSignUp = !this.showSignUp;
  }

  name = '';
  email = '';
  password = '';
  errorMessage: string | null = null;
  isError: boolean = false;

  submit(): void{
    const payload = {
      email: this.email,
      password: this.password,
    }

    this.http.post<{ status?: string }>('http://localhost:8080/api/auth/login', payload
    ).subscribe({
      next: (res) => {
        console.log("login succesfull.", res);
        this.errorMessage = null;
        this.isError = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.error || err?.message || 'login failed';
        this.isError = true;
        console.log("login error", err);
        console.log("miaw");
      }
    })
  }

  
}
