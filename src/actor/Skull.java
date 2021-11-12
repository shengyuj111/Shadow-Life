package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import Level.Level;
import bagel.Image;
import myUtil.MyMath;
import myUtil.Vec2D;

public class Skull extends Actor {
    //state
    private static final int IDLE = 0;
    private static final int ATTACK = 1;

    private static final int LAYER = 2;

    //wander loop
    private final int wanderLoop;
    private int currentWander;

    Level castToLevel;

    public Skull(Vec2D spawnPoint, int direction, int wanderLoop) {
        super(spawnPoint);

        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setMoving(false);
        setActive(true);

        setLayer(LAYER);

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

        if (MyMath.isEven(currentWander) && currentWander != 0) {
            castToLevel.pend(new Projectile(getPosition().clone(), Projectile.FIRE));
        }
        move();
        currentWander++;
        currentWander = currentWander % wanderLoop;
    }

    @Override
    public void animationUpdate() {
        getAnimationManager().toState(IDLE);
        if (MyMath.isEven(currentWander) && currentWander != 0) {
            getAnimationManager().toState(ATTACK);
        } else {
            getAnimationManager().toState(IDLE);
        }
    }

    @Override
    public void onInteract(Actor actor) {
        if (actor instanceof Knight) {
            actor.destroy();
        }
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation idle = new Animation();
        Image frame00 = new Image("res/ActorExtensionPack/Actor/FireSkull/skull_normal_idle_0.png");
        Image frame01 = new Image("res/ActorExtensionPack/Actor/FireSkull/skull_normal_idle_1.png");
        Image frame02 = new Image("res/ActorExtensionPack/Actor/FireSkull/skull_normal_idle_2.png");
        Image frame03 = new Image("res/ActorExtensionPack/Actor/FireSkull/skull_normal_idle_3.png");
        idle.addFrame(frame00);
        idle.addFrame(frame01);
        idle.addFrame(frame02);
        idle.addFrame(frame03);

        Animation attack = new Animation();
        Image frame10 = new Image("res/ActorExtensionPack/Actor/FireSkull/skull_attack_idle_0.png");
        Image frame11 = new Image("res/ActorExtensionPack/Actor/FireSkull/skull_attack_idle_1.png");
        Image frame12 = new Image("res/ActorExtensionPack/Actor/FireSkull/skull_attack_idle_2.png");
        Image frame13 = new Image("res/ActorExtensionPack/Actor/FireSkull/skull_attack_idle_3.png");
        attack.addFrame(frame10);
        attack.addFrame(frame11);
        attack.addFrame(frame12);
        attack.addFrame(frame13);

        animationStateManager.add(idle);
        animationStateManager.add(attack);

        return animationStateManager;
    }

    public void setCastToLevel(Level castToLevel) {
        this.castToLevel = castToLevel;
    }

    @Override
    public Actor clone() {
        Skull skull = new Skull(getPosition(), getDirection(), wanderLoop);
        skull.setCastToLevel(castToLevel);
        return skull;
    }

    public int getWanderLoop() {
        return wanderLoop;
    }

    public int getLAYER() {
        return LAYER;
    }
}
