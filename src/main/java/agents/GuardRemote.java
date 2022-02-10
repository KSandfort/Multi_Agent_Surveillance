package agents;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * This guard can be remotely controlled by W, A, S, D.
 */
public class GuardRemote extends AbstractAgent {

    public GuardRemote() {
    }

    /**
     * Adds controls to a remote agent
     */
    public void addControls() {
        // Get the remote agent
        this.entityInstance.getMap().getGameController().getSimulationGUI().getMainScene().addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.W) {
                System.out.println("You pressed W");
            }
        });
    }

}
