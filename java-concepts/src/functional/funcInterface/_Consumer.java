package functional.funcInterface;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class _Consumer {
    static Consumer<Customer> greetCustomerConsumer =
            customer -> System.out.println("Hello " + customer.customerName + ", thanks for registering phone number "
                                                   + customer.customerPhoneNumber);

    static BiConsumer<Customer, Boolean> greetCustomerBiconsumer =
            (customer, showPhoneNumber) -> {
                System.out.println("Hello " + customer.customerName + ", thanks for " +
                                           "registering phone number "
                                           + (showPhoneNumber ? customer.customerPhoneNumber : "******"));
            };

    public static void main(String[] args) {

        Customer maria = new Customer("Maria", "99999");

        // imperative way
        greetCustomer(maria);
        // functional interface approach
        greetCustomerConsumer.accept(maria);

        // imperative way
        greetCustomerV2(maria, false);
        // biconsumer functional interface
        greetCustomerBiconsumer.accept(maria, false);
    }

    static void greetCustomer(Customer customer) {
        System.out.println("Hello " + customer.customerName + ", thanks for registering phone number " + customer.customerPhoneNumber);
    }

    static void greetCustomerV2(Customer customer, boolean showPhoneNumber) {
        System.out.println("Hello " + customer.customerName + ", thanks for " +
                                   "registering phone number "
                                   + (showPhoneNumber ? customer.customerPhoneNumber : "******"));
    }

    static class Customer {
        private final String customerName;
        private final String customerPhoneNumber;

        public Customer(String customerName, String customerPhoneNumber) {
            this.customerName = customerName;
            this.customerPhoneNumber = customerPhoneNumber;
        }
    }
}
