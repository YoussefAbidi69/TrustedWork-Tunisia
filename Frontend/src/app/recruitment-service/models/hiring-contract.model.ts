export type TypeContrat = 'CDI' | 'CDD' | 'CIVP' | 'STAGE' | 'ALTERNANCE';
export type ContractStatus = 'DRAFT' | 'SIGNED' | 'ACTIVE' | 'TERMINATED';

export interface HiringContract {
    id?: number;
    offerId: number;
    freelancerId: number;
    entrepriseId: number;
    typeContrat: TypeContrat;
    salaireFinal: number;
    dateDebutEffective: string;
    periodeEssai: number;
    commissionPlateforme: number;
    feedbackPostEmbauche3Mois?: string;
    status?: ContractStatus;
    dateContratSigne?: string;
    updatedAt?: string;
}