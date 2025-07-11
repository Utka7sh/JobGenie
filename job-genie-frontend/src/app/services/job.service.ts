import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JobSearchPayload } from '../models/job-search.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private baseUrl = 'http://localhost:8080/ai'; // Base path for AI APIs
  constructor(private http: HttpClient) {}

  generateReferralMessage(payload: any) {
    return this.http.post(`${this.baseUrl}/generate-referral`, payload, {
      responseType: 'text',
    });
  }

  generateCoverLetter(payload: any) {
    return this.http.post(`${this.baseUrl}/generate-cover-letter`, payload, {
      responseType: 'text',
    });
  }

  getJobsFromPlatform(platform: string, payload: JobSearchPayload): Observable<any> {
    return this.http.post(`/api/${platform}/jobs`, payload);

  }
}
