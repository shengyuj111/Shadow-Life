package Music;

import myUtil.MyMath;

import java.util.ArrayList;

public class MusicPlayerManager {
    private static MusicPlayerManager musicPlayerManager = null;
    private ArrayList<MusicPlayer> musicPlayers = new ArrayList<>();;

    private static final int NONE = -1;
    private static int currentPlayingSingleTrack = NONE;

    private MusicPlayerManager() {

    }

    public static MusicPlayerManager getInstance() {
        if (musicPlayerManager == null) {
            musicPlayerManager = new MusicPlayerManager();
        }
        return musicPlayerManager;
    }

    public void playMixed(int musicIndex, int option) {
        if (MyMath.between(0, musicPlayers.size() - 1, musicIndex)) {
            musicPlayers.get(musicIndex).play(option);
        }
    }

    public void playSingleTrack(int musicIndex, int option) {
        if (currentPlayingSingleTrack != NONE) {
            musicPlayers.get(currentPlayingSingleTrack).stop();
        }
        if (MyMath.between(0, musicPlayers.size() - 1, musicIndex)) {
            currentPlayingSingleTrack = musicIndex;
            musicPlayers.get(musicIndex).play(option);
        }
    }

    public int addMusic(MusicPlayer music) {
        int currentIndex = musicPlayers.size();
        musicPlayers.add(music);
        return currentIndex;
    }

    public void stopAll() {
        currentPlayingSingleTrack = NONE;
        for (MusicPlayer musicPlayer : musicPlayers) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.stop();
            }
        }
    }
}
