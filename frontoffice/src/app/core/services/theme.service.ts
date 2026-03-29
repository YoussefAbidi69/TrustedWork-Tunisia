import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

type ThemeMode = 'dark' | 'light';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private static readonly STORAGE_KEY = 'tw-theme';
  private readonly modeSubject = new BehaviorSubject<ThemeMode>(this.readInitialMode());
  readonly mode$ = this.modeSubject.asObservable();

  constructor() {
    this.applyTheme(this.modeSubject.value);
  }

  get isDarkMode(): boolean {
    return this.modeSubject.value === 'dark';
  }

  toggle(): void {
    const next: ThemeMode = this.isDarkMode ? 'light' : 'dark';
    this.modeSubject.next(next);
    this.applyTheme(next);
    localStorage.setItem(ThemeService.STORAGE_KEY, next);
  }

  private readInitialMode(): ThemeMode {
    const saved = localStorage.getItem(ThemeService.STORAGE_KEY);
    return saved === 'light' ? 'light' : 'dark';
  }

  private applyTheme(mode: ThemeMode): void {
    document.documentElement.setAttribute('data-theme', mode);
  }
}
