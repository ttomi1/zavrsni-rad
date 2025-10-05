import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-oauth2-redirect',
  standalone: false,
  templateUrl: './oauth2-redirect.component.html',
  styleUrl: './oauth2-redirect.component.css'
})
export class Oauth2RedirectComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    console.log('✅ Oauth2RedirectComponent pokrenut');
    this.route.queryParams.subscribe(params => {
      const accessToken = params['accessToken'];
      console.log('Access token from URL:', accessToken);
      if (accessToken) {
        localStorage.setItem('token', accessToken); // spremi token
        this.authService.setLoggedIn(true);         // poziv metode umjesto direktnog pristupa
        this.router.navigate(['/']);                // redirect
      } else {
        this.router.navigate(['/login']);
      }
    });
  }
}
