# Big Numbers

If the precision of the basic integer and floating-point types is not sufficient,
you can turn to a couple of handy classes in the java.math package: BigInteger
and BigDecimal. These are classes for manipulating numbers with an arbitrarily
long sequence of digits. The BigInteger class implements arbitrary-precision
integer arithmetic, and BigDecimal does the same for floating-point numbers.

Use the static valueOf method to turn an ordinary number into a big number:

```java
BigInteger a = BigInteger.valueOf(100);
```

For longer numbers, use a constructor with a string parameter:

```java
BigInteger reallyBig
= new BigInteger("222232244629420445529739893461909967206666939096499764990979600");
```

Unfortunately, you cannot use the familiar mathematical operators such as +
and * to combine big numbers. Instead, you must use methods such as add
and multiply in the big number classes.

```java
BigInteger c = a.add(b); // c = a + b
BigInteger d = c.multiply(b.add(BigInteger.valueOf(2))); // d = c * (b + 2)
```