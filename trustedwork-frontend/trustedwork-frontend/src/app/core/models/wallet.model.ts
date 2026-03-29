export interface Wallet {
  id: number;
  userId: number;
  balance: number;
  totalEarned: number;
  totalSpent: number;
  totalCommissionPaid: number;
  stripeAccountId: string;
  stripeAccountStatus: string;
  createdAt: Date;
  updatedAt: Date;
}