export type TalentTag = 'WATCHLIST' | 'FAVORITE' | 'CONTACTED';
export type SourceOrigine = 'SEARCH' | 'HACKATHON' | 'RECOMMENDATION';

export interface TalentPool {
    id?: number;
    entrepriseId: number;
    freelancerId: number;
    tag: TalentTag;
    sourceOrigine: SourceOrigine;
    alerteDisponibilite: boolean;
    notesPrivees?: string;
    dateAjout?: string;
}