package collections;

import java.util.LinkedList;
import java.util.Queue;

public class DequeFifoTest {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            queue.offer(i);
        }

        System.out.println("Queue represented by LinkedList: " + queue);

        Integer res = 0;
        while((res = queue.poll()) != null) {
            System.out.println("Queue element: " + res);
        }
    }
}
