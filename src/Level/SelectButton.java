package Level;

import UI.Button;
import UI.FontManager;
import actor.Actor;
import bagel.Image;
import myUtil.Vec2D;

public class SelectButton extends Button {
    //properties
    private SelectPanel castToPanel;
    private Actor actor;
    private int amount;
    private final Vec2D position;

    public SelectButton(Image button, Vec2D position, Actor actor, int amount) {
        super(button, position);
        this.actor = actor;
        this.amount = amount;
        this.position = position.clone();
    }

    @Override
    public void event() {
        if (castToPanel.selected == null && amount != 0) {
            amount--;
            castToPanel.getAmount().put(actor, amount);
            castToPanel.selected = actor;
        }
    }

    @Override
    public void display() {
        super.display();
        FontManager.sizeFont(20).drawString("" + amount, position.x, position.y);
    }

    public void setCastToPanel(SelectPanel castToPanel) {
        this.castToPanel = castToPanel;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
