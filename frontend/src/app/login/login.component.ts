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

    if(this.email.trim() === '' || this.password.trim() === '') {
      this.errorMessage = 'All fields are required';
      this.isError = true;
      return
    }

    this.errorMessage = null;
    this.isError = false;

    this.http.post<{ status?: string }>('http://localhost:8080/api/auth/login', payload,
      { withCredentials: true }
    ).subscribe({
      next: (res) => {
        console.log("login succesfull.", res);
        this.errorMessage = null;
        this.isError = false;
        this.router.navigate(['/dashboard'])
      },
      error: (err) => {
        this.errorMessage = err?.error?.error || err?.message || 'login failed';
        this.isError = true;
        console.log("login error", err);
        console.log("miaw");
      }
    })
  }

  nameSign = '';
  emailSign = '';
  passwordSign = '';
  errorMessageSign: string | null = null;
  isErrorSign: boolean = false;

  signSubmit(): void{
    const payload = {
      email: this.emailSign,
      password: this.passwordSign,
      name: this.nameSign
    }

    if(this.nameSign.trim() === '' || this.emailSign.trim() === '' || this.passwordSign.trim() === '') {
      this.errorMessageSign = 'All fields are required';
      this.isErrorSign = true;
      return;
    }

    this.errorMessageSign = null;
    this.isErrorSign = false;

    this.http.post<{ status?: string }>('http://localhost:8080/api/auth/register', payload,
      { withCredentials: true }
    ).subscribe({
      next: (res) => {
        console.log("registration succesfull.", res);
        this.errorMessageSign = 'Account registered successfully';
        this.isErrorSign = false;
        this.showSignUp = false;
      },
      error: (err) => {
        this.errorMessageSign = err?.error?.error || err?.message || 'sign up failed';
        this.isErrorSign = true;
        console.log("sign up error", err);
      }
    })
  }


}
