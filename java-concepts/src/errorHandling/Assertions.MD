# Using Assertions

Assertions are a commonly used idiom of defensive programming.

## The Assertion Concept

Suppose you are convinced that a particular property is fulfilled, and you rely
on that property in your code. For example, you may be computing

```java
double y = Math.sqrt(x);
```

You are certain that x is not negative. Perhaps it is the result of another
computation that can’t have a negative result, or it is a parameter of a method
that requires its callers to supply only positive inputs. Still, you want to double-
check rather than allow confusing “not a number” floating-point values creep
into your computation. You could, of course, throw an exception:

```java
if (x < 0) throw new IllegalArgumentException("x < 0");
```

But this code stays in the program, even after testing is complete. If you have
lots of checks of this kind, the program may run quite a bit slower than it
should.

The assertion mechanism allows you to put in checks during testing and to
have them automatically removed in the production code.

The Java language has a keyword assert. There are two forms:

```java
assert condition;

assert condition : expression;
```

Both statements evaluate the condition and throw an AssertionError if it is false.
In the second statement, the expression is passed to the constructor of the
AssertionError object and turned into a message string The sole purpose of the expression part is to produce a 
message string.

To assert that x is non-negative, you can simply use the statement

```java
// only assert
assert x >= 0;

// assert and pass value to the AssertionError, so that it gets displayed later
assert x >= 0 : x;
```

## Assertion Enabling and Disabling
By default, assertions are disabled. Enable them by running the program with
the -enableassertions or -ea option:

```bash
java -enableassertions MyApp
```

Note that you do not have to recompile your program to enable or disable
assertions. Enabling or disabling assertions is a function of the class loader.
When assertions are disabled, the class loader strips out the assertion code
so that it won’t slow execution.

You can even turn on assertions in specific classes or in entire packages. For
example:

```bash
java -ea:MyClass -ea:com.mycompany.mylib MyApp
```

This command turns on assertions for the class MyClass and all classes in the
package and its subpackages. The option -ea... turns on
assertions in all classes of the unnamed package.

You can also disable assertions in certain classes and packages with the
-disableassertions or -da option:

```bash
java -ea:... -da:MyClass MyApp
```

## Using Assertions for Parameters Checking

The Java language gives you three mechanisms to deal with system failures:

- Throwing an exception
- Logging
- Using assertions

When should you choose assertions? Keep these points in mind:

- Assertion failures are intended to be fatal, unrecoverable errors.
- Assertion checks are turned on only during development and testing. (This
  is sometimes jokingly described as “wearing a life jacket when you are
  close to shore, and throwing it overboard once you are in the middle of
  the ocean.”)

Therefore, you would not use assertions for signaling recoverable conditions
to another part of the program or for communicating problems to the program
user. Assertions should only be used to locate internal program errors during
testing.

## Using Assertions for Documenting Assumptions

Many programmers use comments to document their underlying assumptions.
In many cases it makes a lot of sense to use an assertion instead.

assertions are a tactical tool for testing and debugging.