import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { create } from 'cypress/types/lodash';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

describe('TeacherService', () => {
  let service: TeacherService;
  let mockHTTP: HttpTestingController;

  const mockTeacher1 = {
    id: 1,
    lastName: 'TANNER',
    firstName: 'Jean',
    createdAt: '2021-07-01T00:00:00.000Z',
    updatedAt: '2021-07-01T00:00:00.000Z',
  };

  const mockTeacher2 = {
    id: 2,
    lastName: 'DEAN',
    firstName: 'Jean',
    createdAt: '2021-07-01T00:00:00.000Z',
    updatedAt: '2021-07-01T00:00:00.000Z',
  };
  const mockTeachers = [mockTeacher1, mockTeacher2];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TeacherService);
    mockHTTP = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all teachers', () => {
    service.all().subscribe((teachers) => {
      expect(teachers.length).toBe(2);
      expect(teachers).toEqual(mockTeachers);

      const req = mockHTTP.expectOne('api/teacher');
      expect(req.request.method).toBe('GET');
      req.flush(mockTeachers);
    });
  });

  it('should get teacher detail by id', () => {
    service.detail('1').subscribe((teacher) => {
      expect(teacher).toEqual(mockTeachers[0]);
    });
    const req = mockHTTP.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers[0]);
  });
});
