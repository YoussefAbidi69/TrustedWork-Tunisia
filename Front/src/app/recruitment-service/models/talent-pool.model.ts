export interface TalentPool {
    id?: number;
    freelancerId: number;
    entrepriseId: number;
    tag: string;
    sourceOrigine: string;
    alerteDisponibilite: boolean;
    notesPrivees: string;
    dateAjout?: string;
    dateMiseAJour?: string;
}
