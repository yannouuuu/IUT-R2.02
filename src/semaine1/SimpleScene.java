package semaine1;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
// import javafx.geometry.Orientation;
// import javafx.scene.layout.FlowPane;
// import javafx.scene.layout.TilePane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.BorderPane;
// import javafx.geometry.Pos;

public class SimpleScene extends Application {

    public void start(Stage stage) {
        VBox root = new VBox();
        Label msg = new Label("Hello JavaFX");
        root.getChildren().add(msg);

        Scene scene = new Scene(root, 300, 50);
        stage.setScene(scene);
        stage.setTitle("Hello JavaFX");
    
        /* ---------- Style de fênetre ----------  */
        // Configuration de la fenêtre
        // stage.setFullScreen(true); // Mettre en plein écran
        // stage.setAlwaysOnTop(true); // Toujours devant les autres fenêtres
        // stage.setOpacity(0.2); // Régler l'opacité
        // stage.setResizable(false); // Rendre non redimensionnable

        // Styles de fenêtre
        // stage.initStyle(StageStyle.DECORATED); // Style par défaut avec décorations (barre de titre, bordures)
        // stage.initStyle(StageStyle.UNDECORATED); // Sans aucune décoration
        // stage.initStyle(StageStyle.TRANSPARENT); // Fenêtre transparente
        // stage.initStyle(StageStyle.UTILITY); // Fenêtre utilitaire

        /* ---------- FlowPane----------  */
        // // Crée un FlowPane avec une orientation horizontale (par défaut)
        // // et un espacement horizontal et vertical entre les éléments
        // FlowPane fp = new FlowPane(Orientation.HORIZONTAL, 5, 5); // Hgap=5, Vgap=5
        // // Ajoute une marge intérieure (padding) autour du contenu du FlowPane
        // fp.setPadding(new Insets(10));

        // for (int i = 0; i < 10; i++) {
        //     Button b = new Button("Bouton " + (i + 1));
        //     // Pour définir une marge spécifique autour de ce bouton (exemple) :
        //     // FlowPane.setMargin(b, new Insets(5)); // Marge de 5px tout autour
        //     fp.getChildren().add(b);
        // }
        // scene.setRoot(fp);
        // stage.sizeToScene();

        
        /* ---------- HBox----------  */
        // HBox hb = new HBox();
        // hb.setPadding(new Insets(3, 3, 3, 3));
        // hb.setSpacing(3);
        // for (int i = 0; i < 10; i++) {
        //     Button b = new Button("Bouton " + (i + 1));
        //     hb.getChildren().add(b);
        // }
        // hb.setAlignment(Pos.CENTER_LEFT);
        
        // scene.setRoot(hb);
        // stage.show();
        
        /* ---------- VBox----------  */
        // VBox vb = new VBox();
        // vb.setPadding(new Insets(3, 3, 3, 3));
        // vb.setSpacing(3);
        // for (int i = 0; i < 10; i++) {
        //     Button b = new Button("Bouton " + (i + 1));
        //     vb.getChildren().add(b);
        // }
        
        // scene.setRoot(vb);
        // stage.show();

        /* ---------- BorderPane ----------  */
        // Button top = new Button("Top");
        // top.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // Button bottom = new Button("Bottom");
        // bottom.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // Button left = new Button("Left");
        // left.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // Button right = new Button("Right");
        // right.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // Button center = new Button("Center");
        // center.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // BorderPane bp = new BorderPane();
        // bp.setPadding(new Insets(10, 10, 10, 10));
        // bp.setTop(top);
        // bp.setBottom(bottom);
        // bp.setLeft(left);
        // bp.setRight(right);
        // bp.setCenter(center);

        // scene.setRoot(bp);
        // stage.sizeToScene();

        /* ---------- TilePane ----------  */
        // TilePane tp = new TilePane();
        // tp.setPadding(new Insets(10, 10, 10, 10));
        // tp.setHgap(5);
        // tp.setVgap(5);

        // for (int i = 0; i < 16; i++) {
        //     Button b = new Button("Bouton " + (i + 1));
        //     // Permet au bouton de s'agrandir pour remplir la tuile
        //     b.setMaxWidth(Double.MAX_VALUE);
        //     b.setMaxHeight(Double.MAX_VALUE);
        //     tp.getChildren().add(b);
        // }

        // scene.setRoot(tp);
        // stage.sizeToScene();

        /* ---------- GridPane ----------  */
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(5, 5, 5, 5));
        gp.setHgap(5);
        gp.setVgap(5);


        Button b1 = new Button("Bouton 1");
        gp.add(b1, 0, 0);
        Button b2 = new Button("Bouton 2");
        gp.add(b2, 1, 0);
        Button b3 = new Button("Bouton 3");
        gp.add(b3, 2, 0);
        Button b4 = new Button("Bouton 4");
        gp.add(b4, 3, 0);
        Button b5 = new Button("Bouton 5");
        gp.add(b5, 0, 1);
        b5.setMaxSize(999, 999);
        Button b6 = new Button("Bouton 6");
        gp.add(b6, 0, 2);
        Button b7 = new Button("Bouton 7");
        gp.add(b7, 3, 2);
        Button b8 = new Button("Bouton 8");
        gp.add(b8, 0, 4);
        Button b9 = new Button("Bouton 9");
        gp.add(b9, 2, 4);
        Button b10 = new Button("Bouton 10");
        gp.add(b10, 3, 4);


        scene.setRoot(gp);
        stage.sizeToScene();

        /* ---------- Fenêtre modale----------  */
        // Stage stage2 = new Stage();
        // stage2.setTitle("Fenêtre Modale");
        
        // VBox root2 = new VBox(); 
        // Label msg2 = new Label("Fenêtre modale.");
        // root2.getChildren().add(msg2);
        
        // Scene scene2 = new Scene(root2, 300, 50); 
        // stage2.setScene(scene2);
        // stage2.setTitle("Fenêtre modale.");
        
        // stage2.setX(stage.getX() + stage.getWidth());
        // stage2.setY(stage.getY());
        
        // stage2.initOwner(stage);
        // stage2.initModality(Modality.WINDOW_MODAL);

        // stage2.show();

        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}