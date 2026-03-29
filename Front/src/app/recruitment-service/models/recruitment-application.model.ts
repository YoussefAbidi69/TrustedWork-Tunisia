export interface RecruitmentApplication {
    id?: number;
    jobPositionId: number;
    freelancerId: number;
    entrepriseId: number;
    lettreMotivation: string;
    pretentionSalariale: number;
    disponibilite: string;
    matchingScore: number;
    status?: string;
    datePostulation?: string;
    dateMiseAJour?: string;
}
