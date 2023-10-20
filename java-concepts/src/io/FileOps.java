package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.stream.IntStream;

public class FileOps {

    private static void checkingFileOrDirectory() {
        Path p = Paths.get("src/io/xanadu.txt");

        System.out.println("Checking file existence: ");
        System.out.println("File exists: " + Files.exists(p));
        System.out.println("File not exists: " + Files.notExists(p));

        System.out.println("\nChecking file accessibility: ");
        System.out.println("File is regular: " + Files.isRegularFile(p));
        System.out.println("File is readable: " + Files.isReadable(p));
        System.out.println("File is writable: " + Files.isWritable(p));
        System.out.println("File is executable: " + Files.isExecutable(p));

        System.out.println("\nChecking Whether Two Paths Locate the Same File: ");
        Path p2 = Paths.get("src/io/xanadu.txt").toAbsolutePath();
        try {
            System.out.println(Files.isSameFile(p, p2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void CreatingCopyingMovingDeletingFiles() {

        Path path = Paths.get("src/io/fileManipulationTest.txt");
        Path src = Paths.get("src/fileManipulationTestCopy.txt");

        // creating a file
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // copying between an input stream and a file
        try {
            Files.copy(new FileInputStream("src/io/xanadu.txt"), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // copying a file to the src directory
        try {
            Files.copy(path, src, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Deleting files
        try {
            Files.delete(path);
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", path);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", path);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }

    private static void gettingFileInformation() throws IOException {
        Path path = Paths.get("src/io/xanadu.txt");

        System.out.print("Get single attribute: ");
        System.out.println(Files.getLastModifiedTime(path));

        System.out.println("\nGet bulk of basic attributes: ");
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        System.out.println("creationTime: " + attr.creationTime());
        System.out.println("lastAccessTime: " + attr.lastAccessTime());
        System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
        System.out.println("isDirectory: " + attr.isDirectory());
        System.out.println("isOther: " + attr.isOther());
        System.out.println("isRegularFile: " + attr.isRegularFile());
        System.out.println("isSymbolicLink: " + attr.isSymbolicLink());
        System.out.println("size: " + attr.size());
        System.out.println("Key: " + attr.fileKey());

        System.out.println("\nGet bulk of POSIX compliant files attributes: ");
        PosixFileAttributes attrPosix = Files.readAttributes(path, PosixFileAttributes.class);
        System.out.format("%s %s %s%n",
                          attrPosix.owner().getName(),
                          attrPosix.group().getName(),
                          PosixFilePermissions.toString(attrPosix.permissions()));
    }

    private static void readWriteSmallFiles() throws IOException {
        Path path = Paths.get("src/io/xanadu.txt");

        System.out.println("Read small file into a string: ");
        String xanadu = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        System.out.println(xanadu);

        System.out.println("\nRead small file into a list of lines: ");
        List<String> lines = Files.readAllLines(path);
        IntStream.range(0, lines.size())
                 .mapToObj(i -> String.format("%d: %s", i + 1, lines.get(i)))
                 .forEach(System.out::println);

        Path xanaduCopy = Paths.get("src/io/xanaduCopy.txt");
        Files.write(xanaduCopy, xanadu.getBytes(StandardCharsets.UTF_8));
    }

    private static void readingUsingBuffersUtilityMethods() {

        Path path = Paths.get("src/io/xanadu.txt");

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    private static void createDir() throws IOException {
        Files.createDirectory(Paths.get("src/testDirCreation"));
    }

    private static void listDirContents() {

        Path dir = Paths.get("src/");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                System.out.println(file.getFileName());
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
    }

    public static void main(String[] args) throws IOException {
        listDirContents();
    }
}
