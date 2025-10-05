import {Component, EventEmitter, Output} from '@angular/core';
import {PostService} from '../../services/post.service';

@Component({
  selector: 'app-post-create',
  standalone: false,
  templateUrl: './post-create.component.html',
  styleUrl: './post-create.component.css'
})
export class PostCreateComponent {

  @Output() postCreated = new EventEmitter<void>();
  text = '';
  selectedImage!: File | null;

  constructor(private postService: PostService) {}

  onFileSelected(event: any) {
    this.selectedImage = event.target.files[0];
  }

  submitPost() {
    if (!this.selectedImage || !this.text) return;

    this.postService.createPost(this.text, this.selectedImage).subscribe({
      next: () => {
        alert('Post poslan!');
        this.text = '';
      },
      error: (err) => console.error('Greška pri slanju posta', err)
    });

    this.postCreated.emit();
    this.text = '';
    this.selectedImage = null;
  }

}
