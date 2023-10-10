# STREAMS

With a stream, you specify what you want  to have done, not how to do it. You leave the scheduling 
of operations to the implementation. You specify the source of data and the property, and the 
stream library can then optimize the computation, for example by using multiple threads for
computing sums and counts and combining the results.

## From Iterating to Stream operations

When you process a collection, you usually iterate over its elements and do some
work with each of them. For example, suppose we want to count all long words
in a book. First, let’s put them into a list:

```java
String contents = new String(Files.readAllBytes(
    Paths.get("alice.txt")), StandardCharsets.UTF_8); // Read file into string
List<String> words = Arrays.asList(contents.split("\\PL+")); // Split into words; nonletters are delimiters
```

Now we are ready to iterate:

```java
long count = 0;

for (String w : words) {
    if (w.length() > 12) count++; 
}
```

With streams, the same operation looks like this:

```java
long count = words.stream().filter(w -> w.length > 12).count();
```

The stream version is easier to read than the loop because you do not have to
scan the code for evidence of filtering and counting. The method names tell you
right away what the code intends to do

Simply changing stream into parallelStream allows the stream library to do the filtering
and counting in parallel.

```java
long count = words.parallelStream().filter(w -> w.length() > 12).count();
```

The stream and parallelStream methods yield
a stream for the words list. The filter method returns another stream that contains
only the words of length greater than twelve. The count method reduces that stream
to a result.

Streams follow the “what, not how” principle.

A stream seems superficially similar to a collection, allowing you to transform
and retrieve data. But there are significant differences:
- A stream does not store its elements. They may be stored in an underlying
  collection or generated on demand.
- Stream operations don’t mutate their source. For example, the filter method
  does not remove elements from a new stream, but it yields a new stream in
  which they are not present.
- Stream operations are lazy when possible. This means they are not executed
  until their result is needed. For example, if you only ask for the first five long
  words instead of all, the filter method will stop filtering after the fifth match.
  As a consequence, you can even have infinite streams!

The streams workflow is typical when you work with streams. You set up a pipeline of
operations in three stages:
- Create a stream
- Specify intermediate operations for transforming the initial stream into others,
  possibly in multiple steps.
- Apply a terminal operation to produce a result. This operation forces the exe-
  cution of the lazy operations that precede it. Afterwards, the stream can no
  longer be used.

## Stream creation

You can turn any collection into a stream with the **stream** method of the Collection interface. If you 
have an array, use the static **Stream.of** method instead.

The of method has a varargs parameter, so you can construct a stream from any
number of arguments:

```java
Stream<String> words = Stream.of(contents.split("\\PL+")); // split returns a String[] array

Stream<String> song = Stream.of("gently", "down", "the", "stream");
```

Use **Arrays.stream(array, from, to)** to make a stream from array elements between
positions from (inclusive) and to (exclusive).

The Stream interface has two static methods for making infinite streams. The **generate**
method takes a function with no arguments (or, technically, an object of the
Supplier<T> interface). Whenever a stream value is needed, that function is called
to produce a value. You can get a stream of constant values or a stream of random numbers as:

```java
Stream<String> echos = Stream.generate(() -> "Echo");

Stream<Double> randoms = Stream.generate(Math::random);
```

To produce infinite sequences, such as 0 1 2 3 . . . , use the **iterate** method instead.
It takes a “seed” value and a function (technically, a UnaryOperator<T>) and repeatedly
applies the function to the previous result.

```java
Stream<BigInteger> integers
    = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
```

## The Filter, Map, and Flatmap methods

A stream transformation produces a stream whose elements are derived from
those of another stream.

- filter is a transformation that yields a stream with those elements that 
  match a certain condition. The argument of filter is a Predicate<T>—that is, a function from T to boolean.

  ```java
  List<String> wordList = . . .;
  Stream<String> longWords = wordList.stream().filter(w -> w.length() > 12);
  ```
  
- Often, you want to transform the values in a stream in some way. Use the map
  method and pass the function that carries out the transformation.

```java
Stream<String> lowercaseWords = words.stream().map(String::toLowerCase);
Stream<String> firstLetters = words.stream().map(s -> s.substring(0, 1));
```

- To flatten out a stream of streams, and apply some transformation
  use the flatMap method instead of map.

## Extracting Substreams and Concatening Streams

- The call **stream.limit(n)** returns a new stream that ends after n elements (or
when the original stream ends, if it is shorter). This method is particularly useful
for cutting infinite streams down to size. For example,

  ```java
  Stream<Double> randoms = Stream.generate(Math::random).limit(100);
  ```

- The call **stream.skip(n)** does the exact opposite: It discards the first n elements. 
This is handy when splitting text into words since, due to the way the split method
works, the first element is an unwanted empty string. We can make it go away
by calling skip:

  ```java
  Stream<String> words = Stream.of(contents.split("\\PL+")).skip(1);
  ```

- You can concatenate two streams with the static **concat** method of the Stream class:

  ```java
  // letters return a stream of letters
  Stream<String> combined = Stream.concat(
      letters("Hello"), letters("World"));
  // Yields the stream ["H", "e", "l", "l", "o", "W", "o", "r", "l", "d"]
  ```

## Other Stream Transformations

- The **distinct** method returns a stream that yields elements from the original stream,
in the same order, except that duplicates are suppressed. The stream must
obviously remember the elements that it has already seen.

```java
Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently").distinct();
// Only one "merrily" is retained
```

- For sorting a stream, there are several variations of the sorted method. One works
  for streams of Comparable elements, and another accepts a Comparator. Here, we sort
  strings so that the longest ones come first:

  ```java
  Stream<String> longestFirst = 
          words.stream().sorted(Comparator.comparing(String::length).reversed());
  ```

  Of course, you can sort a collection without using streams. The sorted method is
  useful when the sorting process is part of a stream pipeline. 


- the peek method yields another stream with the same elements as the
  original, but a function is invoked every time an element is retrieved. That is
  handy for debugging:

  ```java
  Object[] powers = Stream.iterate(1.0, p -> p * 2)
      .peek(e -> System.out.println("Fetching " + e))
      .limit(20).toArray();
  ```

  When an element is actually accessed, a message is printed. This way you can
  verify that the infinite stream returned by iterate is processed lazily.

## Simple Reductions

Reductions are terminal operations.
They reduce the stream to a non-stream value that can be used in your program.
these methods return an Optional<T> value that either wraps the
answer or indicates that there is none (because the stream happened to be empty).
In the olden days, it was common to return null in such a situation. But that can
lead to null pointer exceptions when it happens in an incompletely tested program.
The Optional type is a better way of indicating a missing return value.

- Get the maximum of a string:

  ```java
  Optional<String> largest = words.max(String::compareToIgnoreCase);
  System.out.println("largest: " + largest.orElse(""));
  ```

- The findFirst returns the first value in a nonempty collection:

  ```java
  Optional<String> startsWithQ = words.filter(s -> s.startsWith("Q")).findFirst();
  ```

- If you just want to know if there is a match, use anyMatch.

  ```java
  boolean aWordStartsWithQ = words.parallel().anyMatch(s -> s.startsWith("Q"));
  ```
  
  There are methods allMatch and noneMatch that return true if all or no elements match
  a predicate. These methods also benefit from being run in parallel.

## The Optional Type

An Optional<T> object is a wrapper for either an object of type T or no object. In the
former case, we say that the value is present. The Optional<T> type is intended as a
safer alternative for a reference of type T that either refers to an object or is null.

### How to work with optional types

The key to using Optional effectively is to use a method that either produces an
alternative if the value is not present, or consumes the value only if it is present.

- Let us look at the first strategy. Often, there is a default that you want to use when
  there was no match, perhaps the empty string:

  ```java
  String result = optionalString.orElse(""); // The wrapped string, or "" if none
  ```
  
  You can also invoke code to compute the default:
  
  ```java
  String result = 
          optionalString.orElseGet(() -> Locale.getDefault().getDisplayName());
          // The function is only called when needed
  ```
  
  Or you can throw an exception if there is no value:
  
  ```java
  String result = 
          optionalString.orElseThrow(IllegalStateException::new);
          // Supply a method that yields an exception object
  ```

- The other strategy for working with optional values is to consume the value 
  only if it is present

  The **ifPresent** method accepts a function. If the optional value exists, it is passed
  to that function. Otherwise, nothing happens.
  
  ```java
  optionalValue.ifPresent(v -> Process v);
  optionalValue.ifPresent(v -> results.add(v));
  ```

- When calling ifPresent, no value is returned from the function. If you want to
process the function result, use map instead:

  ```java
  Optional<Boolean> added = optionalValue.map(results::add);
  ```
  
  Now added has one of three values: true or false wrapped into an Optional, if optionalValue
  was present, or an empty Optional otherwise.

### How not to work with Optional Values

If you don’t use Optional values correctly, you get no benefit over the “something
or null” approach of the past.

- The get method gets the wrapped element of an Optional value if it exists, or throws
  a NoSuchElementException if it doesn’t.

  ```java
  Optional<T> optionalValue = . . .;
  optionalValue.get().someMethod();
  ```
  
  is no safer than:
  
  ```java
  T value = . . .;
  value.someMethod();
  ```

- The isPresent method reports whether an Optional<T> object has a value.

  ```java
  if (optionalValue.isPresent()) optionalValue.get().someMethod();
  ```
  
  is no easier than:
  
  ```java
  if (value != null) value.someMethod();
  ```
  
### Creating Optional Values

If you want to write a method that creates an Optional object, there are several
static methods for that purpose, including **Optional.of(result**) and **Optional.empty()**. For
example,

```java
public static Optional<Double> inverse(Double x) {
    return x == 0 ? Optional.empty() : Optional.of(1 / x);
}
```

The **ofNullable** method is intended as a bridge from possibly null values to optional
values. Optional.ofNullable(obj) returns Optional.of(obj) if obj is not null and Optional.empty()
otherwise.

## Collecting Results

When you are done with a stream, you will often want to look at its elements.

- You can call the **iterator** method, which yields an old-fashioned iterator that you
can use to visit the elements.
- You can call the forEach method to apply a function to each element.
- You can call toArray and get an array of the stream elements.
  Since it is not possible to create a generic array at runtime, the expression
  stream.toArray() returns an Object[] array. If you want an array of the correct type,
  pass in the array constructor:

  ```java
  String[] result = stream.toArray(String[]::new); // stream.toArray() has type Object[]
  ```
  
- For collecting stream elements to another target, there is a convenient collect
  method that takes an instance of the Collector interface. The Collectors class provides
  a large number of factory methods for common collectors.

  ```java
  List<String> result = stream.collect(Collectors.toList());
  
  Set<String> result = stream.collect(Collectors.toSet());
  
  TreeSet<String> result = stream.collect(Collectors.toCollection(TreeSet::new));
  ```

- Suppose you want to collect all strings in a stream by concatenating them. You
  can call
  
  ```java
  String result = stream.collect(Collectors.joining());
  String result = stream.collect(Collectors.joining(", "));
  ```
  
  If your stream contains objects other than strings, you need to first convert them
  to strings, like this:
  
  ```java
  String result = stream.map(Object::toString).collect(Collectors.joining(", "));
  ```
  
- If you want to reduce the stream results to a sum, average, maximum, or mini-
  mum, use one of the summarizing(Int|Long|Double) methods. These methods take a
  function that maps the stream objects to a number and yield a result of type
  (Int|Long|Double)SummaryStatistics, simultaneously computing the sum, count, average,
  minimum, and maximum.

  ```java
  IntSummaryStatistics summary = stream.collect(Collectors.summarizingInt(String::length));
  double averageWordLength = summary.getAverage();
  double maxWordLength = summary.getMax();
  ```
  
## Collecting into Maps

Suppose you have a Stream<Person> and want to collect the elements into a map so
that later you can look up people by their IDs. The Collectors.toMap method has
two function arguments that produce the map’s keys and values.

```java
Map<Integer, String> idToName = people.collect(Collectors.toMap(Person::getId, Person::getName));
```

In the common case when the values should be the actual elements, use
Function.identity() for the second function.

```java
Map<Integer, Person> idToPerson = people.collect(Collectors.toMap(Person::getId, Function.identity()));
```

If there is more than one element with the same key, there is a conflict, and the
collector will throw an IllegalStateException. You can override that behavior by sup-
plying a third function argument that resolves the conflict and determines the
value for the key, given the existing and the new value. Your function could return
the existing value, the new value, or a combination of them.

```java
Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
Map<String, String> languageNames = locales.collect(
    Collectors.toMap(
        Locale::getDisplayLanguage,
        l -> l.getDisplayLanguage(l),
        (existingValue, newValue) -> existingValue)
    );

Map<String, Set<String>> countryLanguageSets = locales.collect(
    Collectors.toMap(
        Locale::getDisplayCountry,
        l -> Collections.singleton(l.getDisplayLanguage()),
        (a, b) ->
        { // Union of a and b
            Set<String> union = new HashSet<>(a);
            union.addAll(b);
            return union;
        }
    ));
```

If you want a TreeMap, supply the constructor as the fourth argument.

## Grouping and Partitioning

In the preceding section, you saw how to collect all languages in a given country.
But the process was a bit tedious. You had to generate a singleton set for each
map value and then specify how to merge the existing and new values. Forming
groups of values with the same characteristic is very common, and the groupingBy
method supports it directly.

```java
Map<String, List<Locale>> countryToLocales = locales.collect(
    Collectors.groupingBy(Locale::getCountry));
```

The function Locale::getCountry is the classifier function of the grouping.

When the classifier function is a predicate function (that is, a function returning
a boolean value), the stream elements are partitioned into two lists: those where
the function returns true and the complement. In this case, it is more efficient to use
partitioningBy instead of groupingBy. For example, here we split all locales into those
that use English and all others:

```java
Map<Boolean, List<Locale>> englishAndOtherLocales = locales.collect(
    Collectors.partitioningBy(l -> l.getLanguage().equals("en")));
List<Locale> englishLocales = englishAndOtherLocales.get(true);
```

## Downstream Collectors

The groupingBy method yields a map whose values are lists. If you want to process
those lists in some way, supply a “downstream collector.”

For example, if you want sets instead of lists, you can use the 
Collectors.toSet collector

```java
Map<String, Set<Locale>> countryToLocaleSet = locales.collect(
    groupingBy(Locale::getCountry, toSet()));
```

Several collectors are provided for reducing grouped elements to numbers:

- Collectors.counting produces a count of the collected elements.
  
  ```java
  Map<String, Long> countryToLocaleCounts = 
    locale.collect(Collectors.groupingBy(Locale::getCountry, Collectors.collect()));
  ```

- summing(Int|Long|Double) takes a function argument, applies the function to the
  downstream elements, and produces their sum.

  ```java
  Map<String, Integer> stateCityPopulation = 
    cities.collect(Collector.groupingBy(City::getState, summingInt(City::getPopulation)));
  ```

- maxBy and minBy take a comparator and produce maximum and minimum of the
  downstream elements.

  ```java
  Map<String, Optional<City>> stateToLargestCity = 
    cities.collect(Collectors.groupingBy(City::getState,
    maxBy(Comparator.comparing(City::getPopulation))));
  ```
  
- The mapping method yields a collector that applies a function to downstream results
  and passes the function values to yet another collector.

  ```java
  Map<String, Optional<String>> stateToLongestCityName = 
    cities.collect(Collectors.groupingBy(City::getState, 
    Collectors.mapping(City::getName, 
    Collectors.maxBy(Comparator.comparing(String::length)))));
  ```

  Here, we group cities by state. Within each state, we produce the names of the
  cities and reduce by maximum length.

  ```java
  Map<String, Set<String>> countryToLanguages = 
    locales.collect(Collectors.groupingBy(Locale::getDisplayCountry,
    Collectors.mapping(Locale::getDisplayLanguage, Collectors.toSet())));
  ```
  
- If the grouping or mapping function has return type int, long, or double, you can
  collect elements into a summary statistics object.

  ```java
  Map<String, IntSummaryStatistics> stateToCityPopulationSummary = 
          cities.collect(Collectors.groupingBy(City::getState,
              Collectors.summarizingInt(City::getPopulation)));
  ```
  
  Then you can get the sum, count, average, minimum, and maximum of the
  function values from the summary statistics objects of each group.

Composing collectors is a powerful approach, but it can also lead to very convoluted expressions. Their best use 
is with groupingBy or partitioningBy to process the “downstream” map values. Otherwise, 
simply apply methods such as map, reduce, count, max, or min directly on streams.

## Reducing Operations

The reduce method is a general mechanism for computing a value from a
stream. The simplest form takes a binary function and keeps applying it, starting
with the first two elements. For example:

```java
List<Integer> values = . . .;
Optional<Integer> sum = values.stream().reduce((x, y) -> x + y);
```

The method returns an Optional because there is no valid result
if the stream is empty.

The operation should be associative: It shouldn’t matter in which order you combine
the elements. In math notation, **(x op y) op z** must be equal to **x op (y op z)**. This
allows efficient reduction with parallel streams.

Often, there is an identity value e such that **e op x = x**, and you can use that element
as the start of the computation. For example, 0 is the identity value for addition.

In the presence of the identity value, you can write the method as:

```java
List<Integer> values = . . .;
Integer sum = values.stream().reduce(0, (x, y) -> x + y)
```

In this case the identity value is returned if the stream is empty, and you no longer need to
deal with the Optional class.

Now suppose you have a stream of objects and want to form the sum of some
property, such as all lengths in a stream of strings. You can’t use the simple form
of reduce. It requires a function (T, T) -> T, with the same types for the arguments
and the result. But in this situation, you have two types: The stream elements have
type String, and the accumulated result is an integer. There is a form of reduce that
can deal with this situation.

First, you supply an “accumulator” function (total, word) -> total + word.length(). That
function is called repeatedly, forming the cumulative total. But when the computation is parallelized, there will be multiple computations of this kind, and you
need to combine their results. You supply a second function for that purpose.
The complete call is

```java
int result = 
        words.reduce(0, (total, word) -> total + word.length
        (total1, total2) -> total1 + total2);
```

## Primitive Type Streams

So far, we have collected integers in a Stream<Integer>, even though it is clearly ineffi-
cient to wrap each integer into a wrapper object. The same is true for the other
primitive types: double, float, long, short, char, byte, and boolean.

The stream library has
specialized types IntStream, LongStream, and DoubleStream that store primitive values di-
rectly, without using wrappers. If you want to store short, char, byte, and boolean,
use an IntStream, and for float, use a DoubleStream.

- To create an IntStream, call the IntStream.of and Arrays.stream methods:

  ```java
  IntStream stream = IntStream.of(1, 1, 2, 3, 5);
  stream = Arrays.stream(values, from, to); // values is an int[] array
  ```

- As with object streams, you can also use the static generate and iterate methods. In
addition, IntStream and LongStream have static methods range and rangeClosed that generate
integer ranges with step size one:

  ```java
  IntStream zeroToNinetyNine = IntStream.range(0, 100); // Upper bound is excluded
  IntStream zeroToHundred = IntStream.rangeClosed(0, 100); // Upper bound is included
  ```

- When you have a stream of objects, you can transform it to a primitive type stream
with the mapToInt, mapToLong, or mapToDouble methods. For example, if you have a
stream of strings and want to process their lengths as integers, you might as well
do it in an IntStream:

  ```java
  Stream<String> words = . . .;
  IntStream lengths = words.mapToInt(String::length);
  ```

- To convert a primitive type stream to an object stream, use the boxed method:

  ```java
  Stream<Integer> integers = IntStream.range(0, 100).boxed();
  ```
- The toArray methods return primitive type arrays.
- Methods that yield an optional result return an OptionalInt, OptionalLong, or
  OptionalDouble. These classes are analogous to the Optional class but have methods
  getAsInt, getAsLong, and getAsDouble instead of the get method.
- There are methods sum, average, max, and min that return the sum, average,
  maximum, and minimum. These methods are not defined for object streams.
- The summaryStatistics method yields an object of type IntSummaryStatistics,
  LongSummaryStatistics, or DoubleSummaryStatistics that can simultaneously report the
  sum, average, maximum, and minimum of the stream.