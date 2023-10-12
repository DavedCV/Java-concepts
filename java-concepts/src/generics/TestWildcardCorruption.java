package generics;

public class TestWildcardCorruption {

    public static void main(String[] args) {
        var managerBuddies =
                new Pair<Manager>(new Manager("David", 10.0, 2023, 10, 12),
                                  new Manager("Liz", 10.0, 2023, 10, 12));
        Pair<? extends Employee> wildcardBuddies = managerBuddies;

        System.out.println(wildcardBuddies.getFirst());
        System.out.println(wildcardBuddies.getSecond());

        //wildcardBuddies.setFirst(new Manager("Juan"));

        Pair<? super Manager> wildcardBuddies2 = managerBuddies;

        wildcardBuddies2.setFirst(new Manager("Hernan", 20.0, 2023, 10, 12));
        System.out.println(wildcardBuddies2.getFirst());

        //Manager testRetrieval = wildcardBuddies2.getFirst();

    }
}