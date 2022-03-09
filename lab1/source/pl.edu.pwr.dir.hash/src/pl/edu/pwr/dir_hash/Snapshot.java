package pl.edu.pwr.dir_hash;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class represents a snapshot of directory.
 */
public class Snapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    // Hashmap containing paths of files in directory and their corresponding MD5 checksum
    private HashMap<String, byte[]> directorySnapshot;
    private String directory;


    /**
     * Create new Snapshot object.
     * @param path Base directory for Snapshot.
     */
    public Snapshot(String path) {
        Path dirPath = FileSystems.getDefault().getPath(path);
        if (!dirPath.toFile().isDirectory())
            throw new IllegalArgumentException("Constructor only allows directory paths.");
        try {
            ChecksumVisitor visitor = new ChecksumVisitor();
            Files.walkFileTree(dirPath, visitor);
            this.directorySnapshot = visitor.getCheckSums();
            this.directory = dirPath.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that compares 2 directory snapshots and returns filenames of changed files.
     *
     * @param s1 First directory snapshot to compare.
     * @param s2 Second directory snapshot to compare.
     * @return Filenames of changed files.
     */
    public static ArrayList<String> dirCmp(Snapshot s1, Snapshot s2) {
        var map1 = s1.getDirectorySnapshot();
        var map2 = s2.getDirectorySnapshot();

        ArrayList<String> modified = new ArrayList<>();

        map1.forEach((s, bytes) -> {
            if (!map2.containsKey(s)) {
                modified.add(s);
                return;
            }
            var toCmp = map2.get(s);
            if (!Arrays.equals(bytes, toCmp)) {
                modified.add(s);
            }
        });

        map2.forEach((s, bytes) -> {
            if (!map1.containsKey(s) && !modified.contains(s)) {
                modified.add(s);
            }
        });

        return modified;
    }

    public HashMap<String, byte[]> getDirectorySnapshot() {
        return directorySnapshot;
    }

    /**
     * Serialize Snapshot object and write it to file.
     * @param path Path to which write the object.
     * @param snapshot Snapshot object to be serialized.
     * @throws Exception If file could not be serialized/written to file.
     */
    public static void saveSnapshotToFile(Path path, Snapshot snapshot) throws Exception {
        try (ObjectOutputStream objectStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectStream.writeObject(snapshot);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Could not write object to file, please make sure the correct path was given.");
        }
    }

    /**
     * Read serialized Snapshot object from file and return it.
     * @param path Path pointing to file.
     * @return Snapshot object read from the file.
     * @throws Exception If file could not be read properly.
     */
    public static Snapshot loadSnapshotFromFile(Path path) throws Exception {
        try (ObjectInputStream objectStream = new ObjectInputStream(Files.newInputStream(path))){
            Snapshot snap = (Snapshot) objectStream.readObject();
            if (snap != null){
                return snap;
            }
            else throw new Exception("File was probably empty");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Wrong file selected");
        }
    }

    public String getDirectory() {
        return directory;
    }
}
