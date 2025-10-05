import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FollowService {

  private apiUrl = 'http://localhost:8080/follows';

  constructor(private http: HttpClient) {}

  toggleFollow(username: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${username}/toggle`, {}, { withCredentials: true });
  }

  isFollowing(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${username}/is-following`, { withCredentials: true });
  }
}
