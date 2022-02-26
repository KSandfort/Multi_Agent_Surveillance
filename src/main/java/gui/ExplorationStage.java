package gui;

import Enities.Entity;
import Enities.EntityKnowledge;
import gui.sceneLayouts.MainLayout;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ExplorationStage extends Stage {

    // Variables
    MainLayout mainLayout;
    GridPane grid;
    Entity trackedEntity;
    EntityKnowledge entityKnowledge;

    public ExplorationStage(MainLayout mainLayout) {
        this.setTitle("Exploration Coverage");
        this.mainLayout = mainLayout;
        trackedEntity = (Entity) mainLayout.getSimulationGUI().getController().getMap().getMovingItems().get(0);
        entityKnowledge = trackedEntity.getEntityKnowledge();
        grid = new GridPane();
        for (int i = 0; i < entityKnowledge.getMapRepresentation().length; i++) {
            for (int j = 0; j < entityKnowledge.getMapRepresentation()[0].length; j++) {
                grid.add(new Text(Integer.toString(entityKnowledge.getMapRepresentation()[i][j])), i, j);
            }
        }
        this.setScene(new Scene(grid, 500, 500));
        this.show();
    }
}
