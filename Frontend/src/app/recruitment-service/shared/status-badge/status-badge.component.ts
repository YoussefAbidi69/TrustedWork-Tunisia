import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-status-badge',
    template: `<span class="badge" [ngClass]="badgeClass">{{ status }}</span>`,
    styleUrls: ['./status-badge.component.css']
})
export class StatusBadgeComponent {
    @Input() status: string = '';

    get badgeClass(): string {
        const s = (this.status || '').toUpperCase();
        if (['OPEN', 'ACTIVE', 'ACCEPTED', 'SIGNED', 'CONFIRMED', 'DISPONIBLE'].includes(s)) return 'badge-success';
        if (['PENDING', 'SUBMITTED', 'SCHEDULED', 'EN_ATTENTE', 'PROPOSED'].includes(s)) return 'badge-warning';
        if (['CLOSED', 'REJECTED', 'CANCELLED', 'EXPIRED', 'TERMINATED'].includes(s)) return 'badge-error';
        if (['DRAFT', 'IN_PROGRESS', 'SHORTLISTED'].includes(s)) return 'badge-info';
        return 'badge-default';
    }
}
