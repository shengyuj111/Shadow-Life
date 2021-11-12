package Page;

import Level.MyLevelManager;
import UI.Button;
import UI.Pane;
import UI.TextBox;
import bagel.Image;
import bagel.util.Colour;
import myUtil.MyMath;
import myUtil.Vec2D;

import java.util.ArrayList;

public class LevelPage extends Pane {
    //level type
    public static final int DEFAULT_LEVEL = 0;
    public static final int MY_LEVEL = 1;
    public static final int INVALID = -1;

    public int DEFAULT_LEVEL_COUNT = 15;

    private final int upPart;
    private final int middlePart;
    private final int bottomPart;

    private static final Vec2D UP_POSITION = new Vec2D(220, 200);
    private static final Vec2D MIDDLE_POSITION1 = new Vec2D(220, 300);
    private static final Vec2D MIDDLE_POSITION2 = new Vec2D(220, 400);
    private static final Vec2D MIDDLE_POSITION3 = new Vec2D(220, 500);
    private static final Vec2D BOTTOM_POSITION = new Vec2D(220, 600);

    Button nextButton = new Button(new Image("res/ActorExtensionPack/UI/next.png"), new Vec2D(780, 450)) {
        @Override
        public void event() {
            nextPage();
        }
    };
    Button prevButton = new Button(new Image("res/ActorExtensionPack/UI/prev.png"), new Vec2D(220, 450)) {
        @Override
        public void event() {
            prevPage();
        }
    };

    Button homeButton = new Button(new Image("res/ActorExtensionPack/UI/home.png"), new Vec2D(955, 0)) {
        @Override
        public void event() {
            start = 0;
            GameStage.getInstance().goTo(GameStage.START_PAGE + GameStage.LOADING);
        }
    };

    TextBox title;

    private final static int TITLE_SIZE = 100;

    private int start = 0;

    private int totalLevel;

    private int levelChosen = INVALID;

    ArrayList<LevelButton> levelButtons;
    private static final int MAX_LEVEL_BUTTON_ROW = 3;
    private static final int MAX_LEVEL_BUTTON_COL = 3;

    public LevelPage(String path, int type) {
        super();

        levelButtons = new ArrayList<>();


        if (type == DEFAULT_LEVEL) {
            totalLevel = DEFAULT_LEVEL_COUNT;
            title = new TextBox("Challenge", 1000, UP_POSITION.clone().out_add(new Vec2D(-30,0)), Colour.WHITE);
        } else if (type == MY_LEVEL) {
            load();
            title = new TextBox("My Map", 1000, UP_POSITION.clone().out_add(new Vec2D(-30,0)), Colour.WHITE);
        }

        generateButton(totalLevel);

        upPart = add(new Image("res/ActorExtensionPack/UI/levelBackUp.png"));
        middlePart = add(new Image("res/ActorExtensionPack/UI/levelBackMiddle.png"));
        bottomPart = add(new Image("res/ActorExtensionPack/UI/levelBackBottom.png"));

    }

    public void load() {
        totalLevel = MyLevelManager.getInstance().getSize();
    }

    @Override
    public void display() {
        title.setFontSize(TITLE_SIZE);

        getSource().get(upPart).drawFromTopLeft(UP_POSITION.x, UP_POSITION.y);
        getSource().get(middlePart).drawFromTopLeft(MIDDLE_POSITION1.x, MIDDLE_POSITION1.y);
        getSource().get(middlePart).drawFromTopLeft(MIDDLE_POSITION2.x, MIDDLE_POSITION2.y);
        getSource().get(middlePart).drawFromTopLeft(MIDDLE_POSITION3.x, MIDDLE_POSITION3.y);
        getSource().get(bottomPart).drawFromTopLeft(BOTTOM_POSITION.x, BOTTOM_POSITION.y);

        for (int i = 0; i < MyMath.min(totalLevel - start, MAX_LEVEL_BUTTON_COL * MAX_LEVEL_BUTTON_ROW); i++) {
            levelButtons.get(i).setNum(start + i + 1);
            levelButtons.get(i).display();
        }

        homeButton.display();

        nextButton.display();
        prevButton.display();

        title.display();

        for (int i = 0; i < MyMath.min(totalLevel - start, MAX_LEVEL_BUTTON_COL * MAX_LEVEL_BUTTON_ROW); i++) {
            levelButtons.get(i).onClick();
        }

        homeButton.onClick();

        nextButton.onClick();
        prevButton.onClick();
    }

    public void setLevelChosen(int levelChosen) {
        this.levelChosen = levelChosen;
    }

    public void generateButton(int num) {
        //generate buttons
        int count = 0;

        for (int i = 0; i < MAX_LEVEL_BUTTON_COL; i++) {
            if (count == MyMath.min(MAX_LEVEL_BUTTON_COL * MAX_LEVEL_BUTTON_ROW, num)) {
                break;
            }
            for (int j = 0; j < MAX_LEVEL_BUTTON_ROW; j++) {
                count++;
                levelButtons.add(new LevelButton(new Image("res/ActorExtensionPack/UI/levelButton.png"), new Vec2D(270 + j * 170,240 + i * 170), count, this));

                if (count == MyMath.min(MAX_LEVEL_BUTTON_COL * MAX_LEVEL_BUTTON_ROW, num)) {
                    break;
                }
            }
        }
    }

    protected void nextPage() {
        if (start + 9 > totalLevel) return;

        start += 9;
    }


    protected void prevPage() {
        if (start - MAX_LEVEL_BUTTON_ROW * MAX_LEVEL_BUTTON_COL < 0) return;

        start -= MAX_LEVEL_BUTTON_ROW * MAX_LEVEL_BUTTON_COL;
    }

    public int getLevelChosen() {
        return levelChosen;
    }
}
