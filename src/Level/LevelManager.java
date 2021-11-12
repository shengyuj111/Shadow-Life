package Level;

import java.util.ArrayList;

public class LevelManager {
    private static final int INVALID = -1;

    private static LevelManager levelManager = null;

    public int currentLevel = INVALID;

    private ArrayList<Level> levels = new ArrayList<Level>();

    public static final boolean[] PLAYING_INVENTORY = {false, false, false, false, false, false, false, false, true, true, true, true, false};
    public static final boolean[] MAKING_INVENTORY = {true, true, true, true, true, true, true, true, true, true, true, true, true};

    private LevelManager() {
        //load map and help guide script
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Easy.csv", "res/ActorExtensionPack/Level/Easy.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Skeleton.csv", "res/ActorExtensionPack/Level/Skeleton.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/MoreSkeleton.csv", "res/ActorExtensionPack/Level/MoreSkeleton.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Spider.csv", "res/ActorExtensionPack/Level/Spider.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Seapider.csv", "res/ActorExtensionPack/Level/Seapider.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/MagicTower.csv", "res/ActorExtensionPack/Level/MagicTower.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Portal.csv", "res/ActorExtensionPack/Level/Portal.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/WhereAmI.csv", "res/ActorExtensionPack/Level/WhereAmI.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Trap!!!.csv", "res/ActorExtensionPack/Level/Trap!!!.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/TTTTraps!!!!!!!!!!!!!.csv", "res/ActorExtensionPack/Level/TTTTraps!!!!!!!!!!!!!.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Skull.csv", "res/ActorExtensionPack/Level/Skull.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Bones.csv", "res/ActorExtensionPack/Level/Bones.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/Witch.csv", "res/ActorExtensionPack/Level/Witch.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/MeteorShower.csv", "res/ActorExtensionPack/Level/MeteorShower.txt", PLAYING_INVENTORY));
        levels.add(new Level(Level.DUNGEON, "res/ActorExtensionPack/Level/MonsterParty.csv", "res/ActorExtensionPack/Level/MonsterParty.txt", PLAYING_INVENTORY));
    }

    public static LevelManager getInstance() {
        if (levelManager == null) {
            levelManager = new LevelManager();
        }
        return levelManager;
    }

    public Level getLevel(int num) {
        currentLevel = num;
        return levels.get(num-1).clone();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean hasNextLevel() {
        return (currentLevel + 1) <= levels.size();
    }
}
