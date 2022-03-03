package pl.edu.pwr.dir_hash;

import java.nio.file.FileSystems;

public class Main {
    public static void main(String[] args) {

//        Snapshot snapsot = new Snapshot("C:\\Users\\ULTUX\\Documents\\Coding\\GeneticAlgorithm");
//        Snapshot.saveSnapshotToFile(FileSystems.getDefault().getPath("fileState.dat"), snapsot);

        Snapshot snapsot = new Snapshot("C:\\Users\\ULTUX\\Documents\\Coding\\GeneticAlgorithm");
        Snapshot lastSnapshot = Snapshot.loadSnapshotFromFile(FileSystems.getDefault().getPath("fileState.dat"));

        Snapshot.dirCmp(snapsot, lastSnapshot).forEach(System.out::println);
    }
}
