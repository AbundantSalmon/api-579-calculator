package api579calculator.ui;

import api579calculator.logic.LevelOneAssessment;
import api579calculator.logic.Measurements;
import api579calculator.logic.Pipe;
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

    @FXML // fx:id="calculateButton"
    private Button calculateButton; // Value injected by FXMLLoader

    @FXML // fx:id="clearButton"
    private Button clearButton; // Value injected by FXMLLoader

    @FXML // fx:id="codeArea"
    private CodeArea codeArea; // Value injected by FXMLLoader

    @FXML // fx:id="remainingLifeGraph"
    private LineChart<Number, Number> remainingLifeGraph;   // Value injected by FXMLLoader
    XYChart.Series<Number, Number> remainingLifeSeries;     // Series that will be displayed by remainingLifeGraph

    /**
     * "Calculate" button pressed
     * Performs calculation and updates the view with the results
     * @param event "Calculate" button clicked
     */
    @FXML
    private void calculate(MouseEvent event) {
        // @TODO Validate inputs and handle invalid inputs
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
        // Measurement points test @TODO add measurement input capability
        Measurements testPoints = new Measurements(new double[]{2.6, 2.6, 2.6, 2.6, 2.6, 2.6, 2.6},
                100.0,
                Measurements.MeasurementLocation.STRAIGHT,
                LocalDate.of(2021,4,5),
                "Used for testing purposes");

        // Assessment
        LevelOneAssessment assessment = new LevelOneAssessment(inputPipe,testPoints, Double.parseDouble(corrosionRate.getText()));

        // Set codeArea with output
        codeArea.replaceText(0, codeArea.getLength(), assessment.getAssessmentResults());

        // Generate for chart and display
        ArrayList<Pair<Long,Double>> corrosionDateData = new ArrayList<>();
        corrosionDateData.add(new Pair<>(inputPipe.getCommissionDate().toEpochDay(), inputPipe.getNomThickness()));
        corrosionDateData.add(new Pair<>(testPoints.getMeasurementDate().toEpochDay(), testPoints.getT_am()));
        corrosionDateData.add(new Pair<>(assessment.predictedFailureDate().toEpochDay(), testPoints.getT_mm()));
        setRemainingLifeSeries(corrosionDateData);
    }

    @FXML
    private void clearResults(MouseEvent event) {
        // Clear codeArea
        codeArea.clear();

        // Clear chart
        setRemainingLifeSeries();
    }

    @FXML
    void closeProgram(ActionEvent event) {
        // Close program
        UiFX.closePrimaryStage();
    }

    @FXML
    void aboutMenuItemOpen(ActionEvent event) {
        Label label = new Label(UiFX.APPLICATION_WINDOW_TITLE + "\n\u00a9 2021 AbundantSalmon");

        label.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(label);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene secondScene = new Scene(vBox, 230, 100);
        Stage stage = new Stage();

        stage.setScene(secondScene);
        stage.setTitle("About");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(UiFX.getPrimaryStage());
        stage.show();
    }

    // Runs after @FXML fields are injected
    @FXML
    public void initialize() {
        // @TODO separate out into separate initialisation functions once setup

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
}
