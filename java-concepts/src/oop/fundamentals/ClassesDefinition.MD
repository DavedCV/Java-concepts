# Defining Your Own Classes

## An employee class

The simplest form for a class definition in Java is

```java
class ClassName
{
    field1
    field2
    . . .
    constructor1
    constructor2
    . . .
    method1
    method2
    . . .
}

class Employee
{
    // instance fields
    private String name;
    private double salary;
    private LocalDate hireDay;
    
    // constructor
    public Employee(String n, double s, int year, int month, int day)
    {
        name = n;
        salary = s;
        hireDay = LocalDate.of(year, month, day);
    }
    
    // a method
    public String getName()
    {
        return name;
    }
    
    // more methods
    . . .
}
```

## Disecting employee class

As you can see by examining the source code, this  class has one constructor and 
four methods. All methods of this class are tagged as public. The **keyword public** means that
any method in any class can call the method.

Next, notice the three instance fields that will hold the data manipulated inside
an instance of the Employee class. The **private keyword** makes sure that the only methods that can access these
instance fields are the methods of the Employee class itself.

## Constructor

```java
public Employee(String n, double s, int year, int month, int day) {
    name = n;
    salary = s;
    hireDay = LocalDate.of(year, month, day);
}
```

As you can see, the name of the constructor is the same as the name of
the class. This constructor runs when you construct objects of the Employee
class—giving the instance fields the initial state you want them to have.

There is an important difference between constructors and other methods. A
constructor can only be called in conjunction with the new operator.

- A constructor has the same name as the class.
- A class can have more than one constructor.
- A constructor can take zero, one, or more parameters.
- A constructor has no return value.
- A constructor is always called with the new operator.

## Working with null references

If you apply a method to a null value, a NullPointerException occurs.

```java
LocalDate birthday = null;
String s = birthday.toString(); // NullPointerException
```

This is a serious error, similar to an “index out of bounds” exception. If your
program does not “catch” an exception, it is terminated. Normally, programs
don’t catch these kinds of exceptions but rely on programmers not to cause
them in the first place.

When you define a class, it is a good idea to be clear about which fields can
be null.

There are two solutions:
- The “permissive” approach is to turn a null argument
into an appropriate non-null value:

```java
if (n == null) name = "unknown"; else name = n;
```

the Objects class has a convenience method for this purpose:

```java
public Employee(String n, double s, int year, int month, int day){
    name = Objects.requireNonNullElse(n, "unknown");
    . . .
}
```

- The “tough love” approach is to reject a null argument:

```java
public Employee(String n, double s, int year, int month, int day){
    Objects.requireNonNull(n, "The name cannot be null");
    name = n;
    . . .
}
```

## Implicit and Explicit parameters

Methods operate on objects and access their instance fields. For example, the
method

```java
public void raiseSalary(double byPercent){
    double raise = salary * byPercent / 100;
    salary += raise;
}
```

sets a new value for the salary instance field in the object on which this method
is invoked. 

The raiseSalary method has two parameters. The first parameter, called the
implicit parameter, is the object of type Employee that appears before the method
name. The second parameter, the number inside the parentheses after the
method name, is an explicit parameter. (Some people call the implicit parameter
the target or receiver of the method call.)

In every method, the keyword **this** refers to the implicit parameter. If you like,
you can write the raiseSalary method as follows:

```java
public void raiseSalary(double byPercent) {
    double raise = this.salary * byPercent / 100;
    this.salary += raise;
}
```

## Benefits of encapsulation

Sometimes, it happens that you want to get and set the value of an instance
field. Then you need to supply three items:

- A private data field
- A public field accessor method
- A public field mutator method

This is a lot more tedious than supplying a single public data field, but there
are considerable benefits.

- First, you can change the internal implementation without affecting any code
other than the methods of the class.

- Mutator methods can perform error checking, whereas
code that simply assigns to a field may not go into the trouble

## Class Bassed Access Privileges

You know that a method can access the private data of the object on which
it is invoked. What people often find surprising is that a method can access
the private data of all objects of its class. For example, consider a method equals
that compares two employees.

```java
class Employee  {
    . . .
    public boolean equals(Employee other) {
        return name.equals(other.name);
    }
}

if (harry.equals(boss)) . . .
```

This method accesses the private fields of harry, which is not surprising. It also
accesses the private fields of boss. This is legal because boss is an object of type
Employee, and a method of the Employee class is permitted to access the private
fields of any object of type Employee.

## Final instance fields

You can define an instance field as final. Such a field must be initialized when
the object is constructed. That is, you must guarantee that the field value has
been set after the end of every constructor. Afterwards, the field may not be
modified again.

The **final** modifier is particularly useful for fields whose type is primitive or
an immutable class. (A class is immutable if none of its methods ever mutate
its objects. For example, the String class is immutable.)

# Static fields and methods

## Static fields

If you define a field as static, then there is only one such field per class. In
contrast, each object has its own copy of nonstatic instance fields.

## Static constants

Static variables are quite rare. However, static constants are more common.
For example, the Math class defines a static constant:

```java
public class Math {
    . . .
    public static final double PI = 3.14159265358979323846;
    . . .
}
```

## Static methods

Static methods are methods that do not operate on objects. You can think of static methods 
as methods that don’t have a this parameter. A static method can access a static field.

Use static methods in two situations:
- When a method doesn’t need to access the object state because all needed
  parameters are supplied as explicit parameters.
- When a method only needs to access static fields of the class.

## Factory methods

Here is another common use for static methods. Classes such as LocalDate
and NumberFormat use static factory methods that construct objects

```java
NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
NumberFormat percentFormatter = NumberFormat.getPercentInstance();
double x = 0.1;
System.out.println(currencyFormatter.format(x)); // prints $0.10
System.out.println(percentFormatter.format(x)); // prints 10%
```

## The main method

Note that you can call static methods without having any objects. The main method does not operate on any objects. In fact, when a program
starts, there aren’t any objects yet. The static main method executes, and constructs the objects that the program 
needs.

# Method parameters

The term **call by value** means that the method gets just the value that the caller provides.
In contrast, **call by reference** means that the method gets the location of the
variable that the caller provides.

Thus, a method can modify the value stored  in a variable passed by reference but not in one passed by value.

The Java programming language **always** uses call by value. That means that
the method gets a copy of all parameter values. In particular, the method
cannot modify the contents of any parameter variables passed to it.

There are, however, two kinds of method parameters:
- Primitive types (numbers, boolean values)
- Object references

It is easily possible—and in fact very common—to implement
methods that change the state of an object parameter. The reason is simple.
The method gets a copy of the object reference, and both the original and
the copy refer to the same object.

Here is a summary of what you can and cannot do with method parameters
in Java:

- A method cannot modify a parameter of a primitive type (that is, numbers
  or boolean values).
- A method can change the state of an object parameter.
- A method cannot make an object parameter refer to a new object.

# Object Construction

## Overloading

Some classes have more than one constructor. This capability is called overloading. Overloading occurs if several methods
have the same name but different parameters.

The compiler must sort out which method to call. It picks the correct method by matching the parameter types in the headers of
the various methods with the types of the values used in the specific method
call. A compile-time error occurs if the compiler cannot match the parameters,
either because there is no match at all or because there is not one that is
better than all others. (The process of finding a match is called overloading
resolution.)

> Java allows you to overload any method—not just constructor methods.
> Thus, to completely describe a method, you need to specify its name together
> with its parameter types. This is called the signature of the method.
> The return type is not part of the method signature. That is, you cannot have
two methods with the same names and parameter types but different return
types.

## Default field initialization

If you don’t set a field explicitly in a constructor, it is automatically set to a
default value: numbers to 0, boolean values to false, and object references to
null.

## The constructor with no arguments

Many classes contain a constructor with no arguments that creates an
object whose state is set to an appropriate default.

```java
public Employee() {
    name = "";
    salary = 0;
    hireDay = LocalDate.now();
}
```

If you write a class with no constructors whatsoever, then a no-argument
constructor is provided for you. This constructor sets all the instance fields
to their default values. So, all numeric data contained in the instance
fields would be 0, all boolean values would be false, and all object variables
would be null.

If a class supplies at least one constructor but does not supply a no-argument
constructor, it is illegal to construct objects without supplying arguments

## Explicit field initialization

By overloading the constructor methods in a class, you can build many ways
to set the initial state of the instance fields of your classes. It is always a good
idea to make sure that, regardless of the constructor call, every instance field
is set to something meaningful.

You can simply assign a value to any field in the class definition. For example:

```java
class Employee {
    private String name = "";
    . . .
}
```

This assignment is carried out before the constructor executes. This syntax
is particularly useful if all constructors of a class need to set a particular
instance field to the same value.

## Calling another constructor

If the first statement of a constructor has the form this(. . .), then the constructor
calls another constructor of the same class. Here is a typical example:

```java
public Employee(double s) {
    // calls Employee(String, double)
    this("Employee #" + nextId, s);
    nextId++;
}
```

## Initialization blocks

Class declarations can contain arbitrary blocks of code. These blocks are executed
whenever an object of that class is constructed. For example:

```java
class Employee {
    private static int nextId;
    private int id;
    private String name;
    private double salary;

    // object initialization block
    {
        id = nextId;
        nextId++;
    }

    public Employee(String n, double s) {
        name = n;
        salary = s;
    }

    public Employee()
    {
        name = "";
        salary = 0;
    }
    ...
}
```

In this example, the id field is initialized in the object initialization block, **no
matter which constructor is used to construct an object. The initialization
block runs first, and then the body of the constructor is executed.**

This mechanism is never necessary and is not common. It is usually more
straightforward to place the initialization code inside a constructor.

To initialize a static field, either supply an initial value or use a static
initialization block.

```java
private static int nextId = 1;

// static initialization block
static {
    var generator = new Random();
    nextId = generator.nextInt(10000);
}
```

## What happens when a constructor is called

1. If the first line of the constructor calls a second constructor, then the
   second constructor executes with the provided arguments.
2. Otherwise,
   - All data fields are initialized to their default values (0, false, or null).
   - All field initializers and initialization blocks are executed, in the order
     in which they occur in the class declaration.
3. The body of the constructor is executed.