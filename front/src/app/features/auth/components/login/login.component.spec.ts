import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { throwError, of } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;

  const mockAuthService = {
    login: jest.fn(),
  };

  const mockSessionService = {
    logIn: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        {
          provide: SessionService,
          useValue: mockSessionService,
        },
        {
          provide: AuthService,
          useValue: mockAuthService,
        },
        {
          provide: Router,
          useValue: { navigate: jest.fn() },
        },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should throw an error', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('error')));
    component.submit();
    expect(component.onError).toBe(true);
  });

  it('should login successfully', () => {
    const mockUser = {
      id: 1,
      token: '12345',
      email: 'test@gmail.com',
      username: 'JeSuisUnTest',
      firstName: 'Jean',
      lastName: 'TANNER',
      password: 'test!1234',
      admin: false,
    };

    mockAuthService.login.mockReturnValue(of(mockUser));
    component.submit();
    expect(sessionService.logIn).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });
  it('should disbable submit button when form is invalid', () => {
    let email = component.form.controls['email'];
    let password = component.form.controls['password'];
    email.setValue('');
    password.setValue('');

    expect(component.form.valid).toBeFalsy();
  });

  it('should disbable submit button when email is blank', () => {
    let email = component.form.controls['email'];
    let password = component.form.controls['password'];
    email.setValue('');
    password.setValue('test!1234');

    expect(component.form.valid).toBeFalsy();
  });

  it('should disbable submit button when password is blank', () => {
    let email = component.form.controls['email'];
    let password = component.form.controls['password'];
    email.setValue('test@gmail.com');
    password.setValue('');

    expect(component.form.valid).toBeFalsy();
  });
});
