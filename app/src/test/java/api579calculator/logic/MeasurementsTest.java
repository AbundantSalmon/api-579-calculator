package api579calculator.logic;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class MeasurementsTest {

    double DELTA = 1e-10; // delta used for double comparisons

    @Test
    public void getT_mm() {
        double[] measurements = {34.2,56.4,12.3};
        Measurements classUnderTest = new Measurements(measurements, 100, Measurements.MeasurementLocation.STRAIGHT, LocalDate.of(2021,4,5), "");
        assertEquals(12.3, classUnderTest.getT_mm(), DELTA);
    }

    @Test
    public void getT_am() {
        double[] measurements = {34.2, 56.4, 12.3};
        Measurements classUnderTest = new Measurements(measurements,100, Measurements.MeasurementLocation.STRAIGHT, LocalDate.of(2021,4,5), "");
        assertEquals(34.3, classUnderTest.getT_am(), DELTA);
    }

    @Test
    public void getCOV() {
        double[] measurements = {34.2, 56.4, 12.3};
        Measurements classUnderTest = new Measurements(measurements,100, Measurements.MeasurementLocation.STRAIGHT, LocalDate.of(2021,4,5), "");
        assertEquals(0.642862101, classUnderTest.getCOV(), DELTA);
    }

    @Test
    public void getMINMINMUMPOINTS() {
        assertEquals(15, Measurements.getMINMINMUMPOINTS());
    }

    @Test
    public void isMinimumRecommendedPoints() {
        double[] measurements1 = {34.2, 56.4, 12.3};
        Measurements classUnderTest1 = new Measurements(measurements1,100, Measurements.MeasurementLocation.STRAIGHT, LocalDate.of(2021,4,5), "");
        assertFalse(classUnderTest1.isMinimumRecommendedPoints());

        double[] measurements2 = {34.2, 56.4, 12.3, 345.5, 34.0, 1.0, 3.5, 8,9,10,11,12,13,14,15};
        Measurements classUnderTest2 = new Measurements(measurements2,100, Measurements.MeasurementLocation.STRAIGHT, LocalDate.of(2021,4,5), "");
        assertTrue(classUnderTest2.isMinimumRecommendedPoints());
    }
}