package org.example.myshopapplication.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;
import org.example.myshopapplication.model.ContactMethod;
public class ContactMethodValidator implements ConstraintValidator<ValidContactMethod, ContactMethod> {

    @Override
    public boolean isValid(ContactMethod contactMethodElement, ConstraintValidatorContext context) {

        if (contactMethodElement == null || contactMethodElement.getMethodType() == null || Strings.isBlank(contactMethodElement.getMethodValue())) {
            return true;
        }

        switch (contactMethodElement.getMethodType()) {

            case EMAIL:
                if (!contactMethodElement.getMethodValue().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Invalid EMAIL format (example: user@mail.com)")
                            .addConstraintViolation();
                    return false;
                }
                break;

            case PHONE:
                if (!contactMethodElement.getMethodValue().matches("^\\d{9,15}$")) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Invalid PHONE format (digits only, 9-15 numbers)")
                            .addConstraintViolation();
                    return false;
                }
                break;

            default:
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Unsupported method type")
                        .addConstraintViolation();
                return false;
        }

        return true;
    }

}
