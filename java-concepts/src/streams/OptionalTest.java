package streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class OptionalTest {

    public static void main(String[] args) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("src/oop/inheritance/streams/alice30.txt")),
                                                        StandardCharsets.UTF_8);
        List<String> wordList = Arrays.asList(contents.split("\\PL+"));

        // using orElse with optional ----------------------------------------------------------------------------------
        Optional<String> optionalValue = wordList.stream()
                                                 .filter(s -> s.contains("fred"))
                                                 .findFirst();
        System.out.println(optionalValue.orElse("No word") + " contains fred");

        // using orElseGet ---------------------------------------------------------------------------------------------
        Optional<String> optionalString = Optional.empty();
        String result = optionalString.orElse("N/A");
        System.out.println("result with orElse: " + result);
        result = optionalString.orElseGet(() -> Locale.getDefault().getDisplayName());
        System.out.println("result with orElseGet: " + result);

        // using orElseThrow -------------------------------------------------------------------------------------------
        try {
            result = optionalString.orElseThrow(IllegalStateException::new);
            System.out.println("result with orElseThrow: " + result);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        // Using ifPresent ---------------------------------------------------------------------------------------------

        optionalValue = wordList.stream()
                .filter(s -> s.contains("red"))
                .findFirst();
        optionalValue.ifPresent(s -> System.out.println(s + " contains red"));

        // using map with optionals ------------------------------------------------------------------------------------
        Set<String> results = new HashSet<>();
        optionalValue.ifPresent(results::add);
        Optional<Boolean> added = optionalValue.map(results::add);
        System.out.println(added);
    }
}
