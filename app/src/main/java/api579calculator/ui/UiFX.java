package api579calculator.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UiFX extends Application {
    static final String APPLICATION_WINDOW_TITLE = "API 579-1 Straight Pipe FFS Calculator";
    static final double WINDOW_WIDTH = 800.0;
    static final double WINDOW_HEIGHT = 600.0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        primaryStage.setTitle(APPLICATION_WINDOW_TITLE);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // used for RichTextFX codeArea text highlighting
        scene.getStylesheets().add(getClass().getResource("mainView.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
