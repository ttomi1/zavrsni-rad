import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'zavrsniFrontend';

  constructor(public router: Router) {}

  shouldShowNavbar(): boolean {
    return !['/login', '/register'].includes(this.router.url);
  }
}
