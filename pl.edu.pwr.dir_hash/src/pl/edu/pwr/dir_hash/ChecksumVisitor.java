package pl.edu.pwr.dir_hash;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Class used to fetch all files in a file tree and to compute their MD5 checksum.
 * Computed files checksum can be retrieved using getResults function.
 */
public class ChecksumVisitor extends SimpleFileVisitor<Path> {
    private HashMap<String, byte[]> checkSums = new HashMap<>();


    /**
     * Function is called when new file is visited in the file tree.
     * This function calculated MD5 checksum for every file and saves it.
     * @param file {@inheritDoc}
     * @param attrs @{@inheritDoc}
     * @return @{@inheritDoc}
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            try (var is = Files.newInputStream(file)){
                //calculate hashes.
                DigestInputStream digestInputStream = new DigestInputStream(is, md);
                digestInputStream.readAllBytes();
                byte[] checksum = md.digest();

                //File hash has been calculated, save it.
                checkSums.put(file.toAbsolutePath().toString(), checksum);
            } catch (IOException e){
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * Get calculated hashes.
     * @return Calculated hashes.
     */
    public HashMap<String, byte[]> getCheckSums() {
        return checkSums;
    }
}
