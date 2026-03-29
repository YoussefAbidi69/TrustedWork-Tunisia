export interface RecruitmentOffer {
    id?: number;
    applicationId: number;
    salairePropose: number;
    posteExact: string;
    dateDebutSouhaitee: string;
    periodeEssaiMois: number;
    avantages: string;
    deadlineReponse: string;
    status: string;
    dateCreation?: string;
    dateMiseAJour?: string;
}
