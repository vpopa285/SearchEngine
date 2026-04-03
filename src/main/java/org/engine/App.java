package org.engine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class App {

    private static Scanner scanner;

    public static void main(String... args) throws IOException {
        String fileName = parseFileName(args);

        if (fileName == null) {
            System.out.println("No file provided.");
            return;
        }

        List<String> people = Files.readAllLines(Path.of(fileName));
        SearchEngine engine = new SearchEngine(people);

        runMenu(people, engine);

        System.out.println("Bye!");

    }

    private static String parseFileName(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if ("--data".equals(args[i]) && i + 1 < args.length) return args[i + 1];
        }
        return null;
    }

    static void runMenu(List<String> people, SearchEngine engine) {
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        boolean running = true;

        while (running) {
            printMenu();
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> search(engine);
                case "2" -> printAll(people);
                case "0" -> running = false;
                default -> System.out.println("Incorrect option! Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("""

                === Menu ===
                1. Find a person
                2. Print all persons
                0. Exit""");
    }

    private static void search(SearchEngine engine) {
        System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");
        String strategyInput = scanner.nextLine().toUpperCase();

        SearchStrategy strategy;
        try {
            strategy = SearchStrategy.valueOf(strategyInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown strategy. Use ALL, ANY, or NONE.");
            return;
        }

        System.out.println("\nEnter a name or email to search all suitable people.");
        String query = scanner.nextLine();

        List<String> results = engine.search(query, strategy);

        if (results.isEmpty()) {
            System.out.println("No matching people found.");
        } else {
            System.out.println(results.size() + " persons found:");
            results.forEach(System.out::println);
        }
    }

    private static void printAll(List<String> people) {
        System.out.println("\n=== List of people ===");
        people.forEach(System.out::println);
    }
}
