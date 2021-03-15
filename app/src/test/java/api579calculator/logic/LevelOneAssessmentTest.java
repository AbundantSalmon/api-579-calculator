package api579calculator.logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LevelOneAssessmentTest {

    double DELTA = 1e-8; // delta used for double comparisons

    // Straight test Pipe
    Pipe straightTestPipe = new Pipe(21.3,
            2.77,
            2.7,
            Pipe.PipeType.STRAIGHT,
            15.0,
            138.0,
            1.0,
            1.0,
            0.4,
            0,
            "Use for testing purposes");

    // Measurement points test
    double[] points = {2.6,2.6,2.6,2.6,2.6,2.6,2.6};
    Measurements testPoints = new Measurements(
            points,
            100.0,
            Measurements.MeasurementLocation.STRAIGHT,
            "Used for testing purposes");

    // Assessment
    LevelOneAssessment testAssessment = new LevelOneAssessment(straightTestPipe,testPoints);

    @Test
    public void isAcceptableAverageMeasuredThicknessPTR() {
        assertTrue(testAssessment.isAcceptableAverageMeasuredThicknessPTR());
    }

    @Test
    public void isAcceptableMAWPPTR() {
        assertTrue(testAssessment.isAcceptableMAWPPTR());
    }

    @Test
    public void isAcceptableMinimumMeasuredThickness() {
        assertTrue(testAssessment.isAcceptableMinimumMeasuredThickness());
    }

    @Test
    public void isFitForService() {
        assertTrue(testAssessment.isFitForService());
    }

    @Test
    public void calculateRemainingThicknessRatio() {
        assertEquals(0.962962963,testAssessment.calculateRemainingThicknessRatio(),DELTA);
    }

    @Test
    public void calculateLongitudinalFlawLengthParameter() {
        assertEquals(19.61203607,testAssessment.calculateLongitudinalFlawLengthParameter(),DELTA);
    }

    @Test
    public void calculateFoliasFactor() {
        assertEquals(22.61721802,testAssessment.calculateFoliasFactor(),DELTA);
    }

    @Test
    public void calculateRSF() {
        assertEquals(0.964542458,testAssessment.calculateRSF(),DELTA);
    }

    @Test
    public void calculateMAWPCr() {
        assertEquals(37.33610822060354,testAssessment.calculateMAWPCr(testPoints.getT_am()),DELTA);
    }
}