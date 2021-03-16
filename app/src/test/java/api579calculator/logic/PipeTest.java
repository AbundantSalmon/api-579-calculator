package api579calculator.logic;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class PipeTest {
    double DELTA = 1e-8; // delta used for double comparisons

    Pipe testPipe = new Pipe(21.3,
            2.77,
            2.7,
            Pipe.PipeType.STRAIGHT,
            15.0,
            138.0,
            1.0,
            1.0,
            0.4,
            0,
            LocalDate.of(2020,3,24),
            "Use for testing purposes");
    @Test
    public void calculateTC_min() {
        assertEquals(1.109375, testPipe.calculateTC_min(), DELTA);
    }

    @Test
    public void calculateTL_min() {
        assertEquals(0.5546875, testPipe.calculateTL_min(), DELTA);
    }

    @Test
    public void calculateMAWPC() {
        assertEquals(38.93416928, testPipe.calculateMAWPC(), DELTA);
    }

    @Test
    public void calculateMAWPL() {
        assertEquals(87.77385159, testPipe.calculateMAWPL(), DELTA);
    }

    @Test
    public void calculateT_min() {
        assertEquals(1.109375, testPipe.calculateT_min(), DELTA);
    }

    @Test
    public void calculateMAWP() {
        assertEquals(38.93416928, testPipe.calculateMAWP(), DELTA);
    }

}