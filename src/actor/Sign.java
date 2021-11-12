package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class Sign extends Actor{

    private static final int LAYER = 0;

    //image path
    private static final String PATH_SIGN_UP = "res/images/up.png";
    private static final String PATH_SIGN_DOWN = "res/images/down.png";
    private static final String PATH_SIGN_LEFT = "res/images/left.png";
    private static final String PATH_SIGN_RIGHT = "res/images/right.png";

    public Sign(Vec2D spawnPoint, int direction) {
        super(spawnPoint);

        //Set Properties
        setMoving(false);
        setActive(false);
        setDirection(direction);

        setLayer(LAYER);

        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        //set appearance
        getAnimationManager().toState(direction);
    }

    @Override
    public void behavior() {
    }

    @Override
    public void animationUpdate() {}

    @Override
    public void onInteract(Actor actor) {
        if (actor instanceof Knight) {
            actor.setDirection(this.getDirection());
        }
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation sign0 = new Animation();
        Animation sign1 = new Animation();
        Animation sign2 = new Animation();
        Animation sign3 = new Animation();
        Image frame0 = new Image(PATH_SIGN_UP);
        Image frame1 = new Image(PATH_SIGN_RIGHT);
        Image frame2 = new Image(PATH_SIGN_DOWN);
        Image frame3 = new Image(PATH_SIGN_LEFT);
        sign0.addFrame(frame0);
        sign1.addFrame(frame1);
        sign2.addFrame(frame2);
        sign3.addFrame(frame3);

        animationStateManager.add(sign0);
        animationStateManager.add(sign1);
        animationStateManager.add(sign2);
        animationStateManager.add(sign3);

        return animationStateManager;
    }

    @Override
    public Actor clone() {
        return new Sign(getPosition(), getDirection());
    }

    public int getLAYER() {
        return LAYER;
    }


}
