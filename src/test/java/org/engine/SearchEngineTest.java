package org.engine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SearchEngineTest {

    List<String> data = List.of(
            "Dwight Joseph djo@gmail.com",
            "Rene Webb webb@gmail.com",
            "Katie Jacobs",
            "Erick Harrington harrington@gmail.com",
            "Myrtle Medina",
            "Erick Burgess"
    );
    private final SearchEngine engine = new SearchEngine(data);

    @Test
    void emptyTest() {
        List<String> result = engine.search("", SearchStrategy.ALL);

        assertThat(result.isEmpty());
    }

    @Test
    void noMatchTest() {
        List<String> result = engine.search("xyzzy", SearchStrategy.ANY);

        assertThat(result.isEmpty());
    }

    @Test
    void allStrategySingleMatchTest() {
        List<String> result = engine.search("harrington erick", SearchStrategy.ALL);

        assertThat(result).hasSize(1)
                .contains("Erick Harrington harrington@gmail.com");
    }

    @Test
    void allStrategyNoMatchTest() {
        List<String> result = engine.search("harrington katie", SearchStrategy.ALL);

        assertTrue(result.isEmpty());
    }

    @Test
    void anyStrategySingleWordTest() {
        List<String> result = engine.search("erick", SearchStrategy.ANY);

        assertThat(result).hasSize(2)
                .contains("Erick Harrington harrington@gmail.com",
                        "Erick Burgess");
    }

    @Test
    void anyStrategyMultiWordsTest() {
        List<String> result = engine.search("katie erick", SearchStrategy.ANY);

        assertThat(result).hasSize(3)
                .contains("Katie Jacobs",
                        "Erick Harrington harrington@gmail.com",
                        "Erick Burgess");
    }

    @Test
    void noneStrategyMultiMatchTest() {
        List<String> result = engine.search("erick", SearchStrategy.NONE);

        assertThat(result).hasSize(4)
                .contains("Dwight Joseph djo@gmail.com",
                        "Rene Webb webb@gmail.com",
                        "Katie Jacobs",
                        "Myrtle Medina");
    }

    @Test
    void noneStrategyNoMatchTest() {
        List<String> result = engine.search("erick katie rene dwight myrtle", SearchStrategy.NONE);

        assertThat(result.isEmpty());
    }

}
