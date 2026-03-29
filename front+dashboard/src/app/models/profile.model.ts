export interface FreelancerProfile {
  id?: number;
  userId: number;
  bio: string;
  titre: string;
  tauxHoraire: number;
  localisation: string;
  disponibilite: 'AVAILABLE' | 'BUSY' | 'NOT_AVAILABLE';
  domaineExpertise: string;
  trustPassportUrl?: string;
}

export interface SkillBadge {
  id?: number;
  profileId: number;
  nomSkill: string;
  niveau: 'JUNIOR' | 'CONFIRMED' | 'EXPERT';
  dateValidation: string;
  certificatHash?: string;
}

export interface CertificationExam {
  id?: number;
  domaine: string;
  questions: string;
  dureeMinutes: number;
  scoreMinimum: number;
  tentatives?: number;
  baremeConfig: string;
}