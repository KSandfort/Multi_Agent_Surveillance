package controller;

/**
 * Handles the game loop.
 */
public class GameLoop {

    private boolean running;
    private final double updateRate = 1.0d/60.0d;

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
        }
    }

    private void render() {
    }

    private void update() {
    }
}
