import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'app-button-primary',
    template: `
    <button
      class="btn-primary-custom"
      [class.loading]="loading"
      [disabled]="disabled || loading"
      (click)="onClick.emit($event)"
    >
      <span class="btn-spinner" *ngIf="loading"></span>
      <span class="btn-icon" *ngIf="icon && !loading">{{ icon }}</span>
      <span class="btn-label"><ng-content></ng-content></span>
    </button>
  `,
    styleUrls: ['./button-primary.component.css']
})
export class ButtonPrimaryComponent {
    @Input() icon: string = '';
    @Input() loading: boolean = false;
    @Input() disabled: boolean = false;
    @Output() onClick = new EventEmitter<Event>();
}
