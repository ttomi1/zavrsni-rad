import { Component, OnInit } from '@angular/core';
import { User } from '../../model/user.model';
import { UserService } from '../../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  username: string = '';
  stats: { followers: number, following: number } | null = null;
  isFollowing: boolean = false;
  isOwnProfile: boolean = false;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // dohvati trenutno ulogiranog korisnika
    this.userService.getProfile().subscribe(currentUser => {
      this.route.paramMap.subscribe(params => {
        const usernameParam = params.get('username');
        if (usernameParam) {
          this.username = usernameParam;

          // provjeri je li ovo moj profil
          this.isOwnProfile = currentUser.userName === usernameParam;

          // dohvati profil korisnika iz URLa
          this.userService.getProfileByUsername(usernameParam).subscribe(user => {
            this.user = user;


            this.userService.getFollowStats(user.userName).subscribe(stats => this.stats = stats);

            // tuđe profile provjeri da li ga pratim
            if (!this.isOwnProfile) {
              this.userService.isFollowing(user.userName).subscribe(isFollowing => this.isFollowing = isFollowing);
            }
          });
        }
      });
    });
  }

  toggleFollow(): void {
    if (!this.user) return;
    this.userService.toggleFollow(this.user.userName).subscribe(() => {
      this.isFollowing = !this.isFollowing;
      if (this.stats) {
        this.stats.followers += this.isFollowing ? 1 : -1;
      }
    });
  }
}

