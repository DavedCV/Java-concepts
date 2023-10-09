package functional.funcInterface;

import java.util.function.Supplier;

public class _Supplier {

    static Supplier<String> getDBConectionURLSupplier = () -> "jdbc://localhost:5432/users";

    public static void main(String[] args) {
        System.out.println("Using normal java function: ");
        System.out.println(getDBConectionURL());

        System.out.println("Using supplier functional interface: ");
        System.out.println(getDBConectionURLSupplier.get());
    }

    static String getDBConectionURL() {
        return "jdbc://localhost:5432/users";
    }
}
