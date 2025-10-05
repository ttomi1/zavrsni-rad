import {Component, OnInit} from '@angular/core';
import {PostService} from '../../services/post.service';
import {Post} from '../../model/post.model';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent{
  showPostModal = false;

  onPostCreated() {
    this.showPostModal = false;
    // ovdje možeš triggerati reload postova ako želiš
  }
}
