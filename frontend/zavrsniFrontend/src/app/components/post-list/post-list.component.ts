import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import { Post } from '../../model/post.model';
import { PostService } from '../../services/post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { CommentService } from '../../services/comment.service';
import { LikeService } from '../../services/like.service';

@Component({
  selector: 'app-post-list',
  standalone: false,
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css']
})
export class PostListComponent implements OnInit {

  posts: Post[] = [];
  currentUsername: string | null = null;

  constructor(
    private postService: PostService,
    private userService: UserService,
    private commentService: CommentService,
    private likeService: LikeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // dohvat trenutnog usera
    this.userService.getProfile().subscribe({
      next: user => this.currentUsername = user.userName,
      error: err => console.error(err)
    });

    // osluškuj promjene rute
    this.route.url.subscribe(segments => {
      if (segments.length === 0) {
        // '/'
        this.loadPosts('home');
      } else if (segments[0].path === 'feed') {
        this.loadPosts('feed');
      } else if (segments[0].path === 'profile' && segments[1]) {
        this.loadPosts('user', segments[1].path);
      }
    });

    console.log('Postovi', this.posts);
  }

  private loadPosts(source: 'home' | 'feed' | 'user', username?: string): void {
    let postsObservable;

    if (source === 'feed') {
      postsObservable = this.postService.getFeedPosts();
    } else if (source === 'user' && username) {
      postsObservable = this.postService.getPostsByUsername(username);
    } else {
      postsObservable = this.postService.getAllPosts();
    }

    postsObservable.subscribe({
      next: posts => {
        this.posts = posts.map(post => ({
          ...post,
          comments: [],
          newCommentText: '',
          likedByMe: false,
          likesCount: 0
        }));

        this.posts.forEach(post => {
          this.loadComments(post);
          this.loadLikes(post);
        });
      },
      error: err => console.error(err)
    });
  }

  private loadComments(post: Post) {
    this.commentService.getComments(post.id).subscribe(comments => post.comments = comments);
  }

  private loadLikes(post: Post) {
    this.likeService.isLikedByMe(post.id).subscribe(liked => post.likedByMe = liked);
    this.likeService.countLikes(post.id).subscribe(count => post.likesCount = count);
  }

  submitComment(post: Post) {
    if (!post.newCommentText) return;
    this.commentService.addComment(post.id, post.newCommentText).subscribe(comment => {
      post.comments = [...(post.comments || []), comment];
      post.newCommentText = '';
    });
  }

  removeComment(post: Post, commentId: number) {
    this.commentService.deleteComment(commentId).subscribe({
      next: () => post.comments = post.comments?.filter(c => c.id !== commentId) || [],
      error: err => console.error(err)
    });
  }

  removePost(postId: number) {
    if (!confirm('Jesi li siguran da želiš obrisati ovaj post?')) return;
    this.postService.deletePost(postId).subscribe({
      next: () => this.posts = this.posts.filter(p => p.id !== postId),
      error: err => console.error(err)
    });
  }

  toggleLike(post: Post) {
    this.likeService.toggleLike(post.id).subscribe(() => {
      post.likedByMe = !post.likedByMe;
      post.likesCount = post.likedByMe
        ? (post.likesCount || 0) + 1
        : (post.likesCount || 0) - 1;
    });
  }
}
