import { CommonModule } from '@angular/common';
import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ThemeService } from '../../../core/services/theme.service';

@Component({
  selector: 'app-user-dropdown',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-dropdown.component.html',
  styleUrl: './user-dropdown.component.css'
})
export class UserDropdownComponent {
  @Input() email = '';
  @Input() kycStatus = '';
  @Output() logoutClicked = new EventEmitter<void>();

  open = false;

  constructor(public readonly theme: ThemeService) {}

  get initials(): string {
    const source = this.email.trim();
    if (!source) {
      return 'U';
    }
    return source.charAt(0).toUpperCase();
  }

  toggleMenu(): void {
    this.open = !this.open;
  }

  closeMenu(): void {
    this.open = false;
  }

  onLogout(): void {
    this.closeMenu();
    this.logoutClicked.emit();
  }

  @HostListener('document:keydown.escape')
  onEscapePressed(): void {
    this.closeMenu();
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement | null;
    if (!target) {
      return;
    }
    if (!target.closest('.user-dropdown')) {
      this.closeMenu();
    }
  }
}
