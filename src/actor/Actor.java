package actor;

import Animation.AnimationStateManager;
import Level.Level;
import myUtil.Vec2D;

public abstract class Actor implements Comparable<Actor> {
    //Moving properties
    private Vec2D position;

    //Basic properties
    private boolean active; //can the actor move
    private boolean destroyed;
    private boolean isMoving;
    private boolean remove;

    //Direction
    private int direction;
    private int face;

    //Direction constant
    private static final Vec2D UP_VEC = new Vec2D(0, -1);
    private static final Vec2D DOWN_VEC = new Vec2D(0, 1);
    private static final Vec2D LEFT_VEC = new Vec2D(-1, 0);
    private static final Vec2D RIGHT_VEC = new Vec2D(1, 0);

    //Direction constant
    protected static final int UP = 0;
    protected static final int RIGHT = 1;
    protected static final int DOWN = 2;
    protected static final int LEFT = 3;

    protected static final int NUM_DIRECTION = 4;

    private int layer = 0;

    //portal
    private boolean transfer;

    private AnimationStateManager getAnimationManager;

    public Actor(Vec2D spawnPoint) {
        //Moving information
        position = new Vec2D();
        position.set(spawnPoint);
        destroyed = false;
        face = LEFT;
        remove = false;

        transfer = false;
    }

    public void move() {
        setMoving(true);
        if (active) {
            if (direction == UP) {
                position.addN(Level.TILED_LENGTH, UP_VEC);
            } else if (direction == DOWN) {
                position.addN(Level.TILED_LENGTH, DOWN_VEC);
            } else if (direction == LEFT) {
                position.addN(Level.TILED_LENGTH, LEFT_VEC);
            } else if (direction == RIGHT) {
                position.addN(Level.TILED_LENGTH, RIGHT_VEC);
            } else {
                //do nothing
                return;
            }
            if (position.x < 64) {
                position.addN(-Level.TILED_LENGTH, LEFT_VEC);
                turnAround();
                if (this instanceof Projectile) {
                    destroy();
                }
            } else if (position.x > (1024 - 64 - 64)) {
                position.addN(-Level.TILED_LENGTH, RIGHT_VEC);
                turnAround();
                if (this instanceof Projectile) {
                    destroy();
                }
            }
            if (position.y < 64) {
                position.addN(-Level.TILED_LENGTH, UP_VEC);
                turnAround();
                if (this instanceof Projectile) {
                    destroy();
                }
            } else if (position.y > (768 - 64 - 64)) {
                position.addN(-Level.TILED_LENGTH, DOWN_VEC);
                turnAround();
                if (this instanceof Projectile) {
                    destroy();
                }
            }
        }
    }

    public void display() {
        getAnimationManager().playAt(getPosition());
    }

    public abstract Actor clone();

    public abstract void behavior();

    public abstract void onInteract(Actor actor);

    public abstract void animationUpdate();

    //Getter and Setter
    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDirection(int direction) {
        if (direction == LEFT) {
            face = LEFT;
        }
        if (direction == RIGHT) {
            face = RIGHT;
        }
        this.direction = direction;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void transfer() {
        transfer = true;
    }

    public void resetTransfer() {
        transfer = false;
    }

    public Vec2D getPosition() {
        return position;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isActive() {
        return active;
    }

    public AnimationStateManager getAnimationManager() {
        return getAnimationManager;
    }

    public void setGetAnimationManager(AnimationStateManager getAnimationManager) {
        this.getAnimationManager = getAnimationManager;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setPosition(Vec2D position) {
        this.position = position;
    }

    public int getFace() {
        return face;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    //useful action
    public void rotate90Clockwise() {
        direction = Math.floorMod(++direction, NUM_DIRECTION);
        if (direction == LEFT) {
            face = LEFT;
        }
        if (direction == RIGHT) {
            face = RIGHT;
        }
    }

    public void destroy() {
        active = false;
        destroyed = true;
    }

    public void turnAround() {
        rotate90Clockwise();
        rotate90Clockwise();
    }

    @Override
    public int compareTo(Actor o) {
        return this.layer - o.getLayer();
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
