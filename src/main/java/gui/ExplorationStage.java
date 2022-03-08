package gui;

import Enities.Entity;
import Enities.EntityKnowledge;
import gui.sceneLayouts.MainLayout;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Window to display the exploration coverage of a map.
 */
public class ExplorationStage extends Stage {

    // Variables
    MainLayout mainLayout;
    BorderPane root;
    GridPane grid;
    Entity trackedEntity;
    EntityKnowledge entityKnowledge;
    int cellSize = 3; // in px

    public ExplorationStage(MainLayout mainLayout) {
        this.setTitle("Exploration Coverage");
        this.mainLayout = mainLayout;
        trackedEntity = (Entity) mainLayout.getSimulationGUI().getController().getMap().getMovingItems().get(0);
        entityKnowledge = trackedEntity.getEntityKnowledge();
        entityKnowledge.setExplorationStage(this);
        root = new BorderPane();
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(1);
        grid.setHgap(1);
        for (int i = 0; i < entityKnowledge.getMapRepresentation().length; i++) {
            for (int j = 0; j < entityKnowledge.getMapRepresentation()[0].length; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(cellSize, cellSize);
                pane.setStyle("-fx-background-color:#777777;");
                grid.add(pane, i, j);
            }
        }
        root.setCenter(grid);
        this.setScene(new Scene(root, 1000, 700));
        this.show();
    }

    public void drawCell(int code, int x, int y) {
        Pane pane = new Pane();
        pane.setPrefSize(cellSize, cellSize);
        switch (code) {
            case 0: { // not discovered
                pane.setStyle("-fx-background-color:#777777;");
                break;
            }
            case 1: { // empty
                pane.setStyle("-fx-background-color:#7777ff;");
                break;
            }
            case 2: { // self
                pane.setStyle("-fx-background-color:#00ff00;");
                break;
            }
            default: { // error
                pane.setStyle("-fx-background-color:#ffaa22;");
                break;
            }
        }
        grid.add(pane, x, y);
    }

}
