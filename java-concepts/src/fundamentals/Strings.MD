# Strings

Conceptually, Java strings are sequences of Unicode characters. Java does not have a built-in string type. Instead, the standard Java library
contains a predefined class called, naturally enough, String. Each quoted string
is an instance of the String class.

## Substrings

You can extract a substring from a larger string with the substring method of
the String class.

```java
String greetings = "Hello";
String s = greeting.substring(0 , 3);
```

There is one advantage to the way substring works: Computing the length of
the substring is easy. The string s.substring(a, b) always has length b − a. For
example, the substring "Hel" has length 3 − 0 = 3.

## Concatenation

Java, like most programming languages, allows you to use + to join
(concatenate) two strings.

When you concatenate a string with a value that is not a string, the latter is
converted to a string.

```java
int age = 13;
String rating = "PG" + age;
```

## Strings are immutable

The String class gives no methods that let you change a character in an existing
string.

How are we going to modify the string? In Java,
it is quite easy: Concatenate the substring that you want to keep with the
characters that you want to replace.

`greeting = greeting.substring(0, 3) + "p!";`

## Testing strings for equality

To test whether two strings are equal, use the equals method. The expression

`s.equals(t);`

returns true if the strings s and t are equal, false otherwise. Note that s and t
can be string variables or string literals. To test whether two strings are identical except for the
upper/lowercase letter distinction, use the equalsIgnoreCase method.

`"Hello".equalsIgnoreCase("hello")`

Do not use the == operator to test whether two strings are equal! It only deter-
mines whether or not the strings are stored in the same location.

## Empty and Null strings

Sometimes, you need to test that a string is neither null nor empty. Then use:

`if (str != null && str.length() != 0) ...`

## Code Points and Code Units

Java strings are sequences of char values. The char data type is a code unit for 
representing Unicode code points in the UTF-16 encoding. The most commonly
used Unicode characters can be represented with a single code unit. The
supplementary characters require a pair of code units.

The length method yields the number of code units required for a given string
in the UTF-16 encoding. For example:

```java
String greeting = "Hello";
int n = greeting.length(); // is 5
```

To get the true length—that is, the number of code points—call:

```java
int cpCount = greeting.codePointCount(0, greeting.length());
```

The call s.charAt(n) returns the code unit at position n, where n is between 0
and s.length() – 1. For example:

```java
char first = greeting.charAt(0); // first is 'H'
char last = greeting.charAt(4); // last is 'o'
```

To get at the ith code point, use the statements:

```java
int index = greeting.offsetByCodePoints(0, i);
int cp = greeting.codePointAt(index);
```

An easier way is to use the codePoints method that yields a “stream” of int values, one for each code point. You 
can just turn the stream into an array and traverse that.

```java
int[] codePoints = str.codePoints().toArray();
```

## Building Strings

Occasionally, you need to build up strings from shorter strings, such as
keystrokes or words from a file. It would be inefficient to use string concatenation 
for this purpose. Every time you concatenate strings, a new String object
is constructed. This is time consuming and wastes memory. Using the
StringBuilder class avoids this problem.

Follow these steps if you need to build a string from many small pieces. First,
construct an empty string builder:

```java 
StringBuilder builder = new StringBuilder();
```

Each time you need to add another part, call the append method.

```java
builder.append(ch); // appends a single character
builder.append(str); // appends a string
```

When you are done building the string, call the toString method. You will get
a String object with the character sequence contained in the builder.

```java
String completedString = builder.toString();
```

> The StringBuilder class was introduced in Java 5. Its predecessor,
> StringBuffer, is slightly less efficient, but it allows multiple threads to add or re-
> move characters. If all string editing happens in a single thread (which is usually
> the case), you should use StringBuilder instead. The APIs of both classes are
> identical.