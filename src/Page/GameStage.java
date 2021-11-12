package Page;

import Music.MusicPlayer;
import Music.MusicPlayerManager;

public class GameStage {
    private static GameStage gameStage = null;

    //stage
    public static final int START_PAGE = 0;
    public static final int LEVEL_SELECT_PAGE = 1;
    public static final int GAME_PAGE = 2;
    public static final int MAKE_PAGE = 3;
    public static final int MY_LEVEL_PAGE = 5;
    public static final int LOADING = 100;

    //music index
    public static int startMusic;
    public static int playMusic;
    public static int makeMusic;
    public static int wining;
    public static int lose;

    private static int currentStage;

    private GameStage() {
        currentStage = 0;
    }

    public static GameStage getInstance() {
        if (gameStage == null) {
            gameStage = new GameStage();
            setMusic();
        }
        return gameStage;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void goTo(int stage) {
        if (stage == START_PAGE) {
            MusicPlayerManager.getInstance().playSingleTrack(startMusic, MusicPlayer.LOOP_OPTION);
        } else if (stage == GAME_PAGE) {
            MusicPlayerManager.getInstance().playSingleTrack(playMusic, MusicPlayer.LOOP_OPTION);
        } else if (stage == MAKE_PAGE) {
            MusicPlayerManager.getInstance().playSingleTrack(makeMusic, MusicPlayer.LOOP_OPTION);
        }
        currentStage = stage;
    }

    private static void setMusic() {
        //initial stage music
        startMusic = MusicPlayerManager.getInstance().addMusic(new MusicPlayer("res/ActorExtensionPack/Sound/wav/startScheme.wav"));
        playMusic = MusicPlayerManager.getInstance().addMusic(new MusicPlayer("res/ActorExtensionPack/Sound/wav/StartMenuBGM.wav"));
        makeMusic = MusicPlayerManager.getInstance().addMusic(new MusicPlayer("res/ActorExtensionPack/Sound/wav/makeScheme.wav"));
        wining = MusicPlayerManager.getInstance().addMusic(new MusicPlayer("res/ActorExtensionPack/Sound/wav/win.wav"));
        lose = MusicPlayerManager.getInstance().addMusic(new MusicPlayer("res/ActorExtensionPack/Sound/wav/lose.wav"));
    }
}
