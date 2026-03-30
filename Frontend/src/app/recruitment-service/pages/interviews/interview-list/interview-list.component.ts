import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { InterviewService } from '../../../services/interview.service';
import { InterviewSchedule } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
  selector: 'app-interview-list',
  templateUrl: './interview-list.component.html',
  styleUrls: ['./interview-list.component.css']
})
export class InterviewListComponent implements OnInit {
  interviews: InterviewSchedule[] = [];
  filtered: InterviewSchedule[] = [];
  loading = true;
  selectedType = '';
  selectedStatus = '';

  // Vrais types et statuts du backend
  typeOptions = ['', 'PHONE', 'VIDEO', 'ONSITE', 'TECHNICAL_TEST'];
  statusOptions = ['', 'PROPOSED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'RESCHEDULED'];

  columns: TableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'applicationId', label: 'Application ID' },
    { key: 'type', label: 'Type', type: 'badge' },
    { key: 'ordreEntretien', label: 'Round #' },
    { key: 'dateFinalConfirmee', label: 'Date', type: 'date' },
    { key: 'dureePrevueMinutes', label: 'Durée (min)' },
    { key: 'status', label: 'Statut', type: 'badge' }
  ];

  constructor(private interviewService: InterviewService, private router: Router) { }

  ngOnInit(): void { this.loadData(); }

  loadData(): void {
    this.loading = true;
    this.interviewService.getAll().subscribe({
      next: (data) => { this.interviews = data; this.applyFilter(); this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  applyFilter(): void {
    this.filtered = this.interviews.filter(i => {
      const matchType = !this.selectedType || i.type === this.selectedType;
      const matchStatus = !this.selectedStatus || i.status === this.selectedStatus;
      return matchType && matchStatus;
    });
  }

  onFilterType(t: string): void { this.selectedType = t; this.applyFilter(); }
  onFilterStatus(s: string): void { this.selectedStatus = s; this.applyFilter(); }

  onCreate(): void { this.router.navigate(['/recruitment/interviews/create']); }
  onView(row: InterviewSchedule): void { this.router.navigate(['/recruitment/interviews', row.id]); }
  onEdit(row: InterviewSchedule): void { this.router.navigate(['/recruitment/interviews', row.id, 'edit']); }
  onDelete(row: InterviewSchedule): void {
    if (confirm('Supprimer cet entretien ?')) {
      this.interviewService.delete(row.id!).subscribe({ next: () => this.loadData() });
    }
  }
}