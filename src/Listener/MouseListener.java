package Listener;

import myUtil.Vec2D;
import bagel.Input;
import bagel.MouseButtons;

public class MouseListener {
    private static MouseListener mouseListener = null;
    private static Vec2D mousePosition = null;
    private static boolean leftClick = false;
    private static boolean rightClick = false;

    private MouseListener() {
    }

    public static MouseListener getInstance() {
        if (mouseListener == null) {
            mouseListener = new MouseListener();
        }
        return mouseListener;
    }

    public Vec2D position() {
        return mousePosition;
    }

    public boolean rightClick() {
        return rightClick;
    }

    public boolean leftClick() {
        return leftClick;
    }

    public void listen(Input input) {
        //get mouse input and position
        if (mousePosition == null) {
            mousePosition = new Vec2D(input.getMouseX(), input.getMouseY());
        }
        mousePosition.set(input.getMouseX(), input.getMouseY());
        leftClick = input.wasPressed(MouseButtons.LEFT);

        rightClick = input.wasPressed(MouseButtons.RIGHT);
    }


}
