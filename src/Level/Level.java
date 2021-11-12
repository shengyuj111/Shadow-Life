package Level;

import Listener.MouseListener;
import Music.MusicPlayer;
import Music.MusicPlayerManager;
import Page.GameStage;
import UI.Button;
import actor.*;
import bagel.Image;
import myUtil.MyMath;
import myUtil.Vec2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Level {
    public static  final int TILED_LENGTH = 64;
    //map identifier
    public static final String DUNGEON = "Dungeon";
    public static final String GRASS = "Grass";

    //stage identifier
    private static final int BEGINNER_STAGE = -1;
    private static final int SELECT_STAGE = 0;
    private static final int SIMULATION_STAGE = 1;
    private static final int END_STAGE = 2;

    //actor identifier
    protected static final String KNIGHT = "Knight";
    protected static final String SKELETON = "Skeleton";
    protected static final String WALL_MAKER = "WallMaker";
    protected static final String TRAP = "Trap";
    protected static final String SKULL = "Skull";
    protected static final String PORTAL = "Portal";
    protected static final String WITCH = "Witch";
    protected static final String CHEST = "Chest";
    protected static final String SPIDER = "Spider";
    protected static final String SIGN_UP = "SignUp";
    protected static final String SIGN_DOWN = "SignDown";
    protected static final String SIGN_LEFT = "SignLeft";
    protected static final String SIGN_RIGHT = "SignRight";

    private final int actorX = 1;
    private final int actorY = 2;
    private final int extraActorX = 3;
    private final int extraActorY = 4;
    private final int extraInfo0 = 3;
    private final int extraInfo1 = 4;
    private final int extraInfo2 = 5;

    //refreshRate
    public static final int STEP_REFRESH = 63;
    public static final int HALF_REFRESH = 31;

    //Direction constant
    protected static final int UP = 0;
    protected static final int RIGHT = 1;
    protected static final int DOWN = 2;
    protected static final int LEFT = 3;

    private int currentStage;
    private Image worldMap;
    private final ArrayList<Actor> actors;
    private final ArrayList<Actor> pending;

    private final String worldType;
    private final String filename;
    private final boolean[] playMode;

    public static final int WIN = 1;
    public static final int CONTINUING = 0;
    public static final int LOSE = -1;

    protected int state = CONTINUING;

    SelectPanel selectPanel;
    EndPanel endPanel;

    private BeginnerPanel beginnerPanel;

    private boolean hasBeginnerPanel = false;

    private Button restartButton = new Button(new Image("res/ActorExtensionPack/UI/restart.png"), new Vec2D(817, 0)) {
        @Override
        public void event() {
            if (currentStage != SELECT_STAGE) {
                restart();
            }
        }
    };
    Button startButton = new Button(new Image("res/ActorExtensionPack/UI/start.png"), new Vec2D(885, 0)) {
        @Override
        public void event() {
            if (currentStage != SIMULATION_STAGE) {
                currentStage = SIMULATION_STAGE;
            }
            for (Actor inputActor : selectPanel.planning) {
                actors.add(inputActor.clone());
            }
        }
    };
    Button homeButton = new Button(new Image("res/ActorExtensionPack/UI/home.png"), new Vec2D(955, 0)) {
        @Override
        public void event() {
            MusicPlayerManager.getInstance().stopAll();
            GameStage.getInstance().goTo(GameStage.START_PAGE + GameStage.LOADING);
            actors.clear();
            currentStage = SELECT_STAGE;
            selectPanel.planning.clear();
        }
    };

    private String beginnerFilename;

    public Level(String worldType, String filename, String beginnerFilename, boolean[] playMode) {
        this.worldType = worldType;
        //initialize map skin
        if (worldType.equals(DUNGEON)) {
            worldMap = new Image("res/ActorExtensionPack/Map/DungeonMap.png");
        } else if (worldType.equals(GRASS)) {
            worldMap = new Image("res/images/background.png");
        }

        //initialize stage
        currentStage = BEGINNER_STAGE;

        //initialize actor
        actors = new ArrayList<>();
        pending = new ArrayList<>();

        endPanel = new EndPanel(this);

        this.beginnerFilename = beginnerFilename;

        this.playMode = playMode;

        this.filename = filename;

        loadCSV(filename);

        hasBeginnerPanel = true;

        selectPanel = new SelectPanel(this, playMode);

        beginnerPanel = new BeginnerPanel(beginnerFilename);
    }

    public Level(String worldType, String filename, boolean[] playMode) {
        this.worldType = worldType;
        //initialize map skin
        if (worldType.equals(DUNGEON)) {
            worldMap = new Image("res/ActorExtensionPack/Map/DungeonMap.png");
        } else if (worldType.equals(GRASS)) {
            worldMap = new Image("res/images/background.png");
        }

        //initialize stage
        currentStage = SELECT_STAGE;

        //initialize actor
        actors = new ArrayList<>();
        pending = new ArrayList<>();

        endPanel = new EndPanel(this);

        this.playMode = playMode;

        this.filename = filename;

        loadCSV(filename);

        selectPanel = new SelectPanel(this, playMode);
    }

    public void update(float frameInUnit) {
        if (currentStage == BEGINNER_STAGE) {
            displayAll();
            selectPanel.display();
            beginnerPanel.display();
            if (MouseListener.getInstance().leftClick()) {
                if (!beginnerPanel.response()) {
                    currentStage = SELECT_STAGE;
                }
            }
        } else if (currentStage == SELECT_STAGE) {
            displayAll();
            selectUpdate();
            selectPanel.event();

            startButton.display();
            homeButton.display();

            startButton.onClick();
            homeButton.onClick();
        } else if (currentStage == SIMULATION_STAGE) {
            for (Actor target : actors) {
                target.setMoving(true);
            }
            simulationUpdate(frameInUnit);
            if (state == WIN || state == LOSE) {
                currentStage = END_STAGE;
            }

            restartButton.display();
            homeButton.display();

            restartButton.onClick();
            homeButton.onClick();
        } else if (currentStage == END_STAGE) {
            displayAll();
            endPanel.display();
            if (state == LOSE || this instanceof MakeLevel) {
                restartButton.onClick();
                restartButton.display();
            }
        }
    }

    private int endCheck() {
        for (Actor actor : actors) {
            if (actor instanceof Knight) {
                if (((Knight) actor).isCarrying()) {
                    state = WIN;
                }
                if (actor.isDestroyed()) {
                    state = LOSE;
                }
            }
        }
        return state;
    }

    private void selectUpdate() {
        selectPanel.display();
    }

    private void simulationUpdate(float frameInUnit) {
        for (Actor target : actors) {
            target.animationUpdate();
        }
        if (frameInUnit == HALF_REFRESH) {
            for (Actor target : actors) {
                if (target instanceof Projectile) {
                    target.behavior();
                }
            }
            applyProjectileInteraction();
        }
        if (frameInUnit == STEP_REFRESH) {
            for (Actor target : actors) {
                target.behavior();
            }
            applyInteraction();
            applyProjectileInteraction();
        }

        state = endCheck();
        ((Collection<Actor>) actors).removeIf(Actor::isDestroyed);

        actors.addAll(pending);
        pending.clear();

        Collections.sort(actors);

        displayAll();
    }

    private void displayAll() {
        //display background
        worldMap.drawFromTopLeft(0,0);
        //update Animation
        for (Actor target : actors) {
            target.animationUpdate();
        }
        //display actor
        for (Actor target : actors) {
            target.display();
        }
    }

    private void applyInteraction() {
        for (Actor actor : actors) {
            for (Actor target : actors) {
                if (actor != target && actor.getPosition().equals(target.getPosition()) && !(target instanceof Projectile)) {
                    target.onInteract(actor);
                }
            }
        }
    }

    private void applyProjectileInteraction() {
        for (Actor projectile : actors) {
            if (projectile instanceof  Projectile) {
                for (Actor target : actors) {
                    if (projectile.getPosition().equals(target.getPosition())) {
                        projectile.onInteract(target);
                    }
                }
            }
        }
    }

    public void loadCSV(String filename) {
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                String[] actorInfo = line.split(",");

                //check for actor type and add to actor list
                //indexing for csv reader
                int actorName = 0;
                switch (actorInfo[actorName]) {
                    case KNIGHT:
                        readKnight(actorInfo, filename, lineNum);
                        break;
                    case SKELETON:
                        readSkeleton(actorInfo, filename, lineNum);
                        break;
                    case WALL_MAKER:
                        readWallMaker(actorInfo, filename, lineNum);
                        break;
                    case TRAP:
                        readTrap(actorInfo, filename, lineNum);
                        break;
                    case SKULL:
                        readSkull(actorInfo, filename, lineNum);
                        break;
                    case PORTAL:
                        readPortal(actorInfo, filename, lineNum);
                        break;
                    case WITCH:
                        readWitch(actorInfo, filename, lineNum);
                        break;
                    case CHEST:
                        readChest(actorInfo, filename, lineNum);
                        break;
                    case SPIDER:
                        readSpider(actorInfo, filename, lineNum);
                        break;
                    case SIGN_UP:
                        readSign(actorInfo, filename, lineNum, UP);
                        break;
                    case SIGN_DOWN:
                        readSign(actorInfo, filename, lineNum, DOWN);
                        break;
                    case SIGN_LEFT:
                        readSign(actorInfo, filename, lineNum, LEFT);
                        break;
                    case SIGN_RIGHT:
                        readSign(actorInfo, filename, lineNum, RIGHT);
                        break;
                    default:
                        System.out.println("error: in file \"" + filename + "\" at line " + lineNum);
                        System.exit(-1);
                }
                lineNum++;
            }

        } catch (IOException e) {
            System.out.println("error: file \"" + filename + "\" not found");
            System.exit(-1);
        }

    }

    public void add(Actor actor) {
        Collections.sort(actors);
        actors.add(actor);
    }

    public void pend(Actor actor) {
        pending.add(actor);
    }

    private void readKnight(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint = new Vec2D();
        spawnPoint.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);
        //add actor
        add(new Knight(spawnPoint));

    }

    private void readTrap(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint = new Vec2D();
        spawnPoint.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);
        //add actor
        add(new Trap(spawnPoint, checkInt(actorInfo[extraInfo0], filename, lineNum)));

    }

    private void readChest(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint = new Vec2D();
        spawnPoint.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);
        //add actor
        add(new Chest(spawnPoint));

    }

    private void readSign(String[] actorInfo, String filename, int lineNum, int direction) {
        //check spawn point
        Vec2D spawnPoint = new Vec2D();
        spawnPoint.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);
        //add actor
        add(new Sign(spawnPoint, direction));

    }

    private void readWallMaker(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint1 = new Vec2D();
        Vec2D spawnPoint2 = new Vec2D();
        spawnPoint1.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);
        spawnPoint2.set(checkInt(actorInfo[extraActorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[extraActorY], filename, lineNum) * TILED_LENGTH);
        //add wall maker
        WallMaker wallMaker1 = new WallMaker(spawnPoint1);
        Actor wallMaker2 = new WallMaker(spawnPoint2, wallMaker1.getRelateID());
        add(wallMaker1);
        add(wallMaker2);
        //add lightning
        float start = MyMath.min(spawnPoint1.x, spawnPoint2.x);
        int lightningType = Lightning.HORIZON;
        float end = MyMath.max(spawnPoint1.x, spawnPoint2.x);

        if (spawnPoint1.x == spawnPoint2.x) {
            start = MyMath.min(spawnPoint1.y, spawnPoint2.y);
            lightningType = Lightning.VERTICAL;
            end = MyMath.max(spawnPoint1.y, spawnPoint2.y);
        }

        for (int i = (int) (start + TILED_LENGTH); i < end; i += TILED_LENGTH) {
            if (lightningType == Lightning.VERTICAL) {
                add(new Lightning(new Vec2D(spawnPoint1.x, i), lightningType, wallMaker1.getRelateID()));
            } else {
                add(new Lightning(new Vec2D(i, spawnPoint1.y), lightningType, wallMaker1.getRelateID()));
            }
        }
    }

    private void readPortal(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint1 = new Vec2D();
        Vec2D spawnPoint2 = new Vec2D();
        spawnPoint1.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);
        spawnPoint2.set(checkInt(actorInfo[extraActorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[extraActorY], filename, lineNum) * TILED_LENGTH);
        //add wall maker

        add(new Portal(spawnPoint1, spawnPoint2, this));
    }

    private void readSkeleton(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint = new Vec2D();
        spawnPoint.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);

        //add attribute
        add(new Skeleton(spawnPoint, checkInt(actorInfo[extraInfo0], filename, lineNum), checkInt(actorInfo[extraInfo1], filename, lineNum)));
    }

    private void readSpider(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint = new Vec2D();
        spawnPoint.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);

        //add attribute
        add(new Spider(spawnPoint, checkInt(actorInfo[extraInfo0], filename, lineNum), checkInt(actorInfo[extraInfo1], filename, lineNum), checkInt(actorInfo[extraInfo2], filename, lineNum)));
    }

    private void readSkull(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint = new Vec2D();
        spawnPoint.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);

        //add attribute
        Skull skull  = new Skull(spawnPoint, checkInt(actorInfo[extraInfo0], filename, lineNum), checkInt(actorInfo[extraInfo1], filename, lineNum));
        skull.setCastToLevel(this);
        add(skull);

    }

    private void readWitch(String[] actorInfo, String filename, int lineNum) {
        //check spawn point
        Vec2D spawnPoint = new Vec2D();
        spawnPoint.set(checkInt(actorInfo[actorX], filename, lineNum) * TILED_LENGTH,
                checkInt(actorInfo[actorY], filename, lineNum) * TILED_LENGTH);

        //add attribute
        Witch witch  = new Witch(spawnPoint, checkInt(actorInfo[extraInfo0], filename, lineNum), checkInt(actorInfo[extraInfo1], filename, lineNum));
        witch.setCastToLevel(this);
        add(witch);

    }

    private int checkInt(String integer, String filename, int lineNum) {
        int result = 0;
        try {
            result = Integer.parseInt(integer);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("error: in file \"" + filename +"\" at line " + lineNum);
            System.exit(-1);
        }
        return result;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void restart() {
        actors.clear();
        currentStage = SELECT_STAGE;
        state = CONTINUING;
        
        MusicPlayerManager.getInstance().playSingleTrack(GameStage.playMusic, MusicPlayer.LOOP_OPTION);
        endPanel.setPlaySound(false);
        loadCSV(filename);
        ((Collection<Actor>) actors).removeIf(actor -> actor instanceof Sign);
    }
     public Level clone() {
        Level newLevel;
        if (hasBeginnerPanel) {
            newLevel = new Level(worldType, filename, beginnerFilename, playMode);
        } else {
            newLevel = new Level(worldType, filename, playMode);
        }
        return newLevel;
     }

    public String getWorldType() {
        return worldType;
    }

    public String getFilename() {
        return filename;
    }

    public SelectPanel getSelectPanel() {
        return selectPanel;
    }

    public int getState() {
        return state;
    }

}
