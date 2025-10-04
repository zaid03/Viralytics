import { Component, output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(private router: Router) {}

  password: boolean = false;
  showPassword: boolean = false;
  showSignUp: boolean = false;

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  toggleSignup(): void {
    this.showSignUp = !this.showSignUp;
  }

  blobPAth = 'assets/blob.svg';
  googlePath = 'assets/google.svg'
}
