package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class Lightning extends Actor {
    //state
    public static final int HORIZON = 0;
    public static final int VERTICAL = 1;

    private static final int LAYER = 3;

    private final int relateID;

    //type
    private final int currentType;

    public Lightning(Vec2D spawnPoint, int currentType, int relateID) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        this.relateID = relateID;
        setMoving(false);
        setActive(false);
        this.currentType = currentType;
        getAnimationManager().toState(this.currentType);
    }

    @Override
    public void behavior() {

    }

    @Override
    public void animationUpdate() {
    }

    @Override
    public void onInteract(Actor actor) {
        if (actor instanceof Knight) {
            actor.destroy();
        }
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation horizon = new Animation();
        Image frame00 = new Image("res/ActorExtensionPack/Actor/WallMaker/wall_horizontal_0.png");
        Image frame01 = new Image("res/ActorExtensionPack/Actor/WallMaker/wall_horizontal_1.png");
        horizon.addFrame(frame00);
        horizon.addFrame(frame01);

        Animation vertical = new Animation();
        Image frame10 = new Image("res/ActorExtensionPack/Actor/WallMaker/wall_vertical_0.png");
        Image frame11 = new Image("res/ActorExtensionPack/Actor/WallMaker/wall_vertical_1.png");
        vertical.addFrame(frame10);
        vertical.addFrame(frame11);

        animationStateManager.add(horizon);
        animationStateManager.add(vertical);

        return animationStateManager;
    }

    public int getRelateID() {
        return relateID;
    }

    @Override
    public Actor clone() {
        return new Lightning(getPosition(), currentType, relateID);
    }

    public int getLAYER() {
        return LAYER;
    }
}
