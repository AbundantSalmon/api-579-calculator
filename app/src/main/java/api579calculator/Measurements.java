package api579calculator;

import java.util.Arrays;

/**
 * Point thickness measurements
 */

public class Measurements { // need to consider thickness profile measurements later
    private static final int MINIMUMPOINTS = 15; // recommended minimum number of point thickness measurements

    public enum MeasurementLocation {
        STRAIGHT,
        //INTRODOS,
        //EXTRADOS
    }

    private final double[] thicknessMeasurements;
    private final MeasurementLocation measurementLocation;
    private final double flawLongitudinalLength;              // s
    private final String notes;

    /**
     * Instantiates a new Measurements.
     *
     * @param thicknessMeasurements the thickness measurements
     */
    Measurements(double[] thicknessMeasurements, double flawLongitudinalLength, MeasurementLocation measurementLocation, String notes)
    {
        if(!(thicknessMeasurements.length > 0.0)) throw new IllegalArgumentException(); // need to at least have one measurement value
        for(double measurement: thicknessMeasurements)
        {
            if(measurement <= 0.0) throw new IllegalArgumentException(); // thickness measurements cannot be negative or zeroS
        }
        this.thicknessMeasurements = thicknessMeasurements; // else there are not identified issues with the thickness measurements by themselves

        if(flawLongitudinalLength >= 0){
            this.flawLongitudinalLength = flawLongitudinalLength;
        } else {
            throw new IllegalArgumentException();
        }
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
     * Check whether API 579 recommends the use of thickness profiles using the COV of the readings .
     *
     * @return the boolean
     */
    public boolean checkCOV()
    {
        double baselineCOV = 0.1;   // COV should be less than 10% as per 4.3.3.2c
                                    // else thickness profiles should be consider for use in the assessment
        return getCOV() <= 0.1;
    }

    /**
     * Gets minimum recommend number of points.
     *
     * @return the minimum recommend number of points
     */
    public static int getMINMINMUMPOINTS() {
        return MINIMUMPOINTS;
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
     * Gets flaw longitudinal length.
     *
     * @return the flaw longitudinal length
     */
    public double getFlawLongitudinalLength()
    {
        return flawLongitudinalLength;
    }

    /**
     * Gets measurement location.
     *
     * @return the measurement location
     */
    public MeasurementLocation getMeasurementLocation()
    {
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

    /**
     * Print measurements details.
     */
    public void printMeasurementsDetails()
    {
        System.out.println("Point Thickness Readings (PTR):");
        System.out.println("Notes: " + notes);
        for(int i = 0; i < thicknessMeasurements.length; ++i)
        {
            System.out.println("Location " + (i+1) + ": \t\t" + thicknessMeasurements[i]);
        }
        if(!isMinimumRecommendedPoints()) // display warning about minimum recommend number of thickness readings
        {
            System.out.println("WARNING! for PTR, API 579 recommends at least " + getMINMINMUMPOINTS() + " points.");
        }
    }
}
