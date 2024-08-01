import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let mockHttp : HttpTestingController;

  const mockUser = {
    id: 1,
    email: 'test@gmail.com',
    username: 'JeSuisUnTest',
    firstName: 'Jean',
    lastName: 'TANNER',
    password: 'test!1234',
    admin: false,
    createdAt: new Date(),
    updateAt: new Date(),
  };


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    mockHttp = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a user by id', () => {
    service.getById('1').subscribe((user) => {
      expect(user).toEqual([mockUser]);
    });

    const req = mockHttp.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush([mockUser]);
  });
  
  it('should delete a user by id', () => {
    service.delete('1').subscribe((user) => {
      expect(user).toEqual({});
    });

    const req = mockHttp.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
