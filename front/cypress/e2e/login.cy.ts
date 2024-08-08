 export default function registerSpec(){

  describe('Register spec', () => {
    beforeEach(() => {
      cy.visit('/login')
    });
  
    it('should display an error  ', () => {
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 401,
        body: {
          message: 'Unauthorized login'
        },
      }).as('UnauthorizedLogin');
  
      cy.get('input[formControlName=email]').type("test@gmail.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.get('.error').should('be.visible');
  
    });
  
  
    it('Login successfull', () => {
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true
        },
      })
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session')
  
      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/sessions')
      
    })
  
    it('should not display submit button with an invalid email', () => {
      cy.get('input[formControlName=email]').type("test")
      cy.get('input[formControlName=password]').type(`${"test!1234"}`)
  
      cy.get('button[type=submit]').should('be.disabled');
    });
  
    it('should not display submit button with an invalid password', () => {
      cy.get('input[formControlName=email]').type("test@gmail.com");
  
      cy.get('button[type=submit]').should('be.disabled');
    });
  });
 
}


