package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class Knight extends Actor{
    //state
    private static final int IDLE_R = 0;
    private static final int WALK_R = 1;
    private static final int IDLE_L = 2;
    private static final int WALK_L = 3;

    private static final int LAYER = 2;

    private boolean carrying = false;

    public Knight(Vec2D spawnPoint) {
        super(spawnPoint);

        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        setMoving(false);
        setDirection(LEFT);
        setActive(true);

    }

    @Override
    public void onInteract(Actor actor) {

    }

    @Override
    public void behavior() {
        resetTransfer();
        move();
    }

    @Override
    public void animationUpdate() {
        if (isMoving()) {
            if (getFace() == LEFT) {
                getAnimationManager().toState(WALK_L);
            } else if (getFace() == RIGHT) {
                getAnimationManager().toState(WALK_R);
            }
        } else {
            if (getFace() == LEFT) {
                getAnimationManager().toState(IDLE_L);
            } else if (getFace() == RIGHT) {
                getAnimationManager().toState(IDLE_R);
            }
        }
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation idleR = new Animation();
        Image idleRFrame0 = new Image("res/ActorExtensionPack/Actor/Knight/knight_right_idle_0.png");
        Image idleRFrame1 = new Image("res/ActorExtensionPack/Actor/Knight/knight_right_idle_1.png");
        idleR.addFrame(idleRFrame0);
        idleR.addFrame(idleRFrame1);

        Animation walkR = new Animation();
        Image walkRFrame0 = new Image("res/ActorExtensionPack/Actor/Knight/knight_right_walk_0.png");
        Image walkRFrame1 = new Image("res/ActorExtensionPack/Actor/Knight/knight_right_walk_1.png");
        walkR.addFrame(walkRFrame0);
        walkR.addFrame(walkRFrame1);

        Animation idleL = new Animation();
        Image idleLFrame0 = new Image("res/ActorExtensionPack/Actor/Knight/knight_left_idle_0.png");
        Image idleLFrame1 = new Image("res/ActorExtensionPack/Actor/Knight/knight_left_idle_1.png");
        idleL.addFrame(idleLFrame0);
        idleL.addFrame(idleLFrame1);

        Animation walkL = new Animation();
        Image walkLFrame0 = new Image("res/ActorExtensionPack/Actor/Knight/knight_left_walk_0.png");
        Image walkLFrame1 = new Image("res/ActorExtensionPack/Actor/Knight/knight_left_walk_1.png");
        walkL.addFrame(walkLFrame0);
        walkL.addFrame(walkLFrame1);

        animationStateManager.add(idleR);
        animationStateManager.add(walkR);

        animationStateManager.add(idleL);
        animationStateManager.add(walkL);
        return animationStateManager;
    }

    @Override
    public Actor clone() {
        return new Knight(getPosition());
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public void setCarrying(boolean carrying) {
        this.carrying = carrying;
    }

    public boolean isCarrying() {
        return carrying;
    }

    public int getLAYER() {
        return LAYER;
    }
}
