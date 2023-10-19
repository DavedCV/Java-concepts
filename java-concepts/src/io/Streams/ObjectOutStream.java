package io.Streams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectOutStream {
    public static void main(String[] args) {
        try (ObjectOutputStream obout =
                     new ObjectOutputStream(new FileOutputStream("src/io/Streams/ObjectOutStreamTest.txt")))
        {
            obout.writeDouble(3.5);
            obout.writeBoolean(false);
            obout.writeLong(13216546516L);
            obout.writeUTF("Hello World!");
            obout.writeObject(new ArrayList<String>(List.of("Hello", "This", "Is", "Serialized")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ObjectInputStream obin =
                     new ObjectInputStream(new FileInputStream("src/io/Streams/ObjectOutStreamTest.txt")))
        {
            System.out.println(obin.readDouble());
            System.out.println(obin.readBoolean());
            System.out.println(obin.readLong());
            System.out.println(obin.readUTF());
            System.out.println(obin.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
