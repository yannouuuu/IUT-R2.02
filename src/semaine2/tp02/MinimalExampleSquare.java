package semaine2.tp02;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.Vector;

public class MinimalExampleSquare extends Application {
    // Structure de données principale
    Vector<MyRectangle> rectangles = new Vector<MyRectangle>();

    // Références au Canvas et à son contexte graphique
    Canvas canvas;
    GraphicsContext gc;

    // Constante pour la taille des carrés
    private static final double SQUARE_SIZE = 40.0;

    // États de l'interaction
    MyRectangle hoveredRectangle = null;        // Rectangle survolé
    MyRectangle dragTargetRectangle = null;     // Rectangle principal pour le glissement en cours
    Vector<MyRectangle> multiSelectedRectangles = new Vector<MyRectangle>(); // Rectangles sélectionnés
    
    // Pour le glissement
    double offsetX, offsetY; // Décalage souris pour le glissement du dragTargetRectangle
    Vector<RelativePosition> dragGroupRelativePositions = new Vector<RelativePosition>(); // Positions relatives pour le glissement de groupe

    // Classes internes
    class MyRectangle {
        public Rectangle rect;
        // On pourrait ajouter une couleur propre, un ID, etc. ici plus tard
    }

    static class RelativePosition {
        MyRectangle myRectInstance;
        double relativeX, relativeY;

        RelativePosition(MyRectangle r, double rx, double ry) {
            myRectInstance = r;
            relativeX = rx;
            relativeY = ry;
        }
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        canvas = new Canvas(500, 500);
        gc = canvas.getGraphicsContext2D();

        setupMouseHandlers();

        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setTitle("Hello Paint Refactored");
        stage.setScene(scene);
        stage.show();
    }

    private void setupMouseHandlers() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnMouseMoved(this::handleMouseMoved);
    }

    // --- Gestionnaires d'événements principaux ---
    private void handleMousePressed(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        MyRectangle clickedRect = findClickedRectangle(x, y);

        if (isShiftDown(e)) {
            handleShiftClick(clickedRect);
        } else if (isControlDown(e)) {
            handleControlClick(clickedRect);
        } else {
            handleNormalClick(clickedRect, x, y);
        }
        repaintCanvas();
    }

    private void handleMouseDragged(MouseEvent e) {
        if (dragTargetRectangle != null && !isShiftDown(e)) {
            performDragMovement(e.getX(), e.getY());
            repaintCanvas();
        }
    }

    private void handleMouseReleased(MouseEvent e) {
        clearDragOperationState();
        // La sélection multiple persiste après le relâchement
    }

    private void handleMouseMoved(MouseEvent e) {
        updateHoverState(e.getX(), e.getY());
        // repaintCanvas() est appelé dans updateHoverState si nécessaire
    }

    // --- Logique des clics (appelée par handleMousePressed) ---
    private void handleShiftClick(MyRectangle clickedRect) {
        if (clickedRect != null) {
            deleteRectangle(clickedRect);
        }
        clearDragOperationState(); 
    }

    private void handleControlClick(MyRectangle clickedRect) {
        if (clickedRect != null) {
            toggleMultiSelectionState(clickedRect);
        }
        clearDragOperationState(); 
    }

    private void handleNormalClick(MyRectangle clickedRect, double eventX, double eventY) {
        clearDragOperationState(); // Toujours commencer par réinitialiser l'état de glissement précédent

        if (clickedRect == null) { // Clic dans le vide
            clearMultiSelection();
            MyRectangle newRect = createAndAddNewRectangle(eventX, eventY);
            addRectToMultiSelection(newRect, true); // Le nouveau est sélectionné et mis au premier plan
            initiateDragForRect(newRect, eventX, eventY);
        } else { // Clic sur un rectangle existant
            if (!isMultiSelected(clickedRect)) {
                clearMultiSelection();
                addRectToMultiSelection(clickedRect, true);
            }
            // Que le rect cliqué ait été nouvellement sélectionné ou faisait déjà partie d'une sélection,
            // il devient la cible du glissement.
            initiateDragForRect(clickedRect, eventX, eventY);
        }
        // Après avoir défini le dragTarget et la sélection, préparer le groupe
        prepareForGroupDrag(); 
    }

    // --- Méthodes d'action et de gestion d'état ---
    private MyRectangle findClickedRectangle(double x, double y) {
        for (int i = rectangles.size() - 1; i >= 0; i--) {
            MyRectangle current = rectangles.get(i);
            if (isPointInsideRect(current, x, y)) {
                return current;
            }
        }
        return null;
    }

    private void deleteRectangle(MyRectangle rectToDelete) {
        rectangles.remove(rectToDelete);
        multiSelectedRectangles.remove(rectToDelete);
        if (isHovered(rectToDelete)) {
            hoveredRectangle = null;
        }
        if (isDragTarget(rectToDelete)) {
            dragTargetRectangle = null; // Important si le rect supprimé était la cible du drag
        }
    }

    private void toggleMultiSelectionState(MyRectangle rect) {
        if (isMultiSelected(rect)) {
            multiSelectedRectangles.remove(rect);
        } else {
            multiSelectedRectangles.add(rect);
            bringToFront(rect); // Mettre au premier plan lors de l'ajout à la sélection
        }
    }
    
    private void addRectToMultiSelection(MyRectangle rect, boolean bringToFrontIfNeeded) {
        if (!isMultiSelected(rect)) {
            multiSelectedRectangles.add(rect);
        }
        if (bringToFrontIfNeeded) {
            bringToFront(rect);
        }
    }

    private void clearMultiSelection() {
        multiSelectedRectangles.clear();
    }

    private MyRectangle createAndAddNewRectangle(double centerX, double centerY) {
        Rectangle fxRect = new Rectangle(centerX - SQUARE_SIZE / 2, centerY - SQUARE_SIZE / 2, SQUARE_SIZE, SQUARE_SIZE);
        MyRectangle newMyRect = new MyRectangle();
        newMyRect.rect = fxRect;
        rectangles.add(newMyRect); // Ajout à la liste principale
        return newMyRect;
    }

    private void initiateDragForRect(MyRectangle target, double eventX, double eventY) {
        dragTargetRectangle = target;
        offsetX = eventX - target.rect.getX();
        offsetY = eventY - target.rect.getY();
        // Le fait de cliquer sur un rectangle pour le glisser le met au premier plan dans handleNormalClick
    }

    private void prepareForGroupDrag() {
        dragGroupRelativePositions.clear();
        if (dragTargetRectangle != null && !multiSelectedRectangles.isEmpty()) {
            for (MyRectangle r_sel : multiSelectedRectangles) {
                double relX = r_sel.rect.getX() - dragTargetRectangle.rect.getX();
                double relY = r_sel.rect.getY() - dragTargetRectangle.rect.getY();
                dragGroupRelativePositions.add(new RelativePosition(r_sel, relX, relY));
            }
        }
    }

    private void performDragMovement(double eventX, double eventY) {
        double leaderNewX = eventX - offsetX;
        double leaderNewY = eventY - offsetY;

        for (RelativePosition rp : dragGroupRelativePositions) {
            rp.myRectInstance.rect.setX(leaderNewX + rp.relativeX);
            rp.myRectInstance.rect.setY(leaderNewY + rp.relativeY);
        }
    }

    private void clearDragOperationState() {
        dragTargetRectangle = null;
        dragGroupRelativePositions.clear();
    }

    private void bringToFront(MyRectangle rect) {
        if (rectangles.remove(rect)) {
            rectangles.add(rect); // Ajoute à la fin, donc dessiné en dernier (au-dessus)
        }
    }

    private void updateHoverState(double eventX, double eventY) {
        MyRectangle previouslyHovered = hoveredRectangle;
        hoveredRectangle = findClickedRectangle(eventX, eventY); // Réutilise findClickedRectangle pour la logique de survol

        if (previouslyHovered != hoveredRectangle) {
            repaintCanvas();
        }
    }

    // --- Méthodes booléennes (prédicats) ---
    private boolean isShiftDown(MouseEvent e) {
        return e.isShiftDown();
    }

    private boolean isControlDown(MouseEvent e) {
        return e.isControlDown();
    }

    private boolean isPointInsideRect(MyRectangle myRect, double x, double y) {
        if (myRect == null || myRect.rect == null) return false;
        return myRect.rect.contains(x, y);
    }

    private boolean isMultiSelected(MyRectangle myRect) {
        return multiSelectedRectangles.contains(myRect);
    }

    private boolean isHovered(MyRectangle myRect) {
        return myRect != null && myRect == hoveredRectangle;
    }

    private boolean isDragTarget(MyRectangle myRect) {
        return myRect != null && myRect == dragTargetRectangle;
    }

    // --- Dessin ---
    private void repaintCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (MyRectangle r : rectangles) {
            applyFillStyle(r);
            gc.fillRect(r.rect.getX(), r.rect.getY(), r.rect.getWidth(), r.rect.getHeight());
            applyStrokeStyle(r);
            gc.strokeRect(r.rect.getX(), r.rect.getY(), r.rect.getWidth(), r.rect.getHeight());
        }
    }

    private void applyFillStyle(MyRectangle r) {
        if (isHovered(r)) {
            gc.setFill(Color.ORANGERED);
        } else {
            gc.setFill(Color.ORANGE);
        }
    }

    private void applyStrokeStyle(MyRectangle r) {
        if (isMultiSelected(r)) {
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(3.5);
            gc.setLineDashes(null); 
        } else {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.setLineDashes(null); 
        }
    }

    // --- Point d'entrée ---
    public static void main(String[] args) {
        Application.launch(args);
    }
}