package org.example;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CronExpression {
    private final String cronString;

    public CronExpression(String cronString) {
        this.cronString = cronString;
    }

    public Map<String, Object> getFieldsAndValues() {
        String[] fields = cronString.split(" ");
        if (fields.length != 6) {
            throw new IllegalArgumentException("Invalid cron string. Expected 6 fields.");
        }

        Map<String, Object> fieldsMap = new LinkedHashMap<>();
        fieldsMap.put("minute", new MinuteField(fields[0]).getValues());
        fieldsMap.put("hour", new HourField(fields[1]).getValues());
        fieldsMap.put("day of month", new DayOfMonthField(fields[2]).getValues());
        fieldsMap.put("month", new MonthField(fields[3]).getValues());
        fieldsMap.put("day of week", new DayOfWeekField(fields[4]).getValues());
        fieldsMap.put("command", fields[5]);

        return fieldsMap;
    }

    public String generateOutput(Map<String, Object> fieldsMap) {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, Object> entry : fieldsMap.entrySet()) {
            String field = entry.getKey();
            Object values = entry.getValue();
            if (field.equals("command")) {
                output.append(String.format("%-14s %s%n", field, values));
            } else if (values instanceof List) {
                List<Integer> intValues = (List<Integer>) values;
                String joinedValues = joinIntList(intValues); // Helper method to join integers
                output.append(String.format("%-14s %s%n", field, joinedValues));
            } else {
                output.append(String.format("%-14s %s%n", field, values.toString()));
            }
        }
        return output.toString();
    }

    private String joinIntList(List<Integer> intValues) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < intValues.size(); i++) {
            sb.append(intValues.get(i));
            if (i < intValues.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}