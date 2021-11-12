package Music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicPlayer {
    private final String musicPath;
    private Clip player;
    private boolean isPlaying = false;

    public static final int NONE = 0;
    public static final int LOOP_OPTION = 1;

    public MusicPlayer(String path) {
        musicPath = path;
    }

    public void play(int option) {
        if (isPlaying) {
            if (player.isRunning()) {
                player.stop();
            }
        }
        File musicFile = new File(musicPath);
        try {
            //play music
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
            player = AudioSystem.getClip();
            if (!player.isOpen()) {
                player.open(audioInput);
            }
            player.start();
            isPlaying = true;
            if (option == LOOP_OPTION) {
                player.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }catch (Exception e){
            System.err.println(e.toString());
        }
    }

    public void stop() {
        if (isPlaying) {
            if (player.isRunning()) {
                player.stop();
            }
            if (player.isOpen()) {
                player.close();
            }
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
