package io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathsTest {

    private static void createAndGetInfoPath() {

        // get and absolute path to the io package
        Path ioPackagePath = Paths.get(System.getProperty("user.dir"), "src", "io");
        System.out.println(ioPackagePath);

        // Some useful methods in a path object
        System.out.printf("toString %s%n", ioPackagePath.toString());
        System.out.printf("getFileName: %s%n", ioPackagePath.getFileName());
        System.out.printf("getName(0): %s%n", ioPackagePath.getName(0));
        System.out.printf("getNameCount: %d%n", ioPackagePath.getNameCount());
        System.out.printf("subpath(0,2): %s%n", ioPackagePath.subpath(0, 2));
        System.out.printf("getParent: %s%n", ioPackagePath.getParent());
        System.out.printf("getRoot: %s%n", ioPackagePath.getRoot());
    }

    private static void convertingPaths() {
        Path thisFilePath = Paths.get("src/io/PathsTest.java");
        System.out.println("Relative original path: " + thisFilePath);

        // transform the path to an absolute one
        System.out.println("Absolute conversion: " + thisFilePath.toAbsolutePath());

        /*
         * - If true is passed to this method and the file system supports symbolic links, this method resolves any
         * symbolic links in the path.
         * - If the Path is relative, it returns an absolute path.
         * - If the Path contains any redundant elements, it returns a path with those elements removed.
         * */
        try {
            System.out.println("Real path conversion: " + thisFilePath.toRealPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void joiningPaths() {
        // Using resolve to get one path from another
        Path thisFilePath = Paths.get("src/io/PathsTest.java");
        Path resolvedPath = Paths.get(System.getProperty("user.dir")).resolve(thisFilePath);
        System.out.println("Resolve path: " + resolvedPath);
    }

    private static void pathBetweenTwoPaths() {
        // get path to get to another path relative to the first
        Path thisFilePath = Paths.get("src/io/PathsTest.java");
        Path resolvedPath = Paths.get(System.getProperty("user.dir")).resolve(thisFilePath);
        Path parentPath = resolvedPath.getParent();
        Path collectionsPath = Paths.get("src/collections").toAbsolutePath();

        System.out.println("Parent path: " + parentPath);
        System.out.println("Collections path: " + collectionsPath);
        System.out.println("Relativize path to get the path to the collections package: " + parentPath.relativize(collectionsPath));
    }

    public static void main(String[] args) {
        // call a method
    }
}
