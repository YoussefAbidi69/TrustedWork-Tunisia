export interface Milestone {
    id?: number;
    contractId: number;
    ordre?: number;
    titre: string;
    description: string;
    montant: number;
    deadline: Date | string;
    startedAt?: Date | string;
    submittedAt?: Date | string;
    validatedAt?: Date | string;
    status: string;
    rejectionReason?: string;
}