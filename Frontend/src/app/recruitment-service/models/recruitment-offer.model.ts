export type OfferStatus = 'SENT' | 'ACCEPTED' | 'DECLINED' | 'NEGOTIATING';

export interface RecruitmentOffer {
    id?: number;
    applicationId: number;
    salairePropose: number;
    posteExact: string;
    dateDebutSouhaitee: string;
    periodeEssaiMois: number;
    avantages?: string;
    deadlineReponse: string;
    contreOffreFreelancer?: string;
    status?: OfferStatus;
    dateEnvoi?: string;
}