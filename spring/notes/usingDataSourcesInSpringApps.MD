# Using Data Sources in Spring Apps

Almost every app today needs to store data it works with, and often apps use data- bases to manage the data they persist. For many years, relational databases have provided applications with a simple and elegant way to store data you can success- fully apply in many scenarios. Spring apps, like other apps, often need to use data- bases to persist data, and for this reason, you need to learn how to implement such capabilities for your Spring apps.

In this chapter, we discuss what a data source is and the most straightforward way to make your Spring app work with a database. That straightforward way is the JdbcTemplate tool that Spring offers.

## What a data source is

In this section, we discuss an essential component your Spring app needs to access a database: the data source. The data source is a component that manages connections to the server handling the database (the database management system, also known as DBMS).

![](/images/dataSources.png)

Without an object taking the responsibility of a data source, the app would need to request a new connection for each operation with the data. This approach is not realistic in a production scenario because communicating through the network for establishing a new connection for each operation would dramatically slow down the application and cause performance issues. The data source makes sure your app only requests a new connection when it really needs it, improving the app’s performance.

When working with any tool related to data persistence in a relational database, Spring expects you to define a data source. For this reason, it’s important we first discuss where a data source fits in the app’s persistence layer and then demonstrate how to implement a data persistence layer in examples.

In a Java app, the language’s capabilities to connect to a relational database is named Java Database Connectivity (JDBC). JDBC offers you a way to connect to a DBMS to work with a database. However, the JDK doesn’t provide a specific implementation for working with a particular technology (such as MySQL, Postgres, or Oracle). The JDK only gives you the abstractions for objects an app needs to work with a relational database. To gain the implementation of this abstraction and enable your app to connect to a certain DBMS technology, you add a runtime dependency named the JDBC driver. Every technology vendor provides the JDBC driver you need to add to your app to enable it to connect to that specific technology. The JDBC driver is not something that comes either from the JDK or from a framework such as Spring.

![](/images/jdbcDriver.png)

The JDBC driver gives you a way to obtain a connection to the DBMS. A first option is to use the JDBC driver directly and implement your app to require a connection each time it needs to execute a new operation on the persisted data. You’ll often find this approach in Java fundamentals tutorials. But this is a waste of time when we are constantly making request.

**A data source object can efficiently manage the connections to minimize the number of unnecessary operations. Instead of using the JDBC driver manager directly, we use a data source to retrieve and manage the connections**

For Java apps, you have multiple choices for data source implementations, but the most commonly used today is the HikariCP (Hikari connection pool) data source. The convention configuration of Spring Boot also considers HikariCP the default data source implementation too, and this is what we’ll use in the examples.

## Using JdbcTemplateto work with persisted data

In this section, we’ll use a tool named JdbcTemplate that allows you to work with a database with JDBC in a simplified fashion. 

JdbcTemplate is the simplest of the tools Spring offers for using a relational database, but it’s an excellent choice for small apps as it doesn’t force you to use any other specific persistence framework. JdbcTemplate is the best Spring choice to implement a persistence layer when you don’t want your app to have any other dependency. I also consider it an excellent way to start learning how to implement the persistence layer of Spring apps.

To demonstrate how JdbcTemplate is used, we’ll implement an example. We’ll follow these steps:

1. Create a connection to the DBMS.
2. Code the repository logic.
3. Call the repository methods in methods that implement REST endpoints’
actions.

For this app, we have a table “purchase” in a database. This table stores details about the products bought from an online shop and the price of the purchase. The columns of this table are as follows:

- **id:** An auto-incrementing unique value that also takes the responsibility of the primary key of the table
- **product:** The name of the purchased product
- **price:** The purchase price

The requirements for the app we implement are simple. We’ll develop a backend service that exposes two endpoints. Clients call one endpoint to add a new record in the purchase table and a second endpoint to get all the records from the purchase table.

When working with a database, we implement all the capabilities related to the persistence layer in classes we (by convention) name **repositories**.

![](/images/persistedDataExampleStructure.png)

We need a model class to define the purchase data in our app. Instances of this class map the rows of the purchase table in the database, so each instance needs an ID, the product, and the price as attributes. The next code snippet shows the Purchase model class:

```java
public class Purchase {
    private int id;
    private String product;
    private BigDecimal price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
```

> When you want to store a floating-point value accurately and make sure you don’t lose decimal precision when executing various operations with the values, use BigDecimal and not double or float.

To easily get a PurchaseRepository instance when we need it in the controller, we’ll also make this object a bean in the Spring context. The simplest approach is to use a stereotype annotation but instead of using @Component, Spring provides a focused annotation for repositories we can use: @Repository. The following listing shows you the repository class definition.

```java
@Repository
public class PurchaseRepository {
}
```

Now that PurchaseRepository is a bean in the application context, we can inject an instance of JdbcTemplate that we’ll use to work with the database. 

```java
@Repository
public class PurchaseRepository {
    private final JdbcTemplate jdbcTemplate;

    // We use constructor injection to get the JdbcTemplate instance from the application context.
    @Autowired
    public PurchaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
```

Finally, you have a JdbcTemplate instance, so you can implement the app’s requirements. JdbcTemplate has an update() method you can use to execute any query for data mutation: INSERT, UPDATE or DELETE. Pass the SQL and the parameters it needs, and that’s it; let JdbcTemplate take care of the rest (obtaining a connection, creating a statement, treating the SQLException, and so on). The following listing adds a storePurchase() method to the PurchaseRepository class. The storePurchase() method uses JdbcTemplate to add a new record in the purchase table.

```java
@Repository
public class PurchaseRepository {
    private final JdbcTemplate jdbcTemplate;

    // We use constructor injection to get the JdbcTemplate instance from the application context.
    @Autowired
    public PurchaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // The method takes a parameter that
    // represents the data to be stored.
    public void storePurchase(Purchase purchase) {

        // The query is written as a string, and question marks (?) replace the queries’
        // parameter values. For the ID, we use NULL because we configured the DBMS to
        // generate the value for this column.
        String sql = "INSERT INTO purchase VALUES (NULL, ?, ?)";

        // The JdbcTemplate update() method sends the query to the
        // database server. The first parameter the method gets is
        // the query, and the next parameters are the values for the
        // parameters. These values replace, in the same order, each
        // question mark in the query.
        jdbcTemplate.update(sql, purchase.getProduct(), purchase.getPrice());
    }
}
```

With a couple of lines of code, you can insert, update, or delete records in tables. Retrieving data is no more difficult than this. As for the insert, you write and send a query.

To retrieve data, this time, you’ll write a SELECT query. And to tell JdbcTemplate how to transform the data into Purchase objects (your model class), you implement a RowMapper: an object responsible for transforming a row from the ResultSet into a specific object. For example, if you want to get the data from the database modeled as Purchase objects, you need to implement a RowMapper to define the way a row is mapped to a Purchase instance.

![](/images/rowMapper.png)

```java
@Repository
public class PurchaseRepository {
    private final JdbcTemplate jdbcTemplate;

    // We use constructor injection to get the JdbcTemplate instance from the application context.
    @Autowired
    public PurchaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // The method takes a parameter that
    // represents the data to be stored.
    public void storePurchase(Purchase purchase) {

        // The query is written as a string, and question marks (?) replace the queries’
        // parameter values. For the ID, we use NULL because we configured the DBMS to
        // generate the value for this column.
        String sql = "INSERT INTO purchase VALUES (NULL, ?, ?)";

        // The JdbcTemplate update() method sends the query to the
        // database server. The first parameter the method gets is
        // the query, and the next parameters are the values for the
        // parameters. These values replace, in the same order, each
        // question mark in the query.
        jdbcTemplate.update(sql, purchase.getProduct(), purchase.getPrice());
    }

    public List<Purchase> findAllPurchases() {
        String sql = "SELECT * FROM purchase";

        // We implement a RowMapper object that tells JdbcTemplate how to map a row in the result set into a
        // Purchase object. In the lambda expression, parameter “r” is the ResultSet (the data you get from the
        // database), while parameter “i” is an int representing the row number.
        RowMapper<Purchase> purchaseRowMapper = (r, i) -> {
            Purchase rowObject = new Purchase();

            // We set the data into a Purchase
            // instance. JdbcTemplate will use
            // this logic for each row in the
            // result set.
            rowObject.setId(r.getInt("id"));
            rowObject.setProduct(r.getString("product"));
            rowObject.setPrice(r.getBigDecimal("price"));

            return rowObject;
        };

        // We send the SELECT query using the
        // query method, and we provide the
        // row mapper object for JdbcTemplate
        // to know how to transform the data
        // it gets in Purchase objects.
        return jdbcTemplate.query(sql, purchaseRowMapper);
    }
}
```

Once you have the repository methods and you can store and retrieve records in the database, it’s time to expose these methods through endpoints.

```java
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseController(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    // We implement an endpoint a client calls to store a purchase record in the database. We use the
    // repository storePurchase() method to persist the data the controller’s action gets from the HTTP request body
    @PostMapping
    public void storePurchases(@RequestBody Purchase purchase) {
        purchaseRepository.storePurchase(purchase);
    }

    // We implement an endpoint the client calls to get all the records from the purchase table. The controller’s
    // action uses the repository’s method to get the data from the database and returns the data to the client in the
    // HTTP response body.
    @GetMapping
    public List<Purchase> findPurchases() {
        return purchaseRepository.findAllPurchases();
    }
}
```

If you run the application now, you can test the two endpoints using Postman or cURL.

## Customizing the configuration of the data source

These are the steps we’ll follow:

1. We’ll add a JDBC driver and configure a data source using the “application.properties” file to point to a database. We’ll still let Spring Boot define the DataSource bean in the Spring context based on the properties we define.
2. We’ll change the project to define a custom DataSource bean and discuss when something like this is needed in real-world scenarios.

### Defining the data source in the application properties file

We add the spring.datasource.url property to define the database location, and the spring.datasource.username and spring.datasource.password properties to define the credentials the app needs to authenticate and get connections from the DBMS.

With these couple of changes, the application now uses the MySQL database. Spring Boot knows to create the DataSource bean using the spring.datasource properties you provided in the “application.properties” file.

## Using a custom DataSource bean

Some scenarios in which you need to define the bean yourself are as follows:

- You need to use a specific DataSource implementation based on a condition you can only get at runtime.
- Your app connects to more than one database, so you have to create multiple data sources and distinguish them using qualifiers.
- You have to configure specific parameters of the DataSource object in certain conditions your app has only at runtime. For example, depending on the environment where you start the app, you want to have more or fewer connections in the connection pool for performance optimizations.
- Your app uses Spring framework but not Spring Boot.

Don’t worry! The DataSource is just a bean you add to the Spring context like any other bean. Instead of letting Spring Boot choose the implementation for you and configure the DataSource object, you define a method annotated with @Bean in a configuration class and add the object to the context yourself. This way, you have full control over the object’s creation.

We’ll create a configuration file and define a method annotated with @Bean, which returns the DataSource instance we add to the Spring context. The next listing shows the configuration class and the definition of the method annotated with @Bean.

```java
@Configuration
@ComponentScan(basePackages = "com.example.jdbctemplatepersisteddata")
@PropertySource("classpath:database.properties")
public class ProjectConfiguration {

    @Value("${url}")
    private String URL;
    @Value("${dbuser}")
    private String USER;
    @Value("${dbpassword}")
    private String PASSWORD;

    // The method returns a DataSource object. If
    // Spring Boot finds a DataSource already exists in
    // the Spring context it doesn’t configure one.
    @Bean
    DataSource dataSource() {
        // We’ll use HikariCP as the data source implementation
        // for this example. However, when you define the bean
        // yourself, you can choose other implementations if
        // your project requires something else.
        HikariDataSource dataSource = new HikariDataSource();

        // We set the connection parameters on the data source.
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setConnectionTimeout(1000);

        // We return the DataSource instance, and Spring adds it to its context.
        return dataSource;
    }
}
```
