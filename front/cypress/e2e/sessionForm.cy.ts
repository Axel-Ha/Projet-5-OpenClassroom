describe('Session Form', () => {
  const mockUserAdmin = {
    id: 1,
    token: '12345',
    email: 'test@gmail.com',
    username: 'JeSuisUnTest',
    firstName: 'Jean',
    lastName: 'TANNER',
    password: 'test!1234',
    admin: true,
    createdAt: new Date(),
    updateAt: new Date(),
  };

  const mockUserNonAdmin = {
    id: 1,
    token: '12345',
    email: 'test@gmail.com',
    username: 'JeSuisUnTest',
    firstName: 'Jean',
    lastName: 'TANNER',
    password: 'test!1234',
    admin: true,
    createdAt: new Date(),
    updateAt: new Date(),
  };

  const mockTeacher1 = {
    id: 1,
    lastName: 'DAN',
    firstName: 'Jean',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockTeacher2 = {
    id: 1,
    lastName: 'DEAN',
    firstName: 'Jean',
    createdAt: new Date(),
    updatedAt: new Date(),
  };
  const mockSession1 = {
    id: 1,
    name: 'Yoga Session 1',
    description: 'Description Yoga Session 1',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockSessions = [mockSession1];

  const mockTeachers = [mockTeacher1, mockTeacher2];

  describe('Admin User', () => {
    beforeEach(() => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: mockUserAdmin,
      }).as('loginUser');

      cy.intercept('GET', '/api/teacher', {
        statusCode: 200,
        body: mockTeachers,
      }).as('getTeachers');
    });

    describe('Create Session', () => {
      beforeEach(() => {
        cy.intercept('GET', '/api/session', {
          statusCode: 200,
          body: {},
        }).as('GetSession');

        cy.visit('/login');
        cy.get('input[formControlName=email]').type('test@gmail.com');
        cy.get('input[formControlName=password]').type(
          `${'test!1234'}{enter}{enter}`
        );

        cy.url().should('contain', '/sessions');

        cy.get('button').contains('Create').click();
        cy.url().should('contain', '/sessions/create');
      });

      it('should go back when Back button is clicked', () => {
          cy.get('button[mat-icon-button]').contains('arrow_back').click();
          cy.url().should('not.contain', '/sessions/detail/1');
      });

      it("should show Create session title", () => {
          cy.get('h1').contains('Create session');
      });

      it('should show the empty forms fields', () => {
          cy.get('input[formControlName=name]').should('be.empty');
          cy.get('input[formControlName=date]').should('be.empty');
          cy.get('mat-select[formControlName="teacher_id"]').should('contain', '');
          cy.get('textarea[formControlName=description]').should('be.empty');
      });

      it("should show the button Save disabled", () => {
          cy.get('button[type="submit"]').should('be.disabled');
      });

      it("should show the list of Teachers",() => {
          cy.get("mat-form-field").contains("Teacher").click({force: true});
          cy.get("mat-option").should("be.visible");
          cy.get("mat-option").should("contain", mockTeacher1.firstName + " " + mockTeacher1.lastName);
          cy.get("mat-option").should("contain", mockTeacher2.firstName + " " + mockTeacher2.lastName);
          cy.get("mat-option").eq(0).click();
          cy.get('mat-option').should('not.be.visible');
      });

      it("should disable button Save when name is empty", () => {
          cy.get('input[formControlName="date"]').type('2024-08-02');
          cy.get('mat-form-field').contains('Teacher').click({ force: true });
          cy.get('mat-option').should('be.visible');
          cy.get('mat-option').eq(1).click();
          cy.get('textarea[formControlName="description"]').type('Yoga Session 1');
          cy.get('button[type="submit"]').should('be.disabled');
      });

      it("should disable button Save when date is empty", () => {
          cy.get('input[formControlName="name"]').type('Yoga Session 1');
          cy.get('mat-form-field').contains('Teacher').click({ force: true });
          cy.get('mat-option').should('be.visible');
          cy.get('mat-option').eq(1).click();
          cy.get('textarea[formControlName="description"]').type('Yoga Session 1');
          cy.get('button[type="submit"]').should('be.disabled');
      });

      it("should disable button Save when teacher is empty", () => {
          cy.get('input[formControlName="name"]').type('Yoga Session 1');
          cy.get('input[formControlName="date"]').type('2024-08-02');
          cy.get('textarea[formControlName="description"]').type('Yoga Session 1');
          cy.get('button[type="submit"]').should('be.disabled');
      });

      it("should disable button Save when description is empty", () => {
          cy.get('input[formControlName="name"]').type('Yoga Session 1');
          cy.get('input[formControlName="date"]').type('2024-08-02');
          cy.get('mat-form-field').contains('Teacher').click({ force: true });
          cy.get('mat-option').should('be.visible');
          cy.get('mat-option').eq(1).click();
          cy.get('button[type="submit"]').should('be.disabled');
      });

      it("should enable button Save when all fields are filled", () => {
          cy.get('input[formControlName="name"]').type('Yoga Session 1');
          cy.get('input[formControlName="date"]').type('2024-08-02');
          cy.get('mat-form-field').contains('Teacher').click({ force: true });
          cy.get('mat-option').should('be.visible');
          cy.get('mat-option').eq(1).click();
          cy.get('textarea[formControlName="description"]').type('Yoga Session 1');
          cy.get('button[type="submit"]').should('be.enabled');
      });

        it('should create a session when Save button is clicked', () => {
          cy.get('input[formControlName="name"]').type('Yoga Session 1');
          cy.get('input[formControlName="date"]').type('2024-08-02');
          cy.get('mat-form-field').contains('Teacher').click({ force: true });
          cy.get('mat-option').should('be.visible');
          cy.get('mat-option').eq(1).click();
          cy.get('textarea[formControlName="description"]').type(
            'Yoga Session 1'
          );

          cy.intercept('POST', '/api/session', {
            statusCode: 200,
            body: mockSessions,
          }).as('CreateSession');

          cy.intercept('GET', 'api/session', {
            statusCode: 200,
            body: mockSessions,
          }).as('GetSessions');

          cy.get('button[type="submit"').click();
          cy.url().should('contain', '/sessions');

          cy.get('simple-snack-bar').should('contain', 'Session created !');
        });

        it('should cancel the session creation if Back button is clicked ', () => {
          cy.get('input[formControlName="name"]').type('Yoga Session 1');
          cy.get('input[formControlName="date"]').type('2024-07-05');
          cy.get('mat-form-field').contains('Teacher').click({ force: true });
          cy.get('mat-option').should('be.visible');
          cy.get('mat-option').eq(0).click();
          cy.get('textarea[formControlName="description"]').type(
            'Yoga session 1 description'
          );
          cy.get('button[type="submit"]').should('be.enabled');

          cy.get('button[routerLink="/sessions"]').click();
          cy.url().should('contain', '/sessions');
          cy.get('mat-card.item').should('not.exist');
        });
    });

    describe('Update Session', () => {
      beforeEach(() => {
        cy.intercept('GET', '/api/session', {
          statusCode: 200,
          body: mockSessions,
        }).as('GetSession');

        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: mockSession1,
        }).as('GetSession1');

        cy.visit('/login');
        cy.get('input[formControlName=email]').type('test@gmail.com');
        cy.get('input[formControlName=password]').type(
          `${'test!1234'}{enter}{enter}`
        );

        cy.url().should('include', '/sessions');

        cy.get('button').contains('Edit').click();

        cy.url().should('contain', '/sessions/update/1');
      });

        it('should go back when Back button is clicked', () => {
          cy.get('button[mat-icon-button]').contains('arrow_back').click();
          cy.url().should('not.contain', '/sessions/update/1');
        });

        it('should show Create session title', () => {
          cy.get('h1').contains('Update session');
        });

        it('should update without new values', () => {
          cy.intercept('PUT', '/api/session/1', {
            statusCode: 200,
            body: mockSession1,
          }).as('UpdateSession');

          cy.get('button[type="submit"]').click();
          cy.url().should('contain', '/sessions');
          cy.get('simple-snack-bar').should('contain', 'Session updated !');
        });

        it('should not save any change if Back button is clicked', () => {
          cy.get('input[formControlName="name"]').type('Edit Yoga Session 1');

          cy.get('button[routerLink="/sessions"]').click();
          cy.url().should('contain', '/sessions');
          cy.get('mat-card.item').should('contain', mockSession1.name);
        });

      it('should update the session with new values', () => {
        const newSession = {
          id: 1,
          name: 'Edit Yoga Session 1',
          description: 'Edit Yoga session 1 desc',
          date: new Date(),
          teacher_id: 2,
          users: [],
          createdAt: new Date(),
          updatedAt: '2024-08-02',
        };

        const sessionUpdated = [newSession];

        cy.intercept('PUT', '/api/session/1', {
          statusCode: 200,
          body: newSession,
        }).as('UpdateSessionEdit');

        cy.intercept('GET', '/api/session/1', {
          statusCode: 200,
          body: newSession,
        }).as('GetSessionEdit');

        cy.intercept('GET', '/api/sessions', {
          statusCode: 200,
          body: sessionUpdated,
        }).as('GetSessionsEdit');

        cy.intercept('GET', '/api/teacher/2', {
          statusCode: 200,
          body: mockTeacher2,
        }).as('GetTeacher2');

        cy.get('input[formControlName="name"]')
          .clear()
          .type('Yoga Session 1 updated');
        cy.get('input[formControlName="date"]').type('2024-07-06');
        cy.get('mat-form-field').contains('Teacher').click({ force: true });
        cy.get('mat-option').should('be.visible');
        cy.get('mat-option').eq(1).click();
        cy.get('textarea[formControlName="description"]')
          .clear()
          .type('Yoga session 1 description updated');

        cy.get('button[type="submit"]').click();

        cy.url().should('contain', '/sessions');

        cy.get('simple-snack-bar').should('contain', 'Session updated !');

        cy.get('mat-card.item').get('button').contains('Detail').click();
        cy.url().should('contain', '/sessions/detail/1');

        cy.get('mat-card-title').should('contain', newSession.name);
        cy.get('mat-card-subtitle').should(
          'contain',
          mockTeacher2.firstName + ' ' + mockTeacher2.lastName.toUpperCase()
        );
        cy.get('mat-card-content')
          .get('span.ml1')
          .eq(3)
          .should('contain', 'August 2, 2024');
        cy.get('div.description').should('contain', newSession.description);
      });
    });
  });
  describe('Non admin user', () => {
    beforeEach(() => {
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: mockUserNonAdmin,
      }).as('NonAdminLogin');

      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: mockSessions,
      }).as('GetSessions');

      cy.intercept('GET', '/api/session/1', {
        statusCode: 200,
        body: mockSession1,
      }).as('GetSession1');

      cy.visit('/login');

      cy.get('input[formControlName=email]').type('john.wick@test.com');
      cy.get('input[formControlName=password]').type(
        'test!1234{enter}{enter}'
      );

      cy.url().should('contain', '/sessions');
    });

    it('should not acces when trying to access create form', () => {
      cy.visit('/sessions/create');
      cy.url().should('contain', '/login');
    });

    it('should not acces when trying to access update form', () => {
      cy.visit('/sessions/update/1');
      cy.url().should('contain', '/login');
    });
  });
});
