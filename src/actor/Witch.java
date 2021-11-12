package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import Level.Level;
import bagel.Image;
import myUtil.MyMath;
import myUtil.Vec2D;

public class Witch extends Actor {
    //state
    private static final int IDLE = 1;
    private static final int WALK = 0;

    private static final int LAYER = 2;

    //wander loop
    private final int wanderLoop;
    private int currentWander;

    Level castToLevel;

    public Witch(Vec2D spawnPoint, int direction, int wanderLoop) {
        super(spawnPoint);

        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        setMoving(false);
        setActive(true);

        setDirection(direction);

        currentWander = 0;
        this.wanderLoop = --wanderLoop;
    }

    @Override
    public void behavior() {
        resetTransfer();
        if (currentWander == 0) {
            turnAround();
        }
        move();
        if (MyMath.isOdd(currentWander) && currentWander != 0) {
            castToLevel.pend(new Projectile(getPosition().out_add(Level.TILED_LENGTH), Projectile.MAGIC));
            castToLevel.pend(new Projectile(getPosition().out_sub(Level.TILED_LENGTH), Projectile.MAGIC));
        }
        currentWander++;
        currentWander = currentWander % wanderLoop;
    }

    @Override
    public void animationUpdate() {
        if (isMoving()) {
            getAnimationManager().toState(WALK);
        }
        getAnimationManager().toState(IDLE);
    }

    @Override
    public void onInteract(Actor actor) {
        if (actor instanceof Knight) {
            actor.destroy();
        }
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation walk = new Animation();
        Image frame00 = new Image("res/ActorExtensionPack/Actor/Witch/witch_walk_0.png");
        Image frame01 = new Image("res/ActorExtensionPack/Actor/Witch/witch_walk_1.png");
        walk.addFrame(frame00);
        walk.addFrame(frame01);

        Animation idle = new Animation();
        Image frame10 = new Image("res/ActorExtensionPack/Actor/Witch/witch_idle_0.png");
        Image frame11 = new Image("res/ActorExtensionPack/Actor/Witch/witch_idle_1.png");
        idle.addFrame(frame10);
        idle.addFrame(frame11);

        animationStateManager.add(idle);
        animationStateManager.add(walk);

        return animationStateManager;
    }

    public void setCastToLevel(Level castToLevel) {
        this.castToLevel = castToLevel;
    }

    @Override
    public Actor clone() {
        Witch witch = new Witch(getPosition(), getDirection(), wanderLoop);
        witch.setCastToLevel(castToLevel);
        return witch;
    }

    public int getWanderLoop() {
        return wanderLoop;
    }

    public int getLAYER() {
        return LAYER;
    }
}
