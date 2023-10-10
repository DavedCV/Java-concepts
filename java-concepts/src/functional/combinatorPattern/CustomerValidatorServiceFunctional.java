package functional.combinatorPattern;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Function;

import static functional.combinatorPattern.ValidationResult.*;

enum ValidationResult {
    SUCCESS,
    PHONE_NOT_VALID,
    EMAIL_NOT_VALID,
    IS_NOT_ADULT
}

/*
 * This is a better way using the combinator pattern
 * */
public interface CustomerValidatorServiceFunctional extends Function<Customer, ValidationResult> {

    static CustomerValidatorServiceFunctional isEmailValid() {
        return customer -> customer.getEmail().contains("@") ? SUCCESS : EMAIL_NOT_VALID;
    }

    static CustomerValidatorServiceFunctional isPhoneNumberValid() {
        return customer -> customer.getPhoneNumber().contains("+0") ? SUCCESS : PHONE_NOT_VALID;
    }

    static CustomerValidatorServiceFunctional isBirthValid() {
        return customer -> Period.between(customer.getBirth(), LocalDate.now()).getYears() > 18 ? SUCCESS :
                IS_NOT_ADULT;
    }

    default CustomerValidatorServiceFunctional and(CustomerValidatorServiceFunctional other) {
        return customer -> {
            ValidationResult result = this.apply(customer);
            return result == SUCCESS ? other.apply(customer) : result;
        };
    }
}
