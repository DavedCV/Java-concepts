package functional.combinatorPattern;

import java.time.LocalDate;
import java.time.Period;

/*
* This is a normal code used to validate data, but there is a much better way, that is better
* structured
* */

public class CustomerValidatorService {

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.startsWith("+0");
    }

    private boolean isBirthValid(LocalDate birth) {
        return Period.between(birth, LocalDate.now()).getYears() > 18;
    }

    public boolean isValid(Customer customer) {
        return isBirthValid(customer.getBirth())
                && isEmailValid(customer.getEmail())
                && isPhoneNumberValid(customer.getPhoneNumber());
    }
}
