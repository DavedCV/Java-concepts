package collections;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;

public class SetTestStream {
    public static void main(String[] args) throws IOException {
        var words = new HashSet<String>();

        Path path = Paths.get("src/collections/alice30.txt");
        var alice = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        Stream<String> wordsStream = Stream.of(alice.split("\\PL+"));
        long totalTime = wordsStream.skip(1).map(word -> {
            long callTime = System.currentTimeMillis();
            words.add(word);
            callTime = System.currentTimeMillis() - callTime;
            return callTime;
        }).reduce(0L, Long::sum);

        Iterator<String> iter = words.iterator();
        for (int i = 1; i <= 20 && iter.hasNext(); i++) {
            System.out.println(iter.next());
        }

        System.out.println("...");
        System.out.println(words.size() + " distinct words. " + totalTime + " milliseconds.");

    }
}
