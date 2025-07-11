import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormsModule,
  ReactiveFormsModule
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { JobService } from '../../services/job.service';
import { JobSearchPayload } from '../../models/job-search.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { AiOutputDialogComponent } from '../ai-output-dialog/ai-output-dialog.component'; // adjust path
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {MatSlideToggle} from '@angular/material/slide-toggle';


@Component({
  selector: 'app-job-form',
  standalone: true,
  templateUrl: './job-form.component.html',
  styleUrls: ['./job-form.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCardModule,
    MatDialogModule,
    AiOutputDialogComponent,
    MatSlideToggle
  ]
})
export class JobFormComponent {
  jobForm: FormGroup;
  showPlatforms = false;
  platforms = ['LinkedIn', 'Naukri', 'Indeed', 'Glassdoor'];
  jobResults: any[] = [];
  visitedJobs: Set<string> = new Set();
  isDarkTheme: any;

  constructor(private fb: FormBuilder, private jobService: JobService, private dialog: MatDialog) {
    this.jobForm = this.fb.group({
      keyword: ['', Validators.required],
      location: [''],
      dateSincePosted: ['', Validators.required],
      salary: [''],
      experienceLevel: ['', Validators.required],
      limit: [10],
      sortBy: ['']
    });
  }

  onNext() {
    if (this.jobForm.valid) {
      this.showPlatforms = true;
    }
  }

  fetchJobs(platform: string) {
    const payload: JobSearchPayload = this.jobForm.value;
    if (platform.toLowerCase() === 'glassdoor') {
      const { keyword, dateSincePosted, experienceLevel } = this.jobForm.value;

      let fromAge = '7'; // default to past week
      if (dateSincePosted === '24hr') fromAge = '1';
      else if (dateSincePosted === 'past week') fromAge = '7';
      else if (dateSincePosted === 'past month') fromAge = '30';

      const sanitizedSeniority = experienceLevel.toLowerCase().replace(/\s+/g, '');
      const encodedRole = encodeURIComponent(keyword.trim());

      const redirectUrl = `http://localhost:8080/redirect/glassdoor?role=${encodedRole}&fromAge=${fromAge}&seniorityType=${sanitizedSeniority}`;
      window.open(redirectUrl, '_blank');
    } else {
      // Use backend for LinkedIn, Naukri, etc.
      this.jobService.getJobsFromPlatform(platform.toLowerCase(), payload).subscribe({
        next: (data) => {
          this.jobResults = data;
        },
        error: (err) => {
          alert('Error fetching jobs');
          console.error(err);
        }
      });
    }
  }
  generateReferral(job: any) {
    const template = prompt("Optional: Enter your referral message template");
    this.jobService.generateReferralMessage({
      jobTitle: job.title,
      companyName: job.company,
      aboutCompany: job.aboutCompany,
      jobDescription: job.description,
      template
    }).subscribe(response => {
      this.dialog.open(AiOutputDialogComponent, {
        data: {
          title: "Referral Message", // or "Cover Letter"
          content: response
        },
        width: '900px',   // wider dialog
        height: '600px',  // taller dialog
        maxHeight: '90vh' // responsive limit
      });

    });
  }

  generateCoverLetter(job: any) {
    const template = prompt("Optional: Enter your cover letter template");
    this.jobService.generateCoverLetter({
      jobTitle: job.title,
      companyName: job.company,
      aboutCompany: job.aboutCompany,
      jobDescription: job.description,
      template
    }).subscribe(response => {
      this.dialog.open(AiOutputDialogComponent, {
        data: {
          title: "Cover Letter",
          content: response
        },
        width: '700px'
      });
    });
  }


  markAsVisited(url: string) {
    this.visitedJobs.add(url);
    window.open(url, '_blank');
  }
}
