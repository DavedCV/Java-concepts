# Introduction to Object Oriented Programming

Object-oriented programming, or OOP for short, is the dominant programming
paradigm these days, having replaced the “structured” or procedural program-
ming techniques that were developed in the 1970s. Since Java is object-
oriented, you have to be familiar with OOP to become productive with Java.

An object-oriented program is made of objects. Each object has a specific
functionality, exposed to its users, and a hidden implementation. Many objects
in your programs will be taken “off-the-shelf” from a library; others will be
custom-designed. Whether you build an object or buy it might depend on
your budget or time. But, basically, as long as an object satisfies your
specifications, you don’t care how the functionality is implemented.

For small problems, the breakdown into procedures works very well. But
objects are more appropriate for larger problems. Consider a simple web
browser. It might require 2,000 procedures for its implementation, all of which
manipulate a set of global data. In the object-oriented style, there might be
100 classes with an average of 20 methods per class. This
structure is much easier for a programmer to grasp. It is also much easier to
find bugs in. Suppose the data of a particular object is in an incorrect state.
It is far easier to search for the culprit among the 20 methods that had access
to that data item than among 2,000 procedures.

## Classes

A class specifies how objects are made. Think of classes as cookie cutters;
objects are the cookies themselves. When you construct an object from a class,
you are said to have created an instance of the class.

As you have seen, all code that you write in Java is inside a class. The standard
Java library supplies several thousand classes for such diverse purposes as
user interface design, dates and calendars, and network programming.
Nonetheless, in Java you still have to create your own classes to describe the
objects of your application’s problem domain.

## Objects

To work with OOP, you should be able to identify three key characteristics
of objects:

- The object’s behavior—what can you do with this object, or what methods
  can you apply to it?
- The object’s state—how does the object react when you invoke those
  methods?
- The object’s identity—how is the object distinguished from others that may
  have the same behavior and state?

All objects that are instances of the same class share a family resemblance
by supporting the same behavior. The behavior of an object is defined by the
methods that you can call.

Next, each object stores information about what it currently looks like. This
is the object’s state. An object’s state may change over time, but not sponta-
neously. A change in the state of an object must be a consequence of method
calls. (If an object’s state changed without a method call on that object,
someone broke encapsulation.)

However, the state of an object does not completely describe it, because each
object has a distinct identity. For example, in an order processing system, two
orders are distinct even if they request identical items. Notice that the indi-
vidual objects that are instances of a class always differ in their identity and
usually differ in their state.

## Relarionships between classes

The most common relationships between classes are

- Dependence (“uses–a”)
- Aggregation (“has–a”)
- Inheritance (“is–a”)