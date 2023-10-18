package collections;

import java.util.ArrayDeque;
import java.util.Deque;

/*
 * For stack is best to use the ArrayDeque concrete implementation
 * */
public class DequeLifoTest {
    public static void main(String[] args) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < 10; i++) {
            stack.offerFirst(i);
        }

        System.out.println("ArrayDeque Stack representation: " + stack);

        Integer res = 0;
        while ((res = stack.pollFirst()) != null) {
            System.out.println("Next stack element: " + res);
        }
    }
}
