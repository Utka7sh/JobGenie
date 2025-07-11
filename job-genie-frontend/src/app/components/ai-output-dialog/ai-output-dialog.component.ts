import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-ai-output-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatInputModule,
    NgIf
  ],
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>

    <mat-dialog-content style="max-height: 500px; overflow-y: auto;">
      <div style="
      white-space: pre-wrap;
      font-family: 'Segoe UI', sans-serif;
      line-height: 1.6;
      font-size: 15px;
      background: #001b32;
      border: 1px solid #ccc;
      padding: 16px;
      border-radius: 8px;
      overflow-wrap: break-word;">
        {{ data.content }}
      </div>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button (click)="copyToClipboard()">Copy</button>
      <button mat-button mat-dialog-close>Close</button>
    </mat-dialog-actions>

  `
})
export class AiOutputDialogComponent {
  copyToClipboard() {
    navigator.clipboard.writeText(this.data.content || '');
  }
  constructor(@Inject(MAT_DIALOG_DATA) public data: { title: string, content: string }) {}
}
