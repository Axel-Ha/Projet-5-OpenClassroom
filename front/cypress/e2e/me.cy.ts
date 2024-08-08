export default function meSpec() {
    describe('Me spec', () => {
        const mockAdminUser = {
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
        }
        const mockNonAdminUser = {
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
        }
    
        describe('Admin User', () => {
            beforeEach(() => {
                cy.visit('/login');
                cy.intercept('POST', '/api/auth/login', {
                    statusCode: 200,
                    body: mockAdminUser
                }).as('loginUser');
    
                cy.get('input[formControlName=email]').type('test@gmail.com');
                cy.get('input[formControlName=password]').type(
                  `${'test!1234'}{enter}{enter}`
                );
        
                cy.url().should('include', '/sessions');
    
                cy.intercept('GET', '/api/user/1', {
                    statusCode: 200,
                    body: mockAdminUser
                }).as('getAdminUser');
    
                cy.get('span.link').contains('Account').click();
            });
    
            it('should show User information title', () => {
                cy.get('mat-card-title').should('contain', 'User information');
            });
    
            it('should show User information', () => {
                cy.get('p').eq(0).should('contain', 'Name: Jean TANNER');
                cy.get('p').eq(1).should('contain', 'Email: test@gmail.com');
                cy.get('p').eq(3).should('contain', 'August 1, 2024');
                cy.get('p').eq(4).should('contain', 'August 1, 2024');
    
    
            });
    
            it('should show You are admin', () => {
                cy.get('p').eq(2).should('contain', 'You are admin');
            });
    
    
            it('should go back when button is clicked', () => {
                cy.get('button[mat-icon-button]').click();
                cy.url().should('not.contain', '/me');
            });
        })
    
        describe('Non Admin User', () => {
            beforeEach(() => {
                cy.visit('/login');
                cy.intercept('POST', '/api/auth/login', {
                    statusCode: 200,
                    body: mockNonAdminUser
                }).as('loginUser');
    
                cy.get('input[formControlName=email]').type('test@gmail.com');
                cy.get('input[formControlName=password]').type(
                  `${'test!1234'}{enter}{enter}`
                );
        
                cy.url().should('include', '/sessions');
    
                cy.intercept('GET', '/api/user/1', {
                    statusCode: 200,
                    body: mockNonAdminUser
                }).as('getAdminUser');
    
                cy.intercept('DELETE', '/api/user/1', {
                    statusCode: 200,
                    body: {}
                }).as('deleteUser');
    
                cy.get('span.link').contains('Account').click();
            });
    
            it('should show User information title', () => {
                cy.get('mat-card-title').should('contain', 'User information');
            });
    
            it('should show User information', () => {
                cy.get('p').eq(0).should('contain', 'Name: Jean TANNER');
                cy.get('p').eq(1).should('contain', 'Email: test@gmail.com');
                cy.get('p').eq(3).should('contain', 'August 1, 2024');
                cy.get('p').eq(4).should('contain', 'August 1, 2024');
    
            });
    
            it('should not show you are a admin', () => {
                cy.get('p').eq(2).should('not.contain', 'You are admin');
            });
    
            it('should show delete button', () => {
                cy.get('button[mat-raised-button]').should('contain', 'delete');
            });
    
            it('should delete user when button is clicked', () => {
                cy.get('button[mat-raised-button]').click();
                cy.get('simple-snack-bar').should('contain', 'Your account has been deleted !');
                cy.url().should('include', 'http://localhost:4200/'); 
            });
    
    
            it('should go back when button is clicked', () => {
                cy.get('button[mat-icon-button]').click();
                cy.url().should('not.contain', '/me');
            });
        })
        
    });

}

