package networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
* Run the program and then  connect via telnet to the server (localhost:8189)
* */
public class EchoServer {
    public static void main(String[] args) throws IOException {
        // establish server socket
        try (ServerSocket s = new ServerSocket(8189)) {
            // wait for client connection
            try (Socket incoming = s.accept()) {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();

                try (Scanner in = new Scanner(inStream, StandardCharsets.UTF_8)) {
                    PrintWriter out =
                            new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true);

                    out.println("Hello! Enter BYE to exit!");

                    // echo client input
                    boolean done = false;
                    while (!done && in.hasNextLine()) {
                        String line = in.nextLine();
                        out.println("Echo: " + line);
                        if (line.trim().equals("BYE")) done = true;
                    }

                    out.close();
                }
            }
        }
    }
}
