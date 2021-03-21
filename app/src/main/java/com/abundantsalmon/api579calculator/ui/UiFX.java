package com.abundantsalmon.api579calculator.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UiFX extends Application {
    public static final String APPLICATION_WINDOW_TITLE = "API 579-1 Straight Pipe FFS Calculator";
    private static final double WINDOW_WIDTH = 800.0;
    private static final double WINDOW_HEIGHT = 600.0;

    private static Stage primaryStage; // Declare Stage member so it is accessible

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        primaryStage.setTitle(APPLICATION_WINDOW_TITLE);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        UiFX.primaryStage = primaryStage;

        // used for RichTextFX codeArea text highlighting
        scene.getStylesheets().add(getClass().getResource("mainView.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Static method to close primary stage.
     */
    public static void closePrimaryStage()
    {
        primaryStage.close();
    }


    /**
     * Gets primary stage.
     *
     * @return the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
