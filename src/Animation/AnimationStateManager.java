package Animation;

import myUtil.Vec2D;

import java.util.ArrayList;

public class AnimationStateManager {
    ArrayList<Animation> animations;
    Animation current;

    public AnimationStateManager() {
        animations = new ArrayList<Animation>();
    }

    public void add(Animation animation) {
        animations.add(animation);
    }

    public void toState(int state) {
        current = animations.get(state);
    }

    public void playAt(Vec2D position) {
        current.playAt(position);
    }
}
