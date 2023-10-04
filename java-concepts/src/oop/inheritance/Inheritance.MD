# Inheritance

The idea behind inheritance is that you can create new classes
that are built on existing classes. When you inherit from an existing class,
you reuse (or inherit) its methods, and you can add new methods and fields
to adapt your new class to new situations. This technique is essential in Java
programming.

# Classes, Superclasses and Subclasses

## Defining subclasses

Here is how you define a Manager class that inherits from the Employee class. Use
the Java keyword extends to denote inheritance.

```java
public class Manager extends Employee {
    //added methods and fields
}
```

The keyword extends indicates that you are making a new class that derives
from an existing class. The existing class is called the **superclass, base class, or
parent class**. The new class is called the **subclass, derived class, or child class**.

Manager object has four fields: name, salary, hireDay, and bonus.
When defining a subclass by extending its superclass, you only need to indi-
cate the differences between the subclass and the superclass. When designing
classes, you place the most general methods in the superclass and more
specialized methods in its subclasses. Factoring out common functionality by
moving it to a superclass is routine in object-oriented programming.

## Overriding methods

Some of the superclass methods are not appropriate for the Manager subclass.
In particular, the getSalary method should return the sum of the base salary
and the bonus. You need to supply a new method to override the superclass
method:

```java
public class Manager extends Employee {
    . . .
    public double getSalary() {
        double baseSalary = super.getSalary();
        return baseSalary + bonus;
    }
    . . .
}
```

Recall that the name and parameter type list for a method is called the
method’s signature. For example, f(int) and f(String) are two methods with the
same name but different signatures. If you define a method in a subclass that
has the same signature as a superclass method, you override the superclass
method.

The return type is not part of the signature. However, when you override a
method, you need to keep the return type compatible. A subclass may change
the return type to a subtype of the original type.

## Subclass Constructors

```java
public Manager(String name, double salary, int year, int month, int day) {
    super(name, salary, year, month, day);
    bonus = 0;
}
```

Here, the keyword super has a different meaning. The instruction is shorthand for “call the constructor of the Employee superclass with n, s, year,
month, and day as parameters.”

Since the Manager constructor cannot access the private fields of the Employee
class, it must initialize them through a constructor. The constructor is invoked
with the special super syntax. The call using super must be the first statement
in the constructor for the subclass.

If the subclass constructor does not call a superclass constructor explicitly,
the no-argument constructor of the superclass is invoked. If the superclass
does not have a no-argument constructor and the subclass constructor does
not call another superclass constructor explicitly, the Java compiler reports
an error.

## Polymorphism

A simple rule can help you decide whether or not inheritance is the right
design for your data. The “is–a” rule states that every object of the subclass
is an object of the superclass. For example, every manager is an employee.

Another way of formulating the “is–a” rule is **the substitution principle**. That
principle states that you can use a subclass object whenever the program
expects a superclass object.

For example, you can assign a subclass object to a superclass variable.

```java
Employee e;
e = new Employee(. . .); // Employee object expected
e = new Manager(. . .); // OK, Manager can be used as well
```

In the Java programming language, object variables are **polymorphic**. A variable
of type Employee can refer to an object of type Employee or to an object of any
subclass of the Employee class (such as Manager, Executive, Secretary, and so on).

## Understanding methods calls

It is important to understand exactly how a method call is applied to an object.

1. The compiler looks at the declared type of the object and the method
   name. Note that there may be multiple methods, all with the same name,
   f, but with different parameter types. For example, there may be a method
   f(int) and a method f(String). The compiler enumerates all methods called f
   in the class C and all accessible methods called f in the superclasses of C.
   (Private methods of the superclass are not accessible.)

   Now the compiler knows all possible candidates for the method to be
   called.
2. Next, the compiler determines the types of the arguments supplied in
   the method call. If among all the methods called f there is a unique
   method whose parameter types are a best match for the supplied argu-
   ments, that method is chosen to be called. This process is called overload-
   ing resolution. For example, in a call x.f("Hello"), the compiler picks f(String)
   and not f(int). The situation can get complex because of type conversions
   (int to double, Manager to Employee, and so on). If the compiler cannot find any
   method with matching parameter types or if multiple methods all match
   after applying conversions, the compiler reports an error.

   Now the compiler knows the name and parameter types of the method
   that needs to be called.
3. If the method is private, static, final, or a constructor, then the compiler
   knows exactly which method to call. This is called static binding. Otherwise, the method to
   be called depends on the actual type of the implicit parameter, and dynamic binding must be used at runtime.
4. When the program runs and uses dynamic binding to call a method, the
   virtual machine must call the version of the method that is appropriate
   for the actual type of the object to which x refers. Let’s say the actual type
   is D, a subclass of C. If the class D defines a method f(String), that method is
   called. If not, D’s superclass is searched for a method f(String), and so on.

   It would be time-consuming to carry out this search every time a method
   is called. Instead, the virtual machine precomputes for each class a
   method table that lists all method signatures and the actual methods to
   be called.

## Preventing Inheritance: Final classes and Methods

Occasionally, you want to prevent someone from forming a subclass of one
of your classes. Classes that cannot be extended are called final classes, and
you use the **final modifier** in the definition of the class to indicate this.

```java
public final class Executive extends Manager  {
    . . .
}
```

ou can also make a specific method in a class final. If you do this, then
no subclass can override that method. (All methods in a final class are
automatically final.) For example:

```java
public class Employee {
    . . .
    public final String getName() {
        return name;
    }
    . . .
}
```

> Recall that fields can also be declared as final. A final field cannot be
changed after the object has been constructed. However, if a class is declared
final, only the methods, not the fields, are automatically final.

Some programmers believe that you should declare all methods as final unless
you have a good reason to want polymorphism. In fact, in C++ and C#,
methods do not use polymorphism unless you specifically request it. That
may be a bit extreme, but we agree that it is a good idea to think carefully
about final methods and classes when you design a class hierarchy.

## Casting

Just as you occasionally need to convert a floating-point number to an integer,
you may need to convert an object reference from one class to another. To
actually make a cast of an object reference, use a syntax similar to what you
use for casting numeric expressions. Surround the target class name with
parentheses and place it before the object reference you want to cast.

```java
Manager boss = (Manager) staff[0];
```

There is only one reason why you would want to make a cast—to use an
object in its full capacity after its actual type has been temporarily forgotten.

- You can cast only within an inheritance hierarchy.
- Use instanceof to check before casting from a superclass to a subclass.

## Abstract classes

As you move up the inheritance hierarchy, classes become more general and
probably more abstract. At some point, the ancestor class becomes so general
that you think of it more as a basis for other classes than as a class with
specific instances you want to use.

Sometimes the implementation of some methods are not completely clear until the
class is extended, in that case if you use the abstract keyword, you do 
not need to implement the method at all. Just set the method as a constraint to
implement when subclassing. In addition to abstract methods, abstract classes can have fields and concrete
methods.

```java
public abstract class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public abstract String getDescription();

    public String getName() {
        return name;
    }
}
```

Abstract methods act as placeholders for methods that are implemented in
the subclasses. When you extend an abstract class, you have two choices.
You can leave some or all of the abstract methods undefined; then you must
tag the subclass as abstract as well. Or you can define all methods, and the
subclass is no longer abstract.

Abstract classes cannot be instantiated. That is, if a class is declared as abstract,
no objects of that class can be created. Note that you can still create object variables of an abstract class, but such a
variable must refer to an object of a nonabstract subclass.

```java
public class Student extends Person {
    private String major;
    
    public Student(String name, String major) {
        super(name);
        this.major = major;
    }

    public String getDescription() {
        return "a student majoring in " + major;
    }
}
```

The Student class defines the getDescription method. Therefore, all methods in
the Student class are concrete, and the class is no longer an abstract class.

## Protected Access

There are times, when you want to restrict a method to subclasses
only or, less commonly, to allow subclass methods to access a superclass
field. In that case, you declare a class feature as protected.

In Java, a protected field is accessible by any class in the same package.