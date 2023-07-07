package Utilities;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeTest {

    @Test
    public void isValidTest1() {
        DateTime dateTime = new DateTime("29.02.23 23:00", 180);
        assertEquals(dateTime.isValid("29.02.23 23:00"), null);
    }

    @Test
    public void isValidTest2() {
        DateTime dateTime = new DateTime("29.02.24 23:00", 180);
        assertEquals(dateTime.isValid("29.02.24 23:00"), true);
    }


    @Test
    public void getOutputStartTest() {
        DateTime dateTime = new DateTime("28.02.23 23:00", 120);
        assertEquals(dateTime.getOutputStart(), "2023-02-28T23:00:00Z");
    }

    @Test
    public void getOutputEndTest1() {
        DateTime dateTime1 = new DateTime("28.02.23 23:00", 180);
        assertEquals(dateTime1.getOutputEnd(), "2023-03-01T02:00:00Z");
    }

    @Test
    public void getOutputEndTest2() {
        DateTime dateTime2 = new DateTime("28.02.24 23:00", 180);
        assertEquals(dateTime2.getOutputEnd(), "2024-02-29T02:00:00Z");
    }

    @Test
    public void getOutputEndTest3() {
        DateTime dateTime3 = new DateTime("30.06.23 23:00", 180);
        assertEquals(dateTime3.getOutputEnd(), "2023-07-01T02:00:00Z");
    }
}