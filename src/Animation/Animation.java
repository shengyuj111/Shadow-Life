package Animation;

import bagel.Image;
import myUtil.Vec2D;

import java.util.ArrayList;

public class Animation {

    private static final int FRAME_REFRESH_TICK = 200;
    private static final int RESET = 0;

    private final ArrayList<Image> animation;

    private long previousTime;
    private int elapsedTime;

    private int frame;

    public Animation() {
        animation = new ArrayList<>();
        elapsedTime = RESET;
        previousTime = System.currentTimeMillis();
        frame = 0;
    }

    public void addFrame(Image frame) {
        animation.add(frame);
    }

    public void replay() {
        frame = 0;
    }

    public void playAt(Vec2D position) {
        animation.get(frame).drawFromTopLeft(position.x, position.y);
        if (notTick()) {
            return;
        }
        frame++;
        frame = frame % animation.size();
    }

    private boolean notTick() {
        //calculate elapsed time
        long currentTime = System.currentTimeMillis();
        elapsedTime += currentTime - previousTime;
        previousTime = currentTime;

        //check for tick
        if (elapsedTime <= FRAME_REFRESH_TICK) {
            return true;
        }
        //reset elapsedTime
        elapsedTime = RESET;
        return false;
    }
}
