package com.abundantsalmon.api579calculator.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

// @TODO change to a MVP approach, currently a mash of MVP all in one
// * Make FXML for window
// * Make controller for FXML
// * Modify Measurements class to accept input/output like a model (currently designed to only get created and do calculations, not change values)
public class ThicknessMeasurementWindow {
    private final Stage stage = new Stage();
    private final TextArea thicknessMeasurementsTextArea = new TextArea("2.6, 2.6, 2.6, 2.6, 2.6, 2.6, 2.6");
    private final TextField flawLongitudinalLengthTextField = new TextField("100.0");
    private final DatePicker measurementDatePicker = new DatePicker();
    private final TextField notesTextField = new TextField("thickness measurement test notes");

    public ThicknessMeasurementWindow()
    {
        Label label = new Label("Enter thickness measurements separated by commas:");

        Button oKButton = new Button("OK");
        Button clearButton = new Button("Clear");

        Label flawLongitudinalLengthLabel = new Label("Flaw Longitudinal Length: ");
        HBox flawLongitudinalLengthHBox = new HBox(flawLongitudinalLengthLabel, flawLongitudinalLengthTextField);
        flawLongitudinalLengthHBox.setAlignment(Pos.CENTER_LEFT);
        flawLongitudinalLengthHBox.setPadding(new Insets(5));

        Label measurementDateLabel = new Label("Date of Measurements: ");
        HBox measurementDateHBox = new HBox(measurementDateLabel, measurementDatePicker);
        measurementDateHBox.setAlignment(Pos.CENTER_LEFT);
        measurementDateHBox.setPadding(new Insets(5));

        Label notesLabel = new Label("Notes: ");
        HBox notesHBox = new HBox(notesLabel, notesTextField);
        notesHBox.setAlignment(Pos.CENTER_LEFT);
        notesHBox.setPadding(new Insets(5));

        HBox buttonHBox = new HBox(oKButton, clearButton);
        buttonHBox.setSpacing(10.0);
        buttonHBox.setPadding(new Insets(5));
        buttonHBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(label, thicknessMeasurementsTextArea, flawLongitudinalLengthHBox, measurementDateHBox, notesHBox, buttonHBox);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene secondScene = new Scene(vBox, 500, 250);

        // Set button actions
        oKButton.setOnAction(event1 -> stage.close());
        clearButton.setOnAction(event1 -> thicknessMeasurementsTextArea.clear());

        // Set up stage
        stage.setScene(secondScene);
        stage.setTitle("Thickness Measurements");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(UiFX.getPrimaryStage());
    }

    public void openWindow()
    {
        stage.show();
    }

    public String getThicknessMeasurementsString()
    {
        return thicknessMeasurementsTextArea.getText();
    }

    public String getFlawLongitudinalLengthString()
    {
        return flawLongitudinalLengthTextField.getText();
    }

    public TextField getFlawLongitudinalLengthTextField() {
        return flawLongitudinalLengthTextField;
    }

    public LocalDate getMeasurementDate()
    {
        return measurementDatePicker.getValue();
    }

    public DatePicker getMeasurementDatePicker() {
        return measurementDatePicker;
    }

    public String getNotes() {
        return notesTextField.getText();
    }

}
