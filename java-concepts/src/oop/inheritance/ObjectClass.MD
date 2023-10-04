# Object

The Object class is the ultimate ancestor—every class in Java extends Object.

The ultimate superclass Object is taken for granted if no superclass is explicitly
mentioned. Since every class in Java extends Object, it is important to be familiar
with the services provided by the Object class.

## Variables of Type Object

You can use a variable of type Object to refer to objects of any type:

```java
Object obj = new Employee("Harry Hacker", 35000);
```

Of course, a variable of type Object is only useful as a generic holder for arbitrary 
values. To do anything specific with the value, you need to have some knowledge about 
the original type and apply a cast.

## equals method

The equals method in the Object class tests whether one object is considered
equal to another. The equals method, as implemented in the Object class, deter-
mines whether two object references are identical.

However, you will often want
to implement state-based equality testing, in which two objects are considered
equal when they have the same state.

For example, let us consider two employees equal if they have the same name,
salary, and hire date.

```java
public class Employee {

    public boolean equals(Object otherObject) {
        // a quick test to see if the objects are identical
        if (this == otherObject) return true;

        // must return false if the explicit parameter is null
        if (otherObject == null) return false;

        // if the classes don't match, they can't be equal
        if (getClass() != otherObject.getClass())
            return false;

        // now we know otherObject is a non-null Employee
        Employee other = (Employee) otherObject;

        // test whether the fields have identical values
        /* The call Objects.equals(a, b) returns true if both arguments
         * are null, false if only one is null, and calls a.equals(b) otherwise.
         * */
        return Objects.equals(name, other.name)
                && salary == other.salary
                && Objects.equals(hireDay, other.hireDay);
    }
}
```

When you define the equals method for a subclass, first call equals on the
superclass. If that test doesn’t pass, then the objects can’t be equal. If the su-
perclass fields are equal, you are ready to compare the instance fields of the
subclass.

```java
public class Manager extends Employee {
    
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
    
        // super.equals checked that this and otherObject belong to the same class
        Manager other = (Manager) otherObject;
        return bonus == other.bonus;
    }
}
```

## Equality testing and inheritance

How should the equals method behave if the implicit and explicit parameters
don’t belong to the same class? This has been an area of some controversy.
In the preceding example, the equals method returns false if the classes don’t
match exactly. But many programmers use an instanceof test instead:

```java
if (!(otherObject instanceof Employee)) return false;
```

This leaves open the possibility that otherObject can belong to a subclass.
However, this approach can get you into trouble. Here is why. The Java
Language Specification requires that the equals method has the following
properties:

1. It is reflexive: For any non-null reference x, x.equals(x) should return true.
2. It is symmetric: For any references x and y, x.equals(y) should return true if
   and only if y.equals(x) returns true.
3. It is transitive: For any references x, y, and z, if x.equals(y) returns true and
   y.equals(z) returns true, then x.equals(z) should return true.
4. It is consistent: If the objects to which x and y refer haven’t changed, then
   repeated calls to x.equals(y) return the same value.
5. For any non-null reference x, x.equals(null) should return false.

However, the symmetry rule has subtle consequences when the parameters
belong to different classes. Consider a call:

```java
e.equals(m)
```

where e is an Employee object and m is a Manager object, both of which happen to
have the same name, salary, and hire date. If Employee.equals uses an instanceof
test, the call returns true. But that means that the reverse call

```java
m.equals(e)
```

also needs to return true—the symmetry rule does not allow it to return false
or to throw an exception.

That leaves the Manager class in a bind. Its equals method must be willing to
compare itself to any Employee, without taking manager-specific information
into account! All of a sudden, the instanceof test looks less attractive.

The way we see it, there are two distinct scenarios:

- If subclasses can have their own notion of equality, then the symmetry
  requirement forces you to use the getClass test.
- If the notion of equality is fixed in the superclass, then you can use the
  instanceof test and allow objects of different subclasses to be equal to one
  another.

## Recipe for writing perfect equals method

1. Name the explicit parameter otherObject—later, you will need to cast it to
   another variable that you should call other.
2. Test whether this happens to be identical to otherObject:

    ```java
    if (this == otherObject) return true;
    ```

    This statement is just an optimization. In practice, this is a common case.
    It is much cheaper to check for identity than to compare the fields.
3. Test whether otherObject is null and return false if it is. This test is required.
    ```java
    if (otherObject == null) return false;
    ```
4. Compare the classes of this and otherObject. If the semantics of equals can
   change in subclasses, use the getClass test:

    ```java
    if (getClass() != otherObject.getClass()) return false;
    ```

   If the same semantics holds for all subclasses, you can use an instanceof
   test:

    ```java
    if (!(otherObject instanceof ClassName)) return false;
    ``` 
5. Cast otherObject to a variable of your class type:

    ```java
    ClassName other = (ClassName) otherObject
    ```
6. Now compare the fields, as required by your notion of equality. Use ==
   for primitive type fields, Objects.equals for object fields. Return true if all
   fields match, false otherwise.

    ```java
    return field1 == other.field1
    && Objects.equals(field2, other.field2)
    && . . .;
    ```
7. If you redefine equals in a subclass, include a call to super.equals(other).
8. If you have fields of array type, you can use the static Arrays.equals method
   to check that the corresponding array elements are equal.

## hashCode method

A hash code is an integer that is derived from an object. Hash codes should
be scrambled—if x and y are two distinct objects, there should be a high
probability that x.hashCode() and y.hashCode() are different.

The hashCode method is defined in the Object class. Therefore, every object has
a default hash code. That hash code is derived from the object’s memory
address.

The hashCode method should return an integer (which can be negative). Just
combine the hash codes of the instance fields so that the hash codes for
different objects are likely to be widely scattered.

For example, here is a hashCode method for the Employee class:

```java
public class Employee {
    public int hashCode() {
        return 7 * name.hashCode()
        + 11 * new Double(salary).hashCode()
        + 13 * hireDay.hashCode();
    }
}
```

However, you can do better. First, use the null-safe method Objects.hashCode. It
returns 0 if its argument is null and the result of calling hashCode on the argument
otherwise. Also, use the static Double.hashCode method to avoid creating a Double
object:

```java
public int hashCode() {
    return 7 * Objects.hashCode(name)
    + 11 * Double.hashCode(salary)
    + 13 * Objects.hashCode(hireDay);
}
```

Even better, when you need to combine multiple hash values, call Objects.hash
with all of them. It will call Objects.hashCode for each argument and combine the
values. Then the Employee.hashCode method is simply

```java
public int hashCode() {
    return Objects.hash(name, salary, hireDay);
}
```

Your definitions of equals and hashCode must be compatible: If x.equals(y) is true,
then x.hashCode() must return the same value as y.hashCode(). For example, if you
define Employee.equals to compare employee IDs, then the hashCode method needs
to hash the IDs, not employee names or memory addresses.

> If you have fields of an array type, you can use the static Arrays.hashCode
method to compute a hash code composed of the hash codes of the array
elements.

## toString method

Another important method in Object is the toString method that returns a string
representing the value of this object.

The toString method is ubiquitous for an important reason: Whenever an object
is concatenated with a string by the “+” operator, the Java compiler automatically 
invokes the toString method to obtain a string representation of the object.

```java
public String toString() {
    return getClass().getName()
    + "[name=" + name
    + ",salary=" + salary
    + ",hireDay=" + hireDay
    + "]";
}
```