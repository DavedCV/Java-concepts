package functional.combinatorPattern;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Customer customer =
                new Customer("Alice", "Alice@gmail.com", "+0123456", LocalDate.of(2002, 04, 06));

        // Using imperative class way
        System.out.println(new CustomerValidatorService().isValid(customer));

        // Using functional way with combinator pattern
        ValidationResult result = CustomerValidatorServiceFunctional
                .isEmailValid()
                .and(CustomerValidatorServiceFunctional.isPhoneNumberValid())
                .and(CustomerValidatorServiceFunctional.isBirthValid()).apply(customer);
        System.out.println(result);
    }
}
