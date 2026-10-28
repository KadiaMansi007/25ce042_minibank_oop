package service;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
public class ReportGenerator {
    public static String generate(Path folder, Path output) throws IOException {
        long deposits = 0, withdrawals = 0;
        StringBuilder report = new StringBuilder();
        if (Files.exists(folder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.log")) {
                for (Path file : stream) {
                    try (BufferedReader reader = Files.newBufferedReader(file)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.trim().split("\\s+");
                            for (int i = 0; i + 2 < parts.length; i++) {
                                if (parts[i].equals("DEPOSIT")) deposits += parseAmount(parts[i + 2]);
                                if (parts[i].equals("WITHDRAW")) withdrawals += parseAmount(parts[i + 2]);
                            }
                        }
                    }
                    BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                    report.append(file.getFileName()).append(" size=").append(attr.size())
                        .append(" modified=").append(attr.lastModifiedTime()).append(System.lineSeparator());
                }
            }
        }
        report.append("Total deposits: ").append(deposits).append(System.lineSeparator());
        report.append("Total withdrawals: ").append(withdrawals).append(System.lineSeparator());
        report.append("Net change: ").append(deposits - withdrawals).append(System.lineSeparator());
        Path parent = output.getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(output, report.toString());
        return report.toString();
    }
    private static long parseAmount(String value) {
        try { return Long.parseLong(value); } catch (NumberFormatException e) { return 0; }
    }
}
