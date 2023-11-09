# Implementing data persistence with Spring Data

In this chapter, you’ll learn to use Spring Data, a Spring ecosystem project that gives you the possibility of implementing a Spring app’s persistence layer with minimum effort. As you already know, an application framework’s essential role is providing out-of-the-box capabilities that you can directly plug into apps.Frameworks help us save time and also make apps’ design easier to understand.

You’ll learn to create the app’s repositories by declaring interfaces. You’ll let the framework provide implementations for these interfaces. You’ll literally enable your app to work with a database without implementing the repository yourself and with minimum effort.

## What Spring Data is

Spring Data is a Spring ecosystem project that simplifies the persistence layer’s development by providing implementations according to the persistence technology we use. This way, we only need to write a few lines of code to define the repositories of our Spring app.

![](/images/springData.png)

Let’s see where Spring Data fits in a Spring app. In an app, you have various technologies you can use to work with persisted data. We have used JDBC, which directly connects to a relational DBMS through a driver manager. But JDBC isn’t the only approach you can use to connect to a relational database. Another common way to implement data persistence is using an ORM framework, such as Hibernate. And relational databases aren’t the only kind of persisting data technologies. An app might use one of the various NoSQL technologies out there to persist data.

Having so many ways to implement the app’s persistence capabilities adds complexity.

The reason I made you aware of all these is to demonstrate why Spring Data is so valuable. You might have already asked yourself, “Is there a way we can implement the persistence for all these technologies instead of having to know different approaches for each?” The answer is yes, and Spring Data helps us achieve this goal.

Spring Data simplifies the implementation of the persistence layer by doing the following:

- Providing a common set of abstractions (interfaces) for various persistence technologies. This way, you use a similar approach for implementing the persistence for different technologies.
- Allowing the user to implement the persistence operations using only the abstractions, for which Spring Data provides the implementations. This way, you write less code, so you more quickly implement the app’s capabilities. Withless written code, the app also becomes easier to understand and maintain.

![](/images/springData2.png)

## How Spring Data works

When developers use the term “Spring Data,” they refer in general to all the capabilities this project provides to your Spring app to connect to one persistence technology or another. In an app, generally you use a specific technology: JDBC, Hibernate, MongoDB, or another technology.

The Spring Data project offers different modules for one technology or another. These modules are independent of one another, and you can add them to your project using different Maven dependencies. So, when you implement an app, you don’t use the Spring Data dependency. There is no such thing as one Spring Data dependency. The Spring Data project provides one Maven dependency for each persistence fashion it supports. For example, you can use the Spring Data JDBC module to connect to the DMBS directly through JDBC, or use the Spring Data Mongo module to connect to a MongoDB database.

Whichever persistence technology your app uses, Spring Data provides a common set of interfaces (contracts) you extend to define the app’s persistence capabilities.

- **Repository** is the most abstract contract. If you extend this contract, your app recognizes the interface you write as a particular Spring Data repository. Still, you won’t inherit any predefined operations (such as adding a new record, retrieving all the records, or getting a record by its primary key). The Repository interface doesn’t declare any method (it is a marker interface).
- **CrudRepository** is the simplest Spring Data contract that also provides some persistence capabilities. If you extend this contract to define your app’s persistence capabilities, you get the simplest operations for creating, retrieving, updating, and deleting records.
- **PagingAndSortingRepository** extends CrudRepository and adds operations related to sorting the records or retrieving them in chunks of a specific number (pages).

Why not only one interface with all the operations in it? By implementing multiple contracts that extend each other instead of providing you one “fat” contract with all the operations, Spring Data gives your app the possibility to implement only the operations it needs. This approach is a known principle called interface segregation.

![](/images/springDataInterfaces.png)

Some Spring Data modules might provide specific contracts to the technology they represent.

## Using Spring Data JDBC

In this section, we use Spring Data JDBC to implement the persistence layer of a Spring app. We discussed that all you need to do is extend a Spring Data contract, but let’s see it in action. In addition to implementing a plain repository, you’ll also learn how to create and use custom repository operations.

The application we build is an electronic wallet managing its users’ accounts. A user can transfer money from their account to another account. In this tutorial, we implement the money transfer use case to allow the user to send money from one account to another. The money transfer operation has two steps

1. Withdraw a given amount from the sender’s account.
2. Deposit the amount in the destination account.

The account table has the following fields:

- **id:** The primary key. We define this field as an INT value that self increments.
- **name:** The name of the account’s owner
- **amount:** The amount of money the owner has in the account

Let’s model the account table records with a class named Account. We use a field to map each column in the table with the proper type.

For several operations it provides, such as retrieving data from the database, Spring Data needs to know which field maps the table’s primary key. You use the @Id annotation to mark the primary key.

```java
public class Account {
    @Id
    private int id;
    private String name;
    private BigDecimal amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
```

Now that you have a model class, we can implement the Spring Data repository. We only need CRUD operations for this application, so we’ll write an interface that extends the CrudRepository interface. All the Spring Data interfaces have two generic types you need to provide:

- The model class (sometimes named entity) for which you write the repository
- The primary key field type

```java
// The first generic type value is the type of
// the model class representing the table. The
// second is the type of the primary key field.
public interface AccountRepository extends CrudRepository<Account, int> {
}
```

When you extend the CrudRepository interface, Spring Data provides simple operations like getting a value by its primary key, getting all the records from the table, deleting records, and so on. But it can’t give you all the possible operations you could implement with SQL queries. In a real-world app, you need custom operations, which need a written SQL query to be implemented. How do you implement a custom operation in a Spring Data repository?

Spring Data makes this aspect so easy that you sometimes don’t even need to write a SQL query. Spring Data knows to interpret the method’s names based on some naming definition rules and creates the SQL query behind the scenes for you.

For example, say you want to write an operation to get all the accounts for a given name. In Spring Data, you can write a method with the following name: findAccountsByName. 

When the method name starts with “find,” Spring Data knows you want to SELECT something. Next, the word “Accounts” tells Spring Data what you want to SELECT. Spring Data is so smart that I could have even named the method findByName. It would still know what to select just because the method is in the AccountRepository interface. In this example, I wanted to be more specific and make the operation name clear. After the “By” in the method’s name, Spring Data expects to get the query’s condition (the WHERE clause). In our case, we want to select “ByName,” so Spring Data translates this to WHERE name = ?.

```java
public interface AccountRepository extends CrudRepository<Account, int> {
    List<Account> findAccountByName(String name);
}
```

This magic of translating a method’s name into a query looks incredible at first sight. However, with experience you realize it’s not a silver bullet. It has a few disadvantages, so I always recommend developers explicitly specify the query instead of relying on Spring Data to translate the method’s name. The main disadvantages of relying on the
method’s name are as follows:

- If the operation requires a more complex query, the method’s name would be too large and difficult to read.
- If a developer refactors the method’s name by mistake, they might affect the app’s behavior without realizing it (unfortunately, not all apps are roughly tested, and we need to consider this).
- Unless you have an IDE that offers you hints while writing the method’s name, you need to learn the Spring Data’s naming rules. Since you already know SQL, learning a set of rules applicable only for Spring Data is not advantageous.
- Performance is affected because Spring Data also has to translate the method name into a query, so the app will initialize slower (the app translates the method names into queries when the app boots).

The simplest way to avoid these problems is using the @Query annotation to specify the SQL query that the app will run when you call that method. When you annotate the method @Query, it’s no longer relevant how you name that method. Spring Data will use the query you provide instead of translating the method’s name into a query.

```java
public interface AccountRepository extends CrudRepository<Account, int> {
    @Query("SELECT * FROM account WHERE name = :name")
    List<Account> findAccountByName(String name);

    // We annotate the methods that define operations that change data with the @Modifying annotation.
    @Modifying
    @Query("UPDATE account SET amount = :amount WHERE id = :id")
    void changeAmount(long id, BigDecimal amount);
}
```

Use DI to get a bean that implements the AccountRepository interface wherever you need it in the app. Don’t worry that you only wrote the interface. Spring Data creates a dynamic implementation and adds a bean to your app’s context.

```java
@Service
public class TransferService {
    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
```

We use the AccountRepository to get the account details and change the accounts’ amounts.

```java
@Service
public class TransferService {
    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transferMoney(
            int idSender,
            int idReceiver,
            BigDecimal amount
    ) throws AccountNotFoundException
    {
        // We get the sender and receiver’s account details.
        Account sender = accountRepository.findById(idSender).orElseThrow(AccountNotFoundException::new);
        Account receiver = accountRepository.findById(idReceiver).orElseThrow(AccountNotFoundException::new);

        // We calculate the new account amounts
        // by subtracting the transferred value
        // from the sender account and adding it
        // to the destination account.
        BigDecimal senderNewAmount = sender.getAmount().subtract(amount);
        BigDecimal receiverNewAmount = receiver.getAmount().abs(amount);

        // We change the accounts’ amounts in the database.
        accountRepository.changeAmount(idSender, senderNewAmount);
        accountRepository.changeAmount(idSender, receiverNewAmount);

    }
}
```

Let’s add a service method to retrieve all the records from the database and get the account details by the owner’s name. Our AccountRepository inherits the findAll() method from the CrudRepository contract, as shown in the following listing.

```java
@Service
public class TransferService {
    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transferMoney(
            int idSender,
            int idReceiver,
            BigDecimal amount
    ) throws AccountNotFoundException
    {
        // We get the sender and receiver’s account details.
        Account sender = accountRepository.findById(idSender).orElseThrow(AccountNotFoundException::new);
        Account receiver = accountRepository.findById(idReceiver).orElseThrow(AccountNotFoundException::new);

        // We calculate the new account amounts
        // by subtracting the transferred value
        // from the sender account and adding it
        // to the destination account.
        BigDecimal senderNewAmount = sender.getAmount().subtract(amount);
        BigDecimal receiverNewAmount = receiver.getAmount().abs(amount);

        // We change the accounts’ amounts in the database.
        accountRepository.changeAmount(idSender, senderNewAmount);
        accountRepository.changeAmount(idSender, receiverNewAmount);
    }

    // AccountRepository inherits this method from the Spring Data CrudRepository interface.
    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> findAccountsByName(String name) {
        return accountRepository.findAccountsByName(name);
    }
}
```

The following listing shows you how the AccountController class exposes the money transfer use case through a REST endpoint.

```java
@RestController
public class AccountController {
    private final TransferService transferService;

    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public void transferMoney(
            @RequestBody int senderAccountId,
            @RequestBody int receiverAccountId,
            @RequestBody BigDecimal amount
    )
    {
        transferService.transferMoney(senderAccountId, receiverAccountId, amount);
    }
}
```

We also implement an endpoint to fetch the records from the database.

```java
@RestController
public class AccountController {
    private final TransferService transferService;

    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public void transferMoney(
            @RequestBody int senderAccountId,
            @RequestBody int receiverAccountId,
            @RequestBody BigDecimal amount
    )
    {
        transferService.transferMoney(senderAccountId, receiverAccountId, amount);
    }

    @GetMapping("/accounts")
    public Iterable<Account> getAllAccounts(
            @RequestParam(required = false) String name
    )
    {
        if (name == null) {
            return transferService.getAllAccounts();
        }

        return transferService.findAccountsByName(name)

    }
}
```
