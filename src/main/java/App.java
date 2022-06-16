import gui.SimulationGUI;

/**
 * This class contains the main method to run the whole GUI-based code.
 * All necessary steps are performed after the SimulationGUI is launched.
 * @author Martin Aviles, Lena Feiler, Willem Ploegstra, Konstantin Sandfort, Christopher Schiffmann
 * @version 1.0
 * @since June 2022
 */
public class App {

    /**
     * Main method that gets executed upon start.
     * @param args
     */
    public static void main(String[] args) {
        new SimulationGUI().launchGUI();
    }
}
