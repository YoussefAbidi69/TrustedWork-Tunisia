import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ExamService } from '../../services/exam.service';
import { SkillService } from '../../services/skill.service';
import { CertificationExam } from '../../models/profile.model';

@Component({
  selector: 'app-take-exam',
  standalone: false,
  templateUrl: './take-exam.html',
  styleUrl: './take-exam.scss'
})
export class TakeExam implements OnInit {
  Object = Object;

  exams: CertificationExam[] = [];
  selectedExam: CertificationExam | null = null;
  questions: any[] = [];
  answers: { [key: number]: string } = {};
  loading = true;
  examStarted = false;
  examFinished = false;
  score = 0;
  timeLeft = 0;
  timer: any;
  profileId = 1;
  badgeCreated = false;
  badgeNiveau = '';

  constructor(
    private examService: ExamService,
    private skillService: SkillService,
    private router: Router
  ) {}

  ngOnInit() {
    this.examService.getAll().subscribe({
      next: (data: CertificationExam[]) => {
        this.exams = data;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  startExam(exam: CertificationExam) {
    this.selectedExam = exam;
    this.examStarted = true;
    this.examFinished = false;
    this.badgeCreated = false;
    this.answers = {};
    this.score = 0;
    this.timeLeft = exam.dureeMinutes * 60;

    try {
      this.questions = JSON.parse(exam.questions);
    } catch {
      this.questions = [];
    }

    this.timer = setInterval(() => {
      this.timeLeft--;
      if (this.timeLeft <= 0) {
        this.submitExam();
      }
    }, 1000);
  }

  submitExam() {
    clearInterval(this.timer);
    let correct = 0;
    this.questions.forEach((q: any, i: number) => {
      if (this.answers[i] === q.reponse) correct++;
    });

    const total = this.questions.length || 1;
    this.score = Math.round((correct / total) * 100);
    this.examFinished = true;
    this.examStarted = false;

    // ✅ Si réussi → créer badge immédiatement
    if (this.selectedExam && this.score >= this.selectedExam.scoreMinimum) {
      this.badgeNiveau = this.score >= 90 ? 'EXPERT' :
                         this.score >= 70 ? 'CONFIRMED' : 'JUNIOR';

      const newSkill = {
        profileId: this.profileId,
        nomSkill: this.selectedExam.domaine,
        niveau: this.badgeNiveau as 'JUNIOR' | 'CONFIRMED' | 'EXPERT',
        dateValidation: new Date().toISOString().split('T')[0],
        certificatHash: 'exam_' + Date.now() + '_' +
                        Math.random().toString(36).substring(7)
      };

      this.skillService.create(newSkill).subscribe({
        next: () => {
          this.badgeCreated = true;
          console.log('✅ Badge créé avec succès !');
        },
        error: (err) => {
          console.error('❌ Erreur création badge:', err);
        }
      });
    }
  }

  formatTime(seconds: number): string {
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  }

  retry() {
    this.examFinished = false;
    this.selectedExam = null;
    this.questions = [];
    this.badgeCreated = false;
  }

  goToMySkills() {
    this.router.navigate(['/front/my-skills']);
  }
}