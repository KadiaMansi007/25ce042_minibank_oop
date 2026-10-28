package service;
import model.Account;
import java.io.*;
import java.nio.file.*;
public class StatePersister {
    public static void save(Account[] accounts, Path file) throws IOException {
        Path parent = file.getParent();
        if (parent != null) Files.createDirectories(parent);
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(accounts);
        }
    }
    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) return new Account[0];
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            return (Account[]) in.readObject();
        }
    }
}
