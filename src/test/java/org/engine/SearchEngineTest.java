package org.engine;

import org.junit.jupiter.api.Test;

import java.util.List;

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

        assertTrue(result.isEmpty());
    }

    @Test
    void noMatchTest() {
        List<String> result = engine.search("xyzzy", SearchStrategy.ANY);

        assertTrue(result.isEmpty());
    }

    @Test
    void allStrategyTest() {
        List<String> result = engine.search("harrington erick", SearchStrategy.ALL);

        assertEquals(1, result.size());
        assertTrue(result.contains("Erick Harrington harrington@gmail.com"));

        result = engine.search("harrington katie", SearchStrategy.ALL);
        assertTrue(result.isEmpty());
    }

    @Test
    void anyStrategyTest() {
        List<String> result = engine.search("erick", SearchStrategy.ANY);

        assertEquals(2, result.size());
        assertTrue(result.contains("Erick Harrington harrington@gmail.com"));
        assertTrue(result.contains("Erick Burgess"));

        result = engine.search("katie erick", SearchStrategy.ANY);
        assertEquals(3, result.size());
    }

    @Test
    void noneStrategyTest() {
        List<String> result = engine.search("erick", SearchStrategy.NONE);

        assertFalse(result.contains("Erick Harrington harrington@gmail.com"));
        assertFalse(result.contains("Erick Burgess"));
        assertTrue(result.contains("Katie Jacobs"));
        assertTrue(result.contains("Myrtle Medina"));

        result = engine.search("erick katie rene dwight myrtle", SearchStrategy.NONE);
        assertTrue(result.isEmpty());
    }

}
