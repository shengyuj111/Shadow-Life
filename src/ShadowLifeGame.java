import Animation.Animation;
import Level.*;
import Listener.MouseListener;
import Music.MusicPlayerManager;
import Page.LevelPage;
import UI.Button;
import UI.TextBox;
import bagel.*;
import bagel.util.Colour;
import myUtil.Vec2D;
import Page.GameStage;
import Page.StartPage;

import static Page.LevelPage.INVALID;

public class ShadowLifeGame extends AbstractGame {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;

    //time control
    private static final float dt = 1;
    private static float framePassed = 0;

    //int init
    private static final int RESET = 0;

    //page and levels
    private Level level = LevelManager.getInstance().getLevel(3);
    private final StartPage startPage = new StartPage();
    private final LevelPage levelPage = new LevelPage("res/ActorExtensionPack/Level", LevelPage.DEFAULT_LEVEL);
    private LevelPage myLevelPage = new LevelPage("res/ActorExtensionPack/Level/MyLevel", LevelPage.MY_LEVEL);
    private final MakeLevel sampleLevel = new MakeLevel(Level.DUNGEON, "res/ActorExtensionPack/Level/EmptyMap.csv", LevelManager.MAKING_INVENTORY);
    private MakeLevel makeLevel = sampleLevel.clone();

    //progress bar
    Animation progress = new Animation();
    private int loadCount = 0;
    Vec2D progressPosition = new Vec2D(192,384);
    private static final int LOADING_FONT_SIZE = 80;
    private static final int LOADING_TIME = 3 * 64;

    //make level identifier
    private boolean fromMyLevel = false;

    //visualize component
    TextBox loading = new TextBox("FAKE LOADING...", 1000, progressPosition, Colour.WHITE);

    Image background = new Image("res/ActorExtensionPack/UI/startpage.png");

    //create generate button for make level
    Button generateButton = new Button(new Image("res/ActorExtensionPack/UI/yes.png"), new Vec2D(955, 500)) {
        @Override
        public void event() {
            MusicPlayerManager.getInstance().stopAll();
            GameStage.getInstance().goTo(GameStage.MY_LEVEL_PAGE);
            makeLevel.generate();
            makeLevel = null;
            MyLevelManager.refresh();
            myLevelPage = new LevelPage("res/ActorExtensionPack/Level", LevelPage.MY_LEVEL);
        }
    };

    Button nextLevel = new Button(new Image("res/ActorExtensionPack/UI/nextLevel.png"), new Vec2D(823, 0)) {
        @Override
        public void event() {
            GameStage.getInstance().goTo(GameStage.GAME_PAGE + GameStage.LOADING);
            if (fromMyLevel) {
                level = MyLevelManager.getInstance().getLevel(MyLevelManager.getInstance().currentLevel + 1);
            } else {
                level = LevelManager.getInstance().getLevel(LevelManager.getInstance().currentLevel + 1);
            }
        }
    };

    //create level button
    Button levelButton = new Button(new Image("res/ActorExtensionPack/UI/LevelMenu.png"), new Vec2D(749, 0)) {
        @Override
        public void event() {
            if (GameStage.getInstance().getCurrentStage() == GameStage.GAME_PAGE) {
                GameStage.getInstance().goTo(GameStage.LEVEL_SELECT_PAGE);
            }
            if (fromMyLevel) {
                GameStage.getInstance().goTo(GameStage.MY_LEVEL_PAGE);
                fromMyLevel = false;
            }
            if (GameStage.getInstance().getCurrentStage() == GameStage.MAKE_PAGE) {
                GameStage.getInstance().goTo(GameStage.MY_LEVEL_PAGE);
            }
        }
    };

    public ShadowLifeGame() {
        super(WIDTH, HEIGHT, "Shadow Life");

        //create new world
        startPage.load();
        GameStage.getInstance().goTo(GameStage.START_PAGE);
        createProgressBar();
    }

    public static void main(String[] args) {
        //generate world
        ShadowLifeGame game = new ShadowLifeGame();
        game.run();
    }


    @Override
    public void update(Input input) {
        //listen input
        MouseListener.getInstance().listen(input);
        background.drawFromTopLeft(0,0);

        if (GameStage.getInstance().getCurrentStage() == GameStage.START_PAGE) {
            if (makeLevel != null) {
                makeLevel = null;
            }
            startPage.display();
        } else if (GameStage.getInstance().getCurrentStage() == GameStage.LEVEL_SELECT_PAGE) {
            fromMyLevel = false;
            levelPage.display();
            if (levelPage.getLevelChosen() != INVALID) {
                GameStage.getInstance().goTo(GameStage.GAME_PAGE + GameStage.LOADING);
                level = LevelManager.getInstance().getLevel(levelPage.getLevelChosen());
                levelPage.setLevelChosen(INVALID);
            }
        } else if (GameStage.getInstance().getCurrentStage() == GameStage.GAME_PAGE) {
            level.update(framePassed % Level.TILED_LENGTH);
            if (level.getState() == Level.LOSE) {
                levelButton.display();
                levelButton.onClick();
            }
            if (level.getState() == Level.WIN) {
                if (fromMyLevel && MyLevelManager.getInstance().hasNextLevel()) {
                    nextLevel.display();
                    nextLevel.onClick();
                } else if (!fromMyLevel && LevelManager.getInstance().hasNextLevel()) {
                    nextLevel.display();
                    nextLevel.onClick();
                }
                levelButton.display();
                levelButton.onClick();
            }

        } else if (GameStage.getInstance().getCurrentStage() == GameStage.MAKE_PAGE) {
            if (makeLevel == null) {
                makeLevel = sampleLevel.clone();
            }
            makeLevel.update(framePassed % Level.TILED_LENGTH);
            if (makeLevel.getState() == Level.WIN) {
                generateButton.display();
                generateButton.onClick();
            }
            if (level.getState() == Level.LOSE) {
                levelButton.display();
                levelButton.onClick();
            }
        } else if (GameStage.getInstance().getCurrentStage() == GameStage.MY_LEVEL_PAGE) {
            if (makeLevel != null) {
                makeLevel = null;
            }
            myLevelPage.display();
            if (myLevelPage.getLevelChosen() != INVALID) {
                fromMyLevel = true;
                GameStage.getInstance().goTo(GameStage.GAME_PAGE + GameStage.LOADING);
                level = MyLevelManager.getInstance().getLevel(myLevelPage.getLevelChosen());
                myLevelPage.setLevelChosen(INVALID);
            }
        } else if (GameStage.getInstance().getCurrentStage() >= GameStage.LOADING) {
            loading.setFontSize(LOADING_FONT_SIZE);
            loading.display();
            loadCount++;
            progress.playAt(progressPosition);
            //check for the time pass
            if (loadCount > LOADING_TIME) {
                MusicPlayerManager.getInstance().stopAll();
                progress.replay();
                loadCount = RESET;
                GameStage.getInstance().goTo(GameStage.getInstance().getCurrentStage() - GameStage.LOADING);
            }
        }

        //update time
        framePassed += dt;
    }

    public void createProgressBar() {
        //add animation for progress bar
        Image f0 = new Image("res/ActorExtensionPack/UI/progressBar/0.png");
        Image f1 = new Image("res/ActorExtensionPack/UI/progressBar/1.png");
        Image f2 = new Image("res/ActorExtensionPack/UI/progressBar/2.png");
        Image f3 = new Image("res/ActorExtensionPack/UI/progressBar/3.png");
        Image f4 = new Image("res/ActorExtensionPack/UI/progressBar/4.png");
        Image f5 = new Image("res/ActorExtensionPack/UI/progressBar/5.png");
        Image f6 = new Image("res/ActorExtensionPack/UI/progressBar/6.png");
        Image f7 = new Image("res/ActorExtensionPack/UI/progressBar/7.png");
        Image f8 = new Image("res/ActorExtensionPack/UI/progressBar/8.png");
        Image f9 = new Image("res/ActorExtensionPack/UI/progressBar/9.png");

        progress.addFrame(f0);
        progress.addFrame(f1);
        progress.addFrame(f2);
        progress.addFrame(f3);
        progress.addFrame(f4);
        progress.addFrame(f5);
        progress.addFrame(f6);
        progress.addFrame(f7);
        progress.addFrame(f8);
        progress.addFrame(f9);
        progress.addFrame(f9);
        progress.addFrame(f9);
        progress.addFrame(f9);
        progress.addFrame(f9);
        progress.addFrame(f9);
        progress.addFrame(f9);
    }
}
