import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { first, of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should disbale submit button when form is invalid', () => {
    component.form.controls.email.setValue(' ');
    component.form.controls.firstName.setValue(' ');
    component.form.controls.lastName.setValue(' ');
    component.form.controls.password.setValue(' ');
    expect(component.form.valid).toBeFalsy();
  });

  it('should throw error on failed registration', () => {
    jest
      .spyOn(authService, 'register')
      .mockReturnValue(throwError(() => new Error('error')));
    const registerRequest = {
      firstName: 'Jean',
      lastName: 'TANNER',
      email: 'test@gmail.com',
      password: 'test!1234',
    };
    component.form.setValue(registerRequest);
    fixture.detectChanges();

    const submitButton = fixture.nativeElement.querySelector(
      'button[type="submit"]'
    );
    submitButton.click();
    fixture.detectChanges();
    expect(component.onError).toBe(true);
  });

  it('should register successfully', () => {
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    const mockRouter = jest.spyOn(router, 'navigate');
    const registerRequest = {
      firstName: 'Jean',
      lastName: 'TANNER',
      email: 'test@gmail.com',
      password: 'test!1234',
    };
    component.form.setValue(registerRequest);
    fixture.detectChanges();

    const submitButton = fixture.nativeElement.querySelector(
      'button[type="submit"]'
    );
    submitButton.click();
    fixture.detectChanges();
    expect(mockRouter).toHaveBeenCalledWith(['/login']);
  });
});
