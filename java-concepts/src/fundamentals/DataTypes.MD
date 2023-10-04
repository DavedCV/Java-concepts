# Data Types

Java is a strongly typed language. This means that every variable must have a
declared type. There are eight primitive types in Java. 
- Four of them are integer types
- two are floating-point number types
- one is the character type char, used for code units in the Unicode encoding scheme
- one is a boolean type for truth values.

## Integer Types

- int:
  - 4 bytes
  - –2,147,483,648 to 2,147,483,647 (just over 2 billion)
- short:
  - 2 bytes
  - –32,768 to 32,767
- long:
  - 8 bytes
  - –9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
- byte:
  - 1 byte
  - –128 to 127
---
- Long integer numbers have a suffix L or l (for example, 4000000000L). 
- Hexadecimal numbers have a prefix 0x or 0X (for example, 0xCAFE). 
- Octal numbers have a prefix 0 (for example, 010 is 8)
  
## Floating Point Types

- float:
  - 4 bytes
  - Approximately ±3.40282347E+38F (6–7 significant decimal digits)
- double
  - 8 bytes
  - Approximately ±1.79769313486231570E+308 (15 significant decimal digits)

---

Numbers of type float have a suffix F or f (for example, 3.14F). Floating-point
numbers without an F suffix (such as 3.14) are always considered to be of type
double. You can optionally supply the D or d suffix (for example, 3.14D).

All floating-point computations follow the IEEE 754 specification. In particular,
there are three special floating-point values to denote overflows and errors:

- Positive infinity
- Negative infinity
- NaN (not a number)

```java
Double.POSITIVE_INFINITY;
Double.NEGATIVE_INFINITY;
Double.NaN;

// check NaN identity
Double x = Double.NaN;
Double.isNaN(x); // true
```

## Char Type

The char type was originally intended to describe individual characters. How-
ever, this is no longer the case. Nowadays, some Unicode characters can be
described with one char value, and other Unicode characters require two char
values.

Literal values of type char are enclosed in single quotes. For example, 'A' is a
character constant with value 65. It is different from "A", a string containing
a single character. Values of type char can be expressed as hexadecimal values
that run from \u0000 to \uFFFF.

## Boolean

The boolean type has two values, false and true. It is used for evaluating logical
conditions. You cannot convert between integers and boolean values.