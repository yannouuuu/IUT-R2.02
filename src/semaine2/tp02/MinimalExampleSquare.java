package semaine2.tp02;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.Vector;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MinimalExampleSquare extends Application {
    // Structure de données pour stocker les carrés 
    // avec les informations associées
    Vector<MyRectangle> rectangles = new Vector<MyRectangle>();

    // Contexte graphique du canvas, nécessaire pour dessiner
    GraphicsContext gc;
    Canvas canvas;

    // Taille par défaut pour les carrés dessinés
    private static final double SQUARE_SIZE = 40.0;

    // Variables pour gérer le glisser-déposer (drag and drop)
    MyRectangle dragTargetRectangle = null; // Le rectangle activement déplacé (anciennement selectedRectangle)
    double offsetX, offsetY; // Décalage entre le clic souris et le coin du rectangle

    // Variable pour gérer le survol de la souris
    MyRectangle hoveredRectangle = null; // Le rectangle actuellement survolé

    // Variable pour la sélection multiple
    Vector<MyRectangle> multiSelectedRectangles = new Vector<MyRectangle>();

    // Classe interne pour stocker nos rectangles et potentiellement d'autres informations
    class MyRectangle {
            public Rectangle rect; // Rectangle JavaFX pour stocker position et dimensions
            // Autres infos (par exemple, couleur, etc.) pourraient être ajoutées ici
    }


    public void start(Stage stage) {
            VBox root = new VBox();
            canvas = new Canvas (500, 500);
            gc = canvas.getGraphicsContext2D();

            canvas.setOnMousePressed(e -> {
                    double x = e.getX();
                    double y = e.getY();
                    MyRectangle clickedMyRect = null;

                    // Trouver le rectangle le plus au-dessus sur lequel on a cliqué
                    for (int i = rectangles.size() - 1; i >= 0; i--) {
                        MyRectangle current = rectangles.get(i);
                        if (current.rect.contains(x, y)) {
                            clickedMyRect = current;
                            break;
                        }
                    }

                    if (e.isShiftDown()) {
                        // Mode suppression si SHIFT est appuyé
                        if (clickedMyRect != null) {
                            rectangles.remove(clickedMyRect);
                            multiSelectedRectangles.remove(clickedMyRect); // Retirer de la sélection multiple aussi
                            if (hoveredRectangle == clickedMyRect) hoveredRectangle = null;
                        }
                        dragTargetRectangle = null;
                    } else if (e.isControlDown()) {
                        // Mode sélection multiple avec CTRL (SHIFT n'est pas appuyé)
                        if (clickedMyRect != null) {
                            if (multiSelectedRectangles.contains(clickedMyRect)) {
                                multiSelectedRectangles.remove(clickedMyRect);
                            } else {
                                multiSelectedRectangles.add(clickedMyRect);
                                // Amener au premier plan (optionnel mais bon usage)
                                if (rectangles.remove(clickedMyRect)) {
                                    rectangles.add(clickedMyRect);
                                }
                            }
                        }
                        dragTargetRectangle = null; // Pas de drag initié lors de la modification de la sélection multiple
                    } else {
                        // Clic normal (ni SHIFT, ni CTRL)
                        dragTargetRectangle = null; // Réinitialiser la cible de drag précédente
                        boolean clickedOnExisting = (clickedMyRect != null);

                        if (!clickedOnExisting || !multiSelectedRectangles.contains(clickedMyRect)) {
                           // Si on clique dans le vide, OU
                           // si on clique sur un carré qui n'était PAS DÉJÀ dans la sélection multiple,
                           // alors on efface la sélection multiple précédente.
                           multiSelectedRectangles.clear();
                        }
                        // Si on clique sur un carré déjà sélectionné (sans CTRL), la sélection multiple est conservée
                        // et ce carré devient la cible du drag.

                        if (clickedMyRect != null) { // Clic sur un rectangle existant
                            if (!multiSelectedRectangles.contains(clickedMyRect)) { // S'il n'y était pas déjà (cas du !ctrl)
                                multiSelectedRectangles.add(clickedMyRect);
                            }
                            dragTargetRectangle = clickedMyRect;
                            offsetX = x - clickedMyRect.rect.getX();
                            offsetY = y - clickedMyRect.rect.getY();
                            // Amener au premier plan
                            if (rectangles.remove(clickedMyRect)) {
                                rectangles.add(clickedMyRect);
                            }
                        } else { // Clic dans le vide : créer un nouveau carré
                            Rectangle newFxRectangle = new Rectangle(x - SQUARE_SIZE / 2, y - SQUARE_SIZE / 2, SQUARE_SIZE, SQUARE_SIZE);
                            MyRectangle newMyRect = new MyRectangle();
                            newMyRect.rect = newFxRectangle;
                            
                            rectangles.add(newMyRect); // Ajouté à la fin, donc au premier plan
                            multiSelectedRectangles.add(newMyRect); // Sélectionner le nouveau carré
                            dragTargetRectangle = newMyRect; // Et le rendre déplaçable
                            offsetX = SQUARE_SIZE / 2; // Le clic est au centre
                            offsetY = SQUARE_SIZE / 2;
                        }
                    }
                    repaint();
            });

            canvas.setOnMouseDragged(e -> {
                if (dragTargetRectangle != null && !e.isShiftDown()) { 
                    double x = e.getX();
                    double y = e.getY();
                    dragTargetRectangle.rect.setX(x - offsetX);
                    dragTargetRectangle.rect.setY(y - offsetY);
                    repaint();
                }
            });

            canvas.setOnMouseMoved(e -> {
                double x = e.getX();
                double y = e.getY();
                MyRectangle previouslyHovered = hoveredRectangle;
                hoveredRectangle = null; // Réinitialiser par défaut

                // Parcourir les rectangles en sens inverse pour prioriser celui du dessus
                for (int i = rectangles.size() - 1; i >= 0; i--) {
                    MyRectangle myRect = rectangles.get(i);
                    if (myRect.rect.contains(x, y)) {
                        hoveredRectangle = myRect;
                        break;
                    }
                }

                // Redessiner seulement si l'état de survol a changé
                if (previouslyHovered != hoveredRectangle) {
                    repaint();
                }
            });

            root.getChildren().add(canvas);

            Scene scene = new Scene(root);
            stage.setTitle("Hello Paint");
            stage.setScene(scene);
            stage.show();
    }

    // Méthode qui sert à redessiner tout l'écran
    // tout le code lié au dessin dans le canvas doit
    // se trouver dans cette méthode et pas ailleurs.
    // repaint() est appelée ailleurs dans le code quand
    // il est nécessaire de redessiner.
    public void repaint() {
            // On efface tout
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            
            for (MyRectangle r : rectangles) {
                    Rectangle fxRect = r.rect;
                    // Choisir la couleur de remplissage en fonction de l'état de survol
                    if (r == hoveredRectangle) {
                        gc.setFill(Color.ORANGERED);
                    } else {
                        gc.setFill(Color.ORANGE);
                    }
                    gc.fillRect(fxRect.getX(), fxRect.getY(), fxRect.getWidth(), fxRect.getHeight());

                    // Définir le style de la bordure (stroke)
                    if (multiSelectedRectangles.contains(r)) {
                        gc.setStroke(Color.BLUE); 
                        gc.setLineWidth(3.5);   
                    } else {
                        gc.setStroke(Color.BLACK); 
                        gc.setLineWidth(2); 
                    }
                    gc.strokeRect(fxRect.getX(), fxRect.getY(), fxRect.getWidth(), fxRect.getHeight());
            }
    }

    public static void main(String[] args) {
            Application.launch(args);
    }
}