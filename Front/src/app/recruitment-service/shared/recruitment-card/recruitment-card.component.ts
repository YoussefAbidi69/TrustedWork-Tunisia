import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-recruitment-card',
    templateUrl: './recruitment-card.component.html',
    styleUrls: ['./recruitment-card.component.css']
})
export class RecruitmentCardComponent {
    @Input() title: string = '';
    @Input() subtitle: string = '';
    @Input() icon: string = '';
    @Input() padding: boolean = true;
}
