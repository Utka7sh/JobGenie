export interface JobSearchPayload {
  keyword: string;
  location?: string;
  dateSincePosted: string;
  salary?: string;
  experienceLevel: string;
  limit: number;
  sortBy?: string;
}
