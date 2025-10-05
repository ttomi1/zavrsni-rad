import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LikeService {

  private apiUrl = 'http://localhost:8080/likes';

  constructor(private http: HttpClient) {}

  toggleLike(postId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${postId}/toggle`, {}, { withCredentials: true });
  }

  countLikes(postId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${postId}/count`);
  }

  isLikedByMe(postId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${postId}/liked-by-me`, { withCredentials: true });
  }
}
