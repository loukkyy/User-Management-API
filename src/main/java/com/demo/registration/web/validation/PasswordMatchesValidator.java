package com.demo.registration.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.demo.registration.dto.UserSignupRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final UserSignupRequest userSignupRequest = (UserSignupRequest) obj;
        return userSignupRequest.getPassword()
            .equals(userSignupRequest.getPasswordConfirmation());
    }

}
