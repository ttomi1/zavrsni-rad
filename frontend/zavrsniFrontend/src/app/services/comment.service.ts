import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { PostComment } from '../model/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) {}

  getComments(postId: number): Observable<PostComment[]> {
    return this.http.get<PostComment[]>(`http://localhost:8080/comments/${postId}`, { withCredentials: true });
  }

  addComment(postId: number, text: string): Observable<PostComment> {
    return this.http.post<PostComment>(`http://localhost:8080/comments/${postId}`, { text }, { withCredentials: true });
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`http://localhost:8080/comments/${commentId}`, { withCredentials: true });
  }
}
