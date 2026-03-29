import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class NavbarComponent {
  
  constructor(public authService: AuthService) {}

  // ID du contrat à tester (à modifier selon ton contrat)
  testContractId: number = 1;

  // Pour le test, on peut entrer manuellement l'ID
  customContractId: number = 1;
  showInput: boolean = false;

  setContractId(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.customContractId = Number(input.value);
  }

  getPaymentUrl(): string {
    return `/payment/checkout/${this.customContractId}`;
  }

  toggleInput(): void {
    this.showInput = !this.showInput;
  }
}