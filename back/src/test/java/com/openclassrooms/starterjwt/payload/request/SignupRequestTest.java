package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class SignupRequestTest {
    private static Validator validator;

    @InjectMocks
    private SignupRequest request;


    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public String makeLongString (int length){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append("a");
        }
        return result.toString();

    }

    @Test
    void signupRequestValidTest(){
        request.setEmail("test@gmail.com");
        request.setPassword("test123");
        request.setFirstName("Jean");
        request.setLastName("TANNER");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void signupRequestInvalidEmailEmptyTest(){
        request.setEmail("");
        request.setPassword("test123");
        request.setFirstName("Jean");
        request.setLastName("TANNER");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("ne doit pas être vide",violations.iterator().next().getMessage());
    }

    @Test
    void signupRequestInvalidEmailTooLongTest(){
        String emailTooLong = makeLongString(45)+"@gmail.com";
        request.setEmail(emailTooLong);
        request.setPassword("test123");
        request.setFirstName("Jean");
        request.setLastName("TANNER");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 0 et 50",violations.iterator().next().getMessage());
    }

    @Test
    void signupRequestInvalidFirstNameTooSmallTest(){
        request.setEmail("test@gmail.com");
        request.setFirstName("Je");
        request.setLastName("TANNER");
        request.setPassword("test123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 3 et 20",violations.iterator().next().getMessage());
    }

    @Test
    void signupRequestInvalidFirstNameTooLongTest(){
        String firstNameTooLong = makeLongString(41);
        request.setEmail("test@gmail.com");
        request.setFirstName(firstNameTooLong);
        request.setLastName("TANNER");
        request.setPassword("test123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 3 et 20",violations.iterator().next().getMessage());
    }

    @Test
    void signupRequestInvalidFirstNameEmptyTest(){
        request.setEmail("test@gmail.com");
        request.setFirstName("");
        request.setLastName("TANNER");
        request.setPassword("test123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("ne doit pas être vide",violations.iterator().next().getMessage());

    }

    @Test
    void signupRequestInvalidLastNameTooSmallTest(){
        request.setEmail("test@gmail.com");
        request.setFirstName("Jean");
        request.setLastName("TA");
        request.setPassword("test123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 3 et 20",violations.iterator().next().getMessage());
    }

    @Test
    void signupRequestInvalidLastTooLongTest(){
        String lastNameTooLong = makeLongString(41);
        request.setEmail("test@gmail.com");
        request.setFirstName("Jean");
        request.setLastName(lastNameTooLong);
        request.setPassword("test123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 3 et 20",violations.iterator().next().getMessage());
    }

    @Test
    void signupRequestInvalidLastNameEmptyTest(){
        request.setEmail("test@gmail.com");
        request.setFirstName("Jean");
        request.setLastName("");
        request.setPassword("test123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void signupRequestInvalidPasswordTooSmallTest(){
        request.setEmail("test@gmail.com");
        request.setFirstName("Jean");
        request.setLastName("TANNER");
        request.setPassword("te");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 6 et 40",violations.iterator().next().getMessage());
    }

    @Test
    void signupRequestInvalidPasswordTooLongTest(){
        String passwordTooLong = makeLongString(41);
        request.setEmail("test@gmail.com");
        request.setFirstName("Jean");
        request.setLastName("TANNER");
        request.setPassword(passwordTooLong);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 6 et 40",violations.iterator().next().getMessage());
    }

    @Test
    void signupRequestInvalidPasswordEmptyTest(){
        request.setEmail("test@gmail.com");
        request.setFirstName("Jean");
        request.setLastName("TANNER");
        request.setPassword("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
