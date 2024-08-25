import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { Location } from '@angular/common';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let location: Location;

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

  const mockActivatedRoute = {
    snapshot: {
      paramMap: convertToParamMap({ id: '1' }),
    },
  };

  const mockRouter = {
    navigate: jest.fn(),
    createUrlTree: jest.fn(),
    navigateByUrl: jest.fn(),
    url: '/sessions/create'
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: { detail: jest.fn(), create: jest.fn(), update: jest.fn()}},
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: { open: jest.fn() } },
        { provide: Router, useValue: mockRouter },
      ],
      declarations: [FormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);

    fixture.detectChanges();

    jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession)); // mock the detail method for the update mode
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create session', () => {
    jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));

    component.sessionForm?.setValue({
      name: 'Yoga Session',
      description: 'Description Yoga Session',
      date: "30/07/2024",
      teacher_id: 1
    });

    component.submit();

    expect(sessionApiService.create).toHaveBeenCalledWith({
      name: 'Yoga Session',
      description: 'Description Yoga Session',
      date: "30/07/2024",
      teacher_id: 1
    });

    expect(matSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });
  it('should submit the form in create mode', () => {
    jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));

    component.sessionForm?.setValue({
      name: 'Test Session',
      date: '2024-08-25',
      teacher_id: 1,
      description: 'Test Description'
    });

    component.submit();

    expect(sessionApiService.create).toBeCalledWith({
      name: 'Test Session',
      date: '2024-08-25',
      teacher_id: 1,
      description: 'Test Description'
    });

    expect(matSnackBar.open).toBeCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(router.navigate).toBeCalledWith(['sessions']);
  });

  it('should submit the form in update mode', () => {
    mockRouter.url = '/sessions/update/1';
    component.ngOnInit();
    fixture.detectChanges();

    jest.spyOn(sessionApiService, 'update').mockReturnValue(of(mockSession));

    component.sessionForm?.setValue({
      name: 'Session Updated',
      date: '2024-07-30',
      teacher_id: 1,
      description: 'Description Updated'
    });

    component.submit();

    expect(sessionApiService.update).toBeCalledWith('1', {
      name: 'Session Updated',
      date: '2024-07-30',
      teacher_id: 1,
      description: 'Description Updated'
    });

    expect(matSnackBar.open).toBeCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(router.navigate).toBeCalledWith(['sessions']);
  });
});
