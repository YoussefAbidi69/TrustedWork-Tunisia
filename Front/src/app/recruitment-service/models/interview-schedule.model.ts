export interface InterviewSchedule {
    id?: number;
    applicationId: number;
    type: string;
    ordreEntretien: number;
    dateFinalConfirmee: string;
    dureePrevueMinutes: number;
    lienVisio: string;
    status: string;
    dateCreation?: string;
    dateMiseAJour?: string;
}
