import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { create, last } from 'cypress/types/lodash';
import { is } from 'cypress/types/bluebird';
import { of } from 'rxjs';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let sessionService: SessionService;
  let userService: UserService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
    logOut: jest.fn(),
  };
  const mockDate = new Date();
  const mockAdminUser = {
    id: 1,
    token: '12345',
    email: 'test@gmail.com',
    username: 'JeSuisUnTest',
    firstName: 'Jean',
    lastName: 'TANNER',
    password: 'test!1234',
    admin: true,
    createdAt: mockDate,
    updateAt: mockDate,
  };

  const mockUser = {
    id: 1,
    token: '12345',
    email: 'test@gmail.com',
    username: 'JeSuisUnTest',
    firstName: 'Jean',
    lastName: 'TANNER',
    password: 'test!1234',
    admin: false,
    createdAt: mockDate,
    updateAt: mockDate,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        {
          provide: SessionService,
          useValue: mockSessionService,
        },
        {
          provide: UserService,
          useValue: {
            getById: jest.fn().mockReturnValue(of(mockAdminUser)),
            delete: jest.fn().mockReturnValue(of({})),
          },
        },
        { provide: Router, useValue: { navigate: jest.fn() } },
        { provide: MatSnackBar, useValue: { open: jest.fn() } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    userService = TestBed.inject(UserService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display user information', () => {
    component.user = mockAdminUser;
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('p').textContent).toContain(
      'Name: Jean TANNER'
    );
    expect(compiled.querySelector('p:nth-child(2)').textContent).toContain(
      'Email: test@gmail.com'
    );
    expect(compiled.querySelector('p:nth-child(3)').textContent).toContain(
      'You are admin'
    );
  });

  it('should display the delete button for non-admin user', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('button')).toBeTruthy();
  });

  it('should delete the user when the button delete is clicked', () => {
    component.user = mockUser;
    fixture.detectChanges();

    jest.spyOn(component, 'delete');
    const button = fixture.nativeElement.querySelector(
      'button[mat-raised-button][color="warn"]'
    );
    button.click();
    expect(component.delete).toHaveBeenCalled();
    expect(userService.delete).toHaveBeenCalledWith('1');
    expect(matSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(router.navigate).toHaveBeenCalledWith(['/']);
    expect(sessionService.logOut).toHaveBeenCalled();
  });
});
