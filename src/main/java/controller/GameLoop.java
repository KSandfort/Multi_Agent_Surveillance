package controller;

/**
 * Handles the game loop.
 */
public class GameLoop {

    // Variables
    private final static boolean DEBUG = true;
    private boolean running;
    private final double updateRate = 1.0d/60.0d;
    private long nextStatTime;
    private int fps;
    private int ups;

    public void run() {
        running = true;
        double accumulator = 0;
        long currentTime, lastUpdate = System.currentTimeMillis();

        while (running) {
            currentTime = System.currentTimeMillis();
            double lastRenderTimeInSeconds = (currentTime - lastUpdate) / 1000d;
            accumulator += lastRenderTimeInSeconds;
            lastUpdate = currentTime;
            
            while (accumulator > updateRate) {
                update();
                accumulator -= updateRate;
            }
            render();
            if (DEBUG)
                printStats();
        }
    }

    private void printStats() {
        if(System.currentTimeMillis() > nextStatTime) {
            System.out.println(String.format(""));
            fps = 0;
            ups = 0;
            nextStatTime = System.currentTimeMillis() + 1000;
        }
    }

    private void render() {
        fps++;
    }

    private void update() {
        ups++;
    }
}
