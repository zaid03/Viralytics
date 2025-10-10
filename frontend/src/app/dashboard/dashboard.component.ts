import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.checkSession(); 
  }

  checkSession(): void {
    this.http.get<{ authenticated: boolean; sessionId?: string; principal?: string; userId?: number }>(
      'http://localhost:8080/api/auth/session',
      { withCredentials: true }
    ).subscribe({
      next: res => console.log('session', res),
      error: err => {
        alert("you must be logged in to view this page");
        console.error('session error', err)
        this.router.navigate(['/']);
      }
    });
  }
}
