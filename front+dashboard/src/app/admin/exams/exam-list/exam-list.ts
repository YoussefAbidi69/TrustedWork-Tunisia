import { Component, OnInit } from '@angular/core';
import { ExamService } from '../../../services/exam.service';
import { CertificationExam } from '../../../models/profile.model';

@Component({
  selector: 'app-exam-list',
  standalone: false,
  templateUrl: './exam-list.html',
  styleUrl: './exam-list.scss'
})
export class ExamList implements OnInit {
  exams: CertificationExam[] = [];
  loading = true;
  showForm = false;
  isEditMode = false;
  editingId?: number;

  newExam: CertificationExam = {
    domaine: '',
    questions: '',
    dureeMinutes: 60,
    scoreMinimum: 70,
    baremeConfig: ''
  };

  domaines = ['Développement Web', 'Design', 'Rédaction', 'Traduction', 'Marketing', 'Data Science'];

  constructor(private examService: ExamService) {}

  ngOnInit() {
    this.loadExams();
  }

  loadExams() {
    this.loading = true;
    this.examService.getAll().subscribe({
      next: data => { this.exams = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  openAddForm() {
    this.isEditMode = false;
    this.editingId = undefined;
    this.newExam = { domaine: '', questions: '', dureeMinutes: 60, scoreMinimum: 70, baremeConfig: '' };
    this.showForm = true;
  }

  openEditForm(e: CertificationExam) {
    this.isEditMode = true;
    this.editingId = e.id;
    this.newExam = { ...e };
    this.showForm = true;
  }

  save() {
    if (this.isEditMode && this.editingId) {
      this.examService.update(this.editingId, this.newExam).subscribe({
        next: () => { this.loadExams(); this.showForm = false; }
      });
    } else {
      this.examService.create(this.newExam).subscribe({
        next: () => { this.loadExams(); this.showForm = false; }
      });
    }
  }

  delete(id: number) {
    if (confirm('Supprimer cet examen ?')) {
      this.examService.delete(id).subscribe(() => this.loadExams());
    }
  }
}