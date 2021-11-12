package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class Projectile extends Actor {
    //state
    public static final int MAGIC = 0;
    public static final int FIRE = 1;

    private static final int LAYER = 3;

    public Projectile(Vec2D spawnPoint, int type) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setLayer(LAYER);

        setMoving(false);
        setDirection(RIGHT);
        setActive(true);

        getAnimationManager().toState(type);
    }

    @Override
    public void behavior() {
        resetTransfer();
        move();
    }

    @Override
    public void animationUpdate() {}

    @Override
    public void onInteract(Actor actor) {
        if (actor instanceof Knight) {
            actor.destroy();
        }
    }

    private static AnimationStateManager newAnimationManager() {
        AnimationStateManager animationStateManager = new AnimationStateManager();

        Animation magic = new Animation();
        Image frame00 = new Image("res/ActorExtensionPack/Actor/Witch/magicBall_0.png");
        Image frame01 = new Image("res/ActorExtensionPack/Actor/Witch/magicBall_1.png");
        Image frame02 = new Image("res/ActorExtensionPack/Actor/Witch/magicBall_2.png");
        magic.addFrame(frame00);
        magic.addFrame(frame01);
        magic.addFrame(frame02);


        Animation fire = new Animation();
        Image frame10 = new Image("res/ActorExtensionPack/Actor/FireSkull/fireBall_0.png");
        Image frame11 = new Image("res/ActorExtensionPack/Actor/FireSkull/fireBall_1.png");
        Image frame12 = new Image("res/ActorExtensionPack/Actor/FireSkull/fireBall_2.png");
        fire.addFrame(frame10);
        fire.addFrame(frame11);
        fire.addFrame(frame12);

        animationStateManager.add(magic);
        animationStateManager.add(fire);

        return animationStateManager;
    }

    @Override
    public Actor clone() {
        return null;
    }

    public int getLAYER() {
        return LAYER;
    }
}
