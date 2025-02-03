package org.example;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main \"<cron expression>\"");
            return;
        }

        try {
            CronExpression cronExpression = new CronExpression(args[0]);
            Map<String, Object> fieldsAndValues = cronExpression.getFieldsAndValues();
            String output = cronExpression.generateOutput(fieldsAndValues);
            System.out.println(output);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
