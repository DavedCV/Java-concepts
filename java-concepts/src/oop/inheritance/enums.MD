# Enumerations

```java
public enum Size { SMALL, MEDIUM, LARGE, EXTRA_LARGE }
```

The type defined by this declaration is actually a class. The class has exactly
four instances—it is not possible to construct new objects.

Therefore, you never need to use equals for values of enumerated types. Simply
use == to compare them.

You can, if you like, add constructors, methods, and fields to an enumerated type. 
Of course, the constructors are only invoked when the enumerated constants are 
constructed. Here is an example:

```java
public enum Size {
    SMALL("S"), MEDIUM("M"), LARGE("L"), EXTRA_LARGE("XL");
    
    private String abbreviation;
    private Size(String abbreviation) { this.abbreviation = abbreviation; }
    public String getAbbreviation() { return abbreviation; }
}
```

The constructor of an enumeration is always private. You can omit the private
modifier, as in the preceding example.
