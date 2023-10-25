# Networking

## Connecting to a server

### Using Telnet

The telnet program is a great debugging tool for network programming. You
should be able to launch it by typing telnet from a command shell.

You may have used telnet to connect to a remote computer, but you can use it to
communicate with other services provided by Internet hosts as well. Here is an
example of what you can do. Type

```bash
telnet time-a.nist.gov 13
```

What is going on? You have connected to the “time of day” service that most
UNIX machines constantly run. The particular server that you connected to is
operated by the National Institute of Standards and Technology and gives the
measurement of a Cesium atomic clock. (Of course, the reported time is not
completely accurate due to network delays.)

By convention, the “time of day” service is always attached to “port” number 13.

The server software is continuously running on the remote machine, waiting for
any network traffic that wants to chat with port 13. When the operating system
on the remote computer receives a network package that contains a request to
connect to port number 13, it wakes up the listening server process and establishes
the connection. The connection stays up until it is terminated by one of the parties

When you began the telnet session with time-a.nist.gov at port 13, a piece of network
software knew enough to convert the string "time-a.nist.gov" to its correct Internet
Protocol (IP) address, 129.6.15.28. The telnet software then sent a connection 
request to that address, asking for a connection to port 13. Once the connection was
established, the remote program sent back a line of data and closed the connection.
In general, of course, clients and servers engage in a more extensive dialog before
one or the other closes the connection.

### Connecting to a Server with Java

`SocketTest.java`

The key statements of this simple program are these:

```java
Socket s = new Socket("time-a.nist.gov", 13);
InputStream inStream = s.getInputStream();
```

The first line opens a socket, which is a network software abstraction that enables
communication out of and into this program. We pass the remote address
and the port number to the socket constructor. If the connection fails, an
UnknownHostException is thrown. If there is another problem, an IOException occurs.
Since UnknownHostException is a subclass of IOException and this is a sample program, we
just catch the superclass.

Once the socket is open, the getInputStream method in java.net.Socket returns an InputStream
object that you can use just like any other stream. Once you have grabbed the
stream, this program simply prints each input line to standard output. This process
continues until the stream is finished and the server disconnects.

This program works only with very simple servers, such as a “time of day” service.
In more complex networking programs, the client sends request data to the
server, and the server might not immediately disconnect at the end of a response.

The Socket class is pleasant and easy to use because the Java library hides the
complexities of establishing a networking connection and sending data across it.

### Socket Timeouts

Reading from a socket blocks until data are available. If the host is unreachable,
your application waits for a long time and you are at the mercy of the underlying
operating system to eventually time out.

You can decide what timeout value is reasonable for your particular application.
Then, call the setSoTimeout method to set a timeout value (in milliseconds).

```java
Socket s = new Socket(. . .);
s.setSoTimeout(10000); // time out after 10 seconds
```

If the timeout value has been set for a socket, all subsequent read and write 
operations throw a SocketTimeoutException when the timeout has been reached before the
operation has completed its work. You can catch that exception and react to
the timeout.

There is one additional timeout issue that you need to address. The constructor

```java
Socket(String host, int port)
```

can block indefinitely until an initial connection to the host is established.

You can overcome this problem by first constructing an unconnected socket and
then connecting it with a timeout:

```java
Socket s = new Socket();
s.connect(new InetSocketAddress(host, port), timeout);
```

### Internet Addresses

Usually, you don’t have to worry too much about Internet addresses—the 
numerical host addresses that consist of 4 bytes (or, with IPv6, 16 bytes) such as
129.6.15.28. However, you can use the InetAddress class if you need to convert
between host names and Internet addresses.

The static **getByName** method returns an InetAddress object of a host. For example,

```java
InetAddress address = InetAddress.getByName("time-a.nist.gov");
```

returns an InetAddress object that encapsulates the sequence of four bytes 129.6.15.28.
You can access the bytes with the **getAddress** method.

```java
byte[] addressBytes = address.getAddress();
```

Some host names with a lot of traffic correspond to multiple Internet addresses,
to facilitate load balancing. For example, at the time of this writing, the host name
google.com corresponds to twelve different Internet addresses. One of them is picked
at random when the host is accessed. You can get all hosts with the **getAllByName**
method.

```java
InetAddress[] addresses = InetAddress.getAllByName(host);
```

Finally, you sometimes need the address of the local host. If you simply ask for
the address of localhost, you always get the local loopback address 127.0.0.1, which
cannot be used by others to connect to your computer. Instead, use the static
**getLocalHost** method to get the address of your local host.

```java
InetAddress address = InetAddress.getLocalHost();
```

## Implementing Servers

Now that we have implemented a basic network client that receives data from
the Internet, let’s program a simple server that can send information to clients.
In the previous section, we have implemented a basic network client that receives
data from the Internet. In the following sections, we will program a simple server
that can send information to clients.

## Server Sockets

A server program, when started, waits for a client to attach to its port. For our
example program, we chose port number 8189, which is not used by any of
the standard services. The **ServerSocket** class establishes a socket. In our case, the
command

```java
ServerSocket s = new ServerSocket(8189);
```

establishes a server that monitors port 8189. The command

```java
Socket incoming = s.accept();
```

tells the program to wait indefinitely until a client connects to that port. Once
someone connects to this port by sending the correct request over the network,
this method returns a Socket object that represents the connection that was made.
You can use this object to get input and output streams, as is shown in the
following code:

```java
InputStream inStream = incoming.getInputStream();
OutputStream outStream = incoming.getOutputStream();
```

Everything that the server sends to the server output stream becomes the input
of the client program, and all the output from the client program ends up in the
server input stream.

In all the examples in this chapter, we transmit text through sockets. We therefore
turn the streams into scanners and writers.

```java
Scanner in = new Scanner(inStream, "UTF-8");
PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"),
true /* autoFlush */);
```

Let’s send the client a greeting:

```java
out.println("Hello! Enter BYE to exit.");
```

When you use telnet to connect to this server program at port 8189, you will see
this greeting on the terminal screen.

In this simple server, we just read the client’s input, a line at a time, and echo it.
This demonstrates that the program receives the input. An actual server would
obviously compute and return an answer depending on the input.

```java
String line = in.nextLine();
out.println("Echo: " + line);
if (line.trim().equals("BYE")) done = true;
```

In the end, we close the incoming socket.

```java
incoming.close();
```

That is all there is to it. Every server program, such as an HTTP web server,
continues performing this loop:

1. It receives a command from the client (“get me this information”) through
   an incoming data stream.
2. It decodes the client command.
3. It gathers the information that the client requested.
4. It sends the information to the client through the outgoing data stream.

### Serving Multiple Clients

There is one problem with the simple server in the preceding example. Suppose
we want to allow multiple clients to connect to our server at the same time. 
Typically, a server runs constantly on a server computer, and clients from all over
the Internet might want to use it at the same time. Rejecting multiple connections
allows any one client to monopolize the service by connecting to it for a long time.
We can do much better through the magic of threads.

Every time we know the program has established a new socket connection—that
is, every time the call to accept() returns a socket—we will launch a new thread to
take care of the connection between the server and that client. The main program
will just go back and wait for the next connection. For this to happen, the main
loop of the server should look like this:

```java
while (true) {
    Socket incoming = s.accept();
    Runnable r = new ThreadedEchoHandler(incoming);
    
    Thread t = new Thread(r);
    t.start();
}
```

The ThreadedEchoHandler class implements Runnable and contains the communication
loop with the client in its run method.

```java
class ThreadedEchoHandler implements Runnable  {
   public void run()  {
      try (
            InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream()
      ) {
         // Process input and send response
      }
      catch(IOException e) {
         // Handle exception
      }
   }
}
```

When each connection starts a new thread, multiple clients can connect to the
server at the same time. You can easily check this out.

1. Compile and run the server program
2. Open several telnet windows
3. Switch between windows and type commands. Note that you can communicate through all of them simultaneously.
4. When you are done, switch to the window from which you launched the server program and press Ctrl+C to kill it.

### Half-Close

The half-close allows one end of a socket connection to terminate its output while
still receiving data from the other end.

Here is a typical situation. Suppose you transmit data to the server but you don’t
know at the outset how much data you have. With a file, you’d just close the file
at the end of the data. However, if you close a socket, you immediately disconnect
from the server and cannot read the response.

The half-close overcomes this problem. You can close the output stream of a
socket, thereby indicating to the server the end of the requested data, but keep
the input stream open.

The client side looks like this:

```java
try (Socket socket = new Socket(host, port)) {
    Scanner in = new Scanner(socket.getInputStream(), "UTF-8");
    PrintWriter writer = new PrintWriter(socket.getOutputStream());
    
    // send request data
    writer.print(. . .);
    writer.flush();
    socket.shutdownOutput();
    
    // now socket is half-closed
    // read response data
    while (in.hasNextLine() != null) { String line = in.nextLine(); . . . }
}
```

The server side simply reads input until the end of the input stream is reached.
Then it sends the response.

Of course, this protocol is only useful for one-shot services such as HTTP where
the client connects, issues a request, catches the response, and then disconnects.

## Interruptible Sockets

When you connect to a socket, the current thread blocks until the connection has
been established or a timeout has elapsed. Similarly, when you read or write data
through a socket, the current thread blocks until the operation is successful or
has timed out.

In interactive applications, you would like to give users an option to simply
cancel a socket connection that does not appear to produce results. However, if
a thread blocks on an unresponsive socket, you cannot unblock it by calling
interrupt.

To interrupt a socket operation, use a SocketChannel, a feature of the java.nio package.
Open the SocketChannel like this:

```java
SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port));
```

A channel does not have associated streams. Instead, it has read and write methods
that make use of Buffer objects. These methods are declared in the interfaces ReadableByteChannel 
and WritableByteChannel.

If you don’t want to deal with buffers, you can use the Scanner class to read from
a SocketChannel because Scanner has a constructor with a ReadableByteChannel parameter:

```java
Scanner in = new Scanner(channel, "UTF-8");
```

To turn a channel into an output stream, use the static Channels.newOutputStream method.

```java
OutputStream outStream = Channels.newOutputStream(channel);
```

Whenever a thread is interrupted during an open, read,
or write operation, the operation does not block, but is terminated with an
exception.

## Getting Web Data