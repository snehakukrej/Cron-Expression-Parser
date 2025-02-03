package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Parser for wildcard (*)
class WildcardParser {
    private final int min;
    private final int max;

    public WildcardParser(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public List<Integer> generateValues() {
        List<Integer> values = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            values.add(i);
        }
        return values;
    }
}

// Parser for step ranges (e.g., */15)
class StepRangeParser extends BaseParser {
    private final int step;

    public StepRangeParser(int min, int max, String field) {
        super(min, max, field);

        String[] parts = field.split("/");

        if (parts.length != 2 || parts[1].trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid step format: missing step value in " + field);
        }

        // Parse and validate step value
        try {
            this.step = Integer.parseInt(parts[1].trim());
            validateValue(this.step);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid step value: " + parts[1]);
        }
    }

    private int[] getRangeFromPrefix(String prefix) {
        int start, end;

        if (prefix.equals("*")) {
            start = min;
            end = max;
        } else if (prefix.contains("-")) {
            String[] rangeParts = prefix.split("-");
            if (rangeParts.length != 2) {
                throw new IllegalArgumentException("Invalid range format: " + prefix);
            }
            start = Integer.parseInt(rangeParts[0].trim());
            end = Integer.parseInt(rangeParts[1].trim());
            validateValue(start);
            validateValue(end);

        } else {
            start = Integer.parseInt(prefix);
            end = max; // If a single number is given, assume the range ends at max
            validateValue(start);

        }

        // Ensure logical correctness
        if (start > end) {
            throw new IllegalArgumentException("Start value cannot be greater than end value: " + start + "-" + end);
        }

        return new int[]{start, end};
    }

    @Override
    public List<Integer> generateValues() {
        String[] parts = field.split("/");
        String prefix = parts[0].trim();
        int[] range = getRangeFromPrefix(prefix);
        int start = range[0];
        int end = range[1];

        List<Integer> values = new ArrayList<>();
        for (int i = start; i <= end; i += step) {
            values.add(i);
        }
        return values;
    }
}

// Parser for ranges (e.g., 1-5)
class RangeParser extends BaseParser {
    private final int start;
    private final int end;

    public RangeParser(int min, int max, String field) {
        super(min, max, field);
        String[] parts = field.split("-");
        this.start = Integer.parseInt(parts[0]);
        this.end = Integer.parseInt(parts[1]);
    }

    @Override
    public List<Integer> generateValues() {
        List<Integer> values = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            validateValue(i);
            values.add(i);
        }
        return values;
    }
}

// Parser for comma-separated lists (e.g., 1,15)
class CommaParser extends BaseParser {
    private final List<Integer> values;

    public CommaParser(int min, int max, String field) {
        super(min, max, field);
        this.values = new ArrayList<>();
        for (String part : field.split(",")) {
            int value = Integer.parseInt(part);
            validateValue(value);
            values.add(value);
        }
    }

    @Override
    public List<Integer> generateValues() {
        return values;
    }
}

// Parser for single values (e.g., 5)
class ValueParser extends BaseParser {
    private final int value;

    public ValueParser(int min, int max, String field) {
        super(min, max, field);
        this.value = Integer.parseInt(field);
        validateValue(value);
    }

    @Override
    public List<Integer> generateValues() {
        return Collections.singletonList(value);
    }
}

// Base class for all Parsers
abstract class BaseParser {
    protected final int min;
    protected final int max;
    protected final String field;

    public BaseParser(int min, int max, String field) {
        this.min = min;
        this.max = max;
        this.field = field;
    }

    public abstract List<Integer> generateValues();

    protected void validateValue(int value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    String.format("Value %d is out of range [%d, %d] for field: %s", value, min, max, field)
            );
        }
    }
}
