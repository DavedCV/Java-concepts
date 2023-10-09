package oop.inheritance.streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectingResults {
    public static void main(String[] args) throws IOException {

        // using iterator to collect results ---------------------------------------------------------------------------
        Iterator<Integer> iter = Stream.iterate(0, n -> n + 1).limit(10).iterator();
        while (iter.hasNext()) System.out.println(iter.next());

        // using toArray to collect results ----------------------------------------------------------------------------
        Object[] numbers1 = Stream.iterate(0, n -> n + 1).limit(10).toArray();
        System.out.println("Object array:" + Arrays.toString(numbers1)); // Note it's an Object[] array

        Integer[] numbers2 = Stream.iterate(0, n -> n + 1).limit(10).toArray(Integer[]::new);
        System.out.println("Numbers array: " + Arrays.toString(numbers2));

        // using collector to collect to a specified collection --------------------------------------------------------
        Set<String> noVowelSet = noVowels().collect(Collectors.toSet());
        show("noVowelSet", noVowelSet);

        TreeSet<String> noVowelTreeSet = noVowels().collect(Collectors.toCollection(TreeSet::new));
        show("noVowelTreeSet", noVowelTreeSet);

        // using collector to concatenate strings ----------------------------------------------------------------------
        String result = noVowels().limit(10).collect(Collectors.joining());
        System.out.println("Joining: " + result);

        String resultCommas = noVowels().limit(10).collect(Collectors.joining(", "));
        System.out.println("Joining with commas: " + resultCommas);

        // using summarizing -------------------------------------------------------------------------------------------
        IntSummaryStatistics summary = noVowels().collect(Collectors.summarizingInt(String::length));

        double averageWordLength = summary.getAverage();
        double maxWordLength = summary.getMax();

        System.out.println("Average word length: " + averageWordLength);
        System.out.println("Max word length: " + maxWordLength);
        System.out.println("For each: ");
        noVowels().limit(10).forEach(System.out::println);
    }

    public static Stream<String> noVowels() throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("src/oop/inheritance/streams/alice30.txt")),
                                     StandardCharsets.UTF_8);
        List<String> wordList = Arrays.asList(contents.split("\\PL+"));
        Stream<String> words = wordList.stream();
        return words.map(s -> s.replaceAll("[aeiouAEIOU]", ""));
    }

    public static <T> void show(String label, Set<T> set) {
        System.out.println(label + ": " + set.getClass().getName());
        System.out.println("[" + set.stream()
                                    .limit(10)
                                    .map(Object::toString)
                                    .collect(Collectors.joining(", "))
                                   + "]");
    }

}
