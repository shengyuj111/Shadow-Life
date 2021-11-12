package UI;

import myUtil.MyMath;
import myUtil.Vec2D;

public class AABB {
    Vec2D min;
    Vec2D max;

    public AABB(Vec2D min, Vec2D max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(Vec2D position) {
        return MyMath.between(max.x, min.x, position.x) && MyMath.between(max.y, min.y, position.y);
    }
}
