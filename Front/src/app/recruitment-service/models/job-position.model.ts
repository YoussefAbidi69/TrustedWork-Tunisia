export interface JobPosition {
    id?: number;
    titre: string;
    description: string;
    typeContrat: string;
    salaireMin: number;
    salaireMax: number;
    localisation: string;
    remote: boolean;
    experienceRequiseAns: number;
    skillsRequis: string;
    deadline: string;
    status: string;
    entrepriseId?: number;
    nombreCandidatures?: number;
    dateCreation?: string;
    dateMiseAJour?: string;
}
