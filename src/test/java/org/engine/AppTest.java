package org.engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AppTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream output;

    List<String> people = List.of(
            "Dwight Joseph djo@gmail.com",
            "Rene Webb webb@gmail.com",
            "Katie Jacobs",
            "Erick Harrington harrington@gmail.com",
            "Myrtle Medina",
            "Erick Burgess"
    );

    private final SearchEngine engine = new SearchEngine(people);


    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void inexistentFileTest() {
        assertThatThrownBy(() -> App.main("--data", "doesnt_exist.txt")).isInstanceOf(IOException.class);
    }

    @Test
    void emptyArgumentTest() throws IOException {
        App.main("--data");

        String out = output.toString(StandardCharsets.UTF_8);
        assertThat(out.contains("No file provided."));
    }

    @Test
    void menuExitAndExistentFileTest() throws IOException {
        String input = "0\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        App.main("--data", "text.txt");

        String out = output.toString(StandardCharsets.UTF_8);
        assertThat(out.contains("Bye!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"4\n0\n", "a\n0\n"})
    void incorrectInputTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        App.runMenu(people, engine);

        String out = output.toString(StandardCharsets.UTF_8);
        assertThat(out.contains("Incorrect option! Try again."));
    }

    @Test
    void incorrectStrategyTest() {
        String input = "1\nalles\n0\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        App.runMenu(people, engine);

        String out = output.toString(StandardCharsets.UTF_8);
        assertThat(out.contains("Unknown strategy. Use ALL, ANY, or NONE."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1\nall\n\n0\n", "1\nAny\n\n0\n", "1\nNoNe\n\n0\n"})
    void caseStrategiesTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        App.runMenu(people, engine);

        String out = output.toString(StandardCharsets.UTF_8);
        assertThat(out.contains("Unknown strategy. Use ALL, ANY, or NONE.")).isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "ALL, erick, Erick Harrington harrington@gmail.com",
            "ALL, erick, 'Erick Burgess'",

            "ALL, katie, Katie Jacobs",

            "ALL, katie erick, No matching people found.",

            "ANY, katie erick, Katie Jacobs",
            "ANY, katie erick, Erick Harrington harrington@gmail.com",
            "ANY, katie erick, Erick Burgess",

            "ANY, myrtle burgess, Myrtle Medina",
            "ANY, myrtle burgess, Erick Burgess",

            "NONE, erick webb joseph, Katie Jacobs",
            "NONE, erick webb joseph, Myrtle Medina",

            "NONE, erick katie medina webb joseph, No matching people found."
    })
    void strategiesTest(SearchStrategy strategy, String query, String waitedOutput) {
        String input = String.join("\n",
                "1",
                strategy.name(),
                query,
                "0"
        );

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        App.runMenu(people, engine);

        String out = output.toString(StandardCharsets.UTF_8);
        assertThat(out.contains(waitedOutput));
    }

    @Test
    void printAllTest() {
        String input = "2\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        App.runMenu(people, engine);

        String out = output.toString(StandardCharsets.UTF_8);

        assertThat(out.contains("""
            === List of people ===
            Dwight Joseph djo@gmail.com
            Rene Webb webb@gmail.com
            Katie Jacobs
            Erick Harrington harrington@gmail.com
            Myrtle Medina
            Erick Burgess"""));
    }

}
