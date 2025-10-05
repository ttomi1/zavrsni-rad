import { Component } from '@angular/core';
import {PostService} from '../../services/post.service';
import {Post} from '../../model/post.model';

@Component({
  selector: 'app-feed',
  standalone: false,
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css'
})
export class FeedComponent {


  constructor() {}

  ngOnInit(): void {

  }
}
