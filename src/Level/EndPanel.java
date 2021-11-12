package Level;

import Music.MusicPlayer;
import Music.MusicPlayerManager;
import Page.GameStage;
import UI.Button;
import UI.Pane;
import UI.TextBox;
import bagel.Image;
import bagel.util.Colour;
import myUtil.Vec2D;

public class EndPanel extends Pane {
    Level castToLevel;

    private final static int TITLE_SIZE = 100;

    TextBox title = new TextBox("...", 1000, new Vec2D(150,380), Colour.WHITE);

    private boolean playSound = false;

    private static final int deadSound = MusicPlayerManager.getInstance().addMusic(new MusicPlayer("res/ActorExtensionPack/Sound/wav/DeadSound.wav"));

    public EndPanel(Level castToLevel) {
        this.castToLevel = castToLevel;
    }

    @Override
    public void display() {
        if (!playSound) {
            //check for end status
            MusicPlayerManager.getInstance().stopAll();
            if (castToLevel.state == Level.WIN) {
                MusicPlayerManager.getInstance().playMixed(GameStage.wining, MusicPlayer.NONE);
            } else if (castToLevel.state == Level.LOSE) {
                MusicPlayerManager.getInstance().playMixed(deadSound, MusicPlayer.NONE);
                MusicPlayerManager.getInstance().playMixed(GameStage.lose, MusicPlayer.NONE);

            }
            playSound = true;
        }
        if (castToLevel.state == Level.WIN) {
            title.setStr("         WIN");
        } else {
            title.setStr("        LOSE");
        }

        title.setFontSize(TITLE_SIZE);

        title.display();
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }
}
