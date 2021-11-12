package UI;

import bagel.Font;

public class FontManager {
    private static final String FONT_PATH = "res/ActorExtensionPack/Font/8-bit-pusab.ttf";

    public static Font sizeFont(int size) {
        return new Font(FONT_PATH, size);
    }
}
