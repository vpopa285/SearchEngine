package org.engine;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchStrategyTest {

    @Test
    void hasExpectedValues() {
        List<String> names = Arrays.stream(SearchStrategy.values()).map(Enum::name).toList();

        assertThat(names.containsAll(List.of("ALL", "ANY", "NONE")));
    }
}
