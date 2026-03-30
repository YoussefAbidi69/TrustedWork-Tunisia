import { Component, Input, Output, EventEmitter } from '@angular/core';

export interface TableColumn {
    key: string;
    label: string;
    type?: 'text' | 'badge' | 'date' | 'currency';
}

@Component({
    selector: 'app-recruitment-table',
    templateUrl: './recruitment-table.component.html',
    styleUrls: ['./recruitment-table.component.css']
})
export class RecruitmentTableComponent {
    @Input() columns: TableColumn[] = [];
    @Input() data: any[] = [];
    @Input() loading: boolean = false;

    @Output() onView = new EventEmitter<any>();
    @Output() onEdit = new EventEmitter<any>();
    @Output() onDelete = new EventEmitter<any>();

    getValue(row: any, key: string): any {
        return key.split('.').reduce((o, k) => o?.[k], row);
    }
}
