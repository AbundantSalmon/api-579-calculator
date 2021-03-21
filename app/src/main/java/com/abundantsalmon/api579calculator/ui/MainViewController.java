package com.abundantsalmon.api579calculator.ui;

import com.abundantsalmon.api579calculator.logic.LevelOneAssessment;
import com.abundantsalmon.api579calculator.logic.Measurements;
import com.abundantsalmon.api579calculator.logic.Pipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainViewController {

    @FXML // fx:id="closeMenuItem"
    private MenuItem closeMenuItem; // Value injected by FXMLLoader

    @FXML // fx:id="aboutMenuItem"
    private MenuItem aboutMenuItem; // Value injected by FXMLLoader

    @FXML // fx:id="outerDiameter"
    private TextField outerDiameter; // Value injected by FXMLLoader

    @FXML // fx:id="nomThickness"
    private TextField nomThickness; // Value injected by FXMLLoader

    @FXML // fx:id="corrThickness"
    private TextField corrThickness; // Value injected by FXMLLoader

    @FXML // fx:id="designPressure"
    private TextField designPressure; // Value injected by FXMLLoader

    @FXML // fx:id="designAllowableMaterialStress"
    private TextField designAllowableMaterialStress; // Value injected by FXMLLoader

    @FXML // fx:id="eFactor"
    private TextField eFactor; // Value injected by FXMLLoader

    @FXML // fx:id="wFactor"
    private TextField wFactor; // Value injected by FXMLLoader

    @FXML // fx:id="yFactor"
    private TextField yFactor; // Value injected by FXMLLoader

    @FXML // fx:id="corrosionAllowance"
    private TextField corrosionAllowance; // Value injected by FXMLLoader

    @FXML // fx:id="commissionDate"
    private DatePicker commissionDatePicker; // Value injected by FXMLLoader

    @FXML // fx:id="notes"
    private TextField notes; // Value injected by FXMLLoader

    @FXML // fx:id="corrosionRate"
    private TextField corrosionRate; // Value injected by FXMLLoader

    @FXML // fx:id="measurementButton"
    private Button measurementButton; // Value injected by FXMLLoader

    @FXML // fx:id="calculateButton"
    private Button calculateButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="codeArea"
    private CodeArea codeArea; // Value injected by FXMLLoader

    @FXML // fx:id="remainingLifeGraph"
    private LineChart<Number, Number> remainingLifeGraph;   // Value injected by FXMLLoader
    XYChart.Series<Number, Number> remainingLifeSeries;     // Series that will be displayed by remainingLifeGraph

    @FXML // fx:id="statusLabel"
    private Label statusLabel; // Value injected by FXMLLoader

    private final ThicknessMeasurementWindow thicknessMeasurementWindow = new ThicknessMeasurementWindow();

    // ArrayList of TextFields which need to be validated that a double has inputted by the user
    private final ArrayList<Pair<TextField, String>> doubleTextFields = new ArrayList<>();

    // ArrayList of DatePicker which need to be validated
    private final ArrayList<Pair<DatePicker, String>> datesToValidate = new ArrayList<>();

    /**
     * "Calculate" button pressed
     * Performs calculation and updates the view with the results
     * @param event "Calculate" button clicked
     */
    @FXML
    private void calculate(MouseEvent event) {
        // @TODO Validate inputs and handle invalid inputs
        if(!validateInputs())
        {
            // error in validation of inputs, do not proceed with rest of calculation
            return;
        }

        Pipe inputPipe = new Pipe(
                Double.parseDouble(outerDiameter.getText()),
                Double.parseDouble(nomThickness.getText()),
                Double.parseDouble(corrThickness.getText()),
                Pipe.PipeType.STRAIGHT,
                Double.parseDouble(designPressure.getText()),
                Double.parseDouble(designAllowableMaterialStress.getText()),
                Double.parseDouble(eFactor.getText()),
                Double.parseDouble(wFactor.getText()),
                Double.parseDouble(yFactor.getText()),
                Double.parseDouble(corrosionAllowance.getText()),
                commissionDatePicker.getValue(),
                notes.getText());

        // Parse thickness measurement values
        String[] thicknessPointsStrings = thicknessMeasurementWindow.getThicknessMeasurementsString().split(",");
        double[] thicknessPointsDoubles = new double[thicknessPointsStrings.length];
        for(int i = 0; i < thicknessPointsStrings.length; ++i)
        {
            thicknessPointsDoubles[i] = Double.parseDouble(thicknessPointsStrings[i]);
        }

        Measurements inputMeasurementPoints = new Measurements(
                thicknessPointsDoubles,
                Double.parseDouble(thicknessMeasurementWindow.getFlawLongitudinalLengthString()),
                Measurements.MeasurementLocation.STRAIGHT,
                thicknessMeasurementWindow.getMeasurementDate(),
                thicknessMeasurementWindow.getNotes());

        // Assessment
        LevelOneAssessment assessment = new LevelOneAssessment(inputPipe, inputMeasurementPoints, Double.parseDouble(corrosionRate.getText()));

        // Set codeArea with output
        codeArea.replaceText(0, codeArea.getLength(), assessment.getAssessmentResults());

        // Generate for chart and display
        ArrayList<Pair<Long,Double>> corrosionDateData = new ArrayList<>();
        corrosionDateData.add(new Pair<>(inputPipe.getCommissionDate().toEpochDay(), inputPipe.getNomThickness()));
        corrosionDateData.add(new Pair<>(inputMeasurementPoints.getMeasurementDate().toEpochDay(), inputMeasurementPoints.getT_am()));
        corrosionDateData.add(new Pair<>(assessment.predictedFailureDate().toEpochDay(), inputMeasurementPoints.getT_mm()));
        setRemainingLifeSeries(corrosionDateData);

        statusLabel.setText("Executed");
    }

    @FXML
    private void clearResults(MouseEvent event) {
        // Clear codeArea
        codeArea.clear();

        // Clear chart
        setRemainingLifeSeries();
    }

    @FXML
    private void closeProgram(ActionEvent event) {
        // Close program
        UiFX.closePrimaryStage();
    }

    @FXML
    private void aboutMenuItemOpen(ActionEvent event) {
        Label label = new Label(UiFX.APPLICATION_WINDOW_TITLE + "\nv0.1.0\n\u00a9 2021 AbundantSalmon");

        label.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(label);
        vBox.setPadding(new Insets(10));

        Scene secondScene = new Scene(vBox, 230, 100);
        Stage stage = new Stage();

        stage.setScene(secondScene);
        stage.setTitle("About");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(UiFX.getPrimaryStage());
        stage.show();
    }

    @FXML
    private void measurementButtonClick(ActionEvent event)
    {
        thicknessMeasurementWindow.openWindow();
    }

    // Runs after @FXML fields are injected
    @FXML
    public void initialize()
    {
        // Initialise doubleTextFields with all the TextFields that should be validated as doubles
        doubleTextFields.add(new Pair<>(outerDiameter, "Outer Diameter"));
        doubleTextFields.add(new Pair<>(nomThickness, "Nominal Thickness"));
        doubleTextFields.add(new Pair<>(corrThickness, "Corroded Thickness"));
        doubleTextFields.add(new Pair<>(designPressure, "Design Pressure"));
        doubleTextFields.add(new Pair<>(designAllowableMaterialStress, "Design Allowable Material Stress"));
        doubleTextFields.add(new Pair<>(eFactor, "E"));
        doubleTextFields.add(new Pair<>(wFactor, "W"));
        doubleTextFields.add(new Pair<>(yFactor, "y"));
        doubleTextFields.add(new Pair<>(corrosionAllowance, "Corrosion Allowance"));
        doubleTextFields.add(new Pair<>(corrosionRate, "Corrosion Rate"));
        doubleTextFields.add(new Pair<>(thicknessMeasurementWindow.getFlawLongitudinalLengthTextField(), "Flaw Longitudinal Length"));

        // Initialise datesToValidate with all DatePickers that should be validated as valid dates
        datesToValidate.add(new Pair<>(commissionDatePicker, "Commission Date"));
        datesToValidate.add(new Pair<>(thicknessMeasurementWindow.getMeasurementDatePicker(), "Thickness Measurement Date"));

        // Initialise codeArea for syntax highlighting
        codeArea.textProperty().addListener(
                (obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });

        // Initialise remainingLifeGraph
        remainingLifeGraph.getXAxis().setLabel("Date");
        remainingLifeGraph.getYAxis().setAutoRanging(true);
        remainingLifeGraph.getYAxis().setLabel("Remaining Thickness");
        remainingLifeGraph.getYAxis().setAutoRanging(true);
        remainingLifeGraph.setTitle("Remaining Life");
        remainingLifeGraph.setLegendVisible(false);
        ((NumberAxis) remainingLifeGraph.getXAxis()).setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number object) {
                return LocalDate.ofEpochDay(object.longValue()).toString();
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        // Initialise remainingLifeSeries
        remainingLifeSeries = new XYChart.Series<>();
        remainingLifeGraph.getData().add(remainingLifeSeries);
        setRemainingLifeSeries();

        System.out.println("MainViewControllerInitialised");
    }

    // ----------------------- Used for codeArea Styling

    // a lot of code modified from:
    // https://github.com/FXMisc/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/XMLEditorDemo.java
    // 2021-03-14

    // Regex pattern to find any text 'true' or 'false'
    private static final Pattern BOOLEAN_TAG = Pattern.compile("(?<BOOLEANTRUE>true)" + "|(?<BOOLEANFALSE>false)");

    private StyleSpans<Collection<String>> computeHighlighting(String text)
    {
        Matcher matcher = BOOLEAN_TAG.matcher(text); // Regex matcher
        int lastKwEnd = 0;  // Group end
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while(matcher.find()) {
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            if(matcher.group("BOOLEANTRUE") != null) {
                spansBuilder.add(Collections.singleton("boolean_true"), matcher.end() - matcher.start());
            } else if (matcher.group("BOOLEANFALSE") != null) {
                spansBuilder.add(Collections.singleton("boolean_false"), matcher.end() - matcher.start());
            }
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd); // add remaining text
        return spansBuilder.create();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^ Used for codeArea Styling

    private void setRemainingLifeSeries(ArrayList<Pair<Long,Double>> data)
    {
        // Clear any data currently in the series
        remainingLifeSeries.getData().clear();

        // Populate the series with the supplied data
        for(Pair<Long,Double> value: data)
        {
            remainingLifeSeries.getData().add(new XYChart.Data<>(value.getKey(), value.getValue()));
        }

        //@TODO add minimum thickness line

        // sets reasonable axis bounds
        remainingLifeGraph.getXAxis().setAutoRanging(false);
        remainingLifeGraph.getYAxis().setAutoRanging(false);
        NumberAxis xAxis = (NumberAxis) remainingLifeGraph.getXAxis();
        NumberAxis yAxis = (NumberAxis) remainingLifeGraph.getYAxis();
        xAxis.setLowerBound(data.get(0).getKey() - 30);
        xAxis.setUpperBound(data.get(data.size() - 1).getKey() + 30);
        yAxis.setLowerBound(data.get(0).getValue() - 1);
        yAxis.setUpperBound(data.get(data.size() - 1).getValue() + 1);

    }

    // overloaded default function, clears data
    private void setRemainingLifeSeries()
    {
        // Clear any data currently in the series
        remainingLifeSeries.getData().clear();
    }

    private boolean validateInputs()
    {
        // @TODO write tests for the validations

        // Validate user has supplied doubles to the text fields
        for(Pair<TextField,String> textFieldPair : doubleTextFields)
        {
            if(!isParsableAsDouble(textFieldPair.getKey().getText()))
            {
                statusLabel.setText("\u26A0 invalid input for: " + textFieldPair.getValue());
                return false;
            }
        }

        // Validate that dates have been supplied
        for(Pair<DatePicker,String> datePickerPair : datesToValidate)
        {
            if(datePickerPair.getKey().getValue() == null)
            {
                statusLabel.setText("\u26A0 invalid date provided: " + datePickerPair.getValue());
                return false;
            }
        }

        // Validate that commission date is before measurement date
        if(commissionDatePicker.getValue().compareTo(thicknessMeasurementWindow.getMeasurementDate()) > 0)
        {
            statusLabel.setText("\u26A0 invalid input: Commission Date needs to before Thickness Measurement Date");
            return false;
        }

        // Validate thickness measurements
        String[] thicknessPointsStrings = thicknessMeasurementWindow.getThicknessMeasurementsString().split(",");
        for(String value : thicknessPointsStrings)
        {
            if(!isParsableAsDouble(value))
            {
                statusLabel.setText("\u26A0 invalid input: Thickness Measurements");
                return false;
            }
        }

        return true;
    }

    private boolean isParsableAsDouble(String value)
    {
        try
        {
            Double.parseDouble(value);
        }
        catch(NumberFormatException e)
        {
            //not a double
            return false;
        }

        return true;
    }
}
