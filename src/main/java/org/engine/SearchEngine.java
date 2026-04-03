package org.engine;

import java.util.*;
import java.util.stream.Collectors;

public class SearchEngine {

    private final List<String> data;
    private final Map<String, Set<Integer>> index;

    public SearchEngine(List<String> data) {
        this.data = data;
        this.index = invertIndex(data);
    }

    public List<String> search(String query, SearchStrategy strategy) {
        String[] terms = query.toLowerCase().split(" ");

        if (terms.length == 0) return new ArrayList<>();

        Set<Integer> matchingIndices = switch (strategy) {
            case ALL -> matchAll(terms);
            case ANY -> matchAny(terms);
            case NONE -> matchNone(terms);
            default -> throw new IllegalArgumentException();
        };

        return matchingIndices.stream()
                .sorted()
                .map(data::get)
                .collect(Collectors.toList());
    }

    private Set<Integer> matchAll(String[] terms) {
        Set<Integer> result = null;

        for (String term : terms) {
            Set<Integer> hits = index.getOrDefault(term, new HashSet<>());

            if (result == null) {
                result = new HashSet<>(hits);
            } else {
                result.retainAll(hits);
            }

            if (result.isEmpty()) break;
        }

        return result == null ? new HashSet<>() : result;
    }

    private Set<Integer> matchAny(String[] terms) {
        Set<Integer> result = new HashSet<>();

        for (String term : terms) {
            result.addAll(index.getOrDefault(term, new HashSet<>()));
        }

        return result;
    }

    private Set<Integer> matchNone(String[] terms) {
        Set<Integer> excluded = matchAny(terms);

        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < data.size(); i++) {
            if (!excluded.contains(i)) {
                result.add(i);
            }
        }

        return result;
    }

    private static Map<String, Set<Integer>> invertIndex(List<String> data) {
        Map<String, Set<Integer>> invertedIndex = new HashMap<>();

        for (int i = 0; i < data.size(); i++) {
            for (String word : data.get(i).split(" ")) {
                word = word.toLowerCase();
                if (!word.isEmpty()) {
                    invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(i);
                }
            }
        }

        return invertedIndex;
    }

}
