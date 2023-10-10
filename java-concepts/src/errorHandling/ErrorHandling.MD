# Exceptions, Assertions and Logging

In a perfect world, users would never enter data in the wrong form, files they
choose to open would always exist, and code would never have bugs.

Encountering errors is unpleasant. If a user loses all the work he or she did
during a program session because of a programming mistake or some external
circumstance, that user may forever turn away from your program. At the
very least, you must:

- Notify the user of an error
- Save all work
- Allow users to gracefully exit the program.

During testing, you need to run lots of checks to make sure your program
does the right thing. But those checks can be time-consuming and unnecessary
after testing has completed. You could just remove the checks and stick them
back in when additional testing is required—but that is tedious. Use the assertion 
facility for selectively activating checks.

When your program does the wrong thing, you can’t always communicate
with the user or terminate. Instead, you may want to record the problem for
later analysis. For this use a logging framework.

## Dealing with errors

If an operation cannot be completed because of an error, the program ought to either

- Return to a safe state and enable the user to execute other commands
- Allow the user to save all work and terminate the program gracefully.

This may not be easy to do, because the code that detects (or even causes)
the error condition is usually far removed from the code that can roll back
the data to a safe state or save the user’s work and exit cheerfully. The mission
of exception handling is to transfer control from where the error occurred to
an error handler that can deal with the situation.

To handle exceptional situations in your program, you must take into account the errors and problems
that may occur. What sorts of problems do you need to consider?

- User input errors
- Device errors
- Physical limitations
- Code errors

The traditional reaction to an error in a method is to return a special error
code that the calling method analyzes. it throws an object
that encapsulates the error information. Note that the method exits immediately; it does not return its normal (or any) value. Moreover, execution does
not resume at the code that called the method; instead, the exception-handling
mechanism begins its search for an exception handler that can deal with this
particular error condition.

Exceptions have their own syntax and are part of a special inheritance hierarchy.

## The Classification of Exceptions

In the Java programming language, an exception object is always an instance
of a class derived from **Throwable**.

Notice that all exceptions descend from Throwable, but the hierarchy immediately
splits into two branches: **Error** and **Exception**.

- The Error hierarchy describes internal errors and resource exhaustion situations
  inside the Java runtime system. You should not throw an object of this type.
  There is little you can do if such an internal error occurs, beyond notifying
  the user and trying to terminate the program gracefully. These situations are
  quite rare.
- When doing Java programming, focus on the Exception hierarchy. The Exception
  hierarchy also splits into two branches: exceptions that derive from
  RuntimeException and those that do not. The general rule is this: A RuntimeException
  happens because you made a programming error. Any other exception occurs
  because a bad thing, such as an I/O error, happened to your otherwise good
  program.

The Java Language Specification calls any exception that derives from the
class Error or the class RuntimeException an unchecked exception. All other exceptions are called checked exceptions. This is useful terminology that we also
adopt. The compiler checks that you provide exception handlers for all checked
exceptions.

## Declaring Checked Exceptions

A Java method can throw an exception if it encounters a situation it cannot
handle. The idea is simple: A method will not only tell the Java compiler
what values it can return, it is also going to tell the compiler what can go wrong.

The place in which you advertise that your method can throw an exception
is the header of the method; the header changes to reflect the checked exceptions the method can throw.

```java
public FileInputStream(String name) throws FileNotFoundException
```

The declaration says that this constructor produces a FileInputStream object
from a String parameter but that it also can go wrong in a special way—by
throwing a FileNotFoundException. If this sad state should come to pass, the constructor 
call will not initialize a new FileInputStream object but instead will throw
an object of the FileNotFoundException class. If it does, the runtime system will
begin to search for an exception handler that knows how to deal with
FileNotFoundException objects.

To understand when
(and what) you have to advertise in the throws clause of the methods you write,
keep in mind that an exception is thrown in any of the following four
situations:

- You call a method that throws a checked exception—for example, the
  FileInputStream constructor.
- You detect an error and throw a checked exception with the throw statement.
- You make a programming error, such as a[-1] = 0 that gives rise to an
  unchecked exception.
- An internal error occurs in the virtual machine or runtime library.

If either of the first two scenarios occurs, you must tell the programmers who
will use your method about the possibility of an exception. Why? Any method
that throws an exception is a potential death trap. If no handler catches the
exception, the current thread of execution terminates.

```java
class MyAnimation {
    ...

    public Image loadImage(String s) throws IOException {
        . . .
    }
}

class MyAnimation {
    ...

    public Image loadImage(String s) throws FileNotFoundException, EOFException {
        . . .
    }
}
```

However, you do not need to advertise internal Java errors—that is, exceptions
inheriting from Error. Any code could potentially throw those exceptions, and
they are entirely beyond your control.

Similarly, you should not advertise unchecked exceptions inheriting from
RuntimeException.

In summary, a method must declare all the checked exceptions that it might
throw. Unchecked exceptions are either beyond your control (Error) or result
from conditions that you should not have allowed in the first place
(RuntimeException). If your method fails to faithfully declare all checked exceptions,
the compiler will issue an error message.

Of course, instead of declaring the exception, you can also catch it. Then the 
exception won’t be thrown out of the method, and no throws specification is 
necessary.

When a method in a class declares that it throws an exception that is an in-
stance of a particular class, it may throw an exception of that class or of any
of its subclasses.

## How to Throw an Exception

```java
String readData(Scanner in) throws EOFException {
    . . .
        
    while (. . .) {
        
        if (!in.hasNext()) // EOF encountered
        {
            if (n < len) throw new EOFException();
        }
        
        . . .
    }
        
    return s;
}
```

Throwing an exception is easy if one of the existing exception
classes works for you. In this case:

1. Find an appropriate exception class.
2. Make an object of that class.
3. Throw it

## Creating Exceptions Classes

Your code may run into a problem which is not adequately described by any
of the standard exception classes. In this case, it is easy enough to create
your own exception class. Just derive it from Exception, or from a child class of
Exception such as IOException. It is customary to give both a default constructor
and a constructor that contains a detailed message. (The toString method of
the Throwable superclass returns a string containing that detailed message, which
is handy for debugging.)

```java
class FileFormatException extends IOException {
    
    public FileFormatException() {}
    
    public FileFormatException(String gripe) {
        super(gripe);
    }
}
```

## Catching Exception

### Catching an Exception

If an exception occurs that is not caught anywhere, the program will terminate
and print a message to the console, giving the type of the exception and a
stack trace.
To catch an exception, set up a try/catch block.

If any code inside the try block throws an exception of the class specified in
the catch clause, then:

1. The program skips the remainder of the code in the try block.
2. The program executes the handler code inside the catch clause.

If none of the code inside the try block throws an exception, then the program
skips the catch clause.

If any of the code in a method throws an exception of a type other than the
one named in the catch clause, this method exits immediately.

```java
public void read(String filename) {
    try {
        var in = new FileInputStream(filename);
        int b;
        while ((b = in.read()) != -1) {
            process input
        }
    } catch (IOException exception) {
        exception.printStackTrace();
    }
}
```

Often, the best choice is to do nothing at all and simply pass the exception
on to the caller. If an error occurs in the read method, let the caller of the read
method worry about it! If we take that approach, then we have to advertise
the fact that the method may throw an IOException.

```java
public void read(String filename) throws IOException {
    var in = new FileInputStream(filename);
    int b;
    while ((b = in.read()) != -1) {
        process input
    }
}
```

As a general rule, you should catch those exceptions that you know how to handle and propagate those that you do not
know how to handle.

Remember, the compiler strictly enforces the throws specifiers. If you call a
method that throws a checked exception, you must either handle it or pass
it on.

There is one exception. If you are writing a method that overrides a superclass method
which throws no exceptions, then you must catch each checked exception in your 
method’s code. You are not allowed to add more throws specifiers to a subclass 
method than are present in the superclass method.

### Catching Multiple Exceptions

You can catch multiple exception types in a try block and handle each type
differently. Use a separate catch clause for each type, as in the following
example:

```java
try
{
    code that might throw exceptions
}
catch (FileNotFoundException e)
{
    emergency action for missing files
}
catch (UnknownHostException e)
{
    emergency action for unknown hosts
}
catch (IOException e)
{
    emergency action for all other I/O problems
}
```

As of Java 7, you can catch multiple exception types in the same catch clause.
For example, suppose that the action for missing files and unknown hosts is
the same. Then you can combine the catch clauses:

```java
try
{
    code that might throw exceptions
}
catch (FileNotFoundException | UnknownHostException e)
{
    emergency action for missing files and unknown hosts
}
catch (IOException e)
{
    emergency action for all other I/O problems
}
```

This feature is only needed when catching exception types that are not
subclasses of one another.

### Rethrowing and Chaining Exceptions

You can throw an exception in a catch clause. Typically, you do this when you
want to change the exception type. If you build a subsystem that other pro-
grammers use, it makes a lot of sense to use an exception type that indicates
a failure of the subsystem. An example of such an exception type is the
ServletException.

Here is how you can catch an exception and rethrow it:

```java
try
{
    access the database
}
catch (SQLException e)
{
    throw new ServletException("database error: " + e.getMessage());
}
```

However, it is a better idea to set the original exception as the “cause” of the
new exception:

```java
try
{
    access the database
}
catch (SQLException original)
{
    var e = new ServletException("database error");
    e.initCause(original);
    throw e;
}
```

Sometimes, you just want to log an exception and rethrow it without any
change:

```java
try
{
    access the database
}
catch (Exception e)
{
    logger.log(level, message, e);
    throw e;
}
```

### The Finally Clause

The code in the finally clause executes whether or not an exception was
caught. In the following example, the program will close the input stream
under all circumstances:

```java
var in = new FileInputStream(. . .);
try {
    // 1
    code that might throw exceptions
    // 2
} catch (IOException e) {
    // 3
    show error message
    // 4
} finally {
    // 5
    in.close();
}
// 6
```
Let us look at the three possible situations in which the program will execute
the finally clause.

1. The code throws no exceptions. In this case, the program first executes
   all the code in the try block. Then, it executes the code in the finally
   clause. Afterwards, execution continues with the first statement after the
   finally clause. In other words, execution passes through points 1, 2, 5,
   and 6.
2. The code throws an exception that is caught in a catch clause—in our case,
   an IOException. For this, the program executes all code in the try block, up
   to the point at which the exception was thrown. The remaining code in
   the try block is skipped. The program then executes the code in the
   matching catch clause, and then the code in the finally clause.

   If the catch clause does not throw an exception, the program executes the
   first line after the finally clause. In this scenario, execution passes through
   points 1, 3, 4, 5, and 6.

   If the catch clause throws an exception, then the exception is thrown back
   to the caller of this method, and execution passes through points 1, 3,
   and 5 only.
3. The code throws an exception that is not caught in any catch clause. Here,
   the program executes all code in the try block until the exception is
   thrown. The remaining code in the try block is skipped. Then, the code
   in the finally clause is executed, and the exception is thrown back to the
   caller of this method. Execution passes through points 1 and 5 only.

You can use the finally clause without a catch clause:

```java
InputStream in = . . .;
try {
    code that might throw exceptions
} finally {
    in.close();
}
```

### The Try-With-Resources Statement

As of Java 7, there is a useful shortcut to the code pattern:

```java

open a resource
try {
    work with the resource
} finally {
    close the resource
}
```

provided the resource belongs to a class that implements the AutoCloseable
interface. That interface has a single method

```java 
void close() throws Exception
```

In its simplest variant, the try-with-resources statement has the form

```java
try (Resource res = . . .) {
    work with res
}
```

When the try block exits, then res.close() is called automatically.

```java
try (var in = 
        new Scanner(new FileInputStream("/usr/share/dict/words"), 
        StandardCharsets.UTF_8)) 
{
    while (in.hasNext())    System.out.println(in.next());
}
```

When the block exits normally, or when there was an exception, the in.close()
method is called, exactly as if you had used a finally block.

You can specify multiple resources.

A difficulty arises when the try block throws an exception and the close method
also throws an exception. The try-with-resources statement handles this situ-
ation quite elegantly. The original exception is rethrown, and any exceptions
thrown by close methods are considered “suppressed.” They are automatically
caught and added to the original exception with the addSuppressed method. If
you are interested in them, call the getSuppressed method which yields an array
of the suppressed expressions from close methods.

### Analyzing Stack Traces Elements

A stack trace is a listing of all pending method calls at a particular point in
the execution of a program. You have almost certainly seen stack trace
listings—they are displayed whenever a Java program terminates with an
uncaught exception.

You can access the text description of a stack trace by calling the printStackTrace
method of the Throwable class.

```java
var t = new Throwable();
var out = new StringWriter();
t.printStackTrace(new PrintWriter(out));
String description = out.toString();
```

A more flexible approach is the StackWalker class that yields a stream of
StackWalker.StackFrame instances, each describing one stack frame. You can iterate
over the stack frames with this call:

```java
StackWalker walker = StackWalker.getInstance();
walker.forEach(frame -> analyze frame)
```

If you want to process the Stream<StackWalker.StackFrame> lazily, call

```java
walker.walk(stream -> process stream)
```

The StackWalker.StackFrame class has methods to obtain the file name and line
number, as well as the class object and method name, of the executing line of
code. The toString method yields a formatted string containing all of this
information.