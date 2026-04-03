package org.engine;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchStrategyTest {

    @Test
    void hasExpectedValues() {
        List<String> names = Arrays.stream(SearchStrategy.values()).map(Enum::name).toList();

        assertTrue(names.containsAll(List.of("ALL", "ANY", "NONE")));
    }
}
