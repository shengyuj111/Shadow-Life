package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class WallMaker extends Actor{
    //state
    private static final int STAND = 0;

    private final int relateID;
    private static int wID = 0;

    private static final int LAYER = 2;

    public WallMaker(Vec2D spawnPoint) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        relateID = wID;
        wID++;

        setLayer(LAYER);

        setMoving(false);
        setActive(false);
        getAnimationManager().toState(STAND);
    }

    public WallMaker(Vec2D spawnPoint, int relateID) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        this.relateID = relateID;

        setMoving(false);
        setActive(false);
        getAnimationManager().toState(STAND);
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

        Animation stand = new Animation();
        Image frame0 = new Image("res/ActorExtensionPack/Actor/WallMaker/wallMaker.png");
        stand.addFrame(frame0);

        animationStateManager.add(stand);

        return animationStateManager;
    }

    public int getRelateID() {
        return relateID;
    }

    @Override
    public Actor clone() {
        return new WallMaker(getPosition(), relateID);
    }

    public int getLAYER() {
        return LAYER;
    }
}
