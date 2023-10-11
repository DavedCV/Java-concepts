# Logging

Every Java programmer is familiar with the process of inserting calls to
System.out.println into troublesome code to gain insight into program behavior.
Of course, once you have figured out the cause of trouble, you remove the
print statements, only to put them back in when the next problem surfaces.
The logging API is designed to overcome this problem. Here are the principal
advantages of the API:

- It is easy to suppress all log records or just those below a certain level,
  and just as easy to turn them back on.
- Suppressed logs are very cheap, so there is only a minimal penalty for
  leaving the logging code in your application.
- Log records can be directed to different handlers—for displaying in the
  console, writing to a file, and so on.
- Both loggers and handlers can filter records. Filters can discard boring log
  entries, using any criteria supplied by the filter implementor.
- Log records can be formatted in different ways—for example, in plain text
  or XML.
- Applications can use multiple loggers, with hierarchical names such as
  com.mycompany.myapp, similar to package names.
- The logging configuration is controlled by a configuration file.
