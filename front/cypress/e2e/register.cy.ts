describe('Register spec', () => {

    beforeEach(() => {
        cy.visit('/register');
    });

    it('should nagivate to login in with a successfull register ', () => {
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 200,
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        });

        cy.intercept('POST', '/api/auth/register', {
            statusCode: 200,
            body: {}
          }).as('Register');

        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("lastName");
        cy.get('input[formControlName=email]').type("test@gmail.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);



        cy.url().should('include', '/login')
    
        });

    it('should display error message when register fails', () => {
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: {
                message: 'Bad Request'
            },
        }).as('RegisterError');

        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("lastName");
        cy.get('input[formControlName=email]').type("test@gmail.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

        cy.get('.error').should('be.visible');
    });

    it('should not display submit button with an invalid email', () => {
        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("lastName");
        cy.get('input[formControlName=email]').type("test");
        cy.get('input[formControlName=password]').type(`${"test!1234"}`);

        cy.get('button[type=submit]').should('be.disabled');
    });

    it('should not display submit button if firstName is blank', () => {
        cy.get('input[formControlName=lastName]').type("lastName");
        cy.get('input[formControlName=email]').type("test");
        cy.get('input[formControlName=password]').type(`${"test!1234"}`);

        cy.get('button[type=submit]').should('be.disabled');
    });

    it('should not display submit button if lastName is blank', () => {
        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=email]').type("test");
        cy.get('input[formControlName=password]').type(`${"test!1234"}`);

        cy.get('button[type=submit]').should('be.disabled');
    });

    it('should not display submit button if password is blank ', () => {
        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("lastName");
        cy.get('input[formControlName=email]').type("test@gmail.com");

        cy.get('button[type=submit]').should('be.disabled');
    });




});