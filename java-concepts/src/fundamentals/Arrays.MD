# Arrays

Arrays hold sequences of values of the same type.

## Declaring Arrays

An array is a data structure that stores a collection of values of the same type.
You access each individual value through an integer index. For example, if a
is an array of integers, then a[i] is the ith integer in the array.

Declare an array variable by specifying the array type—which is the element
type followed by []—and the array variable name. Use the new operator to create the array.

```java
int[] a = new int[100]; // or var a = new int[100];
```

Java has a shortcut for creating an array object and supplying initial values:

```java
int[] smallPrimes = { 2, 3, 5, 7, 11, 13 };
```

You can declare an anonymous array. You can use this syntax to reinitialize an array without creating a new
variable:

```java
new int[] { 17, 19, 23, 29, 31, 37 }

// shorthand
smallPrimes = new int[] { 17, 19, 23, 29, 31, 37 };

// for this
int[] anonymous = { 17, 19, 23, 29, 31, 37 };
smallPrimes = anonymous;
```

## Array copying

You can copy one array variable into another, but then both variables refer to
the same array:

```java
int[] luckyNumbers = smallPrimes;
luckyNumbers[5] = 12; // now smallPrimes[5] is also 12
```

If you actually want to copy all values of one
array into a new array, use the copyOf method in the Arrays class:

```java
int[] copiedLuckyNumbers = Arrays.copyOf(luckyNumbers, luckyNumbers.length);
```

## Multidimensional arrays

Multidimensional arrays use more than one index to access array elements.
They are used for tables and other more complex arrangements.

If you know the array elements, you can use a shorthand
notation for initializing a multidimensional array without a call to new. For
example:

```java
int[][] magicSquare = 
    {
        {16, 3, 2, 13},
        {5, 10, 11, 8},
        {9, 6, 7, 12},
        {4, 15, 14, 1}
    };
```

Once the array is initialized, you can access individual elements by supplying
two pairs of brackets.

To print out a quick-and-dirty list of the elements of a two-dimensional
array, call

```java
Arrays.deepToString(a)
```

## Ragged Arrays

So far, what you have seen is not too different from other programming lan-
guages. But there is actually something subtle going on behind the scenes
that you can sometimes turn to your advantage: Java has no multidimensional
arrays at all, only one-dimensional arrays. Multidimensional arrays are faked
as “arrays of arrays.”

It is also easy to make “ragged” arrays—that is, arrays in which different rows
have different lengths.

To build a ragged array, first allocate the array holding the rows. Next, allocate the rows.