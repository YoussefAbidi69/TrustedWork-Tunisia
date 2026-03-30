export type InterviewType = 'PHONE' | 'VIDEO' | 'ONSITE' | 'TECHNICAL_TEST';

export type InterviewStatus =
    | 'PROPOSED'
    | 'CONFIRMED'
    | 'COMPLETED'
    | 'CANCELLED'
    | 'RESCHEDULED';

export interface InterviewSchedule {
    id?: number;
    applicationId: number;
    type: InterviewType;
    ordreEntretien: number;
    dateFinalConfirmee: string;
    dureePrevueMinutes: number;
    lienVisio?: string;
    status: InterviewStatus;
    feedbackRecruteur?: string;   // ajouté via PATCH /{id}/feedback
    noteRecruteur?: number;       // note /5 ou /10
    createdAt?: string;
}