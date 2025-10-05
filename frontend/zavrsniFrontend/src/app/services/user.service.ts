import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  getProfile(): Observable<User> {
    return this.http.get<User>('http://localhost:8080/user/my-profile', {
      withCredentials: true
    });
  }

  getProfileByUsername(username: string): Observable<User> {
    return this.http.get<User>(`http://localhost:8080/user/${username}`);
  }

  getFollowStats(username: string): Observable<{ followers: number, following: number }> {
    return this.http.get<{ followers: number, following: number }>(
      `http://localhost:8080/follows/${username}/stats`,
      { withCredentials: true }
    );
  }

  toggleFollow(username: string): Observable<void> {
    return this.http.post<void>(
      `http://localhost:8080/follows/${username}/toggle`,
      {},
      { withCredentials: true }
    );
  }

  isFollowing(username: string): Observable<boolean> {
    return this.http.get<boolean>(
      `http://localhost:8080/follows/${username}/is-following`,
      { withCredentials: true }
    );
  }

  searchUsers(query: string): Observable<User[]> {
    return this.http.get<User[]>(`http://localhost:8080/user/search?query=${query}`, { withCredentials: true });
  }

}
