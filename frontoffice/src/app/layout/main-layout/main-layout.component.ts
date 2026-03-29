import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css'
})
export class MainLayoutComponent {
  isAuthRoute = false;

  constructor(private readonly router: Router) {
    this.updateRoute(this.router.url);
    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event) => this.updateRoute(event.urlAfterRedirects));
  }

  private updateRoute(url: string): void {
    const path = url.split('?')[0].split('#')[0] ?? '';
    const normalized = path.startsWith('/') ? path : `/${path}`;
    this.isAuthRoute =
      normalized.startsWith('/auth/') || normalized === '/auth';
  }
}
