import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let mockHttp : HttpTestingController;

  const mockSession1 = {
    id: 1,
    name: 'Yoga Session',
    description: 'Description Yoga Session',
    date: new Date(),
    teacher_id: 1,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockSession2 = {
    id: 2,
    name: 'Pilates Session',
    description: 'Description Pilates Session',
    date: new Date(),
    teacher_id: 2,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date(),
  };
  
  const mockSessions: Session[] = [mockSession1, mockSession2];



  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    mockHttp = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    mockHttp.verify();
  })

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return all sessions', () => {
    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = mockHttp.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should return a session', () => {
    service.detail('1').subscribe((session) => {
      expect(session).toEqual(mockSession1);
    });

    const req = mockHttp.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession1);
  });

  it('should delete a session', () => {
    service.delete('1').subscribe((response) => {
      expect(response).toEqual({});
    });

    const req = mockHttp.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create a session', () => {
    service.create(mockSession1).subscribe((session) => {
      expect(session).toEqual(mockSession1);
    });

    const req = mockHttp.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(mockSession1);
  });

  it('should update a session', () => {
    const mockUpdatedSession = {
      id: 1,
      name: 'Updated Yoga Session',
      description: 'Updated Description',
      date: new Date(),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(),
      updatedAt:new Date() 
    };

    service.update('1', mockUpdatedSession).subscribe((session) => {
      expect(session).toEqual(mockUpdatedSession);
    });

    const req = mockHttp.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(mockUpdatedSession);
  });

  it('should participate a session', () => {
    service.participate('1', '2').subscribe((response) => {
      expect(response).toEqual({});
    });

    const req = mockHttp.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('POST');
    req.flush({});
  });


  it('should unparticipate a session', () => {
    service.unParticipate('1', '2').subscribe((response) => {
      expect(response).toEqual({});
    });

    const req = mockHttp.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
