# Lambda Expressions

## Why lambdas?

A lambda expression is a block of code that you can pass around so it can
be executed later, once or multiple times. 

Whe have used the concept of code that is executed later with callback methods. Or when
passing comparators to sort a string. In both cases, we pass some code that is called
later in time.

Up to now, giving someone a block of code hasn’t been easy in Java. You
couldn’t just pass code blocks around. Java is an object-oriented language,
so you had to construct an object belonging to a class that has a method with
the desired code.

In other languages, it is possible to work with blocks of code directly. The
Java designers have resisted adding this feature for a long time. After all, a
great strength of Java is its simplicity and consistency. A language can become
an unmaintainable mess if it includes every feature that yields marginally
more concise code. However, in those other languages it isn’t just easier to
spawn a thread or to register a button click handler; large swaths of their
APIs are simpler, more consistent, and more powerful. In Java, one could
have written similar APIs taking objects of classes that implement a particular
interface, but such APIs would be unpleasant to use.

For some time, the question was not whether to augment Java for functional
programming, but how to do it. It took several years of experimentation before
a design emerged that is a good fit for Java. In the next section, you will see
how you can work with blocks of code in Java.

## The syntax of lambda expressions

Consider again the sorting example from the preceding section. We pass code
that checks whether one string is shorter than another. We compute

```java
first.length() - second.length()
```

What are first and second? They are both strings. Java is a strongly typed
language, and we must specify that as well:

```java
(String first, String second)
-> first.length() - second.length()
```

You have just seen your first lambda expression. Such an expression is simply
a block of code, together with the specification of any variables that must be
passed to the code.

- You have just seen one form of lambda expressions in Java: **parameters, the
-> arrow, and an expression**. 

- If the code carries out a computation that doesn’t fit in a single expression, 
write it exactly like you would have written a method: enclosed in {} and 
with explicit return statements. 

    ```java
    (String first, String second) -> 
        {
            if (first.length() < second.length()) return -1;
            else if (first.length() > second.length()) return 1;
            else return 0;
        }
    ```

- If a lambda expression has no parameters, you still supply empty parentheses,
just as with a parameterless method:

    ```java
    () -> { for (int i = 100; i >= 0; i--) System.out.println(i); }
    ```

- If the parameter types of a lambda expression can be inferred, you can omit
them. For example

    ```java
    Comparator<String> comp
            = (first, second) // same as (String first, String second)
            -> first.length() - second.length();
    ```

    Here, the compiler can deduce that first and second must be strings because
    the lambda expression is assigned to a string comparator.

- If a method has a single parameter with inferred type, you can even omit the
  parentheses:

    ```java
    ActionListener listener = event ->
        System.out.println("The time is "
        + Instant.ofEpochMilli(event.getWhen()));
        // instead of (event) -> . . . or (ActionEvent event) -> . . .
    ```
  
> It is illegal for a lambda expression to return a value in some branches
but not in others. For example, (int x) -> { if (x >= 0) return 1; } is invalid.

## Functional Interfaces

As we discussed, there are many existing interfaces in Java that encapsulate
blocks of code, such as ActionListener or Comparator. Lambdas are compatible with
these interfaces.

You can supply a lambda expression whenever an object of an interface with
a single abstract method is expected. Such an interface is called a **functional
interface**.

To demonstrate the conversion to a functional interface, consider the Arrays.sort
method. Its second parameter requires an instance of Comparator, an interface
with a single method. Simply supply a lambda:

```java
Arrays.sort(words, (first, second) -> first.length() - second.length());
```

Behind the scenes, the Arrays.sort method receives an object of some class that
implements Comparator<String>. Invoking the compare method on that object executes
the body of the lambda expression. The management of these objects and
classes is completely implementation-dependent, and it can be much more
efficient than using traditional inner classes.

It is best to think of a lambda expression as 
a function, not an object, and to accept that it can be passed to a functional interface. This 
conversion to interfaces is what makes lambda expressions so compelling. The syntax is short 
and simple.

In fact, conversion to a functional interface is the only thing that you can do
with a lambda expression in Java.

- A particularly useful interface in the java.util.function package is Predicate:

    ```java
    public interface Predicate<T> {
        boolean test(T t);
        // additional default and static methods
    }
    ```

  The ArrayList class has a removeIf method whose parameter is a Predicate. It is
  specifically designed to pass a lambda expression. For example, the following
  statement removes all null values from an array list:

    ```java
    list.removeIf(e -> e == null)
    ```
  
- Another useful functional interface is Supplier<T>:

    ```java
    public interface Supplier<T> {
        T get();
    }
    ```
    
    A supplier has no arguments and yields a value of type T when it is called.
    Suppliers are used for lazy evaluation.

## Method references

Sometimes, a lambda expression involves a single method. For example,
suppose you simply want to print the event object whenever a timer event
occurs. Of course, you could call:

```java
var timer = new Timer(1000, event -> System.out.println(event));
```

It would be nicer if you could just pass the println method to the Timer
constructor. Here is how you do that:

```java
var timer = new Timer(1000, System.out::println);
```

The expression System.out::println is a **method reference**. It directs the compiler
to produce an instance of a functional interface, overriding the single
abstract method of the interface to call the given method. In this example,
an ActionListener is produced whose actionPerformed(ActionEvent e) method calls
System.out.println(e).

> Like a lambda expression, a method reference is not an object. It gives
rise to an object when assigned to a variable whose type is a functional interface.

As another example, suppose you want to sort strings regardless of letter
case. You can pass this method expression:

```java
Arrays.sort(strings, String::compareToIgnoreCase)
```

As you can see from these examples, the :: operator separates the method
name from the name of an object or class. There are three variants:

- **object::instanceMethod**

  the method reference is equivalent to a lambda expression
  whose parameters are passed to the method. In the case of System.out::println,
  the object is System.out, and the method expression is equivalent to x ->
  System.out.println(x).

- **Class::instanceMethod**

  the first parameter becomes the implicit parameter of
  the method. For example, String::compareToIgnoreCase is the same as (x, y) ->
  x.compareToIgnoreCase(y).

- **Class::staticMethod**

  all parameters are passed to the static method: Math::pow
  is equivalent to (x, y) -> Math.pow(x, y).

Note that a lambda expression can only be rewritten as a method reference
if the body of the lambda expression calls a single method and doesn’t do
anything else. Consider the lambda expression:

```java
s -> s.length() == 0
```

There is a single method call. But there is also a comparison, so you can’t
use a method reference here.

> When there are multiple overloaded methods with the same name, the
compiler will try to find from the context which one you mean. For example,
there are two versions of the Math.max method, one for integers and one for double
values. Which one gets picked depends on the method parameters of the
functional interface to which Math::max is converted. Just like lambda expressions,
method references don’t live in isolation. They are always turned into instances
of functional interfaces.

You can capture the this parameter in a method reference. For example,
this::equals is the same as x -> this.equals(x). It is also valid to use super. The
method expression super::instanceMethod used this as the target and invokes the superclass version
of the given method.

```java
class Greeter {
    public void greet(ActionEvent event) {
        System.out.println("Hello, the time is "
        + Instant.ofEpochMilli(event.getWhen()));
    }
}

class RepeatedGreeter extends Greeter {
    public void greet(ActionEvent event) {
        var timer = new Timer(1000, super::greet);
        timer.start();
    }
}
```

When the RepeatedGreeter.greet method starts, a Timer is constructed that executes
the super::greet method on every timer tick.

## Constructor references

Constructor references are just like method references, except that the name
of the method is new. For example, Person::new is a reference to a Person construc-
tor. Which constructor? It depends on the context. Suppose you have a list
of strings. Then you can turn it into an array of Person objects, by calling the
constructor on each of the strings, with the following invocation:

```java
ArrayList<String> names = . . .;
Stream<Person> stream = names.stream().map(Person::new);
List<Person> people = stream.collect(Collectors.toList());
```

What’s important is that the map method calls the
Person(String) constructor for each list element. If there are multiple Person con-
structors, the compiler picks the one with a String parameter because it infers
from the context that the constructor is called with a string

You can form constructor references with array types. For example, int[]::new
is a constructor reference with one parameter: the length of the array. It is
equivalent to the lambda expression x -> new int[x].

Array constructor references are useful to overcome a limitation of Java. It is
not possible to construct an array of a generic type T. The expression new T[n]
is an error since it would be erased to new Object[n]. That is a problem for library authors. 
For example, suppose we want to have an array of Person objects. The Stream interface has a
toArray method that returns an Object array:

```java
Object[] people = stream.toArray();
```

But that is unsatisfactory. The user wants an array of references to Person, not
references to Object. The stream library solves that problem with constructor
references. Pass Person[]::new to the toArray method:

```java
Person[] people = stream.toArray(Person[]::new);
```

The toArray method invokes this constructor to obtain an array of the correct
type. Then it fills and returns the array.

## Variable Scope (Closure)

Often, you want to be able to access variables from an enclosing method or
class in a lambda expression. Consider this example:

```java
public static void repeatMessage(String text, int delay) {

    ActionListener listener = event -> {
        System.out.println(text);
        Toolkit.getDefaultToolkit().beep();
    };

    new Timer(delay, listener).start();
}
        
repeatMessage("Hello", 1000); // prints Hello every 1,000 milliseconds
```

Now look at the variable text inside the lambda expression. Note that this
variable is not defined in the lambda expression. Instead, it is a parameter
variable of the repeatMessage method.

If you think about it, something nonobvious is going on here. The code of
the lambda expression may run long after the call to repeatMessage has returned
and the parameter variables are gone. How does the text variable stay around?

To understand what is happening, we need to refine our understanding of a
lambda expression. A lambda expression has three ingredients:

1. A block of code
2. Parameters
3. Values for the free variables—that is, the variables that are not parameters
   and not defined inside the code

In our example, the lambda expression has one free variable, text. The data
structure representing the lambda expression must store the values for the
free variables—in our case, the string "Hello". We say that such values have
been captured by the lambda expression.

> The technical term for a block of code together with the values of the
free variables is a closure. If someone gloats that their language has closures,
rest assured that Java has them as well. In Java, lambda expressions are
closures.

As you have seen, a lambda expression can capture the value of a variable
in the enclosing scope. In Java, to ensure that the captured value is well-
defined, there is an important restriction. In a lambda expression, you can
only reference variables whose value doesn’t change.

```java
public static void countDown(int start, int delay) {
    ActionListener listener = event -> {
        start--; // ERROR: Can't mutate captured variable
        System.out.println(start);
    };
    new Timer(delay, listener).start();
}
```

It is also illegal to refer, in a lambda expression, to a variable that is mutated
outside.

```java
public static void repeat(String text, int count){
    for(int i=1;i<=count;i++){
        ActionListener listener=event->{
            System.out.println(i+": "+text);
            // ERROR: Cannot refer to changing i
        };
        new Timer(1000,listener).start();
    }
}
```

**The rule is that any captured variable in a lambda expression must be effectively 
final**. An effectively final variable is a variable that is never assigned a
new value after it has been initialized.

The body of a lambda expression has **the same scope as a nested block**. When you use the this keyword in a lambda expression, you refer to the this
parameter of the method that creates the lambda.

## Processing lambda expressions

Up to now, you have seen how to produce lambda expressions and pass them
to a method that expects a functional interface. Now let us see how to write
methods that can consume lambda expressions.

The point of using lambdas is **deferred execution**. After all, if you wanted to
execute some code right now, you’d do that, without wrapping it inside a
lambda. There are many reasons for executing code later, such as:

- Running the code in a separate thread
- Running the code multiple times
- Running the code at the right point in an algorithm (for example, the
  comparison operation in sorting)
- Running the code when something happens (a button was clicked, data
  has arrived, and so on)
- Running the code only when necessary
---

- Suppose you want to repeat an action n times.
The action and the count are passed to a repeat method:

    ```java
    repeat(10, () -> System.out.println("Hello, World!"));
    ```
    
    To accept the lambda, we need to pick (or, in rare cases, provide) a functional
    interface.
    
    ```java
    public static void repeat(int n, Runnable action) {
        for (int i = 0; i < n; i++) action.run();
    }
    ```
    
    Note that the body of the lambda expression is executed when action.run() is
    called.


- If we want to tell the action in which iteration it occurs. For that, we need to pick a functional in-
  terface that has a method with an int parameter and a void return. The standard
  interface for processing int values is

    ```java
    public interface IntConsumer {
        void accept(int value);
    }
    ```
    
    Here is the improved version of the repeat method:
    
    ```java
    public static void repeat(int n, IntConsumer action) {
        for (int i = 0; i < n; i++) action.accept(i);
    }
  
    repeat(10, i -> System.out.println("Countdown: " + (9 - i)));
    ```

## More about Comparators

The Comparator interface has a number of convenient static methods for creating
comparators. These methods are intended to be used with lambda expressions
or method references.

The static comparing method takes a “key extractor” function that maps a type
T to a comparable type (such as String). The function is applied to the objects
to be compared, and the comparison is then made on the returned keys.

For example, suppose you have an array of Person objects. Here is how you can
sort them by name:

```java
Arrays.sort(people, Comparator.comparing(Person::getName));
 ```

You can chain comparators with the thenComparing method for breaking ties. For
example:

```java
Arrays.sort(people, 
      Comparator.comparing(Person::getLastName)
      .thenComparing(Person::getFirstName));
```

If two people have the same last name, then the second comparator is used.

There are a few variations of these methods. You can specify a comparator
to be used for the keys that the comparing and thenComparing methods extract. For
example, here we sort people by the length of their names:

```java
Arrays.sort(people, Comparator.comparing(Person::getName,
    (s, t) -> Integer.compare(s.length(), t.length())));
```

Moreover, both the comparing and thenComparing methods have variants that avoid
boxing of int, long, or double values. An easier way of producing the preceding
operation would be

```java
Arrays.sort(people, Comparator.comparingInt(p -> p.getName().length()));
```