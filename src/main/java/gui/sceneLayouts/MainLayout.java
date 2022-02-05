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
    Label stepCountLabel;

    Button playPauseButton;
    Button stepButton;
    Slider simSpeedSlider;
    Label speedLabel;
    public static int yOffset = 50;

    public Circle circle;
    public int circleX = 20;


    /**
     * Constructor
     */
    public MainLayout() {
        this.setStyle("-fx-font: 12px 'Verdana';");
        initComponents();
    }

    /**
     * Creates all components on the main layout.
     */
    public void initComponents() {
        // Canvas - Center
        canvas = new Pane();
        canvas.setStyle("-fx-background-color: gray;");
        canvas.setPrefSize(1200, 800);

        this.setCenter(canvas);

        // Info - Top
        HBox infoBox = new HBox();
        infoBox.setSpacing(20);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPrefHeight(50);
        stepCountLabel = new Label("Current Step: 0");
        infoBox.getChildren().setAll(stepCountLabel);
        this.setTop(infoBox);

        // Controls - Bottom
        HBox controlsContainer = new HBox();
        controlsContainer.setSpacing(20);
        controlsContainer.setAlignment(Pos.CENTER);
        controlsContainer.setPrefHeight(yOffset);
        playPauseButton = new Button("Play / Pause");
        stepButton = new Button("Step");
        int i = 50;
        stepButton.setOnAction(e -> {
            circle.relocate(i, 20);
        });
        simSpeedSlider = new Slider();
        simSpeedSlider.setMin(1);
        simSpeedSlider.setMax(1000);
        speedLabel = new Label("Simulation Speed:");

        controlsContainer.getChildren().addAll(playPauseButton, stepButton, speedLabel, simSpeedSlider);
        this.setBottom(controlsContainer);
    }

    public Label getStepCountLabel() {
        return stepCountLabel;
    }
}
