package UI;

import Listener.MouseListener;
import Music.MusicPlayer;
import Music.MusicPlayerManager;
import bagel.Drawing;
import bagel.util.Colour;
import bagel.util.Point;
import myUtil.Vec2D;
import bagel.Image;

public abstract class Button {

    private final AABB boundingBox;
    private final Image button;
    private static final int buttonPressedSound = MusicPlayerManager.getInstance().addMusic(new MusicPlayer("res/ActorExtensionPack/Sound/wav/ButtonPressed.wav"));

    public Button(Image button, Vec2D position) {
        this.button = button;
        Vec2D adding = new Vec2D((float)button.getWidth(), (float)button.getHeight());
        this.boundingBox = new AABB(position, position.out_add(adding));
    }

    public void onClick() {
        if (boundingBox.contains(MouseListener.getInstance().position()))
        {
            //draw highlight
            Point a = boundingBox.min.toPoint();
            Point b = new Point(boundingBox.max.x, boundingBox.min.y);
            Point c = boundingBox.max.toPoint();
            Point d = new Point(boundingBox.min.x, boundingBox.max.y);
            Drawing.drawLine(a,b,2,Colour.WHITE);
            Drawing.drawLine(c,b,2,Colour.WHITE);
            Drawing.drawLine(c,d,2,Colour.WHITE);
            Drawing.drawLine(a,d,2,Colour.WHITE);

            //check for mouse event
            if (MouseListener.getInstance().leftClick())
            {
                MusicPlayerManager.getInstance().playMixed(buttonPressedSound, MusicPlayer.NONE);
                event();
            }
        }
    }

    public abstract void event();

    public void display() {
        button.drawFromTopLeft(boundingBox.min.x, boundingBox.min.y);
    }
}