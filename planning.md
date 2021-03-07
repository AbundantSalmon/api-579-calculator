# API 579-1 (2016) Fitness-for-Service
The program will be able to perform basic calculations (Level 1 for now) as per Part 4 - Assessment of General Metal Loss.

## Part 4 - Assessment of General Metal Loss Level 1

### Flow
    1. Determine t_min
    2. Thickness measurements of metal on equipment
        1. Point thickness readings
        2. Inspection planes and take thickness profile data
            1. Determine t_mm and L
            2. Determine CTP's inthe longitudinal and circumferential directions
            3. Determine s, c, and t_am for the CTP's
    3. Determine type B or C component
    4. Perform calculations based on conditions
    5. Determine if successful or need to send them to level 2 or level 3
        
### Assumptions
    1. Only assessing piping
    2. Not in cyclic service.
    3. Only assessing Type A Components (P4.2.5), i.e. Level 1 Assessment

### Inputs 
    1. Equipment data
        1. Piping
            1. Line Size (Diameter)
            2. Schedule
                1. Nominal Thickness
            3. Straight or Elbow
                1. Radius if Elbow
            4. Temperature 
            5. Design Pressure
            6. Design Allowable Stress
            7. Quality Factor (E)
            8. Weld Joint strength reduction (W)
            9. Y Coefficent
            10. Corrosion allowance (CA)
    2. Thickness readings 
        1. Measurement Date
        2. Point thickness (Minimum of 15 thickness recommended)
        3. Thickness profiles
        4. Measurement location (Elbow Introdos/Extrados)
    3. User/Inspector/Engineer Data
        1. Report Date
        2. ID
    4. Applicability Questions
    5. Corrosion rate


### Outputs
    1. Point thickness
        1. t_mm
        2. t_am
        3. COV
        4. Average measured thickness or MAWP acceptance criterion
    2. Critical Thickness Profile
        1. t_mm
        2. t_ml
        3. D_ml
        4. R_t
        5. L
        6. t^s_am
        7. t^c_am
        8. Average measured thickness or MAWP acceptance criterion
    3. Graphical Representation
        1. Remaining life R_life
    4. Suggestion of result


### Calculations
    1. If thickness readings Coefficient Of Variation (COV) is greater than 10%, the warn user than thickness profiles should be used instead
    2.

## Todo

[ ] The *user* is able to enter appropriate data (measurements, physical properties, etc) to be used in the General Metal Loss Assessment
[ ] The *user* is able to generate results from the calculations defined in Part 4 - Assessment of General Metal Loss
[ ] The *user* is able to generate a diagram showing a representation of the metal loss over time and future predicted metal loss
[ ] The *user* is able to generate a pdf report the calculation
[ ] The *user* is able to store the calculation and recall the calculation for further modification

[ ] Write or reference the assumptions utilises by the standard's assessment
