package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class Chest extends Actor {
    //state
    private static final int OPEN = 0;
    private static final int CLOSED = 1;

    private static final int LAYER = 1;

    private boolean opened;

    public Chest(Vec2D spawnPoint) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setMoving(false);
        setActive(false);
        opened = false;

        setLayer(LAYER);

        getAnimationManager().toState(CLOSED);
    }

    @Override
    public void behavior() {
    }

    @Override
    public void animationUpdate() {
        if (opened) {
            getAnimationManager().toState(OPEN);
        }
    }

    @Override
    public void onInteract(Actor actor) {
        if (actor instanceof Knight && !opened) {
            opened = true;
            ((Knight) actor).setCarrying(true);
        }
        actor.turnAround();
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation open = new Animation();
        Animation close = new Animation();
        Image frame0 = new Image("res/ActorExtensionPack/Actor/Chest/chest_1.png");
        Image frame2 = new Image("res/ActorExtensionPack/Actor/Chest/chest_0.png");
        open.addFrame(frame0);
        close.addFrame(frame2);

        animationStateManager.add(open);
        animationStateManager.add(close);

        return animationStateManager;
    }

    @Override
    public Actor clone() {
        return new Chest(getPosition());
    }

    public int getLAYER() {
        return LAYER;
    }
}
