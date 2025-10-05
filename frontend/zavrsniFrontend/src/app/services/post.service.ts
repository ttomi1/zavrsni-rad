import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Post} from '../model/post.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) {}

  getAllPosts(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/post');
  }

  createPost(text: string, image: File): Observable<any> {
    const formData = new FormData();
    formData.append('text', text);
    formData.append('image', image);
    return this.http.post('http://localhost:8080/post', formData, { withCredentials: true });
  }

  getMyPosts(): Observable<Post[]> {
    return this.http.get<Post[]>('http://localhost:8080/user/my-posts', { withCredentials: true });
  }

  getPostsByUsername(username: string): Observable<Post[]> {
    return this.http.get<Post[]>(`http://localhost:8080/user/${username}/posts`, {
      withCredentials: true
    });
  }

  deletePost(postId: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/post/${postId}`, { withCredentials: true });
  }

  getFeedPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`http://localhost:8080/post/feed`);
  }
}
