import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../core/services/user.service';
import { UserDropdownComponent } from './user-dropdown/user-dropdown.component';
import { filter } from 'rxjs';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, UserDropdownComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  kycStatus = '';

  constructor(
    public readonly auth: AuthService,
    private readonly users: UserService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.refreshCurrentUserState();

    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        if (this.auth.isLoggedIn() && !this.kycStatus) {
          this.refreshCurrentUserState();
        }
      });
  }

  refreshCurrentUserState(): void {
    if (!this.auth.isLoggedIn()) {
      this.kycStatus = '';
      return;
    }

    this.users.getMyProfile().subscribe({
      next: (profile) => {
        this.kycStatus = profile.kycStatus || '';
      },
      error: () => {
        this.kycStatus = '';
      }
    });
  }

  logout(): void {
    this.auth.logout();
    this.kycStatus = '';
    void this.router.navigate(['/auth/login']);
  }
}
