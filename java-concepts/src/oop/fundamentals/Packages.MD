# Packages

Java allows you to group classes in a collection called a package. Packages are
convenient for organizing your work and for separating your work from code libraries provided by others.

## Packages names

The main reason for using packages is to guarantee the uniqueness of class
names. Suppose two programmers come up with the bright idea of supplying
an Employee class. As long as both of them place their class into different
packages, there is no conflict.

In fact, to absolutely guarantee a unique package
name, use an Internet domain name (which is known to be unique) written
in reverse.

## Class Importation

A class can use all classes from its own package and all public classes from
other packages.

You can access the public classes in another package in two ways. The first
is simply to use the fully qualified name; that is, the package name followed
by the class name. For example:

`java.time.LocalDate today = java.time.LocalDate.now();`

A simpler, and more common, approach is to use
the import statement. The point of the import statement is to give you a shorthand
to refer to the classes in the package. Once you add an import, you no longer
have to give the classes their full names

You can import a specific class or the whole package. You place import state-
ments at the top of your source files (but below any package statements).

`import java.time.*;`
`import java.time.LocalDate;

The java.time.* syntax is less tedious. It has no negative effect on code size.
However, if you import classes explicitly, the reader of your code knows
exactly which classes you use.

However, note that you can only use the * notation to import a single package.
You cannot use import java.* or import java.\*.\* to import all packages with the
java prefix.

## Static Imports

A form of the import statement permits the importing of static methods and
fields, not just classes.

For example, if you add the directive
    
`import static java.lang.System.*;`

to the top of your source file, then you can use the static methods and fields
of the System class without the class name prefix:

```java 
out.println("Goodbye, World!"); // i.e., System.out
exit(0); // i.e., System.exit
```

## Addition of a class into a package

To place classes inside a package, put the name of the package at the top of
your source file, before the code that defines the classes in the package.

```java
package com.horstmann.corejava;
public class Employee {
    . . .
}
```

If you don’t put a package statement in the source file, then the classes in that
source file belong to the unnamed package. The unnamed package has no
package name.

Place source files into a subdirectory that matches the full package name. For
example, all source files in the com.horstmann.corejava package should be in a
subdirectory com/horstmann/corejava (com\horstmann\corejava on Windows). The compiler
places the class files into the same directory structure.

To compile a program, simply change to the base directory and run the command with the full
package name like this.

```java
javac PackageTest.java
```

## Package access

You have already encountered the access modifiers public and private. Features
tagged as public can be used by any class. Private features can be used only
by the class that defines them. If you don’t specify either public or private, the
feature (that is, the class, method, or variable) can be accessed by all methods
in the same package.

## The class path

To share classes among programs, you need to do the following:

1. Place your class files inside a directory—for example, /home/user/classdir.
   Note that this directory is the base directory for the package tree. If you
   add the class com.horstmann.corejava.Employee, then the Employee.class file must
   be located in the subdirectory /home/user/classdir/com/horstmann/corejava.
2. Place any JAR files inside a directory—for example, /home/user/archives.
3. Set the class path. The class path is the collection of all locations that can
   contain class files.

In UNIX, the elements on the class path are separated by colons: `/home/user/classdir:.:/home/user/archives/archive.
jar` the period denotes the current directory.

The class path lists all directories and archive files that are starting points for

Suppose the virtual machine searches for the class file of the com.horstmann
.corejava.Employee class. It first looks in the Java API classes. It won’t find the
class file there, so it turns to the class path. It then looks for the following
files:

- /home/user/classdir/com/horstmann/corejava/Employee.class
- com/horstmann/corejava/Employee.class starting from the current directory
- com/horstmann/corejava/Employee.class inside /home/user/archives/archive.jar