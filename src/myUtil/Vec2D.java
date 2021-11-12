package myUtil;

import bagel.util.Point;
import bagel.util.Vector2;


/**
 * This class is created to replace the job for Point and Vector, where it increase the running speed by using float.
 */

public class Vec2D {
    public float x;
    public float y;

    /**
     * default constructor for vec2D, initialize vec2D to 0 vector
     */
    public Vec2D() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    /**
     * constructor for vec2D, initialize vec2d with two values
     * @param var1 a float
     * @param var2 a float
     */
    public Vec2D(float var1, float var2) {
        this.x = var1;
        this.y = var2;
    }

    /**
     * constructor for vec2D, initialize vec2d with two values
     * @param var1 a double
     * @param var2 a double
     */
    public Vec2D(double var1, double var2) {
        this.x = (float) var1;
        this.y = (float) var2;
    }

    /**
     * set vector with var1 and var2
     * @param var1 a float
     * @param var2 a float
     */
    public void set(float var1,float var2) {
        this.x = var1;
        this.y = var2;
    }

    /**
     * set vector with var1 and var2
     * @param var1 a double
     * @param var2 a double
     */
    public void set(double var1,double var2) {
        this.x = (float)var1;
        this.y = (float)var2;
    }

    /**
     * copy vec value to itself
     * @param vec a float
     */
    public void set(Vec2D vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    /**
     * copy itself
     * @return the clone
     */
    public Vec2D clone() {
        Vec2D out = new Vec2D();
        out.set(this);
        return out;
    }

    /**
     * from vector2D to point
     * @return point
     */
    public Point toPoint() {
        return new Point(this.x,this.y);
    }

    /**
     * from vector2D to Vector2
     * @return Vector2
     */
    public Vector2 toVector2() {
        return new Vector2(this.x,this.y);
    }

    /**
     * from point to vector2D
     * @param point
     * @return vector2D
     */
    public static Vec2D toVec2D(Point point) {
        return new Vec2D((float)point.x, (float)point.y);
    }

    /**
     * from vector2 to vector2D
     * @param vec
     * @return vector2D
     */
    public static Vec2D toVec2D(Vector2 vec) {
        return new Vec2D((float)vec.x, (float)vec.y);
    }

    /**
     * return the length of a vector
     * @return the length of a vector
     */
    public float length() {
        return MyMath.sqrt(this.sqrLength());
    }

    /**
     * return the square length of a vector for comparing
     * @return the square length of a vector
     */
    public float sqrLength() {
        return MyMath.sqr(this.x) + MyMath.sqr(this.y);
    }

    /**
     * return the distance between two vec
     * @return the distance between two vec
     */
    public static float distance(Vec2D vec1, Vec2D vec2) {
        return MyMath.sqrt(sqrDistance(vec1, vec2));
    }

    /**
     * return the square distance between two vec for comparing
     * @return the square distance between two vec
     */
    public static float sqrDistance(Vec2D vec1, Vec2D vec2) {
        return MyMath.sqr(vec1.x - vec2.x) + MyMath.sqr(vec1.y - vec2.y);
    }

    /**
     * return the vector of two points
     * @return the vector of two points
     */
    public static Vec2D makeVec(Vec2D vec1, Vec2D vec2) {
        return vec2.out_sub(vec1);
    }

    /**
     * return the dot product
     * @param vec1 a vector
     * @param vec2 a vector
     * @return return the dot product
     */
    public static float dotProduct(Vec2D vec1, Vec2D vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }

    /**
     * toString method with tag
     */
    public String toString(String tag) {
        return tag + ": " + this.toString();
    }

    /**
     * toString method
     */
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    /**
     * print the String with tag
     */
    public void print(String tag) {
        System.out.println(tag + ": (" + this.x + ", " + this.y + ")\n");
    }

    /**
     * print the String
     */
    public void print() {
        System.out.println("(" + this.x + ", " + this.y + ")\n");
    }

    /**
     * return the normal
     * @param vec1 a vector
     * @param vec2 a vector
     * @return return the normal
     */
    public static Vec2D normal(Vec2D vec1, Vec2D vec2) {
        return new Vec2D(-(vec2.y - vec1.y), (vec2.x - vec1.x));
    }

    /**
     * return the radian value of the vector
     */
    public float radian() {
        float radians = (float)StrictMath.atan(this.y / this.x);
        if (this.x >= 0 && this.y >= 0) return radians;
        if (this.x <= 0 && this.y <= 0) return radians + 3.14159f;
        if (this.x >= 0 && this.y <= 0) return 2.0f * 3.14159f + radians;
        if (this.x <= 0 && this.y >= 0) return radians + 3.14159f;
        return 0.0f;
    }

    /**
     * return the final point point by the original point
     * @param radians the angle
     * @param length the moving distance
     * @return the final point point by the original point
     */
    public Vec2D pointer(float radians, float length) {
        return this.out_add(new Vec2D(length * MyMath.cos(radians),  length * MyMath.sin(radians)));
    }

    /**
     * check for equality
     * @param x float
     * @param y float
     * @return result of check
     */
    public boolean equals(float x, float y) {
        return this.x == x && this.y == y;
    }

    /**
     * check for equality
     * @param vec
     * @return result of check
     */
    public boolean equals(Vec2D vec) {
        return this.x == vec.x && this.y == vec.y;
    }

    /**
     * clamp for vector
     * @param b1 boundary 1
     * @param b2 boundary 2
     * @param var
     * @return the clamp result
     */
    public static Vec2D clamp(Vec2D b1, Vec2D b2, Vec2D var) {
        float fMaxX = MyMath.max(b1.x, b2.x);
        float fMinX = MyMath.min(b1.x, b2.x);
        float fMaxY = MyMath.max(b1.y, b2.y);
        float fMinY = MyMath.min(b1.y, b2.y);
        return new Vec2D(MyMath.clamp(fMaxX, fMinX, var.x), MyMath.clamp(fMaxY, fMinY, var.y));
    }

    //basic operation
    //without return a new vec

    /**
     * vector add a variable
     * @param var
     */
    public void add(float var) {
        this.x += var;
        this.y += var;
    }

    /**
     * vector add a vector
     * @param vec
     */
    public void add(Vec2D vec) {
        this.x += vec.x;
        this.y += vec.y;
    }

    /**
     * vector minus a variable
     * @param var
     */
    public void sub(float var) {
        this.x -= var;
        this.y -= var;
    }

    /**
     * vector minus a vector
     * @param vec
     */
    public void sub(Vec2D vec) {
        this.x -= vec.x;
        this.y -= vec.y;
    }

    /**
     * vector divide by a variable
     * @param var
     */
    public void div(float var) {
        this.x /= var;
        this.y /= var;
    }

    /**
     * vector divide by a vector
     * @param vec
     */
    public void div(Vec2D vec) {
        this.x /= vec.x;
        this.y /= vec.y;
    }

    /**
     *vector times a variable
     * @param var
     */
    public void mul(float var) {
        this.x *= var;
        this.y *= var;
    }

    /**
     * vector times a vector
     * @param vec
     */
    public void mul(Vec2D vec) {
        this.x *= vec.x;
        this.y *= vec.y;
    }

    /**
     * vector add vector n times
     * @param n
     * @param vec
     */
    public void addN(float n, Vec2D vec) {
        this.add(vec.out_mul(n));
    }

    /**
     * vector minus vector n times
     * @param n
     * @param vec
     */
    public void subN(float n, Vec2D vec) {
        this.sub(vec.out_mul(n));
    }

    /**
     *vector divide by vector n times
     * @param n
     * @param vec
     */
    public void divN(float n, Vec2D vec) {
        this.div(vec.out_mul(n));
    }

    /**
     *vector multiply vector n times
     * @param n
     * @param vec
     */
    public void mulN(float n, Vec2D vec) {
        this.mul(vec.out_mul(n));
    }

    //return a new vec

    /**
     * return a new vector using add(float var)
     * @param var
     * @return a new vector using add(float var)
     */
    public Vec2D out_add(float var) {
        Vec2D out = this.clone();
        out.add(var);
        return out;
    }

    /**
     * return a new vector using add(Vec2D vec)
     * @param vec
     * @return a new vector using add(Vec2D vec)
     */
    public Vec2D out_add(Vec2D vec) {
        Vec2D out = this.clone();
        out.add(vec);
        return out;
    }

    /**
     * return a new vector using sub(float var)
     * @param var
     * @return a new vector using sub(float var)
     */
    public Vec2D out_sub(float var) {
        Vec2D out = this.clone();
        out.sub(var);
        return out;
    }

    /**
     * return a new vector using sub(Vec2D vec)
     * @param vec
     * @return a new vector using sub(Vec2D vec)
     */
    public Vec2D out_sub(Vec2D vec) {
        Vec2D out = this.clone();
        out.sub(vec);
        return out;
    }

    /**
     * return a new vector using div(float var)
     * @param var
     * @return a new vector using div(float var)
     */
    public Vec2D out_div(float var) {
        Vec2D out = this.clone();
        out.div(var);
        return out;
    }

    /**
     * return a new vector using div(Vec2D vec)
     * @param vec
     * @return a new vector using div(Vec2D vec)
     */
    public Vec2D out_div(Vec2D vec) {
        Vec2D out = this.clone();
        out.div(vec);
        return out;
    }

    /**
     * return a new vector using mul(float var)
     * @param var
     * @return a new vector using mul(float var)
     */
    public Vec2D out_mul(float var) {
        Vec2D out = this.clone();
        out.mul(var);
        return out;
    }

    /**
     * return a new vector using mul(Vec2D vec)
     * @param vec
     * @return a new vector using mul(Vec2D vec)
     */
    public Vec2D out_mul(Vec2D vec) {
        Vec2D out = this.clone();
        out.mul(vec);
        return out;
    }

    /**
     * return a new vector using addN(float n, Vec2D vec)
     * @param n
     * @param vec
     * @return a new vector using addN(float n, Vec2D vec)
     */
    public Vec2D out_addN(float n, Vec2D vec) {
        Vec2D out = this.clone();
        out.addN(n,vec);
        return out;
    }

    /**
     * return a new vector using subN(float n, Vec2D vec)
     * @param n
     * @param vec
     * @return a new vector using subN(float n, Vec2D vec)
     */
    public Vec2D out_subN(float n, Vec2D vec) {
        Vec2D out = this.clone();
        out.subN(n,vec);
        return out;
    }

    /**
     * return a new vector using divN(float n, Vec2D vec)
     * @param n float
     * @param vec vector
     * @return a new vector using divN(float n, Vec2D vec)
     */
    public Vec2D out_divN(float n, Vec2D vec) {
        Vec2D out = this.clone();
        out.divN(n,vec);
        return out;
    }

    /**
     * return a new vector using mulN(float n, float vec)
     * @param n float
     * @param vec vector
     * @return a new vector using mulN(float n, float vec)
     */
    public Vec2D out_mulN(float n, Vec2D vec) {
        Vec2D out = this.clone();
        out.mulN(n,vec);
        return out;
    }

}
