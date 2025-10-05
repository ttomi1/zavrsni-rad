import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { User } from '../../model/user.model';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  userId: number = 0;
  user: User | null = null;
  myUsername: string | null = null;
  isLoggedIn: boolean = false;

  constructor(
    public authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // subscribe na login status
    this.authService.loggedIn$.subscribe(status => {
      this.isLoggedIn = status;
      if (status) {
        this.loadUserData();
      } else {
        this.user = null;
        this.myUsername = null;
      }
    });
  }

  loadUserData() {
    this.authService.getUserId().subscribe({
      next: id => this.userId = id,
      error: err => {
        console.error('Greška pri dohvaćanju userId:', err);
        this.userId = 0;
      }
    });

    this.userService.getProfile().subscribe({
      next: user => {
        this.user = user;
        this.myUsername = user.userName;
      },
      error: err => console.error('Greška pri dohvaćanju profila:', err)
    });
  }

  logout() {
    this.authService.logout();
  }
}
