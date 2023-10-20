package networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SocketTest {
    public static void main(String[] args) throws IOException {
        try (
                Socket s = new Socket();
        )
        {
            s.connect(new InetSocketAddress("time-a.nist.gov", 13), 10000);
            Scanner in = new Scanner(s.getInputStream(), StandardCharsets.UTF_8);

            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println(line);
            }

            in.close();
        }
    }
}
