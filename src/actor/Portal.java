package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import Level.Level;
import bagel.Image;
import myUtil.Vec2D;

import java.util.ArrayList;

public class Portal extends Actor{

    //state
    private static final int PORTAL_REST = 0;

    private static final int LAYER = 1;

    private final int pairID;
    private static int pair = 0;
    private ArrayList<Actor> temp;

    Level castToLevel;

    public Portal(Vec2D spawnPoint, Vec2D pairSpawnPoint, Level castToLevel) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        setMoving(false);
        setActive(false);
        this.pairID = getPair();
        setCastToLevel(castToLevel);

        addPair(pairSpawnPoint, castToLevel);

        getAnimationManager().toState(PORTAL_REST);
    }

    public Portal(Vec2D spawnPoint, Vec2D pairSpawnPoint, ArrayList<Actor> temp, Level castToLevel) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        setMoving(false);
        setActive(false);
        this.pairID = getPair();
        setCastToLevel(castToLevel);
        this.temp = temp;
        addPair(pairSpawnPoint);

        getAnimationManager().toState(PORTAL_REST);
    }

    public Portal(Vec2D spawnPoint, int pairID, Level castToLevel) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        setMoving(false);
        setActive(false);
        this.pairID = pairID;

        setCastToLevel(castToLevel);

        getAnimationManager().toState(PORTAL_REST);
    }

    @Override
    public void behavior() {
    }

    @Override
    public void animationUpdate() {
    }

    @Override
    public void onInteract(Actor actor) {
        if (!actor.isActive()) {
            return;
        }
        for (Actor pair : castToLevel.getActors()) {
            if (pair != this && pair instanceof Portal) {
                if (((Portal) pair).getPairID() == this.pairID) {
                    if (!actor.isTransfer()) {
                        actor.setPosition(pair.getPosition().clone());
                        actor.transfer();
                    }
                }
            }
        }
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation portalRest = new Animation();
        Image frame0 = new Image("res/ActorExtensionPack/Actor/Portal/portal_0.png");
        Image frame1 = new Image("res/ActorExtensionPack/Actor/Portal/portal_1.png");
        Image frame2 = new Image("res/ActorExtensionPack/Actor/Portal/portal_2.png");
        portalRest.addFrame(frame0);
        portalRest.addFrame(frame1);
        portalRest.addFrame(frame2);

        animationStateManager.add(portalRest);

        return animationStateManager;
    }

    public void setCastToLevel(Level castToLevel) {
        this.castToLevel = castToLevel;
    }

    public void addPair(Vec2D spawnPoint, Level castToLevel) {
        castToLevel.add(new Portal(spawnPoint, this.getPairID(), castToLevel));
    }

    public void addPair(Vec2D spawnPoint) {
        temp.add(new Portal(spawnPoint, this.getPairID(), castToLevel));
    }

    public static int getPair() {
        pair++;
        return pair;
    }

    public int getPairID() {
        return pairID;
    }

    @Override
    public Actor clone() {
        return new Portal(getPosition(), getPairID(), castToLevel);
    }

    public int getLAYER() {
        return LAYER;
    }
}
