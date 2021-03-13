package api579calculator.logic;

/**
 * Pipe class used to describe pipes and it's properties under analysis.
 * All values in mm, MPa and N.
 * Current calculations only consider straight pipes.
 *
 * @author AbundantSalmon
 */
public class Pipe //potentially extend to children pipe-straight and pipe-elbow later
{
    private static final double MINOUTERDIAMETER = 0.0;
    private static final double MINTHICKNESS = 0.0;
    private static final double MINDESIGNPRESSURE = 0.0;
    private static final double MINDESIGNALLOWABLEMATERIALSTRESS = 0.0;
    private static final double MINEFACTOR = 0.0;
    private static final double MAXEFACTOR = 1.0; // need to check if appropriate max
    private static final double MINWFACTOR = 0.0;

//    private double elbowRadius;
//    private static final double MINELBOWRADIUS = 0.0;
    private static final double MAXWFACTOR = 1.0; // need to check if appropriate max
    private static final double MINYFACTOR = 0.0;
    private static final double MAXYFACTOR = 1.0; // need to check if appropriate max
    private static final double MINCORROSIONALLOWANCE = 0.0;
    private final double outerDiameter;
    private final double nomThickness;
    private final double corrThickness;
    private final PipeType type;
    private final double designPressure;
    private final double designAllowableMaterialStress;
    private final double eFactor; // Quality Factor
    private final double wFactor; // Weld Joint Strength Reduction Factor
    private final double yFactor; // Temperature Material Coefficient
    private final double corrosionAllowance;
    private final double tSL = 0.0; // supplemental thickness for mechanical loads other than pressure.
    private final String notes;
                                    // not currently used as assuming Type A Component.

    /**
     * Instantiates a new Straight Pipe.
     *
     * @param outerDiameter                 the outer diameter
     * @param nomThickness                  the nominal thickness
     * @param corrThickness                 the corroded thickness
     * @param type                          the type
     * @param designPressure                the design pressure
     * @param designAllowableMaterialStress the design allowable material stress
     * @param eFactor                       the e factor
     * @param wFactor                       the w factor
     * @param yFactor                       the y factor
     * @param corrosionAllowance            the corrosion allowance
     * @param notes                         the notes
     */
    public Pipe(double outerDiameter,
                double nomThickness,
                double corrThickness,
                PipeType type,
                double designPressure,
                double designAllowableMaterialStress,
                double eFactor,
                double wFactor,
                double yFactor,
                double corrosionAllowance,
                String notes)
    {
        if(outerDiameter > MINOUTERDIAMETER)
        {
            this.outerDiameter = outerDiameter;
        } else {
            throw new IllegalArgumentException();
        }

        if(nomThickness > MINTHICKNESS)
        {
            this.nomThickness = nomThickness;
        } else {
            throw new IllegalArgumentException();
        }

        if(corrThickness > MINTHICKNESS)
        {
            this.corrThickness = corrThickness;
        } else {
            throw new IllegalArgumentException();
        }

        if(type == PipeType.STRAIGHT || type == PipeType.ELBOW)
        {
            this.type = type;
        } else {
            throw new IllegalArgumentException();
        }

        if(designPressure > MINDESIGNPRESSURE)
        {
            this.designPressure = designPressure;
        } else {
            throw new IllegalArgumentException();
        }

        if(designAllowableMaterialStress > MINDESIGNALLOWABLEMATERIALSTRESS)
        {
            this.designAllowableMaterialStress = designAllowableMaterialStress;
        } else {
            throw new IllegalArgumentException();
        }

        if(eFactor >= MINEFACTOR && eFactor <= MAXEFACTOR)
        {
            this.eFactor = eFactor;
        } else {
            throw new IllegalArgumentException();
        }

        if(wFactor >= MINWFACTOR && wFactor <= MAXWFACTOR)
        {
            this.wFactor = wFactor;
        } else {
            throw new IllegalArgumentException();
        }

        if(yFactor >= MINYFACTOR && yFactor <= MAXYFACTOR)
        {
            this.yFactor = yFactor;
        } else {
            throw new IllegalArgumentException();
        }

        if(corrosionAllowance >= MINCORROSIONALLOWANCE)
        {
            this.corrosionAllowance = corrosionAllowance;
        } else {
            throw new IllegalArgumentException();
        }

        this.notes = notes;
    }

    /**
     * Print pipe info to console.
     */
    public void printPipeInfo()
    {
        System.out.println( "Pipe Data:\n" +
                            "Type: " + getType() + "\n" +
                            "OD: " + getOuterDiameter() + "\n" +
                            "Nominal THK: " + getNomThickness() + "\n" +
                            "Corroded THK: " + getCorrThickness() + "\n" +
                            //"Elbow Radius: " + getElbowRadius() + "\n" +
                            "Design Pressure: " + getDesignPressure() + "\n" +
                            "Design Allowable Stress: " + getDesignAllowableMaterialStress() + "\n" +
                            "E: " + geteFactor() + "\n" +
                            "W: " + getwFactor() + "\n" +
                            "y: " + getyFactor() + "\n" +
                            "CA: " + getCorrosionAllowance() + "\n" +
                            "t_min: " + calculateT_min() + "\n" +
                            "MAWP: " + calculateMAWP() + "\n" +
                            "Notes: " + getNotes() + "\n" //+
                            //"sigma_max: " + calculateSigmaMax() + "\n"
        );
    }

//    /**
//     * Instantiates a new Elbow Pipe.
//     *
//     * @param outerDiameter                 the outer diameter
//     * @param corrThickness                     the thickness
//     * @param type                          the type
//     * @param elbowRadius                   the elbow radius
//     * @param designPressure                the design pressure
//     * @param designAllowableMaterialStress the design allowable material stress
//     * @param eFactor                       the e factor
//     * @param wFactor                       the w factor
//     * @param yFactor                       the y factor
//     * @param corrosionAllowance            the corrosion allowance
//     * @param notes                         the notes
//     */
//    Pipe(double outerDiameter, double corrThickness, PipeType type, double elbowRadius, double designPressure, double designAllowableMaterialStress, double eFactor, double wFactor, double yFactor, double corrosionAllowance, String notes)
//    {
//        this(outerDiameter, corrThickness, type, designPressure, designAllowableMaterialStress, eFactor, wFactor, yFactor, corrosionAllowance, notes);
//        if(type != PipeType.ELBOW)
//        {
//            throw new IllegalArgumentException("This constructor is only for elbow pipes");
//        } else {
//            if(elbowRadius >= MINELBOWRADIUS)
//            {
//                this.elbowRadius = elbowRadius;
//            } else {
//                throw new IllegalArgumentException();
//            }
//        }
//    }

    /**
     * Gets outer diameter.
     *
     * @return the outer diameter
     */
    public double getOuterDiameter() {
        return outerDiameter;
    }

    /**
     * Gets nominal thickness.
     *
     * @return the nomThickness
     */
    public double getNomThickness() {
        return nomThickness;
    }

    /**
     * Gets corroded thickness.
     *
     * @return the corrThickness
     */
    public double getCorrThickness() {
        return corrThickness;
    }

    /**
     * Gets PipeType.
     *
     * @return the type
     */
    public PipeType getType() {
        return type;
    }

    /**
     * Gets design pressure.
     *
     * @return the design pressure
     */
    public double getDesignPressure() {
        return designPressure;
    }

//    /**
//     * Gets elbow radius.
//     *
//     * @return the elbow radius if Elbow pipe, else NaN
//     */
//    public double getElbowRadius() {
//        if(type == PipeType.ELBOW)
//        {
//            return elbowRadius;
//        }  else {
//            return Double.NaN;
//        }
//    }

    /**
     * Gets design allowable material stress.
     *
     * @return the design allowable material stress
     */
    public double getDesignAllowableMaterialStress() {
        return designAllowableMaterialStress;
    }

    /**
     * Gets the E factor.
     *
     * @return the E factor
     */
    public double geteFactor() {
        return eFactor;
    }

    /**
     * Gets the W factor.
     *
     * @return the W factor
     */
    public double getwFactor() {
        return wFactor;
    }

    /**
     * Gets the y factor.
     *
     * @return the y factor
     */
    public double getyFactor() {
        return yFactor;
    }

    /**
     * Gets corrosion allowance.
     *
     * @return the corrosion allowance
     */
    public double getCorrosionAllowance() {
        return corrosionAllowance;
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
     * Calculate t^c_min as per 2C.146.
     *
     * @return the t^c_min
     */
    public double calculateTC_min()
    {
        return  (designPressure*outerDiameter)
                /
                (2*(designAllowableMaterialStress*eFactor+designPressure*yFactor)) +
                corrosionAllowance;
    }

    /**
     * Calculate t^L_min as per 2C.149.
     *
     * @return the t^L_min
     */
    public double calculateTL_min()
    {
        return  (designPressure*outerDiameter)
                /
                (4*(designAllowableMaterialStress*eFactor+designPressure*yFactor)) +
                tSL +
                corrosionAllowance;
    }

    /**
     * Calculate MAWP^C as per 2C.147.
     *
     * @return the MAWP^C
     */
    public double calculateMAWPC()
    {
        return calculateMAWPC(corrThickness); // calculate using overloaded function, t_am = t_corr
    }

    /**
     * Calculate MAWP^C as per 2C.147.
     * Overloaded to allow calculation with t_am which is thickness provide that is not t_corr,
     * used for Acceptance criteria Table 4.4
     *
     * @return the MAWP^C
     */
    public double calculateMAWPC(double t_am)
    {
        return  (2*designAllowableMaterialStress*eFactor*(t_am))
                /
                (outerDiameter-2*yFactor*(t_am));
    }

    /**
     * Calculate MAWP^L as per 2C.150.
     *
     * @return the MAWP^L
     */
    public double calculateMAWPL()
    {
        return  (4*designAllowableMaterialStress*eFactor*(corrThickness -tSL))
                /
                (outerDiameter-4*yFactor*(corrThickness -tSL));
    }

    /**
     * Calculate t_min as per 2C.152.
     *
     * @return the t_min
     */
    public double calculateT_min()
    {
        return Math.max(calculateTC_min(), calculateTL_min());
    }

//    /**
//     * Calculate sigma^C_m as per 2C.148.
//     *
//     * @return the sigma^C_m
//     */
//    public double calculateSigmaC_m()
//    {
//        return  (designPressure/eFactor)*
//                (outerDiameter/(2*(corrThickness))-
//                yFactor);
//    }
//
//    /**
//     * Calculate sigma^L_m as per 2C.151.
//     *
//     * @return the sigma^L_m
//     */
//    public double calculateSigmaL_m()
//    {
//        return  (designPressure/eFactor)*
//                (outerDiameter/(4*(thickness-tSL))-
//                        yFactor);
//    }

    /**
     * Calculate MAWP as per 2C.153.
     *
     * @return the mawp
     */
    public double calculateMAWP()
    {
        return Math.min(calculateMAWPC(), calculateMAWPL());
    }

    /**
     * The enum PipeType.
     * Two types of pipes available for analysis.
     */
    public enum PipeType
    {
        STRAIGHT,
        ELBOW           // Not currently used right now
    }


//    /**
//     * Gets sigma_max as per 2C.154.
//     *
//     * @return the sigma_max
//     */
//    public double calculateSigmaMax()
//    {
//        return Math.max(calculateSigmaC_m(), calculateSigmaL_m());
//    }
}
