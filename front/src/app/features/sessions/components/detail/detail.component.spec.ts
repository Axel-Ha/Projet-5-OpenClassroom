import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionService } from '../../../../services/session.service';
import { RouterTestingModule } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { expect } from '@jest/globals';
import { MatIconModule } from '@angular/material/icon';

describe('DetailComponent Integration Tests', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockSessionServiceAdmin = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  const mockSession = {
    id: 1,
    name: 'Yoga Session',
    description: 'Description Yoga Session',
    date: new Date(),
    teacher_id: 1,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockTeacher = {
    id: 1,
    lastName: 'TANNER',
    firstName: 'Jean',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: convertToParamMap({ id: '1' }),
    },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        MatProgressSpinnerModule,
        BrowserAnimationsModule
      ],
      providers: [
        {
          provide: SessionApiService,
          useValue: {
            detail: jest.fn(),
            delete: jest.fn(),
            participate: jest.fn(),
            unParticipate: jest.fn(),
          },
        },
        {
          provide: TeacherService,
          useValue: { detail: jest.fn().mockReturnValue(of(mockTeacher)) },
        },
        { provide: SessionService, useValue: mockSessionServiceAdmin },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: { open: jest.fn() } },
        { provide: Router, useValue: { navigate: jest.fn() } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate back', () => {
    const windowHistoryBack = jest.spyOn(window.history, 'back');
    component.back();
    expect(windowHistoryBack).toHaveBeenCalled();
  });

  it('should delete session', () => {
    const deleteSession = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of({}));
    const matSnackBarOpen = jest.spyOn(matSnackBar, 'open');

    component.delete();
    expect(deleteSession).toHaveBeenCalledWith('1');
    expect(matSnackBarOpen).toHaveBeenCalledWith('Session deleted !', 'Close', {duration: 3000});
  });

  it('should participate to session', () => {
    const participate = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of());
    component.participate();
    expect(participate).toHaveBeenCalledWith('1', '1');
  });

  it('should unparticipate to session', () => {
    const unParticipate = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of());
    component.unParticipate();
    expect(unParticipate).toHaveBeenCalledWith('1', '1');
  });
});


