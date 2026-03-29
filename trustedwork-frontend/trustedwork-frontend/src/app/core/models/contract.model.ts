export interface Contract {
  id?: number;
  reference: string;
  clientId: number;
  freelancerId: number;
  clientWalletId: number;
  freelancerWalletId: number;
  projectId: number;
  projectTitle: string;
  description: string;
  montantTotal: number;
  slaFreelancerHeures: number;
  slaClientJours: number;
  dateSignature?: Date | string;
  dateDebut: Date | string;
  dateFin: Date | string;
  commissionRate: number;
  status: string;
  cancelledAt?: Date | string;
  cancellationReason?: string;
  createdAt?: Date | string;
  updatedAt?: Date | string;
}