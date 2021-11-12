package Level;

import UI.*;
import bagel.Image;
import bagel.util.Colour;
import myUtil.Vec2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BeginnerPanel extends Pane {
    private int dialogBox;

    private final String filename;

    private final ArrayList<String> content;

    private int currentIndex;

    //text box
    TextBox textBox = new TextBox("", 600, new Vec2D(220, 120), Colour.WHITE);
    private final Vec2D position = new Vec2D(50, 50);

    public BeginnerPanel(String filename) {
        super();
        this.filename = filename;
        currentIndex = 0;
        content = new ArrayList<>();
        load();
    }

    public void load() {
        //load script frame
        dialogBox = add(new Image("res/ActorExtensionPack/UI/dialogueBox.png"));

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.add(line);
            }

        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
    private static final int FONT_SIZE = 40;
    @Override
    public void display() {
        getSource().get(dialogBox).drawFromTopLeft(position.x, position.y);

        textBox.setStr(content.get(currentIndex));
        textBox.setFontSize(FONT_SIZE);
        textBox.display();
    }

    public boolean response() {
        currentIndex++;
        return currentIndex != content.size();
    }

    public BeginnerPanel clone() {
        return new BeginnerPanel(filename);
    }
}
