// API Response types matching Spring Boot backend

export interface ClientResponse {
  id: number;
  lastName: string;
  firstName: string;
  patronymic?: string;
  phoneNumber?: string;
  email?: string;
  telegramChatId?: string;
  status: string;
  important: boolean;
  discountPercent: number;
  note?: string;
  importanceNote?: string;
  totalOrders: number;
  totalSpent: number;
  totalProfit: number;
  firstVisitAt?: string;
  lastVisitAt?: string;
}

export interface UserResponse {
  id: number;
  username: string;
  role: string;
  firstName: string;
  lastName?: string;
  email: string;
  locationId?: number;
  active: boolean;
  completedOrdersCount: number;
  completedWorksCount: number;
  totalRevenue: number;
  lastCompletedAt?: string;
}

export interface OrderResponse {
  id: number;
  orderNumber: string;
  publicToken: string;
  client?: ClientResponse;
  masterId?: number;
  receiverId: number;
  locationId: number;
  status: string;
  acceptedAt?: string;
  mustBeFinishedAt: string;
  finishedAt?: string;
  pickUp?: string;
  deviceType: string;
  manufacturer: string;
  model: string;
  serialNumber: string;
  malfunction: string;
  malfunctionDescription?: string;
  accessories?: string;
  conditionOnAcceptance?: string;
  locked: boolean;
  passwordProvided: boolean;
  photosTaken: boolean;
  priceEstimate?: number;
  finalPrice?: number;
  diagnosticsPrice?: number;
  costPrice?: number;
  profit?: number;
  clientAgreedToDiagnostics: boolean;
  clientAgreedToPasswordUse: boolean;
  storageDeadlineDays: number;
  approvedPrice?: number;
  approvedDeadline?: string;
  approvedAt?: string;
}

export interface PaymentResponse {
  id: number;
  direction: string;
  orderId?: number;
  expenseCategory?: string;
  orderWorkId?: number;
  recipientName?: string;
  recipientContact?: string;
  amount: number;
  method: string;
  paidAt?: string;
  acceptedByUserId: number;
  comment?: string;
}

export interface PartResponse {
  id: number;
  name: string;
  sku: string;
  purchasePrice: number;
  salePrice: number;
  quantityInStock: number;
  minStock: number;
  locationId?: number;
  active: boolean;
  lowStock: boolean;
}

export interface OrderWorkResponse {
  id: number;
  orderId: number;
  name: string;
  clientPrice: number;
  quantity: number;
  executorType: string;
  masterId?: number;
  externalMasterName?: string;
  externalMasterContact?: string;
  costPrice: number;
  paidToExecutor: number;
  comment?: string;
}

export interface LocationResponse {
  id: number;
  name: string;
  address?: string;
  phone?: string;
  workingHours?: string;
  active: boolean;
  defaultLocation: boolean;
}

export interface ServiceCenterResponse {
  id: number;
  name: string;
  legalName: string;
  inn: string;
  ogrn?: string;
  legalAddress?: string;
  actualAddress?: string;
  phone?: string;
  email?: string;
  directorName?: string;
}

// Auth types
export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: UserResponse;
}
