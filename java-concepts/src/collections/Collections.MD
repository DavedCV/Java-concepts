# Collections

The data structures that you choose can make a big difference when you try
to implement methods in a natural style or are concerned with performance.
Do you need to search quickly through thousands (or even millions) of sorted
items? Do you need to rapidly insert and remove elements in the middle of
an ordered sequence? Do you need to establish associations between keys
and values?

This chapter shows how the Java library can help you accomplish the tradi-
tional data structuring needed for serious programming.

## The Java Collections Framework

### Separating Collection Interfaces and Implementation

As is common with modern data structure libraries, the Java collection library
separates **interfaces** and **implementations**. Let us look at that separation with a
familiar data structure, the queue.

A queue **interface** specifies that you can add elements at the tail end of the
queue, remove them at the head, and find out how many elements are in
the queue. You use a queue when you need to collect objects and retrieve
them in a “first in, first out” fashion.

The interface tells you nothing about how the queue is implemented. Of the
two common implementations of a queue, one uses a “circular array” and
one uses a linked list. Each implementation can be expressed by a class that implements the Queue
interface.

When you use a queue in your program, you don’t need to know which implementation is 
actually used once the collection has been constructed. Therefore, it makes sense 
to use the concrete class only when you construct the collection object. **Use the 
interface type to hold the collection reference**.

```java
// CircularArrayQueue is not a real class 
Queue<Customer> expressLane = new CircularArrayQueue<>(100);
expressLane.add(new Customer("Harry"));
```

With this approach, if you change your mind, you can easily use a different
implementation.

```java
// LinkedListQueue is not a real class
Queue<Customer> expressLane = new LinkedListQueue<>();
expressLane.add(new Customer("Harry"));
```

When you study the API documentation, you will find another set of classes
whose name begins with Abstract, such as AbstractQueue. These classes are
intended for library implementors. In the (perhaps unlikely) event that you
want to implement your own queue class, you will find it easier to extend
AbstractQueue than to implement all the methods of the Queue interface.

### The Collection Interface

The fundamental interface for collection classes in the Java library is the
Collection interface. The interface has two fundamental methods:

```java
public interface Collection<E> {
    boolean add(E element);
    Iterator<E> iterator();
    . . .
}
```

There are several methods in addition to these two; we will discuss them
later.

The add method adds an element to the collection. The add method returns
true if adding the element actually changes the collection, and false if the col-
lection is unchanged. For example, if you try to add an object to a set and
the object is already present, the add request has no effect because sets reject
duplicates.

The iterator method returns an object that implements the Iterator interface.
You can use the iterator object to visit the elements in the collection one by
one.

### Iterators

The Iterator interface has four methods:

```java
public interface Iterator<E> {
    E next();
    boolean hasNext();
    void remove();
    default void forEachRemaining(Consumer<? super E> action);
}
```

- By repeatedly calling the **next method**, you can visit the elements from the
  collection one by one. However, if you reach the end of the collection, the
  next method throws a NoSuchElementException.
- You need to call the **hasNext method** before calling next. That method returns true if the iterator object still
  has more elements to visit.
- Instead of writing a loop, you can call the forEachRemaining method with a
  lambda expression that consumes an element. The lambda expression is
  invoked with each element of the iterator, until there are none left.

    ```java
    iterator.forEachRemaining(element -> do something with element);
    ```
- The remove method of the Iterator interface removes the element that was re-
  turned by the last call to next. In many situations, that makes sense—you need
  to see the element before you can decide that it is the one that should be
  removed. But if you want to remove an element in a particular position, you

  More importantly, there is a dependency between the calls to the next and
  remove methods. It is illegal to call remove if it wasn’t preceded by a call to next.  

---

The compiler simply translates the “for each” loop into a loop with an iterator.

The “for each” loop works with any object that implements the Iterable
interface, an interface with a single abstract method:

```java
public interface Iterable<E> {
    Iterator<E> iterator();
    . . .
}
```

The Collection interface extends the Iterable interface. Therefore, you can use
the “for each” loop with any collection in the standard library

---

### Generic Utility Methods

The Collection and Iterator interfaces are generic, which means you can write
utility methods that operate on any kind of collection. For example, here is
a generic method that tests whether an arbitrary collection contains a given
element:

```java
public static <E> boolean contains(Collection<E> c, Object obj) {
    for (E element : c)
        if (element.equals(obj))
            return true;
    return false;
}
```

The designers of the Java library decided that some of these utility methods
are so useful that the library should make them available. That way, library
users don’t have to keep reinventing the wheel. The contains method is one
such method.

In fact, the Collection interface declares quite a few useful methods that all
implementing classes must supply. Among them are

```java
int size()
boolean isEmpty()
boolean contains(Object obj)
boolean containsAll(Collection<?> c)
boolean equals(Object other)
boolean addAll(Collection<? extends E> from)
boolean remove(Object obj)
boolean removeAll(Collection<?> c)
void clear()
boolean retainAll(Collection<?> c)
Object[] toArray()
<T> T[] toArray(T[] arrayToFill)
```

Of course, it is a bother if every class that implements the Collection interface
has to supply so many routine methods. To make life easier for implementors,
the library supplies a class AbstractCollection that leaves the fundamental methods
size and iterator abstract but implements the routine methods in terms of them.

A concrete collection class can now extend the AbstractCollection class. It is up
to the concrete collection class to supply an iterator method, but the contains
method has been taken care of by the AbstractCollection superclass. However,
if the subclass has a more efficient way of implementing contains, it is free to
do so.

## Interfaces in the Collections Framework

The Java collections framework defines a number of interfaces for different
types of collections

![](interfacesCollectionFramework.png)

- There are two fundamental interfaces for collections: Collection and Map. As you
  already saw, you insert elements into a collection with a method 

    ```java
    boolean add (E element)
    ```
    
    However, maps hold key/value pairs, and you use the put method to
    insert them:
    
    ```java
    V put(K key, V value)
    ```
    
    To read elements from a collection, visit them with an iterator. However, you
    can read values from a map with the get method:
    
    ```java
    V get(K key)
    ```

- A List is an **ordered collection**. Elements are added into a particular position in
  the container. An element can be accessed in two ways: by an iterator or by
  an integer index. The latter is called random access because elements can be
  visited in any order. In contrast, when using an iterator, one must visit them
  sequentially.

    The List interface defines several methods for random access:
    
    ```java
    void add(int index, E element)
    void remove(int index)
    E get(int index)
    E set(int index, E element)
    ```
    
    The ListIterator interface is a subinterface of Iterator. It defines a method for
    adding an element before the iterator position:

    ```java
    void add(E element)
    ```

    Frankly, this aspect of the collections framework is poorly designed. In practice,
    there are two kinds of ordered collections, with very different performance
    tradeoffs. An ordered collection that is backed by an array has fast random
    access, and it makes sense to use the List methods with an integer index. In
    contrast, a linked list, while also ordered, has slow random access, and it is
    best traversed with an iterator. It would have been an easy matter to provide
    two interfaces.


- The **Set interface** is identical to the Collection interface, but the behavior of the
  methods is more tightly defined. The add method of a set should reject duplicates. The equals method of a set should be defined so that two sets are
  identical if they have the same elements, but not necessarily in the same order.
  The hashCode method should be defined so that two sets with the same elements
  yield the same hash code.

    Why make a separate interface if the method signatures are the same?
    Conceptually, not all collections are sets. Making a Set interface enables
    programmers to write methods that accept only sets.

- The SortedSet and SortedMap interfaces expose the comparator object used for
  sorting, and they define methods to obtain views of subsets of the collections.
- Finally, Java 6 introduced interfaces NavigableSet and NavigableMap that contain
  additional methods for searching and traversal in sorted sets and maps.

## Concrete Collections

All classes in the image implement the Collection interface, with the exception of the classes with names
ending in Map. Those classes implement the Map interface instead.

![](concreteClasses.png)
![](concreteClassesRelationships.png)

### Linked Lists

Arrays and array lists suffer from a major
drawback. Removing an element from the middle of an array is expensive
since all array elements beyond the removed one must be moved toward the
beginning of the array. The same is true for inserting elements
in the middle.

Another well-known data structure, the linked list, solves this problem. Where
an array stores object references in consecutive memory locations, a linked
list stores each object in a separate link. Each link also stores a reference to
the next link in the sequence. In the Java programming language, all linked
lists are actually **doubly linked**; that is, each link also stores a reference to its
predecessor.

```java
var staff = new LinkedList<String>();
staff.add("Amy");
staff.add("Bob");
staff.add("Carl");
Iterator<String> iter = staff.iterator();
String first = iter.next(); // visit first element
String second = iter.next(); // visit second element
iter.remove(); // remove last visited element
```

There is, however, an important difference between linked lists and generic
collections. A linked list is an ordered collection in which the position of the
objects matters. The LinkedList.add method adds the object to the end of the list.

But you will often want to add objects somewhere in the middle of a list.
This position-dependent add method is the responsibility of an iterator, since
iterators describe positions in collections. Using iterators to add elements
makes sense only for collections that have a natural ordering. There is no add method in the Iterator inter-
face. Instead, the collections library supplies a subinterface ListIterator that
contains an add method:

```java
interface ListIterator<E> extends Iterator<E> {
    void add(E element);
    . . .
}
```

In addition, the ListIterator interface has two methods that you can use for
traversing a list backwards.

```java
E previous()
boolean hasPrevious()
```

Like the next method, the previous method returns the object that it skipped over.

The listIterator method of the LinkedList class returns an iterator object that
implements the ListIterator interface.

The add method adds the new element before the iterator position.

```java
var staff = new LinkedList<String>();
staff.add("Amy");
staff.add("Bob");
staff.add("Carl");
ListIterator<String> iter = staff.listIterator();
iter.next(); // skip past first element
iter.add("Juliet");
```

Finally, a set method replaces the last element, returned by a call to next or
previous, with a new element.

```java
ListIterator<String> iter = list.listIterator();
String oldValue = iter.next(); // returns first element
iter.set(newValue); // sets first element to newValue
```

As you might imagine, if an iterator traverses a collection while another iterator is modifying it, confusing situations can occur. For example, suppose an
iterator points before an element that another iterator has just removed.
The iterator is now invalid and should no longer be used. The linked list iterators have been designed to detect such modifications. If an iterator finds
that its collection has been modified by another iterator or by a method
of the collection itself, it throws a ConcurrentModificationException.

```java
List<String> list = . . .;
ListIterator<String> iter1 = list.listIterator();
ListIterator<String> iter2 = list.listIterator();
iter1.next();
iter1.remove();
iter2.next(); // throws ConcurrentModificationException
```

To avoid concurrent modification exceptions, follow this simple rule: You
can attach as many iterators to a collection as you like, provided that all of
them are only readers. Alternatively, you can attach a single iterator that can
both read and write.

Many other useful methods for operating on linked lists are declared
in the Collection interface. These are, for the most part, implemented in the
AbstractCollection superclass of the LinkedList class.

We recommend that you simply stay away from all methods that use an integer
index to denote a position in a linked list. If you want random access into a
collection, use an array or ArrayList, not a linked list.

### Array Lists

The List interface describes an ordered collection in which the
position of elements matters. There are two protocols for visiting the elements:
through an iterator and by random access with methods get and set. The latter
is not appropriate for linked lists, but of course get and set make a lot of sense
for arrays.

The collections library supplies the familiar ArrayList class that
also implements the List interface. An ArrayList encapsulates a dynamically
reallocated array of objects.

### Hash Sets

#### Hash Table context

If you don’t care about the ordering of the elements, there are data 
structures that let you find elements much faster. The drawback is that 
those data structures give you no control over the order in which the 
elements appear.

A well-known data structure for finding objects quickly is the **hash table**. A
hash table computes an integer, called the hash code, for each object. A hash code is somehow derived from the 
instance fields of an object, 
preferably in such a way that objects with different data yield different 
codes.

If you define your own classes, you are responsible for implementing your
own hashCode method. Your implementation needs to be compatible with the 
equals method: If a.equals(b), then a and b must have the same hash code.

In Java, hash tables are implemented as arrays of linked lists. Each list is
called a bucket. . If the hash codes are reasonably randomly distributed and the
number of buckets is large enough, only a few comparisons should be
necessary to find an object.

> As of Java 8, the buckets change from linked lists into balanced binary
trees when they get full. This improves performance if a hash function was
poorly chosen and yields many collisions, or if malicious code tries to flood a
hash table with many values that have identical hash codes.

If you want more control over the performance of the hash table, you can
specify the initial bucket count. The bucket count gives the number of buckets
used to collect objects with identical hash values. If too many elements are
inserted into a hash table, the number of collisions increases and retrieval
performance suffers.

If you know how many elements, approximately, will eventually be in the
table, you can set the bucket count. Typically, you should set it to somewhere
between 75% and 150% of the expected element count.

Of course, you do not always know how many elements you need to store,
or your initial guess may be too low. If the hash table gets too full, it needs
to be rehashed. To rehash the table, a table with more buckets is created, all
elements are inserted into the new table, and the original table is discarded.
The load factor determines when a hash table is rehashed. For example, if the
load factor is 0.75 (which is the default) and the table is more than 75% full, it
is automatically rehashed with twice as many buckets. For most applications,
it is reasonable to leave the load factor at 0.75.

#### HashSet

Hash tables can be used to implement several important data structures. The
simplest among them is the **set type**. A set is a collection of elements without
duplicates. The add method of a set first tries to find the object to be added,
and adds it only if it is not yet present.

The Java collections library supplies a **HashSet class** that implements a set based
on a hash table. You add elements with the add method. The contains method
is redefined to make a fast lookup to see if an element is already present in
the set. It checks only the elements in one bucket and not all elements in the
collection.

The hash set iterator visits all buckets in turn. Since hashing scatters the elements around in the table, they are visited in a seemingly random order. You
would only use a HashSet if you don’t care about the ordering of the elements
in the collection.

### TreeSet

The TreeSet class is similar to the hash set, with one added improvement. A
tree set is a sorted collection.
You insert elements into the collection in any
order. When you iterate through the collection, the values are automatically
presented in sorted order.

As the name of the
class suggests, the sorting is accomplished by a tree data structure. Every time an element is added to a tree, it is placed into its proper sorting
position. Therefore, the iterator always visits the elements in sorted order.

Adding an element to a tree is slower than adding it to a hash table. But it is still much faster than checking 
for duplicates in an array or linked list. If the tree contains n elements, then an average
of log2 n comparisons are required to find the correct position for the new
element.

> In order to use a tree set, you must be able to compare the elements.
The elements must implement the Comparable interface, or you must supply a
Comparator when constructing the set.

Any two elements must be
comparable, and the comparison can only be zero if the elements are equal.

> As of Java 6, the TreeSet class implements the NavigableSet interface. That
interface adds several convenient methods for locating elements and for
backward traversal.

### Queues and Deques

A queue lets you efficiently add elements at the tail
and remove elements from the head. A double-ended queue, or deque, lets
you efficiently add or remove elements at the head and tail.

Adding elements
in the middle is not supported. Java 6 introduced a Deque interface. It is 
implemented by the ArrayDeque and LinkedList classes, both of which provide 
deques whose size grows as needed.

### Priority Queues

A priority queue retrieves elements in sorted order after they were inserted
in arbitrary order. That is, whenever you call the remove method, you get the
smallest element currently in the priority queue.

Just like a TreeSet, a priority queue can either hold elements of a class that
implements the Comparable interface or a Comparator object you supply in the
constructor.

A typical use for a priority queue is job scheduling. Each job has a priority.
Jobs are added in random order. Whenever a new job can be started, the
highest priority job is removed from the queue. (Since it is traditional for
priority 1 to be the “highest” priority, the remove operation yields the minimum
element.)

## Maps

