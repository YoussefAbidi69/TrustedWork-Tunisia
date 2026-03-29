import { Component } from '@angular/core';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { ThemeService } from './core/services/theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [MainLayoutComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(_theme: ThemeService) {}
}