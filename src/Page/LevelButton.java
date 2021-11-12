package Page;

import UI.Button;
import UI.TextBox;
import bagel.Image;
import bagel.util.Colour;
import myUtil.Vec2D;

public class LevelButton extends Button {
    private int num;
    private final LevelPage castToLevelPage;
    TextBox textBox;

    public LevelButton(Image button, Vec2D position, int num, LevelPage castToLevelPage) {
        super(button, position);
        this.num = num;
        this.castToLevelPage = castToLevelPage;

        textBox = new TextBox("", (float) button.getWidth(), position.out_add(new Vec2D(30,100)), Colour.WHITE);
    }

    @Override
    public void event() {
        castToLevelPage.setLevelChosen(num);
    }

    @Override
    public void display() {
        super.display();
        textBox.setFontSize(100);
        textBox.setStr("" + num);
        textBox.display();
    }

    public void setNum(int num) {
        this.num = num;
    }


}
