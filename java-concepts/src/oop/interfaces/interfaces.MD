# INTERFACES

## interface concept

In the Java programming language, an interface is not a class but a set of
requirements for the classes that want to conform to the interface.

- All methods of an interface are automatically public. For that reason, it is not
  necessary to supply the keyword public when declaring a method in an
  interface (However, when implementing the interface, you must declare the method as public)
- it is possible to supply simple methods implementations in interfaces
- Interfaces never have instance fields.
- Just as methods in an interface are automatically public, fields are always public
  static final.

Supplying instance fields and methods that operate on them is the job of the
classes that implement the interface. You can think of an interface as an abstract class with no instance fields.

While each class can have only one superclass, classes can implement multiple
interfaces. This gives you the maximum amount of flexibility in defining a
class’s behavior.

To make a class implement an interface, you carry out two steps:
1. You declare that your class intends to implement the given interface.
2. You supply definitions for all methods in the interface.

> ### Note with compareTo and inheritance:
> According to the language standard: “The implementor must ensure
> sgn(x.compareTo(y)) = -sgn(y.compareTo(x)) for all x and y. (This implies that
> x.compareTo(y) must throw an exception if y.compareTo(x) throws an exception.)”
> 
> Since Manager extends Employee, it implements Comparable<Employee> and not
> Comparable<Manager>. If Manager chooses to override compareTo, it must be prepared
> to compare managers to employees. It can’t simply cast an employee to a
> manager:
> 
> ```java
> class Manager extends Employee {
>     public int compareTo(Employee other) {
>         Manager otherManager = (Manager) other; // NO
>         . . .
>     }
>   
>     . . .
> }
> ```
> 
> That violates the “antisymmetry” rule. If x is an Employee and y is a Manager, then
> the call x.compareTo(y) doesn’t throw an exception—it simply compares x and y
> as employees. But the reverse, y.compareTo(x), throws a ClassCastException.
> 
> If subclasses have different notions of comparison, then you should outlaw
> comparison of objects that belong to different classes. Each compareTo method
> should start out with the test
> 
> `if (getClass() != other.getClass()) throw new ClassCastException();`
> 
> If there is a common algorithm for comparing subclass objects, simply provide
> a single compareTo method in the superclass and declare it as final.

## Properties of interfaces 

Interfaces are not classes. In particular, you can never use the new operator to
instantiate an interface. However, even though you can’t construct interface objects, you 
can still declare interface variables. An interface variable must refer to an object of a class that implements the
interface.

You can use instanceof to check whether an object implements an interface.

```java
// Employee implements Comparable interface
Comparable x = new Employee();

// true
x instanceof Comparable
```

Just as you can build hierarchies of classes, you can extend interfaces. This
allows for multiple chains of interfaces that go from a greater degree of gen-
erality to a greater degree of specialization.

```java
public interface Moveable {
    void move(double x, double y);
}

public interface Powered extends Moveable {
    double milesPerGallon();
    double SPEED_LIMIT = 95; // a public static final constant
}
```

Use commas to separate the interfaces that you want to implement:

`class Employee implements Cloneable, Comparable`

## Static and private methods

As of Java 8, you are allowed to add static methods to interfaces. There was
never a technical reason why this should be outlawed. It simply seemed to
be against the spirit of interfaces as abstract specifications.

As of Java 9, methods in an interface can be private. A private method can be
static or an instance method. Since private methods can only be used in the
methods of the interface itself, their use is limited to being helper methods
for the other methods of the interface.

## Default methods

You can supply a default implementation for any interface method. You must
tag such a method with the default modifier.

```java
public interface compareTo<T> {
    default int compareTo(T other) {
        // by default, all elements are the same
        return 0;
    }
}

public interface Iterator<E> {
  boolean hasNext();
  E next();
  default void remove() { throw new UnsupportedOperationException("remove"); }
  . . .
}

public interface Collection {
  int size(); // an abstract method
  default boolean isEmpty() { return size() == 0; }
. . .
}
```

## Resolving default methods conflicts

What happens if the exact same method is defined as a default method in
one interface and then again as a method of a superclass or another interface?

1. Superclasses win. If a superclass provides a concrete method, default
   methods with the same name and parameter types are simply ignored.
2. Interfaces clash. If an interface provides a default method, and another
   interface contains a method with the same name and parameter types
   (default or not), then you must resolve the conflict by overriding that
   method.

```java
interface Person {
    default String getName() { 
        return ""; 
    }
}
interface Named {
    default String getName() {  
        return getClass().getName() + "_" + hashCode(); 
    }
}

class Student implements Person, Named {
  public String getName() { 
      return Person.super.getName(); 
  }
  . . .
}
```
The class inherits two inconsistent getName methods provided by the Person and
Named interfaces. Instead of choosing one over the other, the Java compiler re-
ports an error and leaves it up to the programmer to resolve the ambiguity.
Simply provide a getName method in the Student class. In that method, you can
choose one of the two conflicting methods.

Now assume that the Named interface does not provide a default implementation
for getName. Can the Student class inherit the default method from the Person interface? This
might be reasonable, but the Java designers decided in favor of uniformity.
It doesn’t matter how two interfaces conflict. If at least one interface provides
an implementation, the compiler reports an error, and the programmer must
resolve the ambiguity.

## Interfaces and callbacks

A common pattern in programming is the callback pattern. In this pattern,
you specify the action that should occur whenever a particular event happens.
For example, you may want a particular action to occur when a button is
clicked or a menu item is selected.

For example, when you construct a timer, you set the time interval and tell it what it should
do whenever the time interval has elapsed.

How do you tell the timer what it should do? In many programming languages,
you supply the name of a function that the timer should call periodically.
However, the classes in the Java standard library take an object-oriented approach. You pass an object of some class. The timer then calls one of the
methods on that object. Passing an object is more flexible than passing a
function because the object can carry additional information.

Of course, the timer needs to know what method to call. The timer requires
that you specify an object of a class that implements the ActionListener interface
of the java.awt.event package. Here is that interface:

```java
public interface ActionListener {
  void actionPerformed(ActionEvent event);
}

class TimePrinter implements ActionListener {
  public void actionPerformed(ActionEvent event) {
    System.out.println("At the tone, the time is " 
                               + Instant.ofEpochMilli(event.getWhen()));
    Toolkit.getDefaultToolkit().beep();
  }
}
    
var listener = new TimePrinter();
Timer t = new Timer(1000, listener);
t.start();
```
The timer calls the actionPerformed method when the time interval has expired. 

Note the ActionEvent parameter of the actionPerformed method. This parameter
gives information about the event, such as the time when the event happened.

Next, construct an object of this class and pass it to the Timer constructor.

Finally, start the timer.

## The comparator interface

You have seen how you can sort an array of objects, provided they are instances of 
classes that implement the Comparable interface. For example, you can sort an array of strings
since the String class implements Comparable<String>, and the String.compareTo method
compares strings in dictionary order.

Now suppose we want to sort strings by increasing length, not in dictionary
order. We can’t have the String class implement the compareTo method in two
ways—and at any rate, the String class isn’t ours to modify.

To deal with this situation, there is a second version of the Arrays.sort method
whose parameters are an array and a comparator—an instance of a class that
implements the Comparator interface.

To compare strings by length, define a class that implements Comparator<String>.
To sort an array, pass a LengthComparator object to the Arrays.sort method:

```java
class LengthComparator implements Comparator<String> {
    public int compare(String first, String second) {
        return first.length() - second.length();
    }    
}

// using comparator instance
var comp = new LengthComparator();
if (comp.compare(words[i], words[j]) > 0) {}

// sorting an array using the comparator
String[] friends = {"Peter", "Paul", "Mary"};
Arrays.sort(friends, comp);
```