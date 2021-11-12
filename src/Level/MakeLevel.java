package Level;

import Music.MusicPlayer;
import Music.MusicPlayerManager;
import Page.GameStage;
import actor.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class MakeLevel extends Level {
    public MakeLevel(String worldType, String filename, boolean[] playMode) {
        super(worldType, filename, playMode);
    }

    public void generate() {
        Path file = Paths.get("res/levelGenerateData.txt");
        int newName = 0;

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            if ((line = reader.readLine()) != null) {
                newName = Integer.parseInt(line.replace("\n", ""));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.exit(-1);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("" + (newName + 1), 0, ("" + (newName + 1)).length());
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.exit(-1);
        }

        file = Paths.get("res/ActorExtensionPack/Level/MyLevel/MyLevel" + newName + ".csv");

        byte[] data = makeLevelFileString().getBytes();

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(file, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(-1);
        }
    }

    @Override
    public MakeLevel clone() {
        super.clone();
        return new MakeLevel(getWorldType(), getFilename(), LevelManager.MAKING_INVENTORY);
    }

    public String makeLevelFileString() {
        StringBuilder s = new StringBuilder();
        for (Actor actor : getSelectPanel().planning) {
            //generate actors into csv
            if (!actor.isRemove()) {
                if (actor instanceof Knight) {
                    s.append(KNIGHT).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append("\n");
                } else if (actor instanceof Skeleton) {
                    s.append(SKELETON).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append(",").append(actor.getDirection()).append(",").append(((Skeleton) actor).getWanderLoop()).append("\n");
                } else if (actor instanceof Trap) {
                    s.append(TRAP).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append(",").append(((Trap) actor).getOn()).append("\n");
                } else if (actor instanceof Spider) {
                    s.append(SPIDER).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append(",").append(((Spider) actor).getDirection0()).append(",").append(((Spider) actor).getDirection1()).append(",").append(((Spider) actor).getWanderLoop()).append("\n");
                } else if (actor instanceof Skull) {
                    s.append(SKULL).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append(",").append(actor.getDirection()).append(",").append(((Skull) actor).getWanderLoop()).append("\n");
                } else if (actor instanceof Portal) {
                    s.append(PORTAL).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append(",");
                    for (Actor actor1 : getSelectPanel().planning) {
                        if (actor1 != actor && actor1 instanceof Portal && ((Portal) actor).getPairID() == ((Portal) actor1).getPairID()) {
                            s.append((int) (actor1.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor1.getPosition().y / TILED_LENGTH)).append("\n");
                            actor1.setRemove(true);
                        }
                    }
                } else if (actor instanceof WallMaker) {
                    s.append(WALL_MAKER).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append(",");
                    for (Actor actor1 : getSelectPanel().planning) {
                        if (actor1 != actor && actor1 instanceof WallMaker && ((WallMaker) actor).getRelateID() == ((WallMaker) actor1).getRelateID()) {
                            s.append((int) (actor1.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor1.getPosition().y / TILED_LENGTH)).append("\n");
                            actor1.setRemove(true);
                        }
                    }
                } else if (actor instanceof Witch) {
                    s.append(WITCH).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append(",").append(actor.getDirection()).append(",").append(((Witch) actor).getWanderLoop()).append("\n");
                } else if (actor instanceof Sign) {
                    if (actor.getDirection() == UP) {
                        s.append(SIGN_UP).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append("\n");
                    } else if (actor.getDirection() == RIGHT) {
                        s.append(SIGN_RIGHT).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append("\n");
                    } else if (actor.getDirection() == DOWN) {
                        s.append(SIGN_DOWN).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append("\n");
                    } else if (actor.getDirection() == LEFT) {
                        s.append(SIGN_LEFT).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append("\n");
                    }
                } else if (actor instanceof Chest) {
                    s.append(CHEST).append(",").append((int) (actor.getPosition().x / TILED_LENGTH)).append(",").append((int) (actor.getPosition().y / TILED_LENGTH)).append("\n");
                }
            }
        }
        return s.toString();
    }

    @Override
    public void restart() {
        super.restart();
        MusicPlayerManager.getInstance().playSingleTrack(GameStage.makeMusic, MusicPlayer.NONE);
    }
}
