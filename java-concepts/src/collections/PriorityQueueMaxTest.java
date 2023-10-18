package collections;

import java.time.LocalDate;
import java.util.PriorityQueue;

public class PriorityQueueMaxTest {
    public static void main(String[] args) {

        // to use a max priorityQueue change the order using a comparator
        var pq = new PriorityQueue<LocalDate>((el1, el2) -> -1 * el1.compareTo(el2));

        pq.add(LocalDate.of(1906, 12, 9)); // G. Hopper
        pq.add(LocalDate.of(1815, 12, 10)); // A. Lovelace
        pq.add(LocalDate.of(1903, 12, 3)); // J. von Neumann
        pq.add(LocalDate.of(1910, 6, 22)); // K. Zuse

        System.out.println("Iterating over elements . . .");
        for (LocalDate date : pq) {
            System.out.println(date);
        }

        System.out.println("Removing element...");
        while (!pq.isEmpty()) {
            System.out.println(pq.remove());
        }
    }
}
