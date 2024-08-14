export default function sessionDetail() {
  describe('Session Detail spec', () => {
    const mockSession1 = {
      id: 1,
      name: 'Yoga Session 1',
      description: 'Description Yoga Session 1',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      creaAt: '2024-08-01T00:00:00.000Z',
      updatedAt: '2024-08-01T00:00:00.000Z',
    };

    const mockSession2 = {
      id: 2,
      name: 'Yoga Session 2',
      description: 'Description Yoga Session 2',
      date: new Date(),
      teacher_id: 1,
      users: [],
      creaAt: '2024-08-01T00:00:00.000Z',
      updatedAt: '2024-08-01T00:00:00.000Z',
    };

    const mockTeacher = {
      id: 1,
      lastName: 'DAN',
      firstName: 'Jean',
      creaAt: '2024-08-01T00:00:00.000Z',
      updatedAt: '2024-08-01T00:00:00.000Z',
    };

    const mockSessions = [mockSession1, mockSession2];

    describe('Admin User', () => {
      const mockUserAdmin = {
        id: 1,
        token: '12345',
        email: 'test@gmail.com',
        username: 'JeSuisUnTest',
        firstName: 'Jean',
        lastName: 'TANNER',
        password: 'test!1234',
        admin: true,
        creaAt: '2024-08-01T00:00:00.000Z',
        updateAt: '2024-08-01T00:00:00.000Z',
      };
      beforeEach(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
          statusCode: 200,
          body: mockUserAdmin,
        }).as('loginUser');

        cy.intercept('GET', '/api/teacher/1', {
          statusCode: 200,
          body: mockTeacher,
        }).as('getTeacher');

        cy.intercept('GET', '/api/session', {
          statusCode: 200,
          body: mockSessions,
        }).as('getSessions');

        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: mockSession1,
        }).as('getSessions');

        cy.intercept('GET', '/api/session/2', {
          statusCode: 200,
          body: mockSession2,
        }).as('getSessions');

        cy.intercept('DELETE', '/api/session/1', {
          statusCode: 200,
          body: {},
        }).as('DeleteSession1');

        cy.get('input[formControlName=email]').type('test@gmail.com');
        cy.get('input[formControlName=password]').type(
          `${'test!1234'}{enter}{enter}`
        );

        cy.url().should('include', '/sessions');

        cy.get('mat-card.item')
          .eq(0)
          .get('span.ml1')
          .contains('Detail')
          .click();
        cy.url().should('contain', '/sessions/detail/1');
      });

      it('should go back when Back button is clicked', () => {
        cy.get('button[mat-icon-button]').contains('arrow_back').click();
        cy.url().should('not.contain', '/sessions/detail/1');
      });

      it('should delete a session', () => {
        cy.get('button[mat-raised-button]').contains('delete').click();
        cy.get('simple-snack-bar').should('contain', 'Session deleted !');
        cy.url().should('contain', '/sessions');
      });
    });

    describe('User', () => {
      const mockUserNonAdmin = {
        id: 1,
        token: '12345',
        email: 'test@gmail.com',
        username: 'JeSuisUnTest',
        firstName: 'Jean',
        lastName: 'TANNER',
        password: 'test!1234',
        admin: false,
        creaAt: '2024-08-01T00:00:00.000Z',
        updateAt: new Date(),
      };

      const mockSession1WithoutUser = {
        id: 2,
        name: 'Yoga Session 1',
        description: 'Description Yoga Session 1',
        date: new Date(),
        teacher_id: 1,
        users: [],
        creaAt: '2024-08-01T00:00:00.000Z',
        updatedAt: '2024-08-01T00:00:00.000Z',
      };

      beforeEach(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
          statusCode: 200,
          body: mockUserNonAdmin,
        }).as('loginUser');

        cy.intercept('GET', '/api/teacher/1', {
          statusCode: 200,
          body: mockTeacher,
        }).as('getTeacher');

        cy.intercept('GET', '/api/session', {
          statusCode: 200,
          body: mockSessions,
        }).as('getSessions');

        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: mockSession1,
        }).as('getSessions');

        cy.intercept('GET', '/api/session/2', {
          statusCode: 200,
          body: mockSession2,
        }).as('getSessions');

        cy.intercept('POST', '/api/session/1/participate/1', {
          statusCode: 200,
          body: {},
        }).as('participate');

        cy.intercept('DELETE', '/api/session/1/participate/1', {
          statusCode: 200,
          body: {},
        }).as('Unparticipate');

        cy.get('input[formControlName=email]').type('test@gmail.com');
        cy.get('input[formControlName=password]').type(
          `${'test!1234'}{enter}{enter}`
        );

        cy.url().should('include', '/sessions');

        cy.get('mat-card.item')
          .eq(0)
          .get('span.ml1')
          .contains('Detail')
          .click();
        cy.url().should('contain', '/sessions/detail/1');
      });

      it('should go back when Back button is clicked', () => {
        cy.get('button[mat-icon-button]').contains('arrow_back').click();
        cy.url().should('not.contain', '/sessions/detail/1');
      });

      it('should not show delete button', () => {
        cy.get('button[mat-raised-button]')
          .contains('delete')
          .should('not.exist');
      });

      it('should show teacher name', () => {
        cy.get('mat-card-subtitle')
          .get('span.ml1')
          .should(
            'contain',
            mockTeacher.firstName + ' ' + mockTeacher.lastName
          );
      });

      it('should show session title', () => {
        cy.get('mat-card-title').get('h1').should('contain', 'Yoga Session 1');
      });
      it('should show Do not participate Button', () => {
        cy.get('button').contains('Do not participate').should('be.visible');
      });
    });

    describe('Participate/Unparticipate', () => {
      const mockUserNonAdmin = {
        id: 1,
        token: '12345',
        email: 'test@gmail.com',
        username: 'JeSuisUnTest',
        firstName: 'Jean',
        lastName: 'TANNER',
        password: 'test!1234',
        admin: false,
        creaAt: '2024-08-01T00:00:00.000Z',
        updateAt: '2024-08-01T00:00:00.000Z',
      };

      const mockSession1WithoutUser = {
        id: 2,
        name: 'Yoga Session 1',
        description: 'Description Yoga Session 1',
        date: new Date(),
        teacher_id: 1,
        users: [],
        creaAt: '2024-08-01T00:00:00.000Z',
        updatedAt: '2024-08-01T00:00:00.000Z',
      };

      beforeEach(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
          statusCode: 200,
          body: mockUserNonAdmin,
        }).as('loginUser');

        cy.intercept('GET', '/api/teacher/1', {
          statusCode: 200,
          body: mockTeacher,
        }).as('getTeacher');

        cy.intercept('GET', '/api/session', {
          statusCode: 200,
          body: mockSessions,
        }).as('getSessions');

        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: mockSession1,
        }).as('getSessions');

        cy.intercept('GET', '/api/session/2', {
          statusCode: 200,
          body: mockSession2,
        }).as('getSessions');

        cy.intercept('POST', '/api/session/1/participate/1', {
          statusCode: 200,
          body: {},
        }).as('participate');

        cy.intercept('DELETE', '/api/session/1/participate/1', {
          statusCode: 200,
          body: {},
        }).as('Unparticipate');

        cy.get('input[formControlName=email]').type('test@gmail.com');
        cy.get('input[formControlName=password]').type(
          `${'test!1234'}{enter}{enter}`
        );

        cy.url().should('include', '/sessions');
      });

      it('should unparcticipate to a session', () => {
        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: mockSession1,
        }).as('GetSession1');

        cy.get('mat-card.item')
          .eq(0)
          .get('span.ml1')
          .contains('Detail')
          .click();
        cy.url().should('contain', '/sessions/detail/1');

        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: mockSession1WithoutUser,
        }).as('GetSessionWithoutUser');

        cy.get('button').contains('Do not participate').click();

        cy.get('button').contains('Participate').should('be.visible');
        cy.get('mat-card-content')
          .get('span.ml1')
          .contains('attendees')
          .should(
            'contain',
            mockSession1WithoutUser.users.length + ' attendees'
          );
      });

      it('should parcticipate to a session', () => {
        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: mockSession1WithoutUser,
        }).as('GetSessionWithoutUser');

        cy.get('mat-card.item')
          .eq(0)
          .get('span.ml1')
          .contains('Detail')
          .click();
        cy.url().should('contain', '/sessions/detail/1');

        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: mockSession1,
        }).as('GetSession1');

        cy.get('button').contains('Participate').click();

        cy.get('button').contains('Do not participate').should('be.visible');
        cy.get('mat-card-content')
          .get('span.ml1')
          .contains('attendees')
          .should('contain', mockSession1.users.length + ' attendees');
      });
    });
  });
}
