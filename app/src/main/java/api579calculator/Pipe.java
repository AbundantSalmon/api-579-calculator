package api579calculator;

/**
 * Pipe class used to describe pipes and it's properties under analysis.
 * All values in mm, MPa and N.
 *
 * @author AbundantSalmon
 */
public class Pipe //potentially extend to children pipe-straight and pipe-elbow later
{
    /**
     * The enum PipeType.
     * Two types of pipes available for analysis.
     */
    public enum PipeType
    {
        STRAIGHT,
        ELBOW
    }

    private final double outerDiameter;
    private static final double MINOUTERDIAMETER = 0.0;

    private final double thickness;
    private static final double MINTHICKNESS = 0.0;

    private final PipeType type;

    private double elbowRadius;
    private static final double MINELBOWRADIUS = 0.0;

    private final double designPressure;
    private static final double MINDESIGNPRESSURE = 0.0;

    private final double designAllowableMaterialStress;
    private static final double MINDESIGNALLOWABLEMATERIALSTRESS = 0.0;
    
    private final double eFactor; // Quality Factor
    private static final double MINEFACTOR = 0.0;
    private static final double MAXEFACTOR = 1.0; // need to check if appropriate max

    private final double wFactor; // Weld Joint Strength Reduction Factor
    private static final double MINWFACTOR = 0.0;
    private static final double MAXWFACTOR = 1.0; // need to check if appropriate max

    private final double yFactor; // Temperature Material Coefficient
    private static final double MINYFACTOR = 0.0;
    private static final double MAXYFACTOR = 1.0; // need to check if appropriate max

    private final double corrosionAllowance;
    private static final double MINCORROSIONALLOWANCE = 0.0;

    private final String notes;

    /**
     * Instantiates a new Straight Pipe.
     *
     * @param outerDiameter                 the outer diameter
     * @param thickness                     the thickness
     * @param type                          the type
     * @param designPressure                the design pressure
     * @param designAllowableMaterialStress the design allowable material stress
     * @param eFactor                       the e factor
     * @param wFactor                       the w factor
     * @param yFactor                       the y factor
     * @param corrosionAllowance            the corrosion allowance
     * @param notes                         the notes
     */
    Pipe(double outerDiameter, double thickness, PipeType type, double designPressure, double designAllowableMaterialStress, double eFactor, double wFactor, double yFactor, double corrosionAllowance, String notes)
    {
        if(outerDiameter > MINOUTERDIAMETER)
        {
            this.outerDiameter = outerDiameter;
        } else {
            throw new IllegalArgumentException();
        }

        if(thickness > MINTHICKNESS)
        {
            this.thickness = thickness;
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
     * Instantiates a new Elbow Pipe.
     *
     * @param outerDiameter                 the outer diameter
     * @param thickness                     the thickness
     * @param type                          the type
     * @param elbowRadius                   the elbow radius
     * @param designPressure                the design pressure
     * @param designAllowableMaterialStress the design allowable material stress
     * @param eFactor                       the e factor
     * @param wFactor                       the w factor
     * @param yFactor                       the y factor
     * @param corrosionAllowance            the corrosion allowance
     */
    Pipe(double outerDiameter, double thickness, PipeType type, double elbowRadius, double designPressure, double designAllowableMaterialStress, double eFactor, double wFactor, double yFactor, double corrosionAllowance, String notes)
    {
        this(outerDiameter, thickness, type, designPressure, designAllowableMaterialStress, eFactor, wFactor, yFactor, corrosionAllowance, notes);
        if(type != PipeType.ELBOW)
        {
            throw new IllegalArgumentException("This constructor is only for elbow pipes");
        } else {
            if(elbowRadius >= MINELBOWRADIUS)
            {
                this.elbowRadius = elbowRadius;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Print pipe info to console.
     */
    public void printPipeInfo()
    {
        System.out.println( "The Pipe's Information are as follows:\n" +
                            "Notes: " + getNotes() + "\n" +
                            "Type: " + getType() + "\n" +
                            "OD: " + getOuterDiameter() + "\n" +
                            "THK: " + getThickness() + "\n" +
                            "Elbow Radius: " + getElbowRadius() + "\n" +
                            "Design Pressure: " + getDesignPressure() + "\n" +
                            "Design Allowable Stress: " + getDesignAllowableMaterialStress() + "\n" +
                            "E: " + geteFactor() + "\n" +
                            "W: " + getwFactor() + "\n" +
                            "y: " + getyFactor() + "\n" +
                            "CA: " + getCorrosionAllowance() + "\n"
        );
    }

    /**
     * Gets outer diameter.
     *
     * @return the outer diameter
     */
    public double getOuterDiameter() {
        return outerDiameter;
    }

    /**
     * Gets thickness.
     *
     * @return the thickness
     */
    public double getThickness() {
        return thickness;
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
     * Gets elbow radius.
     *
     * @return the elbow radius if Elbow pipe, else NaN
     */
    public double getElbowRadius() {
        if(type == PipeType.ELBOW)
        {
            return elbowRadius;
        }  else {
            return Double.NaN;
        }
    }

    /**
     * Gets design pressure.
     *
     * @return the design pressure
     */
    public double getDesignPressure() {
        return designPressure;
    }

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
}
