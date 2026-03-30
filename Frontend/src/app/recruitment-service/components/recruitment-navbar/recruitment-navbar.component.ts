import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-recruitment-navbar',
    templateUrl: './recruitment-navbar.component.html',
    styleUrls: ['./recruitment-navbar.component.css']
})
export class RecruitmentNavbarComponent {
    constructor(private router: Router) { }

    get pageTitle(): string {
        const url = this.router.url;
        if (url.includes('job-positions')) return 'Job Positions';
        if (url.includes('applications')) return 'Applications';
        if (url.includes('interviews')) return 'Interviews';
        if (url.includes('offers')) return 'Offers';
        if (url.includes('contracts')) return 'Contracts';
        if (url.includes('talent-pool')) return 'Talent Pool';
        return 'Dashboard';
    }
}
