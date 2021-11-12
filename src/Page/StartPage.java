package Page;

import Music.MusicPlayerManager;
import UI.Button;
import UI.Pane;
import UI.TextBox;
import bagel.Image;
import bagel.util.Colour;
import myUtil.Vec2D;

public class StartPage extends Pane {

    Button start = new Button(new Image("res/ActorExtensionPack/UI/redButton.png"), new Vec2D(400,250)) {
        TextBox textBox = new TextBox("START", (float) new Image("res/ActorExtensionPack/UI/redButton.png").getWidth(), new Vec2D(400,250).out_add(new Vec2D(40,50)), Colour.WHITE);
        @Override
        public void event() {
            MusicPlayerManager.getInstance().stopAll();
            GameStage.getInstance().goTo(GameStage.LEVEL_SELECT_PAGE + GameStage.LOADING);
        }

        @Override
        public void display() {
            super.display();
            textBox.setFontSize(50);
            textBox.display();
        }
    };

    Button create = new Button(new Image("res/ActorExtensionPack/UI/bblueButton.png"), new Vec2D(400,350)) {
        TextBox textBox = new TextBox("MAKE", (float) new Image("res/ActorExtensionPack/UI/redButton.png").getWidth(), new Vec2D(400,350).out_add(new Vec2D(40,50)), Colour.WHITE);
        @Override
        public void event() {
            MusicPlayerManager.getInstance().stopAll();
            GameStage.getInstance().goTo(GameStage.MAKE_PAGE + GameStage.LOADING);
        }

        @Override
        public void display() {
            super.display();
            textBox.setFontSize(50);
            textBox.display();
        }
    };

    Button myMap = new Button(new Image("res/ActorExtensionPack/UI/clayButton.png"), new Vec2D(400,450)) {
        TextBox textBox = new TextBox("MY MAP", (float) new Image("res/ActorExtensionPack/UI/redButton.png").getWidth(), new Vec2D(400,450).out_add(new Vec2D(40,50)), Colour.WHITE);
        @Override
        public void event() {
            MusicPlayerManager.getInstance().stopAll();
            GameStage.getInstance().goTo(GameStage.MY_LEVEL_PAGE + GameStage.LOADING);
        }

        @Override
        public void display() {
            super.display();
            textBox.setFontSize(50);
            textBox.display();
        }
    };

    Button exit = new Button(new Image("res/ActorExtensionPack/UI/blueButton.png"), new Vec2D(400,550)) {
        TextBox textBox = new TextBox("EXIT", (float) new Image("res/ActorExtensionPack/UI/redButton.png").getWidth(), new Vec2D(400,550).out_add(new Vec2D(40,50)), Colour.WHITE);
        @Override
        public void event() {
            System.exit(0);
        }

        @Override
        public void display() {
            super.display();
            textBox.setFontSize(50);
            textBox.display();
        }
    };

    private int upPart;
    private int middlePart;
    private int bottomPart;

    private static final Vec2D UP_POSITION = new Vec2D(220, 200);
    private static final Vec2D MIDDLE_POSITION1 = new Vec2D(220, 300);
    private static final Vec2D MIDDLE_POSITION2 = new Vec2D(220, 400);
    private static final Vec2D BOTTOM_POSITION = new Vec2D(220, 500);

    private final static int TITLE_SIZE = 100;

    TextBox title = new TextBox("SHADOW LIFE", 1000, UP_POSITION.clone().out_add(new Vec2D(-30,0)), Colour.WHITE);

    public StartPage() {
        super();
        load();
    }

    public void load() {
        upPart = add(new Image("res/ActorExtensionPack/UI/levelBackUp.png"));
        middlePart = add(new Image("res/ActorExtensionPack/UI/levelBackMiddle.png"));
        bottomPart = add(new Image("res/ActorExtensionPack/UI/levelBackBottom.png"));
    }

    @Override
    public void display() {
        title.setFontSize(TITLE_SIZE);

        getSource().get(upPart).drawFromTopLeft(UP_POSITION.x, UP_POSITION.y);
        getSource().get(middlePart).drawFromTopLeft(MIDDLE_POSITION1.x, MIDDLE_POSITION1.y);
        getSource().get(middlePart).drawFromTopLeft(MIDDLE_POSITION2.x, MIDDLE_POSITION2.y);
        getSource().get(bottomPart).drawFromTopLeft(BOTTOM_POSITION.x, BOTTOM_POSITION.y);

        title.display();

        start.display();
        create.display();
        myMap.display();
        exit.display();


        start.onClick();
        create.onClick();
        myMap.onClick();
        exit.onClick();
    }
}
