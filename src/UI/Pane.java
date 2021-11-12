package UI;

import bagel.Image;

import java.util.ArrayList;

public abstract class Pane {
    private final ArrayList<Image> source;

    public Pane() {
        source = new ArrayList<>();
    }

    public int add(Image image) {
        int index = source.size();
        source.add(image);
        return index;
    }

    public abstract void display();

    public ArrayList<Image> getSource() {
        return source;
    }
}
