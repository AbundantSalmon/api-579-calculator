package api579calculator.logic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * API 579 Fitness-for-Service - General Metal Loss - Level 1 - Type A - Straight Piping - PTR
 */
public class LevelOneAssessment {
    private final Pipe pipeUnderAssessment;
    private final Measurements thicknessMeasurements;

    /**
     * Instantiates a new Level one assessment.
     *
     * @param pipeUnderAssessment   the pipe under assessment
     * @param thicknessMeasurements the thickness measurements
     */
    public LevelOneAssessment(Pipe pipeUnderAssessment, Measurements thicknessMeasurements)
    {
        this.pipeUnderAssessment = pipeUnderAssessment;
        this.thicknessMeasurements = thicknessMeasurements;
    }

    /**
     * Is acceptable using the average measured thickness criterion Table 4.4.
     * Average Measured Thickness from Point Thickness Readings (PTR).
     * Assuming FCA_ml = 0
     *
     * @return the boolean
     */
    public boolean isAcceptableAverageMeasuredThicknessPTR()
    {
        return thicknessMeasurements.getT_am() >= pipeUnderAssessment.calculateTC_min();
    }

    /**
     * Is acceptable using the MAWP criterion Table 4.4.
     * MAWP from Point Thickness Readings (PTR).
     * Assuming FCA_ml = 0
     *
     * @return the boolean
     */
    public boolean isAcceptableMAWPPTR()
    {
        // Should compare against the Design Pressure of the Pipe,
        // not against the recalculated the MAWP based on the measured thickness
        return calculateMAWPCr(thicknessMeasurements.getT_am()) >= pipeUnderAssessment.getDesignPressure(); //pipeUnderAssessment.calculateMAWP();
    }

    /**
     * Is acceptable Minimum Measured Thickness Table 4.4.
     * Assuming FCA_ml = 0
     *
     * @return the boolean
     */
    public boolean isAcceptableMinimumMeasuredThickness()
    {
        double t_min = pipeUnderAssessment.calculateT_min();
        double t_lim = Math.max(0.2* pipeUnderAssessment.getNomThickness(), 2.5);
        return thicknessMeasurements.getT_mm() >= Math.max(0.5*t_min,t_lim);
    }

    /**
     * Is fit for service boolean.
     *
     * @return the boolean
     */
    public boolean isFitForService()
    {
        return (isAcceptableAverageMeasuredThicknessPTR() && isAcceptableMAWPPTR() && isAcceptableMinimumMeasuredThickness());
    }

    /**
     * Calculate remaining thickness ratio double R_t as per 4.4.2.2.
     *
     * @return the double
     */
    public double calculateRemainingThicknessRatio()
    {
        return thicknessMeasurements.getT_mm()/ pipeUnderAssessment.getCorrThickness();
    }

    /**
     * Calculate longitudinal flaw length parameter as per 5.4.2.2.
     *
     * @return the double
     */
    public double calculateLongitudinalFlawLengthParameter()
    {
        return 1.285*thicknessMeasurements.getFlawLongitudinalLength()/Math.sqrt((pipeUnderAssessment.getOuterDiameter()-2*pipeUnderAssessment.getCorrThickness())*pipeUnderAssessment.getCorrThickness());
    }

    /**
     * Calculate folias factor as per Table 5.2.
     *
     * @return the double
     */
    public double calculateFoliasFactor()
    {
        double lambda = calculateLongitudinalFlawLengthParameter();
        if(lambda > 20){
            lambda = 20;
        }
        return  1.0010
                -0.014195*  lambda
                +0.29090*   Math.pow(lambda,2)
                -0.096420*  Math.pow(lambda,3)
                +0.020890*  Math.pow(lambda,4)
                -0.0030540* Math.pow(lambda,5)
                +2.9570E-4* Math.pow(lambda,6)
                -1.8462E-5* Math.pow(lambda,7)
                +7.1553E-7* Math.pow(lambda,8)
                -1.5631E-8* Math.pow(lambda,9)
                +1.4656E-10*Math.pow(lambda,10);
    }

    /**
     * Calculate Remaining Service Factor (RSF) as per 5.4.2.2..
     *
     * @return the double
     */
    public double calculateRSF()
    {
        return  calculateRemainingThicknessRatio()/
                (1-1/calculateFoliasFactor()*(1-calculateRemainingThicknessRatio()));
    }

    /**
     * Calculate MAWP^C_r using t_am as per Table 4.4 and 2.4.2.2.
     *
     * @param t_am the t am
     * @return the double
     */
    public double calculateMAWPCr(double t_am)
    {
        double allowableRSF = 0.9; // 2.4.2.2d
        double RSF = calculateRSF();
        if(RSF < allowableRSF){
            return pipeUnderAssessment.calculateMAWPC(t_am) * (RSF / allowableRSF);
        } else {
            return pipeUnderAssessment.calculateMAWPC(t_am);
        }
    }

    /**
     * Print assessment results.
     */
    public void printAssessmentResults()
    {
        System.out.println("API 579 Fitness-for-Service - General Metal Loss - Level 1 - Type A - Straight Piping - PTR");
        System.out.println("---------------------------------------------");
        pipeUnderAssessment.printPipeInfo();
        System.out.println("---------------------------------------------");
        thicknessMeasurements.printMeasurementsDetails();
        System.out.println("---------------------------------------------");
        System.out.println("Assessment:");
        System.out.println("STEP 1:");
        System.out.println("Determine t_mm: " + thicknessMeasurements.getT_mm());
        System.out.println("Determine t_am: " + thicknessMeasurements.getT_am());
        System.out.println("Determine COV: " + thicknessMeasurements.getCOV());
        System.out.println();
        System.out.println("STEP 2:");
        System.out.println("Check COV <= 0.1: " + thicknessMeasurements.checkCOV());
        if(!thicknessMeasurements.checkCOV()) // display warning message
        {
            System.out.println("WARNING! API 579 recommends using thickness profiles instead of PTR due to the deviation of the obtained measurements");
        }
        System.out.println();
        System.out.println("STEP 3:");
        System.out.println("Checking Acceptance Criteria as per Table 4.4");
        System.out.println("Average Measured Thickness from Point Thickness Readings (PTR):");
        System.out.println("t_am >= t^c_min:");
        System.out.println(thicknessMeasurements.getT_am() + " >= " + pipeUnderAssessment.calculateTC_min() + ": " + isAcceptableAverageMeasuredThicknessPTR());
        System.out.println();
        System.out.println("MAWP from Point Thickness Readings (PTR):");
        System.out.println("MAWP^c_r >= MAWP (Design Pressure):");
        System.out.println(calculateMAWPCr(thicknessMeasurements.getT_am()) + " >= " + pipeUnderAssessment.getDesignPressure() + ": " + isAcceptableMAWPPTR());
        System.out.println();
        System.out.println("Minimum Measured Thickness:");
        System.out.println("t_mm >= max(0.5*t_min, t_lim):");
        System.out.println(thicknessMeasurements.getT_mm() + " >= max(" + 0.5*pipeUnderAssessment.calculateT_min()+", " +Math.max(0.2* pipeUnderAssessment.getNomThickness(), 2.5) + "): " + isAcceptableMinimumMeasuredThickness());
        System.out.println();
        System.out.println("Pipe is \"Fit-for-Service\": " + isFitForService());

    }

    public String getAssessmentResults()
    {
        // Capture system.out.println message and redirect to string to return

        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // Print some output: goes to your special stream
        printAssessmentResults();
        // Put things back
        System.out.flush();
        System.setOut(old);

        // return captured system.out.print
        return baos.toString();
    }
}
