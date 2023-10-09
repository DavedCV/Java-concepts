package functional.funcInterface;

import java.util.function.Predicate;

public class _Predicate {

    static Predicate<String> isPhoneNumberValidPredicate =
            phoneNumber -> phoneNumber.startsWith("07") && phoneNumber.length() == 11;

    static Predicate<String> containsNumber3 = phoneNumber -> phoneNumber.contains("3");

    public static void main(String[] args) {

        // imperative way
        System.out.println("Normal java function: ");
        System.out.println(isPhoneNumberValid("07000000000"));
        System.out.println(isPhoneNumberValid("09000000000"));

        // functional way
        System.out.println("Functional interface way: ");
        System.out.println(isPhoneNumberValidPredicate.test("07000000000"));
        System.out.println(isPhoneNumberValidPredicate.test("09000000000"));

        // chained predicates
        System.out.println("Chained predicates: ");
        System.out.println(isPhoneNumberValidPredicate.and(containsNumber3).test("07000000030"));
        System.out.println(isPhoneNumberValidPredicate.and(containsNumber3).test("07000000000"));
    }

    static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.startsWith("07") && phoneNumber.length() == 11;
    }
}
