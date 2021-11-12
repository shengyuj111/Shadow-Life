package UI;

import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import myUtil.Vec2D;

public class TextBox {
    private int fontSize = 20;

    private String str;
    private final float width;
    private final Vec2D position;
    private final Colour colour;

    private static final float HEIGHT_RATIO = 2.0f / 3.0f;

    public TextBox(String str, float width, Vec2D position, Colour colour) {
        this.str = str;
        this.width = width;
        this.position = position;
        this.colour = colour;
    }

    public void display() {
        showLine(str, position);
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    private void showLine(String str, Vec2D position) {
        Font font = FontManager.sizeFont(fontSize);
        if (font.getWidth(str) <= width) {
            DrawOptions drawOptions = new DrawOptions();
            font.drawString(str, position.x, position.y, drawOptions.setBlendColour(colour));
            return;
        }
        for (int i = 1; i < str.length(); i++) {
            if (font.getWidth(str.substring(0, i)) > width) {
                if (i != 1) {
                    DrawOptions drawOptions = new DrawOptions();
                    font.drawString(str.substring(0, i - 1), position.x, position.y, drawOptions.setBlendColour(colour));
                    showLine(str.substring(i - 1), position.out_add(new Vec2D(0, HEIGHT_RATIO * fontSize)));
                    return;
                } else {
                    throw new ArrayIndexOutOfBoundsException("Width is too short");
                }
            }
        }

    }

    public void setStr(String str) {
        this.str = str;
    }
}
