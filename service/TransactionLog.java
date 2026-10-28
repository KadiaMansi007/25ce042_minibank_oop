package service;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
public class TransactionLog {
    public static void append(Path file, String line) throws IOException {
        Path parent = file.getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.write(file, List.of(LocalDateTime.now() + " " + line),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
