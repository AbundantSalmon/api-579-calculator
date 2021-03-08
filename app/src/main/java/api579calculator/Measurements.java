package api579calculator;

import java.util.Arrays;

/**
 * Point thickness measurements
 */

public class Measurements { // need to consider thickness profile measurements later
    private static final int MINMINMUMPOINTS = 15; // recommended minimum number of point thickness measurements

    public enum MeasurementLocation {
        STRAIGHT,
        INTRODOS,
        EXTRADOS
    }

    private final double[] thicknessMeasurements;
    private MeasurementLocation measurementLocation;
    private String notes;

    /**
     * Instantiates a new Measurements.
     *
     * @param thicknessMeasurements the thickness measurements
     */
    Measurements(double[] thicknessMeasurements, MeasurementLocation measurementLocation, String notes)
    {
        if(!(thicknessMeasurements.length > 0.0)) throw new IllegalArgumentException(); // need to at least have one measurement value
        for(double measurement: thicknessMeasurements)
        {
            if(measurement <= 0.0) throw new IllegalArgumentException(); // thickness measurements cannot be negative or zeroS
        }
        this.thicknessMeasurements = thicknessMeasurements; // else there are not identified issues with the thickness measurements by themselves

        this.measurementLocation = measurementLocation;
        this.notes = notes;
    }

    /**
     * Gets t_mm.
     *
     * @return the t_mm
     */
    public double getT_mm()
    {
        Arrays.sort(thicknessMeasurements); // sort values in thicknessMeasurements array
        return thicknessMeasurements[0]; // return the smallest value in as per the sorted array
    }

    /**
     * Gets t_am.
     *
     * @return the t_am
     */
    public double getT_am()
    {
        double sum = 0.0;
        for(double measurement : thicknessMeasurements)
        {
            sum += measurement;
        }
        return sum/thicknessMeasurements.length;
    }

    /**
     * Gets COV.
     *
     * @return the COV
     */
    public double getCOV() {
        double[] intermediateArray = Arrays.copyOf(thicknessMeasurements, thicknessMeasurements.length);
        double t_am = getT_am();
        // each thickness reading - t_am
        for (int i = 0; i < intermediateArray.length; ++i) {
            intermediateArray[i] -= t_am;
        }

        // square of each thickness reading
        for (int i = 0; i < intermediateArray.length; ++i) {
            intermediateArray[i] *= intermediateArray[i];
        }

        // sum of each thickness reading
        double sum = 0.0;
        for (double reading : intermediateArray) {
            sum += reading;
        }

        // COV = 1/t_am * (S/(N-1))^0.5
        return 1 / t_am * Math.pow((sum / (intermediateArray.length - 1)), 0.5);
    }

    /**
     * Gets minimum recommend number of points.
     *
     * @return the minimum recommend number of points
     */
    public static int getMINMINMUMPOINTS() {
        return MINMINMUMPOINTS;
    }

    /**
     * Is minimum recommended points boolean.
     *
     * @return the boolean
     */
    public boolean isMinimumRecommendedPoints()
    {
        return thicknessMeasurements.length >= getMINMINMUMPOINTS();
    }

    /**
     * Gets measurement location.
     *
     * @return the measurement location
     */
    public MeasurementLocation getMeasurementLocation() {
        return measurementLocation;
    }

    /**
     * Gets notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }
}
