package org.example;

import java.util.List;

abstract class BaseField {
    protected final String field;
    protected final int min;
    protected final int max;

    public BaseField(String field, int min, int max) {
        this.field = field;
        this.min = min;
        this.max = max;
    }

    public List<Integer> getValues() {
        if (field.equals("*")) {
            return new WildcardParser(min, max).generateValues();
        } else if (field.contains("/")) {
            return new StepRangeParser(min, max, field).generateValues();
        } else if (field.contains("-")) {
            return new RangeParser(min, max, field).generateValues();
        } else if (field.contains(",")) {
            return new CommaParser(min, max, field).generateValues();
        } else {
            return new ValueParser(min, max, field).generateValues();
        }
    }
}

// Minute field (0-59)
class MinuteField extends BaseField {
    public MinuteField(String field) {
        super(field, 0, 59);
    }
}

// Hour field (0-23)
class HourField extends BaseField {
    public HourField(String field) {
        super(field, 0, 23);
    }
}

// Day of month field (1-31)
class DayOfMonthField extends BaseField {
    public DayOfMonthField(String field) {
        super(field, 1, 31);
    }
}

// Month field (1-12)
class MonthField extends BaseField {
    public MonthField(String field) {
        super(field, 1, 12);
    }
}

// Day of week field (1-7, where 1=Monday and 7=Sunday)
class DayOfWeekField extends BaseField {
    public DayOfWeekField(String field) {
        super(field, 1, 7);
    }
}

