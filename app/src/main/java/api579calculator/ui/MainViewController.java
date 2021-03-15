package api579calculator.ui;

import api579calculator.logic.LevelOneAssessment;
import api579calculator.logic.Measurements;
import api579calculator.logic.Pipe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainViewController {
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

    @FXML // fx:id="notes"
    private TextField notes; // Value injected by FXMLLoader

    @FXML // fx:id="calculateButton"
    private Button calculateButton; // Value injected by FXMLLoader

    @FXML // fx:id="codeArea"
    private CodeArea codeArea; // Value injected by FXMLLoader

    /**
     * "Calculate" button pressed
     * Performs calculation and updates the view with the results
     * @param event
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
                notes.getText());
        // Measurement points test @TODO add measurement input capability
        Measurements testPoints = new Measurements(new double[]{2.6, 2.6, 2.6, 2.6, 2.6, 2.6, 2.6},
                100.0,
                Measurements.MeasurementLocation.STRAIGHT,
                "Used for testing purposes");

        // Assessment
        LevelOneAssessment assessment = new LevelOneAssessment(inputPipe,testPoints);
        codeArea.replaceText(0, codeArea.getLength(), assessment.getAssessmentResults());
    }

    // Runs after @FXML fields are injected
    @FXML
    public void initialize() {
        // Initialise codeArea for syntax highlighting
        codeArea.textProperty().addListener(
                (obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });
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

}
