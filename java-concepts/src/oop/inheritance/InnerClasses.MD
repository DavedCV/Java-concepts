# Inner Classes

An inner class is a class that is defined inside another class. Why would you
want to do that? There are two reasons:

- Inner classes can be hidden from other classes in the same package
- Inner classes methods can access the data from the scope in which trey are defined including
    the data that would otherwise be private

Inner classes used to be very important for concisely implementing callbacks,
but nowadays lambda expressions do a much better job. Still, inner classes
can be very useful for structuring your code.

## Use of an inner class to access object state

```java
public class TalkingClock {
    private int interval;
    private boolean beep;

    public TalkingClock(int interval, boolean beep) { . . . }
    public void start() { . . . }
    
    public class TimePrinter implements ActionListener // an inner class
    {
        public void actionPerformed(ActionEvent event) {
            System.out.println("At the tone, the time is " 
                                       + Instant.ofEpochMilli(event.getWhen()));
            
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    }
}
```

Note that the actionPerformed method checks the beep flag before emitting a beep.
Something surprising is going on. The TimePrinter class has no instance field
or variable named beep. Instead, beep refers to the field of the TalkingClock object
that created this TimePrinter. As you can see, an inner class method gets to
access both its own data fields and those of the outer object creating it. 

**For this to work, an object of an inner class always gets an implicit reference
to the object that created it.** The outer class reference is set in the constructor. The compiler modifies all
inner class constructors, adding a parameter for the outer class reference.

## Special syntax rules for inner classes 

The expression `OuterClass.this` denotes the outer class reference. 

For example, you can write the actionPerformed method of the TimePrinter inner 
class as:

```java
public void actionPerformed(ActionEvent event) {
    . . .   
    if (TalkingClock.this.beep) Toolkit.getDefaultToolkit().beep();
}
```

Conversely, you can write the inner object constructor more explicitly, using
the syntax:

```java
outerObject.new InnerClass(construction parameters)

ActionListener listener = this.new TimePrinter();
```

Here, the outer class reference of the newly constructed TimePrinter object is
set to the this reference of the method that creates the inner class object. This
is the most common case. As always, the this. qualifier is redundant. However,
it is also possible to set the outer class reference to another object by explic-
itly naming it. For example, since TimePrinter is a public inner class, you can
construct a TimePrinter for any talking clock:

```java
var jabberer = new TalkingClock(1000, true);
TalkingClock.TimePrinter listener = jabberer.new TimePrinter();
```
Note that you refer to an inner class as `OuterClass.InnerClass` when it occurs outside the 
scope of the outer class.

> Any static fields declared in an inner class must be final and initialized
with a compile-time constant. If the field was not a constant, it might not be
unique.
> 
>An inner class cannot have static methods. The Java Language Specification
gives no reason for this limitation. It would have been possible to allow static
methods that only access static fields and methods from the enclosing class.
Apparently, the language designers decided that the complexities outweighed
the benefits.

## Are Inner Classes Useful? Actually Necessary? Secure?

Inner classes are a phenomenon of the compiler, not the virtual machine.
Inner classes are translated into regular class files with $ (dollar signs) delimiting outer and inner class names, and the virtual machine does not have any
special knowledge about them.

For example, the TimePrinter class inside the TalkingClock class is translated to a
class file TalkingClock$TimePrinter.class.

Inner classes are genuinely more powerful than regular classes because
they have more access privileges.

If an inner class accesses a private data field, then it is possible
to access that data field through other classes added to the package of the
outer class, but to do so requires skill and determination. A programmer
cannot accidentally obtain access but must intentionally build or modify a
class file for that purpose.

## Local inner classes

If you look carefully at the code of the TalkingClock example, you will find that
you need the name of the type TimePrinter only once: when you create an object
of that type in the start method.

In a situation like this, you can **define the class locally in a single method**.

```java
public void start() {
    
    class TimePrinter implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.out.println("At the tone, the time is "
                                   + Instant.ofEpochMilli(event.getWhen()));
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    }
        
    var listener = new TimePrinter();
    var timer = new Timer(interval, listener);
    timer.start();    
}
```

Local classes are never declared with an access specifier (that is, public or
private). Their scope is always restricted to the block in which they are declared.

Local classes have one great advantage: They are completely hidden from
the outside world—not even other code in the TalkingClock class can access
them. No method except start has any knowledge of the TimePrinter class.

## Accessing variables from outer methods

Local classes have another advantage over other inner classes. Not only can
they access the fields of their outer classes; they can even access local vari-
ables! However, those local variables must be effectively final. That means,
they may never change once they have been assigned. 

Here is a typical example. Let’s move the interval and beep parameters from
the TalkingClock constructor to the start method.

```java
public void start(int interval, boolean beep) {
    
    class TimePrinter implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.out.println("At the tone, the time is "
                                   + Instant.ofEpochMilli(event.getWhen()));
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    }
        
    var listener = new TimePrinter();
    var timer = new Timer(interval, listener);
    timer.start();    
}
```

Note that the TalkingClock class no longer needs to store a beep instance field.
It simply refers to the beep parameter variable of the start method.

For the code in the actionPerformed method to work, the TimePrinter class must
have copied the beep field as a local variable of the start method, before the
beep parameter value went away. That is indeed exactly what happens.

## Anonymous inner classes

When using local inner classes, you can often go a step further. If you want
to make only a single object of this class, you don’t even need to give the
class a name. Such a class is called an **anonymous inner class**.

```java
public void start(int interval, boolean beep) {
    
    var listener = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            System.out.println("At the tone, the time is "
                + Instant.ofEpochMilli(event.getWhen()));
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    };

    var timer = new Timer(interval, listener);
    timer.start();
}
```

**This syntax is very cryptic indeed. What it means is this: Create a new object
of a class that implements the ActionListener interface, where the required
method actionPerformed is the one defined inside the braces { }.**

In general the syntax is:

```java
new SuperType(construction parameters) {
    inner class methods and data
}
```

Here, **SuperType can be an interface**, such as ActionListener; then, the inner class
implements that interface. **SuperType can also be a class**; then, the inner class
extends that class.

An anonymous inner class cannot have constructors because the name of a
constructor must be the same as the name of a class, and the class has no
name. Instead, the construction parameters are given to the superclass con-
structor. In particular, whenever an inner class implements an interface, it
cannot have any construction parameters. Nevertheless, you must supply a
set of parentheses as in

```java
new InterfaceType() {
    methods and data
}
```
You have to look carefully to see the difference between the construction of
a new object of a class and the construction of an object of an anonymous
inner class extending that class.

```java
var queen = new Person("Mary"); // a Person object
        
var count = new Person("Dracula") { . . }; // an object of an inner class extending Person
```

> Even though an anonymous class cannot have constructors, you can
provide an object initialization block


> It is often convenient to make an anonymous subclass that is almost,
> but not quite, like its superclass. But you need to be careful with the equals
> method.
>
> We recommended that your equals methods use a test `if (getClass() != other.getClass()) return false;` An anonymous
> subclass will fail this test.

## Static Inner Classes

Occasionally, you may want to use an inner class simply to hide one class
inside another—but you don’t need the inner class to have a reference to the
outer class object. You can suppress the generation of that reference by
declaring the inner class static.

Of course, only inner classes can be declared static. A static inner class is
exactly like any other inner class, except that an object of a static inner class
does not have a reference to the outer class object that generated it.

> Unlike regular inner classes, static inner classes can have static fields
and methods.

> Inner classes that are declared inside an interface are automatically
static and public.