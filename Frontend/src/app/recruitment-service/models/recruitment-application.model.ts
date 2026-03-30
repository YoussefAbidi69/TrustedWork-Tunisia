export type ApplicationStatus =
    | 'SUBMITTED'
    | 'REVIEWED'
    | 'SHORTLISTED'
    | 'INTERVIEW'
    | 'OFFERED'
    | 'HIRED'
    | 'REJECTED'
    | 'PENDING';

export interface RecruitmentApplication {
    id?: number;
    jobPositionId: number;
    jobPositionTitre?: string;   // retourné par le backend pour affichage
    freelancerId: number;
    entrepriseId: number;
    lettreMotivation: string;
    pretentionSalariale: number;
    disponibilite: string;
    matchingScore: number;
    scoreDetails?: string;       // JSON XAI décomposé
    status?: ApplicationStatus;
    motifRejet?: string;         // rempli si REJECTED
    datePostulation?: string;
    updatedAt?: string;
}