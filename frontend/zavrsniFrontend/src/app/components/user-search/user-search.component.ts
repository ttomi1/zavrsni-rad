import { Component } from '@angular/core';
import {debounceTime, distinctUntilChanged, of, switchMap} from 'rxjs';
import {UserService} from '../../services/user.service';
import {User} from '../../model/user.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-search',
  standalone: false,
  templateUrl: './user-search.component.html',
  styleUrl: './user-search.component.css'
})
export class UserSearchComponent {
  searchTerm: string = '';
  results: User[] = [];

  constructor(private userService: UserService, private router: Router) {}

  onSearchChange() {
    if (!this.searchTerm.trim()) {
      this.results = [];
      return;
    }

    of(this.searchTerm).pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => this.userService.searchUsers(term))
    ).subscribe(users => this.results = users);
  }

  goToProfile(username: string) {
    this.router.navigate(['/profile', username]);
    this.results = [];
    this.searchTerm = '';
  }
}
