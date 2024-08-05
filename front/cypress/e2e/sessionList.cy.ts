describe('Session List ', () => {
  const mockSession1 = {
    id: 1,
    name: 'Yoga Session 1',
    description: 'Description Yoga Session 1',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2],
    createdAt: '2024-08-01T00:00:00.000Z',
    updatedAt: '2024-08-01T00:00:00.000Z',
  };

  const mockSessions = [mockSession1];

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
      createdAt: '2024-08-01T00:00:00.000Z',
      updatedAt: '2024-08-01T00:00:00.000Z',
    };

    const mockTeacher1 = {
        id: 1,
        lastName: 'DAN',
        firstName: 'Jean',
        createdAt: '2024-08-01T00:00:00.000Z',
        updatedAt: '2024-08-01T00:00:00.000Z',
      };

    const mockTeachers = [mockTeacher1];

    beforeEach(() => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: mockUserAdmin,
      }).as('loginUser');

      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: mockSessions,
      }).as('getSessions');

      cy.intercept('GET', '/api/session/1', {
        statusCode: 200,
        body: mockSession1,
      }).as('getSession1');

      cy.intercept('GET', '/api/teacher', {
        statusCode: 200,
        body: mockTeachers,
      }).as('getTeachers');

      cy.get('input[formControlName=email]').type('john.wick@test.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

      cy.url().should('include', '/sessions');

    });

     it('should show Create button', () => {
         cy.get('button[routerLink="create"]').should('be.visible');
     });

     it('should navigate to create form page when Create button is clicked', () => {
         cy.get('button[routerLink="create"]').click();
         cy.url().should('contain', '/sessions/create');
     });

     it('should list all sessions', () => {
         cy.get('mat-card.item').eq(0).should('be.visible');
     });

    it('should show session 1 details', () => {
        cy.get('mat-card.item').eq(0).should('contain', mockSession1.name);
        cy.get('mat-card.item').eq(0).should('contain', "Session on August 4, 2024");
        cy.get('mat-card.item').eq(0).should('contain', mockSession1.description); 
    });

     it('should show session 1 Details and Edit', () => {
         cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').should('be.visible');
         cy.get('mat-card.item').eq(0).get('span.ml1').contains('Edit').should('be.visible');
     });

     it('should navigate to session 1 details page when Detail button is clicked', () => {
         cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').click();
         cy.url().should('contain', '/sessions/detail/1');
     });

     it('should navigate to session 1 edit page when Edit button is clicked', () => {
         cy.get('mat-card.item').eq(0).get('span.ml1').contains('Edit').click();
         cy.url().should('contain', '/sessions/update/1');
     });

  });

    describe('Non Admin User', () => {
        const mockUserNonAdmin = {
            id: 1,
            token: '12345',
            email: 'test@gmail.com',
            username: 'JeSuisUnTest',
            firstName: 'Jean',
            lastName: 'TANNER',
            password: 'test!1234',
            admin: false,
            createdAt: '2024-08-01T00:00:00.000Z',
            updatedAt: '2024-08-01T00:00:00.000Z',
        };

        const mockTeacher1 = {
            id: 1,
            lastName: 'DAN',
            firstName: 'Jean',
            createdAt: '2024-08-01T00:00:00.000Z',
            updatedAt: '2024-08-01T00:00:00.000Z',
          };
    
        const mockTeachers = [mockTeacher1];
    
        beforeEach(() => {
          cy.visit('/login');
          cy.intercept('POST', '/api/auth/login', {
            statusCode: 200,
            body: mockUserNonAdmin,
          }).as('loginUser');
    
          cy.intercept('GET', '/api/session', {
            statusCode: 200,
            body: mockSessions,
          }).as('getSessions');
    
          cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: mockSession1,
          }).as('getSession1');
    
          cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: mockTeachers,
          }).as('getTeachers');
    
          cy.get('input[formControlName=email]').type('john.wick@test.com');
          cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
    
          cy.url().should('include', '/sessions');
    
        });

         it('should not show Create button', () => {
             cy.get('button[routerLink="create"]').should('not.exist');
         });

         it('should list all sessions', () => {
             cy.get('mat-card.item').eq(0).should('be.visible');
         });

        it('should show session 1 details', () => {
            cy.get('mat-card.item').eq(0).should('contain', mockSession1.name);
            cy.get('mat-card.item').eq(0).should('contain', "Session on August 4, 2024");
            cy.get('mat-card.item').eq(0).should('contain', mockSession1.description); 
        });
    
         it('should show session 1 Details and Edit', () => {
             cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').should('be.visible');
         });

         it('should not show Edit button', () => {
             cy.get('mat-card.item').eq(0).get('span.ml1').contains('Edit').should('not.exist');
         });

         it('should navigate to session 1 details page when Detail button is clicked', () => {
             cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').click();
             cy.url().should('contain', '/sessions/detail/1');
         });
        
    
    });

      
});
