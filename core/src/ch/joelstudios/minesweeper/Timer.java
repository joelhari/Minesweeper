package ch.joelstudios.minesweeper;

import com.badlogic.gdx.utils.TimeUtils;

public class Timer {
    private long startTime;
    private boolean running = false;

    public void start() {
        startTime = TimeUtils.millis();
        running = true;
    }

    public long getTime() {
        return TimeUtils.millis() - startTime;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
