package Level;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class MyLevelManager {
    private static final int INVALID = -1;

    private static MyLevelManager myLevelManager = null;

    public int currentLevel = INVALID;

    ArrayList<Level> levels = new ArrayList<Level>();

    private MyLevelManager() {
        //read all data
        try (Stream<Path> paths = Files.walk(Paths.get("res/ActorExtensionPack/Level/MyLevel"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> levels.add(new Level(Level.DUNGEON, path.toString(), LevelManager.PLAYING_INVENTORY)));

        } catch (IOException e) {
            System.out.println("Fail to read my level data");
            System.exit(-1);
        }
    }

    public static MyLevelManager getInstance() {
        if (myLevelManager == null) {
            myLevelManager = new MyLevelManager();
        }
        return myLevelManager;
    }

    public Level getLevel(int num) {
        currentLevel = num;
        return levels.get(num - 1).clone();
    }

    public int getSize() {
        return levels.size();
    }

    public static void refresh() {
        myLevelManager = new MyLevelManager();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean hasNextLevel() {
        return (currentLevel + 1) <= levels.size();
    }
}
