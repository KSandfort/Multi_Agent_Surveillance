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

        root = new BorderPane();
        Button test = new Button("Test");
        root.setTop(test);

        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(2);
        grid.setHgap(2);
        for (int i = 0; i < entityKnowledge.getMapRepresentation().length; i++) {
            for (int j = 0; j < entityKnowledge.getMapRepresentation()[0].length; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(cellSize, cellSize);
                pane.setStyle("-fx-background-color:#38ee00;");
                grid.add(pane, i, j);
            }
        }

        test.setOnAction(e -> {
            Pane pane = new Pane();
            pane.setPrefSize(cellSize, cellSize);
            pane.setStyle("-fx-background-color:#222222;");
            grid.add(pane, 10, 10);
        });
        root.setCenter(grid);
        this.setScene(new Scene(root, 1250, 850));
        this.show();
    }

    public void setCell(int x, int y, int code) {
        Pane pane = new Pane();
        pane.setPrefSize(cellSize, cellSize);
        pane.setStyle("-fx-background-color:#222222;");
        grid.add(pane, x, y);
    }
}
