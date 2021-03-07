/**
 *
 */

package api579calculator;

// potenially extend to pipe-straight and pipe-elbow later

// all values in mm, MPa and N
public class Pipe
{
    public static enum PipeType
    {
        STRAIGHT,
        ELBOW
    }

    private double outerDiameter = 0.0;
    private static final double MINOUTERDIAMETER = 0.0;

    private double thickness = 0.0; 
    private static final double MINTHICKNESS = 0.0;

    private PipeType type = PipeType.STRAIGHT;

    private double elbowRadius = 0.0;
    private static final double MINELBOWRADIUS = 0.0;

    private double designPressure = 0.0;
    private static final double MINDESIGNPRESSURE = 0.0;

    private double designAllowableMaterialStress = 0.0;
    private static final double MINDESIGNALLOWABLEMATERIALSTRESS = 0.0;
    
    private double eFactor = 1.0; // Quality Factor
    private static final double MINEFACTOR = 0.0;
    private static final double MAXEFACTOR = 1.0; // need to check if appropriate max

    private double wFactor = 1.0; // Weld Joint Strength Reduction Factor
    private static final double MINWFACTOR = 0.0;
    private static final double MAXWFACTOR = 1.0; // need to check if appropriate max

    private double yFactor = 0.4; // Temperature Material Coefficento
    private static final double MINYFACTOR = 0.0;
    private static final double MAXYFACTOR = 1.0; // need to check if appropriate max

    private double corrosionAllowance = 1.0;
    private static final double MINCORROSIONALLOWANCE = 0.0;

    // Straight Pipe Constructor
    Pipe(double outerDiameter, double thickness, PipeType type, double designPressure, double designAllowableMaterialStress, double eFactor, double wFactor, double yFactor, double corrosionAllowance)
    {
        if(outerDiameter > MINOUTERDIAMETER)
        {
            this.outerDiameter = outerDiameter;
        } else {
            // throw exception
        }

        if(thickness > MINTHICKNESS)
        {
            this.thickness = thickness;
        } else {
            // throw exception
        }

        if(type == PipeType.STRAIGHT)
        {
            this.type = type;
        } else {
            // throw exception
        }

        if(designPressure > MINDESIGNPRESSURE)
        {
            this.designPressure = designPressure;
        } else {
            // throw exception
        }

        if(designAllowableMaterialStress > MINDESIGNALLOWABLEMATERIALSTRESS)
        {
            this.designAllowableMaterialStress = designAllowableMaterialStress;
        } else {
            // throw exception
        }

        if(eFactor >= MINEFACTOR && eFactor <= MAXEFACTOR)
        {
            this.eFactor = eFactor;
        } else {
            // throw exception
        }

        if(wFactor >= MINWFACTOR) 
        {
            this.wFactor = wFactor;
        } else {
            // throw exception
        }

        if(yFactor >= MINYFACTOR)
        {
            this.yFactor = yFactor;
        } else {
            // throw exception
        }

        if(corrosionAllowance >= MINCORROSIONALLOWANCE)
        {
            this.corrosionAllowance = corrosionAllowance;
        } else {
            // throw exception
        }
    }

    // Elbow Constructor
    Pipe(double outerDiameter, double thickness, PipeType type, double elbowRadius, double designPressure, double designAllowableMaterialStress, double eFactor, double wFactor, double yFactor, double corrosionAllowance)
    {
        this(outerDiameter, thickness, type, designPressure, designAllowableMaterialStress, eFactor, wFactor, yFactor, corrosionAllowance);
        if(type != PipeType.ELBOW)
        {
            // throw exception
        } else {
            if(elbowRadius >= MINELBOWRADIUS)
            {
                this.elbowRadius = elbowRadius;
            } else {
                // throw exception
            }
        }
    }

    // prints pipe info
    public void printPipeInfo()
    {
        System.out.println( type + "\n" +
                            outerDiameter + "\n" +
                            thickness);
    }
}
