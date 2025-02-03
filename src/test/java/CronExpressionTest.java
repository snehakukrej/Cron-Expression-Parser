import org.example.CronExpression;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CronExpressionTest {

    @Test
    public void testDefaultCronString() {
        String cronString = "*/15 6 1,15 * 1-5 /usr/bin/find";
        Map<String, Object> value = new CronExpression(cronString).getFieldsAndValues();

        assertEquals(Arrays.asList(0, 15, 30, 45), value.get("minute"));
        assertEquals(Arrays.asList(6), value.get("hour"));
        assertEquals(Arrays.asList(1, 15), value.get("day of month"));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), value.get("month"));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), value.get("day of week"));
        assertEquals("/usr/bin/find", value.get("command"));
    }

    @Test
    public void testWildcard() {
        String cronString = "* * * * * /usr/bin/find";
        Map<String, Object> value = new CronExpression(cronString).getFieldsAndValues();

        assertEquals(range(0, 60), value.get("minute"));
        assertEquals(range(0, 24), value.get("hour"));
        assertEquals(range(1, 32), value.get("day of month"));
        assertEquals(range(1, 13), value.get("month"));
        assertEquals(range(1, 8), value.get("day of week"));
        assertEquals("/usr/bin/find", value.get("command"));
    }

    @Test
    public void testComma() {
        String cronString = "1,2,3,4 7,8,9 25,26 1,2 6,7 /usr/bin/find";
        Map<String, Object> value = new CronExpression(cronString).getFieldsAndValues();

        assertEquals(Arrays.asList(1, 2, 3, 4), value.get("minute"));
        assertEquals(Arrays.asList(7, 8, 9), value.get("hour"));
        assertEquals(Arrays.asList(25, 26), value.get("day of month"));
        assertEquals(Arrays.asList(1, 2), value.get("month"));
        assertEquals(Arrays.asList(6, 7), value.get("day of week"));
        assertEquals("/usr/bin/find", value.get("command"));
    }

    @Test
    public void testRange() {
        String cronString = "1-5 6-10 15-20 1-12 1-7 /usr/bin/find";
        Map<String, Object> value = new CronExpression(cronString).getFieldsAndValues();

        assertEquals(range(1, 6), value.get("minute"));
        assertEquals(range(6, 11), value.get("hour"));
        assertEquals(range(15, 21), value.get("day of month"));
        assertEquals(range(1, 13), value.get("month"));
        assertEquals(range(1, 8), value.get("day of week"));
        assertEquals("/usr/bin/find", value.get("command"));
    }

    @Test
    public void testStepRange() {
        String cronString = "*/5 */2 */3 */4 */2 /usr/bin/find";
        Map<String, Object> value = new CronExpression(cronString).getFieldsAndValues();

        assertEquals(range(0, 60, 5), value.get("minute"));
        assertEquals(range(0, 24, 2), value.get("hour"));
        assertEquals(range(1, 32, 3), value.get("day of month"));
        assertEquals(range(1, 13, 4), value.get("month"));
        assertEquals(range(1, 8, 2), value.get("day of week"));
        assertEquals("/usr/bin/find", value.get("command"));
    }

    @Test
    public void testStepRangeWithBeginning() {
        String cronString = "2/5 5/2 10/3 5/4 3/2 /usr/bin/find";
        Map<String, Object> value = new CronExpression(cronString).getFieldsAndValues();

        assertEquals(range(2, 60, 5), value.get("minute"));
        assertEquals(range(5, 24, 2), value.get("hour"));
        assertEquals(range(10, 32, 3), value.get("day of month"));
        assertEquals(range(5, 13, 4), value.get("month"));
        assertEquals(range(3, 8, 2), value.get("day of week"));
        assertEquals("/usr/bin/find", value.get("command"));
    }

    @Test
    public void testValue() {
        String cronString = "1 6 15 11 5 /usr/bin/find";
        Map<String, Object> value = new CronExpression(cronString).getFieldsAndValues();

        assertEquals(Arrays.asList(1), value.get("minute"));
        assertEquals(Arrays.asList(6), value.get("hour"));
        assertEquals(Arrays.asList(15), value.get("day of month"));
        assertEquals(Arrays.asList(11), value.get("month"));
        assertEquals(Arrays.asList(5), value.get("day of week"));
        assertEquals("/usr/bin/find", value.get("command"));
    }

    @Test
    void parserFailedTest() {
        String input = "*/15 0 1,15,25-30 * 1-5";
        assertThrows(RuntimeException.class, () -> new CronExpression(input).getFieldsAndValues());
    }

    private List<Integer> range(int start, int end) {
        return range(start, end, 1);
    }

    private List<Integer> range(int start, int end, int step) {
        List<Integer> list = new java.util.ArrayList<>();
        for (int i = start; i < end; i += step) {
            list.add(i);
        }
        return list;
    }
}