import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AiOutputDialogComponent } from './ai-output-dialog.component';

describe('AiOutputDialogComponent', () => {
  let component: AiOutputDialogComponent;
  let fixture: ComponentFixture<AiOutputDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AiOutputDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AiOutputDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
