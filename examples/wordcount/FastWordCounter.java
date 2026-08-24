import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * High-performance, concurrent word counter optimized for mixed file sizes in Java 21+.
 * Uses Virtual Threads for heavy file I/O concurrency without thread-starvation overhead.
 */
public class FastWordCounter {

    // Simple, high-speed custom tokenization to avoid slow regex parsing
    private static void countWordsInFile(Path path, ConcurrentHashMap<String, Integer> wordCounts) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int len = line.length();
                int start = -1;

                for (int i = 0; i < len; i++) {
                    char c = line.charAt(i);
                    // Fast alphanumeric boundaries (adjust logic if handling punctuation differently)
                    if (Character.isLetterOrDigit(c)) {
                        if (start == -1) {
                            start = i;
                        }
                    } else {
                        if (start != -1) {
                            String word = line.substring(start, i).toLowerCase();
                            wordCounts.merge(word, 1, Integer::sum);
                            start = -1;
                        }
                    }
                }
                if (start != -1) {
                    String word = line.substring(start, len).toLowerCase();
                    wordCounts.merge(word, 1, Integer::sum);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + path + " - " + e.getMessage());
        }
    }

    public static ConcurrentHashMap<String, Integer> processDirectory(String dirPath) throws IOException {
        ConcurrentHashMap<String, Integer> globalCounts = new ConcurrentHashMap<>(65536);

        // Gather all regular files
        List<Path> files;
        try (Stream<Path> walk = Files.walk(Paths.get(dirPath))) {
            files = walk.filter(Files::isRegularFile).collect(Collectors.toList());
        }

        // Use Virtual Threads via a structured task scope or a simple unbounded virtual thread executor.
        // Virtual threads are cheap to spin up, making them perfect for thousands of small files blockages.
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (Path file : files) {
                executor.submit(() -> countWordsInFile(file, globalCounts));
            }
        } // Auto-close blocks until all submitted virtual threads complete natively

        return globalCounts;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a directory path.");
            return;
        }

        try {
            long startTime = System.currentTimeMillis();
            ConcurrentHashMap<String, Integer> results = processDirectory(args[0]);
            long endTime = System.currentTimeMillis();

            System.out.println("Processing finished in: " + (endTime - startTime) + " ms");
			for (var entry : results.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
            System.out.println("Unique words found: " + results.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
