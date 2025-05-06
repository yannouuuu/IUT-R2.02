package semaine2.td02;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Evenements extends Application {
    Label label;

    class myClickHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            label.setText("Toto");
        }
    }

    public void start (Stage stage) {
        VBox root = new VBox();
        label = new Label("coucou");
        Button button = new Button("changer");
        button.setOnMouseClicked(new myClickHandler()); // .setReaction() 

        root.getChildren().addAll(label, button);

        Scene scene = new Scene(root);
        stage.setTitle("Ex1");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}