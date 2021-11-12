package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class Trap extends Actor {
    //state
    private static final int TRAP_REST = 0;
    private static final int TRAP_ATTACK = 1;

    public static final int ON = 1;
    public static final int OFF = 0;

    private static final int LAYER = 0;

    private boolean attack;

    private int on;

    public Trap(Vec2D spawnPoint, int on) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        setMoving(false);
        setActive(false);

        this.on = on;
        if (on == ON) {
            attack = true;
        } else if (on == OFF) {
            attack = false;
        }
    }

    @Override
    public void behavior() {
        attack = !attack;
    }

    @Override
    public void animationUpdate() {
        if (!attack) {
            getAnimationManager().toState(TRAP_REST);
        } else {
            getAnimationManager().toState(TRAP_ATTACK);
        }

    }

    @Override
    public void onInteract(Actor actor) {
        if (actor instanceof Knight && attack) {
            actor.destroy();
        }
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation trapRest = new Animation();
        Animation trapAttack = new Animation();
        Image frame0 = new Image("res/ActorExtensionPack/Actor/Trap/trap_0.png");
        Image frame2 = new Image("res/ActorExtensionPack/Actor/Trap/trap_2.png");
        trapRest.addFrame(frame0);
        trapAttack.addFrame(frame2);

        animationStateManager.add(trapRest);
        animationStateManager.add(trapAttack);

        return animationStateManager;
    }

    @Override
    public Actor clone() {
        return new Trap(getPosition(), on);
    }

    public int getLAYER() {
        return LAYER;
    }

    public void trigger() {
        if (on == ON) {
            on = OFF;
            attack = false;
        } else if (on == OFF) {
            on = ON;
            attack = true;
        }
    }

    public int getOn() {
        return on;
    }
}
