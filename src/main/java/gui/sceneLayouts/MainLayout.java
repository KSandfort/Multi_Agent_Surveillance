package gui.sceneLayouts;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class represents the main layout, that contains a visual
 * representation of the map.
 */
public class MainLayout extends BorderPane {

    // Variables
    Pane canvas;
    Button playPauseButton;
    Button stepButton;
    Slider simSpeedSlider;
    Label speedLabel;

    Circle circle;

    /**
     * Constructor
     */
    public MainLayout() {
        this.setStyle("-fx-font: 12px 'Verdana';");


        // Canvas - Center
        canvas = new Pane();
        canvas.setStyle("-fx-background-color: gray;");
        canvas.setPrefSize(1200, 800);

        Circle circle = new Circle(50, Color.BLUE);
        circle.relocate(20, 20);
        Rectangle rectangle = new Rectangle(100,100,Color.RED);
        rectangle.relocate(70,70);
        canvas.getChildren().addAll(circle,rectangle);

        this.setCenter(canvas);

        // Controls - Bottom
        HBox controlsContainer = new HBox();
        controlsContainer.setSpacing(20);
        controlsContainer.setAlignment(Pos.CENTER);
        controlsContainer.setPrefHeight(50);
        playPauseButton = new Button("Play / Pause");
        stepButton = new Button("Step");
        simSpeedSlider = new Slider();
        simSpeedSlider.setMin(1);
        simSpeedSlider.setMax(1000);
        speedLabel = new Label("Simulation Speed:");

        controlsContainer.getChildren().addAll(playPauseButton, stepButton, speedLabel, simSpeedSlider);
        this.setBottom(controlsContainer);
    }

    public void updateGUI(int x) {
        circle.relocate(x, 20);
    }
}
