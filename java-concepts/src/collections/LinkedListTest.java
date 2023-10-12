package collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class LinkedListTest {
    public static void main(String[] args) {
        var a = new LinkedList<String>();
        a.add("Amy");
        a.add("Carl");
        a.add("Erica");
        System.out.println("Original a: " + a);

        var b = new LinkedList<String>();
        b.add("Bob");
        b.add("Doug");
        b.add("Frances");
        b.add("Gloria");
        System.out.println("Original b: " + b);

        // merge the words from b into a
        ListIterator<String> aIter = a.listIterator();
        Iterator<String> bIter = b.iterator();

        // Use ListIterator to add elements to the linked list
        while (bIter.hasNext()) {
            if (aIter.hasNext()) aIter.next();
            aIter.add(bIter.next());
        }
        System.out.println("Alternate merge b into a: " + a);

        // remove every second word from b
        bIter = b.iterator();
        while(bIter.hasNext()) {
            bIter.next(); // skip one element
            if (bIter.hasNext()) {
                bIter.next(); // skip next element
                bIter.remove();
            }
        }
        System.out.println("Remove every pair from b: " + b);

        // bulk operation: remove all words in b from a
        a.removeAll(b);
        System.out.println("Remove all left items of b, from a merged: " + a);
    }
}
