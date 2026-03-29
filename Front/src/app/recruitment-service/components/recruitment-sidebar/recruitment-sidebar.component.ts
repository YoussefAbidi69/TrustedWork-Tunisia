import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface NavItem {
    label: string;
    icon: string;
    route: string;
}

@Component({
    selector: 'app-recruitment-sidebar',
    templateUrl: './recruitment-sidebar.component.html',
    styleUrls: ['./recruitment-sidebar.component.css']
})
export class RecruitmentSidebarComponent {
    collapsed = false;

    navItems: NavItem[] = [
        { label: 'Dashboard', icon: '📊', route: '/recruitment' },
        { label: 'Job Positions', icon: '💼', route: '/recruitment/job-positions' },
        { label: 'Published Jobs', icon: '📢', route: '/recruitment/job-positions/published' }, // ← ajouter
        { label: 'By Entreprise', icon: '🏢', route: '/recruitment/job-positions/entreprise' },
        { label: 'Applications', icon: '📋', route: '/recruitment/applications' },
        { label: 'Interviews', icon: '🎤', route: '/recruitment/interviews' },
        { label: 'Offers', icon: '📨', route: '/recruitment/offers' },
        { label: 'Contracts', icon: '📝', route: '/recruitment/contracts' },
        { label: 'Talent Pool', icon: '🌟', route: '/recruitment/talent-pool' }

    ];

    constructor(public router: Router) { }

    toggleSidebar(): void {
        this.collapsed = !this.collapsed;
    }

    isActive(route: string): boolean {
        return this.router.url === route;
    }
}
