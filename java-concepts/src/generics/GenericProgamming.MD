# Generic Programming 

Generic classes and methods have type parameters. This allows them to de-
scribe precisely what should happen when they are instantiated with specific
types. Prior to generic classes, programmers had to use the Object for writing
code that works with multiple types. This was both cumbersome and unsafe.

With the introduction of generics, Java has an expressive type system that
allows designers to describe in detail how types of variables and methods
should vary. In straightforward situations, you will find it simple to implement
generic code. In more advanced cases, it can get quite complex—for implemetors. The goal is to provide classes and methods that other programmers can
use without surprises.

The introduction of generics in Java 5 constitutes the most significant change
in the Java programming language since its initial release. A major design
goal was to be backwards compatible with earlier releases. As a result, Java
generics have some uncomfortable limitations.

## Why Generic Programming 

Generic programming means writing code that can be reused for objects of
many different types. For example, you don’t want to program separate
classes to collect String and File objects. And you don’t have to—the single
class ArrayList collects objects of any class. This is one example of generic
programming.

### The advantages of Type Parameters

Before generic classes were added to Java, generic programming was achieved
with inheritance. The ArrayList class simply maintained an array of Object
references. This approach has two problems. A cast is necessary whenever you retrieve
a value, Moreover, there is no error checking; you can add values of any class.

```java
ArrayList files = new ArrayList();
. . .
String filename = (String) files.get(0);
files.add(new File(". . ."));
```

Generics offer a better solution: type parameters. The ArrayList class now has a
type parameter that indicates the element type.

```java
var files = new ArrayList<String>();
```

This makes your code easier to read. You can tell right away that this particular
array list contains String objects. The compiler can make good use of the type information too. 
No cast is required for calling get. The compiler knows that the return type is String, not
Object. The compiler also knows that the add method of an ArrayList<String> has a parameter of type String. That is a lot safer than having an Object parameter. Now
the compiler can check that you don’t insert objects of the wrong type.

This is the appeal of type parameters: They make your programs easier to
read and safer.

## Defining a Simple Generic Class

A generic class is a class with one or more type variables.

```java
public class Pair<T> {
    private T first;
    private T second;

    public Pair() { first = null; second = null; }
    public Pair(T first, T second) { this.first = first; this.second = second; }
    
    public T getFirst() { return first; }
    public T getSecond() { return second; }
    
    public void setFirst(T newValue) { first = newValue; }
    public void setSecond(T newValue) { second = newValue; }
}
```

The Pair class introduces a type variable T, enclosed in angle brackets < >, after
the class name. A generic class can have more than one type variable. For
example, we could have defined the Pair class with separate types for the first
and second field:

```java
public class Pair<T, U> { . . . }
```

The type variables are used throughout the class definition to specify method
return types and the types of fields and local variables.

> It is common practice to use uppercase letters for type variables, and
to keep them short. The Java library uses the variable E for the element type of
a collection, K and V for key and value types of a table, and T (and the neighboring
letters U and S, if necessary) for “any type at all.”


You instantiate the generic type by substituting types for the type variables,
such as:

```java
Pair<String>
```

In other words, the generic class acts as a factory for ordinary classes.

## Generic Methods

You can also define a single method with type parameters.

```java
class ArrayAlg {
    public static <T> T getMiddle(T... a) {
        return a[a.length / 2];
    }
}
```

This method is defined inside an ordinary class, not inside a generic class.
However, it is a generic method, as you can see from the angle brackets and
the type variable. Note that the type variables are inserted after the modifiers
(public static, in our case) and before the return type.

You can define generic methods both inside ordinary classes and inside
generic classes.

When you call a generic method, you can place the actual types, enclosed in
angle brackets, before the method name:

```java
String middle = ArrayAlg.<String>getMiddle("John", "Q.", "Public");
```

In this case (and indeed in most cases), you can omit the <String> type param-
eter from the method call. The compiler has enough information to infer
the method that you want. It matches the type of the arguments against the
generic type T... and deduces that T must be String.

```java
String middle = ArrayAlg.getMiddle("John", "Q.", "Public");
```

## Bounds for Type Variables

Sometimes, a class or a method needs to place restrictions on type variables.

```java
class ArrayAlg {
    public static <T> T min(T[] a) // almost correct {
        if (a == null || a.length == 0) return null;
        T smallest = a[0];
        for (int i = 1; i < a.length; i++)
            if (smallest.compareTo(a[i]) > 0) smallest = a[i];
        return smallest;
    }
}
```

But there is a problem. Look inside the code of the min method. The variable
smallest has type T, which means it could be an object of an arbitrary class.
How do we know that the class to which T belongs has a compareTo method?

The solution is to restrict T to a class that implements the Comparable interface—a
standard interface with a single method, compareTo. You can achieve this by
giving a **bound** for the type variable T:

```java
public static <T extends Comparable> T min(T[] a) . . .
```

Now, the generic min method can only be called with arrays of classes that
implement the Comparable interface.

The notation ` <T extends BoundingType> ` expresses that T should be a subtype of the bounding type

A type variable or wildcard can have multiple bounds. For example `T extends Comparable & Serializable`. If you have a class as a
bound, it must be the first one in the bounds list.

## Generic Code and The Virtual Machine

The virtual machine does not have objects of generic types—all objects belong
to ordinary classes.

You need to remember these facts about translation of Java
generics:

- There are no generics in the virtual machine, only ordinary classes and
  methods.
- All type parameters are replaced by their bounds.
- Bridge methods are synthesized to preserve polymorphism.
- Casts are inserted as necessary to preserve type safety.

### Type Erasure

Whenever you define a generic type, a corresponding raw type is automatically
provided. The name of the raw type is simply the name of the generic type,
with the type parameters removed. The type variables are erased and replaced
by their bounding types (or Object for variables without bounds).

For example, the raw type for Pair<T> looks like this:

```java
public class Pair  {
    private Object first;
    private Object second;
    
    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    public Object getFirst() { return first; }

    public Object getSecond() { return second; }

    public void setFirst(Object newValue) { first = newValue; }

    public void setSecond(Object newValue) { second = newValue; }
}
```

Since T is an unbounded type variable, it is simply replaced by Object.

The result is an ordinary class, just as you might have implemented it before
generics were added to Java.

Your programs may contain different kinds of Pair, such as Pair<String> or
Pair<LocalDate>, but erasure turns them all into raw Pair types.

## Restrictions and Limitations

Most of these restrictions are a consequence of type erasure.

### Type Parameters Cannot Be Instantiated with Primitive Types

You cannot substitute a primitive type for a type parameter. Thus, there is
no Pair<double>, only Pair<Double>. The reason is, of course, type erasure. After
erasure, the Pair class has fields of type Object, and you can’t use them to store
double values.

### Runtime Type Inquiry Only Works with Raw Types

Objects in the virtual machine always have a specific nongeneric type.
Therefore, all type inquiries yield only the raw type.

```java
if (a instanceof Pair<String>) // ERROR
```

could only test whether a is a Pair of any type. The same is true for the test

```java
if (a instanceof Pair<T>) // ERROR
```

or the cast

```java
Pair<String> p = (Pair<String>) a; // warning--can only test that a is a Pair
```

In the same spirit, the getClass method always returns the raw type. For
example:

```java
Pair<String> stringPair = . . .;
Pair<Employee> employeePair = . . .;
if (stringPair.getClass() == employeePair.getClass()) // they are equal
```

The comparison yields true because both calls to getClass return Pair.class.

### You Cannot Create Arrays of Parametrized Types

You cannot instantiate arrays of parameterized types, such as

```java
var table = new Pair<String>[10]; // ERROR
```

If you need to collect parameterized type objects, simply use an ArrayList:
ArrayList<Pair<String>> is safe and effective.

### You Cannot Instantiate Type Variables

You cannot use type variables in an expression such as new T(. . .). For
example, the following Pair<T> constructor is illegal:

```java
public Pair() { first = new T(); second = new T(); } // ERROR
```

Type erasure would change T to Object, and surely you don’t want to call new
Object().

The best workaround, available since Java 8, is to make the caller provide a
constructor expression. For example:

```java
Pair<String> p = Pair.makePair(String::new);
```

The makePair method receives a Supplier<T>, the functional interface for a function
with no arguments and a result of type T:

```java
public static <T> Pair<T> makePair(Supplier<T> constr) {
    return new Pair<>(constr.get(), constr.get());
}
```

### You Cannot Construct a Generic Array

Just as you cannot instantiate a single generic instance, you cannot instantiate
an array. The reasons are different—an array is, after all, filled with null values,
which would seem safe to construct. But an array also carries a type, which
is used to monitor array stores in the virtual machine. That type is erased.
For example, consider

```java
public static <T extends Comparable> T[] minmax(T... a) {
    T[] mm = new T[2]; // ERROR
    . . .
}
```

Type erasure would cause this method to always construct an array Comparable[2].

...

---
If the array is only used as a private instance field of a class, you can declare
the element type of the array to be the erased type and use casts. For
example, the ArrayList class could be implemented as follows:

```java
public class ArrayList<E> {
    private Object[] elements;
    . . .
    @SuppressWarnings("unchecked") 
    public E get(int n) { return (E) elements[n]; }
    public void set(int n, E e) { elements[n] = e; } // no cast needed
}
```

The actual implementation is not quite as clean:

```java
public class ArrayList<E> {
    private E[] elements;
    . . .
    public ArrayList() { elements = (E[]) new Object[10]; }
}
```

Here, the cast E[] is an outright lie, but type erasure makes it undetectable.

---

...

In this situation, it is best to ask the user to provide an array constructor
expression:

```java
String[] names = ArrayAlg.minmax(String[]::new, "Tom", "Dick", "Harry");
```

The constructor expression String[]::new denotes a function that, given the
desired length, constructs a String array of that length.

The method uses that parameter to produce an array of the correct type:

```java
public static <T extends Comparable> T[] minmax(IntFunction<T[]> constr, T... a) {
    T[] result = constr.apply(2);
    . . .
}
```

### Type Variables Are Not Valid in Static Contexts of Generic Classes

You cannot reference type variables in static fields or methods.

### You Cannot Throw or Catch Instances of a Generic Class

You can neither throw nor catch objects of a generic class. In fact, it is not
even legal for a generic class to extend Throwable. For example, the following
definition will not compile:

```java
public class Problem<T> extends Exception { /* . . . */ }
// ERROR--can't extend Throwable
```

You cannot use a type variable in a catch clause. For example, the following
method will not compile:

```java
public static <T extends Throwable> void doWork(Class<T> t) {
    try {
        do work
    }
    catch (T e) // ERROR--can't catch type variablE {
        Logger.getGlobal().info(. . .);
    }
}
```

However, it is OK to use type variables in exception specifications. The
following method is legal:

```java
public static <T extends Throwable> void doWork(T t) throws T // OK {
  try {
    do work
  }
  catch (Throwable realCause) {
    t.initCause(realCause);
    throw t;
  }
}
```

### Beware of Clashes after Erasure

It is illegal to create conditions that cause clashes when generic types are
erased. Here is an example. Suppose we add an equals method to the Pair class,
like this:

```java
public class Pair<T> {
    public boolean equals(T value) { 
        return first.equals(value) && second.equals(value); 
    }
    . . .
}
```

Consider a Pair<String>. Conceptually, it has two equals methods:

```java
boolean equals(String) // defined in Pair<T>
boolean equals(Object) // inherited from Object
```

But the intuition leads us astray. The erasure of the method
`boolean equals(T)` is `boolean equals(Object)`. which clashes with the 
Object.equals method.

The remedy is, of course, to rename the offending method.

The generics specification cites another rule: “To support translation by
erasure, we impose the restriction that a class or type variable may not at
the same time be a subtype of two interface types which are different
parameterizations of the same interface.” For example, the following is illegal:

```java
class Employee implements Comparable<Employee> { . . . }
class Manager extends Employee implements Comparable<Manager> { . . . } // ERROR
```

Manager would then implement both Comparable<Employee> and Comparable<Manager>,
which are different parameterizations of the same interface.

There would be a conflict with the
synthesized bridge methods. A class that implements Comparable<X> gets a bridge
method

```java
public int compareTo(Object other) { return compareTo((X) other); }
```

You cannot have two such methods for different types X.

## Inheritance Rules for Generic Types

Consider a class and a subclass, such as Employee and Manager.
Is Pair<Manager> a subclass of Pair<Employee>? Perhaps surprisingly, the answer is
“no.” For example, the following code will not compile:

```java
Manager[] topHonchos = . . .;
Pair<Employee> result = ArrayAlg.minmax(topHonchos); // ERROR
```

The minmax method returns a Pair<Manager>, not a Pair<Employee>, and it is illegal to
assign one to the other.

**In general, there is no relationship between Pair\<S> and Pair\<T>, no matter how
S and T are related.**

You can always convert a parameterized type to a raw type. For example,
Pair<Employee> is a subtype of the raw type Pair. This conversion is necessary for
interfacing with legacy code.

This sounds scary. However, keep in mind that you are no worse off than
you were with older versions of Java. The security of the virtual machine is
not at stake. When the foreign object is retrieved with getFirst and assigned
to a Manager variable, a ClassCastException is thrown, just as in the good old days.
You merely lose the added safety that generic programming normally provides.

```java
var managerBuddies = new Pair<Manager>(ceo, cfo);
Pair rawBuddies = managerBuddies; // OK
rawBuddies.setFirst(new File(". . .")); // only a compile-time warning
```

Finally, generic classes can extend or implement other generic classes. In this
regard, they are no different from ordinary classes. For example, the class
ArrayList<T> implements the interface List<T>. That means an ArrayList<Manager> can
be converted to a List<Manager>.

## Wildcard Types