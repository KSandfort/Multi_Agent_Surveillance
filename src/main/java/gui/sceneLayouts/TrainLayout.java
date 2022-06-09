package gui.sceneLayouts;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * Layout to display the current training status.
 */
@Getter
@Setter
public class TrainLayout extends BorderPane {

    // Variables

    /**
     * Constructor
     */
    public TrainLayout() {
        this.setStyle("-fx-font: 12px 'Verdana';");
        initComponents();
    }

    /**
     * Inits all components on this layout.
     */
    private void initComponents() {

    }
}
