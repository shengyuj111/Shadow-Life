package Level;

import Listener.MouseListener;
import UI.Button;
import UI.Pane;
import actor.*;
import bagel.Image;
import myUtil.MyMath;
import myUtil.Vec2D;

import java.util.*;

import static actor.Trap.OFF;

public class SelectPanel extends Pane {
    //select component
    protected Actor selected = null;

    private final Level castToLevel;

    //worked level
    Vec2D position = new Vec2D(0,640);
    Vec2D framePosition = new Vec2D(28,680);
    Vec2D itemOffset = new Vec2D(0,5);
    Vec2D itemGap = new Vec2D(100,0);

    //Direction constant
    protected static final int UP = 0;
    protected static final int RIGHT = 1;
    protected static final int DOWN = 2;
    protected static final int LEFT = 3;

    //default value
    protected static int INVALID = -1;

    //default value
    private int start = 0;
    private int end = 10;
    private boolean hide = false;
    private boolean remove = false;

    //pane component
    private int leftPart;
    private int middlePart;
    private int rightPart;
    private int targetValid;
    private int targetInvalid;

    private int target = targetValid;

    //limit
    private static final int MAX_ITEM_DISPLAY = 10;

    private static final int LEFT_WIDTH = 68;
    private static final int MIDDLE_WIDTH = 64;
    private static final int RIGHT_WIDTH = 68;
    private static final int SELECT_FRAME_WIDTH = 72;
    private static final int FRAME_WIDTH = 28;
    private static final int WIDTH = 1024;
    private static final int OFFSET = -5;

    //wall data
    private static final int X_UPPERBOUND = 1024 - 64;
    private static final int X_LOWER_BOUND = 64;
    private static final int Y_UPPERBOUND = 768 - 64;
    private static final int Y_LOWER_BOUND = 64;

    private final ArrayList<Actor> selectActor;
    HashMap<Actor, Integer> amount;

    ArrayList<SelectButton> buttons = new ArrayList<SelectButton>();
    ArrayList<Actor> planning;

    private boolean place = false;
    private Vec2D storedPosition = new Vec2D();

    Button nextButton = new Button(new Image("res/ActorExtensionPack/UI/next.png"), new Vec2D(1000, 680)) {
        @Override
        public void event() {
            nextItem();
        }
    };
    Button prevButton = new Button(new Image("res/ActorExtensionPack/UI/prev.png"), new Vec2D(5, 680)) {
        @Override
        public void event() {
            prevItem();
        }
    };

    Button hideButton = new Button(new Image("res/ActorExtensionPack/UI/hide.png"), new Vec2D(470, 625)) {
        @Override
        public void event() {
            hide = true;
        }
    };
    Button showButton = new Button(new Image("res/ActorExtensionPack/UI/show.png"), new Vec2D(470, 740)) {
        @Override
        public void event() {
            hide = false;
        }
    };
    Button deleteButton = new Button(new Image("res/ActorExtensionPack/UI/remove.png"), new Vec2D(928, 608)) {
        @Override
        public void event() {
            remove = true;
        }
    };
    Button deleteCancelButton = new Button(new Image("res/ActorExtensionPack/UI/removeCancel.png"), new Vec2D(928, 608)) {
        @Override
        public void event() {
            remove = false;
        }
    };

    private static final int INFINITY = 99;

    public SelectPanel(Level castToLevel, boolean[] chosen) {
        super();
        selectActor = new ArrayList<>();
        planning = new ArrayList<>();
        amount = new HashMap<>();
        //add all types of actor
        selectActor.add(new Knight(new Vec2D()));
        selectActor.add(new Skeleton(new Vec2D(), RIGHT, INVALID));
        selectActor.add(new Trap(new Vec2D(), OFF));
        selectActor.add(new Spider(new Vec2D(), RIGHT, UP, INVALID));
        selectActor.add(new Skull(new Vec2D(), UP, INVALID));
        selectActor.add(new Portal(new Vec2D(), INVALID, null));
        selectActor.add(new WallMaker(new Vec2D()));
        selectActor.add(new Witch(new Vec2D(), UP, INVALID));
        selectActor.add(new Sign(new Vec2D(), UP));
        selectActor.add(new Sign(new Vec2D(), LEFT));
        selectActor.add(new Sign(new Vec2D(), RIGHT));
        selectActor.add(new Sign(new Vec2D(), DOWN));
        selectActor.add(new Chest(new Vec2D()));

        //remove unwanted
        for (int i = 0; i < chosen.length; i++) {
            selectActor.get(i).animationUpdate();
            if (!chosen[i]) {
                selectActor.get(i).setRemove(true);
            }
        }

        //initialize chest and knight amount
        for (Actor actor : selectActor) {
            if (actor instanceof Knight || actor instanceof Chest) {
                amount.put(actor, 1);
            } else if (actor instanceof Sign && chosen == LevelManager.PLAYING_INVENTORY) {
                amount.put(actor, 0);
            } else {
                amount.put(actor, INFINITY);
            }
        }

        //put sign to inventory
        for (Actor readActor : castToLevel.getActors()) {
            if (readActor instanceof Sign) {
                for (Actor reference : selectActor) {
                    if (reference instanceof Sign && readActor.getDirection() == reference.getDirection()) {
                        amount.put(reference, amount.get(reference) + 1);
                        break;
                    }
                }
            }
        }

        Collection<Actor> list = castToLevel.getActors();
        list.removeIf(actor -> actor instanceof Sign);

        ((Collection<Actor>) selectActor).removeIf(Actor::isRemove);

        this.castToLevel = castToLevel;

        load();
    }

    public void load() {
        //back component
        leftPart = add(new Image("res/ActorExtensionPack/UI/inventoryLeft.png"));
        middlePart = add(new Image("res/ActorExtensionPack/UI/inventoryMiddle.png"));
        rightPart = add(new Image("res/ActorExtensionPack/UI/inventoryRight.png"));
        targetValid = add(new Image("res/ActorExtensionPack/UI/target_valid.png"));
        targetInvalid = add(new Image("res/ActorExtensionPack/UI/target_invalid.png"));

        //item frame component
        for (int i = 0, j = OFFSET; i  < MyMath.min(MAX_ITEM_DISPLAY, selectActor.size()); i++, j+=FRAME_WIDTH) {
            buttons.add(new SelectButton(new Image("res/ActorExtensionPack/UI/selectFrame.png"),new Vec2D(framePosition.x + j, framePosition.y), selectActor.get(i), amount.get(selectActor.get(i))));
            buttons.get(i).setCastToPanel(this);
            j+=SELECT_FRAME_WIDTH;
        }
    }

    @Override
    public void display() {
        for (Actor actor : planning) {
            actor.display();
        }

        //place event
        if (!remove) {
            //place actor
            placeUpdate();
            //change trap status
            for (Actor actor : planning) {
                if (getGridPosition().equals(actor.getPosition()) && MouseListener.getInstance().leftClick()) {
                    if (actor instanceof Trap) {
                        ((Trap) actor).trigger();
                        actor.animationUpdate();
                    }
                }
            }
        } else {
            //remove actor selected
            if (selected != null) {
                for (Actor reference : selectActor) {
                    if (selected.getClass() == reference.getClass()) {
                        if (!(selected instanceof Sign)) {
                            amount.put(reference, amount.get(reference) + 1);
                        } else if (reference instanceof Sign && selected.getDirection() == reference.getDirection()) {
                            amount.put(reference, amount.get(reference) + 1);
                        }
                    }
                }
            }

            selected = null;
            storedPosition = new Vec2D();
            place = false;

            //remove clicked actor
            deleteUpdate();
        }

        if (!hide) {
            //draw back board
            getSource().get(leftPart).drawFromTopLeft(position.x, position.y);
            for (int i = LEFT_WIDTH; i < WIDTH; i += MIDDLE_WIDTH) {
                getSource().get(middlePart).drawFromTopLeft(position.x+i, position.y);
            }
            getSource().get(rightPart).drawFromTopLeft(WIDTH-RIGHT_WIDTH, position.y);

            //draw item frame
            for (Button button : buttons) {
                button.display();
            }

            //draw function button
            nextButton.display();
            prevButton.display();

            //draw actor
            for (int i = start, j = 0; i < MyMath.min(end, selectActor.size()); i++, j++) {
                int offset = i - start;
                selectActor.get(i).getAnimationManager().playAt(framePosition.out_add(itemOffset).out_addN(offset, itemGap));
                //update button
                buttons.get(j).setActor(selectActor.get(i));
                buttons.get(j).setAmount(amount.get(selectActor.get(i)));
            }
        }


        if (hide) {
            showButton.display();
        } else {
            hideButton.display();
        }

        if (remove) {
            deleteCancelButton.display();
        } else {
            deleteButton.display();
        }

        //draw selected actor
        if (selected != null) {
            //selected.getAnimationManager().playAt(MouseListener.getInstance().position().out_sub(MOUSE_OFFSET));
            getSource().get(target).drawFromTopLeft(getGridPosition().x, getGridPosition().y);
            selected.getAnimationManager().playAt(getGridPosition());
        }
    }

    public void event() {
        if (!hide) {
            //check mouse event
            for (int i = 0; i  < MyMath.min(MAX_ITEM_DISPLAY, selectActor.size()); i++) {
                buttons.get(i).onClick();
            }

            nextButton.onClick();
            prevButton.onClick();
        }

        if (hide) {
            showButton.onClick();
        } else {
            hideButton.onClick();
        }

        if (remove) {
            deleteCancelButton.onClick();
        } else {
            deleteButton.onClick();
        }
    }

    protected void nextItem() {
        if (end + 1 > selectActor.size()) return;

        end++;
        start++;
    }

    protected void prevItem() {
        if (start - 1 < 0) return;

        end--;
        start--;
    }

    public void deleteUpdate() {
        //delete single actor
        if (remove && MouseListener.getInstance().leftClick()) {
            Actor planningRemove = null;
            for (Actor actor : planning) {
                if (actor.getPosition().equals(getGridPosition())) {
                    planningRemove = actor;
                    for (Actor reference : selectActor) {
                        if (!(actor instanceof Sign) && actor.getClass() == reference.getClass()) {
                            amount.put(reference, amount.get(reference) + 1);
                            //check different sign
                        } else if ((actor instanceof Sign) && (reference instanceof Sign) && actor.getDirection() == reference.getDirection()) {
                            amount.put(reference, amount.get(reference) + 1);
                        }
                    }
                    break;
                }
            }
            //delete related actor
            if (planningRemove instanceof WallMaker) {
                int id = ((WallMaker) planningRemove).getRelateID();
                Collection<Actor> list = planning;
                list.removeIf(actor -> actor instanceof WallMaker && ((WallMaker) actor).getRelateID() == id);
                list.removeIf(actor -> actor instanceof Lightning && ((Lightning) actor).getRelateID() == id);
            } else if (planningRemove instanceof Lightning) {
                int id = ((Lightning) planningRemove).getRelateID();
                Collection<Actor> list = planning;
                list.removeIf(actor -> actor instanceof WallMaker && ((WallMaker) actor).getRelateID() == id);
                list.removeIf(actor -> actor instanceof Lightning && ((Lightning) actor).getRelateID() == id);
            } else if (planningRemove instanceof Portal) {
                int id = ((Portal) planningRemove).getPairID();
                Collection<Actor> list = planning;
                list.removeIf(actor -> actor instanceof Portal && ((Portal) actor).getPairID() == id);
            } else {
                planning.remove(planningRemove);
            }
        }
    }

    public void placeUpdate() {
        checkValidation();
        if (selected == null) {
            return;
        }
        if (MouseListener.getInstance().rightClick()) {
            //cancel function
            amount.put(selected, amount.get(selected) + 1);
            selected = null;
            storedPosition = new Vec2D();
            place = false;
            return;
        }
        if (selected instanceof Knight) {
            placeKnight();
        } else if (selected instanceof Skeleton) {
            placeSkeleton();
        } else if (selected instanceof Trap) {
            placeTrap();
        } else if (selected instanceof Spider) {
            placeSpider();
        } else if (selected instanceof Skull) {
            placeSkull();
        } else if (selected instanceof Portal) {
            placePortal();
        } else if (selected instanceof WallMaker) {
            placeWallMaker();
        } else if (selected instanceof Witch) {
            placeWitch();
        } else if (selected instanceof Sign) {
            placeSign(selected.getDirection());
        } else if (selected instanceof Chest) {
            placeChest();
        }
    }

    private void invalidPlace() {
        target = targetInvalid;
    }

    private void validPlace() {
        target = targetValid;
    }

    private void checkValidation() {
        for (Actor target : castToLevel.getActors()) {
            if (target.getPosition().equals(getGridPosition())) {
                invalidPlace();
                return;
            }
        }
        //check for clicked actor
        for (Actor target : planning) {
            if (target.getPosition().equals(getGridPosition())) {
                invalidPlace();
                return;
            }
        }
        //check for wall
        if (!MyMath.between(X_LOWER_BOUND, X_UPPERBOUND, MouseListener.getInstance().position().x)
                || !MyMath.between(Y_LOWER_BOUND, Y_UPPERBOUND, MouseListener.getInstance().position().y) ) {
            invalidPlace();
            return;
        }
        validPlace();
    }

    private boolean place(Actor actor) {
        planning.add(actor);
        return true;
    }

    private static Vec2D getGridPosition() {
        float xDiff = MouseListener.getInstance().position().x % Level.TILED_LENGTH;
        float yDiff = MouseListener.getInstance().position().y % Level.TILED_LENGTH;

        return MouseListener.getInstance().position().out_sub(new Vec2D(xDiff, yDiff));
    }

    private void placeKnight() {
        if (MouseListener.getInstance().leftClick() && target == targetValid) {
            Actor knight = new Knight(getGridPosition());
            knight.animationUpdate();
            if (place(knight)) {
                selected = null;
            }
        }
    }

    private void placeSkull() {
        Skull skull = new Skull(storedPosition, UP, INVALID);

        //set spawn point
        if (!place) {
            if (MouseListener.getInstance().leftClick()) {
                storedPosition.set(getGridPosition());
                place = true;
            }
        } else {
            // ensure the attribute
            if (getGridPosition().x != storedPosition.x) {
                invalidPlace();
            } else if (getGridPosition().equals(storedPosition)) {
                invalidPlace();
            } else if (MouseListener.getInstance().leftClick() && target == targetValid) {
                int direction = UP;
                float wander = MyMath.diff(getGridPosition().y, storedPosition.y);
                if (getGridPosition().y <= storedPosition.y) {
                    direction = DOWN;
                }

                skull = new Skull(storedPosition, direction, (int) (wander / Level.TILED_LENGTH + 1));
                skull.setCastToLevel(castToLevel);

                planning.add(skull);

                selected = null;
                place = false;
                storedPosition = new Vec2D();
            }

            skull.animationUpdate();
            skull.display();
        }

    }

    private void placeWitch() {
        Witch witch = new Witch(storedPosition, UP, INVALID);

        //set spawn point
        if (!place) {
            if (MouseListener.getInstance().leftClick()) {
                storedPosition.set(getGridPosition());
                place = true;
            }
        } else {
            // ensure the attribute
            if (getGridPosition().x != storedPosition.x) {
                invalidPlace();
            } else if (getGridPosition().equals(storedPosition)) {
                invalidPlace();
            } else if (MouseListener.getInstance().leftClick() && target == targetValid) {
                int direction = UP;
                float wander = MyMath.diff(getGridPosition().y, storedPosition.y);
                if (getGridPosition().y <= storedPosition.y) {
                    direction = DOWN;
                }

                witch = new Witch(storedPosition, direction, (int) (wander / Level.TILED_LENGTH + 1));
                witch.setCastToLevel(castToLevel);

                planning.add(witch);
                selected = null;
                place = false;
                storedPosition = new Vec2D();
            }

            witch.animationUpdate();
            witch.display();
        }

    }

    private void placePortal() {
        Actor portal = new Portal(storedPosition, INVALID, castToLevel);

        //set spawn point
        if (!place) {
            if (MouseListener.getInstance().leftClick()) {
                storedPosition.set(getGridPosition());
                place = true;
            }
        } else {
            if (getGridPosition().equals(storedPosition)) {
                invalidPlace();
            } else {
                // ensure the attribute
                if (MouseListener.getInstance().leftClick() && target == targetValid) {
                    portal = new Portal(getGridPosition(), storedPosition, planning, castToLevel);
                    planning.add(portal);
                    selected = null;
                    place = false;
                    storedPosition = new Vec2D();
                }

                portal.animationUpdate();
                portal.display();
            }
        }

    }

    private void placeSpider() {
        Actor spider = new Spider(storedPosition, RIGHT, UP, INVALID);

        //set spawn point
        if (!place) {
            if (MouseListener.getInstance().leftClick()) {
                storedPosition.set(getGridPosition());
                place = true;
            }
        } else {
            // ensure the attribute
            if (MyMath.abs(getGridPosition().x - storedPosition.x) != MyMath.abs(getGridPosition().y - storedPosition.y)) {
                invalidPlace();
            } else if (getGridPosition().equals(storedPosition)) {
                invalidPlace();
            } else {
                if (MouseListener.getInstance().leftClick() && target == targetValid) {
                    int direction0 = LEFT;
                    if (MouseListener.getInstance().position().x > storedPosition.x) {
                        direction0 = RIGHT;
                    }

                    int direction1 = DOWN;
                    if (MouseListener.getInstance().position().y < storedPosition.y) {
                        direction1 = UP;
                    }

                    //set attribute
                    float wander = MyMath.diff(getGridPosition().x, storedPosition.x);

                    spider = new Spider(storedPosition, direction0, direction1, (int) (wander / Level.TILED_LENGTH));
                    planning.add(spider);

                    selected = null;
                    place = false;
                    storedPosition = new Vec2D();
                }
            }

            spider.animationUpdate();
            spider.display();
        }

    }

    private void placeWallMaker() {
        WallMaker wallMaker1 = new WallMaker(storedPosition, INVALID);

        //set spawn point
        if (!place) {
            if (MouseListener.getInstance().leftClick()) {
                storedPosition.set(getGridPosition());
                place = true;
            }
        } else {
            // ensure the attribute
            if (getGridPosition().x != storedPosition.x && getGridPosition().y != storedPosition.y) {
                invalidPlace();
            } else if (getGridPosition().equals(storedPosition)) {
                invalidPlace();
            } else {
                if (MouseListener.getInstance().leftClick() && target == targetValid) {
                    wallMaker1 = new WallMaker(storedPosition);
                    planning.add(wallMaker1);
                    Actor wallMaker2 = new WallMaker(getGridPosition(), wallMaker1.getRelateID());
                    planning.add(wallMaker2);
                    Vec2D spawnPoint1 = storedPosition;
                    Vec2D spawnPoint2 = getGridPosition();
                    float start = MyMath.min(spawnPoint1.x, spawnPoint2.x);
                    int lightningType = Lightning.HORIZON;
                    float end = MyMath.max(spawnPoint1.x, spawnPoint2.x);

                    if (spawnPoint1.x == spawnPoint2.x) {
                        start = MyMath.min(spawnPoint1.y, spawnPoint2.y);
                        lightningType = Lightning.VERTICAL;
                        end = MyMath.max(spawnPoint1.y, spawnPoint2.y);
                    }

                    for (int i = (int) (start + Level.TILED_LENGTH); i < end; i += Level.TILED_LENGTH) {
                        if (lightningType == Lightning.VERTICAL) {
                            planning.add(new Lightning(new Vec2D(spawnPoint1.x, i), lightningType, wallMaker1.getRelateID()));
                        } else {
                            planning.add(new Lightning(new Vec2D(i, spawnPoint1.y), lightningType, wallMaker1.getRelateID()));
                        }
                    }

                    selected = null;
                    place = false;
                    storedPosition = new Vec2D();
                }
            }

            wallMaker1.animationUpdate();
            wallMaker1.display();
        }

    }

    private void placeSkeleton() {
        Skeleton skeleton = new Skeleton(storedPosition, Skeleton.TYPE_HORIZON, INVALID);
        //set attribute
        int type = Skeleton.TYPE_HORIZON;
        float wander = MyMath.diff(getGridPosition().x, storedPosition.x);
        if (getGridPosition().x == storedPosition.x) {
            type = Skeleton.TYPE_VERTICAL;
            wander = MyMath.diff(getGridPosition().y, storedPosition.y);
        }
        skeleton.setCurrentType(type);
        skeleton.setWanderLoop((int) (wander / Level.TILED_LENGTH + 1));

        //set spawn point
        if (!place) {
            if (MouseListener.getInstance().leftClick()) {
                storedPosition.set(getGridPosition());
                place = true;
            }
        } else {
            // ensure the attribute
            if (getGridPosition().x != storedPosition.x && getGridPosition().y != storedPosition.y) {
                invalidPlace();
            } else {
                if (MouseListener.getInstance().leftClick() && target == targetValid) {
                    if (getGridPosition().x > storedPosition.x) {
                        skeleton.setDirection(RIGHT);
                    } else if (getGridPosition().x < storedPosition.x) {
                        skeleton.setDirection(LEFT);
                    }

                    if (getGridPosition().y > storedPosition.y) {
                        skeleton.setDirection(DOWN);
                    } else if (getGridPosition().y < storedPosition.y) {
                        skeleton.setDirection(UP);
                    }

                    planning.add(skeleton);
                    selected = null;
                    place = false;
                    storedPosition = new Vec2D();
                }
            }

            skeleton.animationUpdate();
            skeleton.display();
        }

    }

    private void placeTrap() {
        if (MouseListener.getInstance().leftClick() && target == targetValid) {
            Actor trap = new Trap(getGridPosition(), OFF);
            trap.animationUpdate();
            if (place(trap)) {
                selected = null;
            }
        }
    }

    private void placeChest() {
        if (MouseListener.getInstance().leftClick() && target == targetValid) {
            Actor chest = new Chest(getGridPosition());
            chest.animationUpdate();
            if (place(chest)) {
                selected = null;
            }
        }
    }

    private void placeSign(int direction) {
        if (MouseListener.getInstance().leftClick() && target == targetValid) {
            Actor sign = new Sign(getGridPosition(), direction);
            sign.animationUpdate();
            if (place(sign)) {
                selected = null;
            }
        }
    }

    public HashMap<Actor, Integer> getAmount() {
        return amount;
    }
}
