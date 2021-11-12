package actor;

import Animation.Animation;
import Animation.AnimationStateManager;
import bagel.Image;
import myUtil.Vec2D;

public class Skeleton extends Actor {
    //state
    private static final int IDLE_VERTICAL = 0;
    private static final int IDLE_HORIZON = 1;
    private static final int WALK_VERTICAL = 2;
    private static final int WALK_HORIZON = 3;

    private static final int IDLE_VERTICAL_LEFT = 4;
    private static final int IDLE_HORIZON_LEFT = 5;
    private static final int WALK_VERTICAL_LEFT = 6;
    private static final int WALK_HORIZON_LEFT = 7;

    private static final int LAYER = 2;

    //type
    public static final int TYPE_VERTICAL = 0;
    public static final int TYPE_HORIZON = 1;

    //wander loop
    private int wanderLoop;
    private int currentWander;

    private int currentType;

    public Skeleton(Vec2D spawnPoint, int direction, int wanderLoop) {
        super(spawnPoint);
        if (getAnimationManager() == null) {
            setGetAnimationManager(newAnimationManager());
        }

        setMoving(false);
        setActive(true);

        setDirection(direction);

        setLayer(LAYER);

        //initialize type
        if (direction == RIGHT || direction == LEFT) {
            this.currentType = TYPE_HORIZON;
        } else if (direction == UP || direction == DOWN) {
            this.currentType = TYPE_VERTICAL;
        }

        currentWander = 0;
        this.wanderLoop = --wanderLoop;
    }

    @Override
    public void behavior() {
        resetTransfer();
        move();
        if (currentWander == wanderLoop - 1) {
            turnAround();
        }
        currentWander++;
        currentWander = currentWander % wanderLoop;

    }

    @Override
    public void animationUpdate() {
        if (isMoving()) {
            if (currentType == TYPE_VERTICAL) {
                if (getFace() == LEFT) {
                    getAnimationManager().toState(WALK_VERTICAL_LEFT);
                } else {
                    getAnimationManager().toState(WALK_VERTICAL);
                }
            } else if (currentType == TYPE_HORIZON) {
                if (getFace() == LEFT) {
                    getAnimationManager().toState(WALK_HORIZON_LEFT);
                } else {
                    getAnimationManager().toState(WALK_HORIZON);
                }
            }
        } else {
            if (currentType == TYPE_VERTICAL) {
                if (getFace() == LEFT) {
                    getAnimationManager().toState(IDLE_VERTICAL_LEFT);
                } else {
                    getAnimationManager().toState(IDLE_VERTICAL);
                }
            } else if (currentType == TYPE_HORIZON) {
                if (getFace() == LEFT) {
                    getAnimationManager().toState(IDLE_HORIZON_LEFT);
                } else {
                    getAnimationManager().toState(IDLE_HORIZON);
                }
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

        Animation idleVertical = new Animation();
        Image frame00 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_idle_0.png");
        Image frame01 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_idle_1.png");
        Image frame02 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_idle_2.png");
        idleVertical.addFrame(frame00);
        idleVertical.addFrame(frame01);
        idleVertical.addFrame(frame02);

        Animation idleHorizon = new Animation();
        Image frame10 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_idle_0.png");
        Image frame11 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_idle_1.png");
        Image frame12 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_idle_2.png");
        idleHorizon.addFrame(frame10);
        idleHorizon.addFrame(frame11);
        idleHorizon.addFrame(frame12);

        Animation walkVertical = new Animation();
        Image frame20 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_walk_0.png");
        Image frame21 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_walk_1.png");
        walkVertical.addFrame(frame20);
        walkVertical.addFrame(frame21);

        Animation walkHorizon = new Animation();
        Image frame30 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_walk_0.png");
        Image frame31 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_walk_1.png");
        walkHorizon.addFrame(frame30);
        walkHorizon.addFrame(frame31);

        Animation idleVerticalL = new Animation();
        Image frame40 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_idle_left_0.png");
        Image frame41 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_idle_left_1.png");
        Image frame42 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_idle_left_2.png");
        idleVerticalL.addFrame(frame40);
        idleVerticalL.addFrame(frame41);
        idleVerticalL.addFrame(frame42);

        Animation idleHorizonL = new Animation();
        Image frame50 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_idle_left_0.png");
        Image frame51 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_idle_left_1.png");
        Image frame52 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_idle_left_2.png");
        idleHorizonL.addFrame(frame50);
        idleHorizonL.addFrame(frame51);
        idleHorizonL.addFrame(frame52);

        Animation walkVerticalL = new Animation();
        Image frame60 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_walk_left_0.png");
        Image frame61 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_up_walk_left_1.png");
        walkVerticalL.addFrame(frame60);
        walkVerticalL.addFrame(frame61);

        Animation walkHorizonL = new Animation();
        Image frame70 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_walk_left_0.png");
        Image frame71 = new Image("res/ActorExtensionPack/Actor/Skeleton/skeleton_down_walk_left_1.png");
        walkHorizonL.addFrame(frame70);
        walkHorizonL.addFrame(frame71);

        animationStateManager.add(idleVertical);
        animationStateManager.add(idleHorizon);
        animationStateManager.add(walkVertical);
        animationStateManager.add(walkHorizon);

        animationStateManager.add(idleVerticalL);
        animationStateManager.add(idleHorizonL);
        animationStateManager.add(walkVerticalL);
        animationStateManager.add(walkHorizonL);

        return animationStateManager;
    }

    public int getWanderLoop() {
        return wanderLoop;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    public void setWanderLoop(int wanderLoop) {
        this.wanderLoop = wanderLoop;
    }

    @Override
    public Actor clone() {
        return new Skeleton(getPosition(),getDirection(), wanderLoop);
    }

    public int getLAYER() {
        return LAYER;
    }
}
