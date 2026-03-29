import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import { SkillService } from '../../services/skill.service';
import { ExamService } from '../../services/exam.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  totalProfiles = 0;
  totalSkills = 0;
  totalExams = 0;

  constructor(
    private profileService: ProfileService,
    private skillService: SkillService,
    private examService: ExamService
  ) {}

  ngOnInit() {
    this.profileService.getAll().subscribe(data => this.totalProfiles = data.length);
    this.skillService.getAll().subscribe(data => this.totalSkills = data.length);
    this.examService.getAll().subscribe(data => this.totalExams = data.length);
  }
}