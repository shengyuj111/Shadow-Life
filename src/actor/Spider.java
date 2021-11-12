package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class Spider extends Actor {
    //state
    private static final int IDLE = 0;
    private static final int WALK = 1;
    private static final int IDLE_RIGHT = 2;
    private static final int WALK_RIGHT = 3;

    private static final int LAYER = 2;

    //wander loop
    private final int wanderLoop;
    private int currentWander;

    private int direction0;
    private int direction1;

    public Spider(Vec2D spawnPoint, int direction0, int direction1, int wanderLoop) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setMoving(false);
        setActive(true);

        setLayer(LAYER);

        setDirection(direction0);

        this.direction0 = direction0;
        this.direction1 = direction1;

        currentWander = 0;
        this.wanderLoop = wanderLoop;
    }

    @Override
    public void behavior() {
        resetTransfer();

        setDirection(direction0);
        move();
        setDirection(direction1);
        move();

        if (currentWander == wanderLoop - 1) {
            if (direction0 == RIGHT) {
                direction0 = LEFT;
            } else if (direction0 == LEFT) {
                direction0 = RIGHT;
            }

            if (direction1 == UP) {
                direction1 = DOWN;
            } else if (direction1 == DOWN) {
                direction1 = UP;
            }
        }
        currentWander++;
        currentWander = currentWander % wanderLoop;

    }

    @Override
    public void animationUpdate() {
        if (isMoving()) {
            if (getFace() == RIGHT) {
                getAnimationManager().toState(WALK_RIGHT);
            } else {
                getAnimationManager().toState(WALK);
            }
        } else {
            if (getFace() == RIGHT) {
                getAnimationManager().toState(IDLE_RIGHT);
            } else {
                getAnimationManager().toState(IDLE);
            }
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
        Image frame00 = new Image("res/ActorExtensionPack/Actor/Spider/spider_idle_0.png");
        Image frame01 = new Image("res/ActorExtensionPack/Actor/Spider/spider_idle_1.png");
        idle.addFrame(frame00);
        idle.addFrame(frame01);

        Animation walk = new Animation();
        Image frame10 = new Image("res/ActorExtensionPack/Actor/Spider/spider_move_0.png");
        Image frame11 = new Image("res/ActorExtensionPack/Actor/Spider/spider_move_1.png");
        Image frame12 = new Image("res/ActorExtensionPack/Actor/Spider/spider_move_2.png");
        Image frame13 = new Image("res/ActorExtensionPack/Actor/Spider/spider_move_3.png");
        walk.addFrame(frame10);
        walk.addFrame(frame11);
        walk.addFrame(frame12);
        walk.addFrame(frame13);

        Animation idleR = new Animation();
        Image frame20 = new Image("res/ActorExtensionPack/Actor/Spider/spider_idle_right_0.png");
        Image frame21 = new Image("res/ActorExtensionPack/Actor/Spider/spider_idle_right_1.png");
        idleR.addFrame(frame20);
        idleR.addFrame(frame21);

        Animation walkR = new Animation();
        Image frame30 = new Image("res/ActorExtensionPack/Actor/Spider/spider_move_right_0.png");
        Image frame31 = new Image("res/ActorExtensionPack/Actor/Spider/spider_move_right_1.png");
        Image frame32 = new Image("res/ActorExtensionPack/Actor/Spider/spider_move_right_2.png");
        Image frame33 = new Image("res/ActorExtensionPack/Actor/Spider/spider_move_right_3.png");
        walkR.addFrame(frame30);
        walkR.addFrame(frame31);
        walkR.addFrame(frame32);
        walkR.addFrame(frame33);

        animationStateManager.add(idle);
        animationStateManager.add(walk);

        animationStateManager.add(idleR);
        animationStateManager.add(walkR);

        return animationStateManager;
    }

    @Override
    public Actor clone() {
        return new Spider(getPosition(), direction0, direction1, wanderLoop);
    }

    public int getWanderLoop() {
        return wanderLoop;
    }

    public int getLAYER() {
        return LAYER;
    }

    public int getDirection0() {
        return direction0;
    }

    public int getDirection1() {
        return direction1;
    }
}
