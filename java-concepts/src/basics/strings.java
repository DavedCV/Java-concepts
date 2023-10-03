package basics;

public class strings {
    public static void main(String[] args) {
        String greetings = "hello";
        String substr = greetings.substring(0, 3);

        System.out.println("Original: " + greetings);
        System.out.println("Subtring: " + substr);

        System.out.println("Using join: " + String.join(" / ", "hola", "como", "estas"));

        boolean stringEq = "hello".equals(greetings);
        boolean stringEqCaseInsensitive = "HELLO".equalsIgnoreCase(greetings);

        System.out.println("Testing equality: " + stringEq);
        System.out.println("Testing equality case insensitive: " + stringEqCaseInsensitive);

        // mutable strings
        StringBuilder builder = new StringBuilder();
        builder.append("fisrt sentence");
        builder.append(" + ");
        builder.append(" second sentence");
        System.out.println("String created with string builder: " + builder.toString());
    }
}
