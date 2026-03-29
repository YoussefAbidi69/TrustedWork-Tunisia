export interface HiringContract {
    id?: number;
    offerId: number;
    freelancerId: number;
    entrepriseId: number;
    typeContrat: string;
    salaireFinal: number;
    dateDebutEffective: string;
    periodeEssai: boolean;
    commissionPlateforme: number;
    status: string;
    dateCreation?: string;
    dateMiseAJour?: string;
}
