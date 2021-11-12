package myUtil;

public class MyMath {
    /**
     * Commonly used value
     */
    public static final float HALF = 0.5f;
    public static final float DOUBLE = 2.0f;

    /**
     * Commonly used radian value
     */
    public static final float PI = 3.14159f;
    public static final float RAD0 = 0.0f;
    public static final float RAD30 = 0.52360f;
    public static final float RAD45 = 0.78540f;
    public static final float RAD60 = 1.04720f;
    public static final float RAD90 = HALF * PI;
    public static final float RAD120 = 4.0f * RAD30;
    public static final float RAD135 = 3.0f * RAD45;
    public static final float RAD150 = 5.0f * RAD30;
    public static final float RAD180 = PI;
    public static final float RAD270 = 3.0f * RAD90;
    public static final float RAD360 = 2.0f * PI;

    /**
     * return the square of the float
     * @param a a float
     * @return The square of value a
     */
    public static float sqr(float a) {
        return a * a;
    }

    /**
     * return the square root of the float
     * @param a a float
     * @return The square root of value a
     */
    public static float sqrt(float a) {
        return (float)StrictMath.sqrt(a);
    }

    /**
     * return the sin of the float
     * @param a a float
     * @return The sin of value a
     */
    public static float sin(float a) {
        return (float)StrictMath.sin(a);
    }

    /**
     * return the cos of the float
     * @param a a float
     * @return The cos of value a
     */
    public static float cos(float a) {
        return (float)StrictMath.cos(a);
    }

    /**
     * return the tan of the float
     * @param a a float
     * @return The tan of value a
     */
    public static float tan(float a) {
        return (float)StrictMath.tan(a);
    }

    /**
     * return the arcsin of the float
     * @param a a float
     * @return The arcsin of value a
     */
    public static float asin(float a) {
        return (float)StrictMath.asin(a);
    }

    /**
     * return the arccos of the float
     * @param a a float
     * @return The arccos of value a
     */
    public static float acos(float a) {
        return (float)StrictMath.acos(a);
    }

    /**
     * return the arctan of the float
     * @param a a float
     * @return The arctan of value a
     */
    public static float atan(float a) {
        return (float)StrictMath.atan(a);
    }

    /**
     * return the sinh of the float
     * @param a a float
     * @return The sinh of value a
     */
    public static float sinh(float a) {
        return (float)StrictMath.sinh(a);
    }

    /**
     * return the cosh of the float
     * @param a a float
     * @return The cosh of value a
     */
    public static float cosh(float a) {
        return (float)StrictMath.cosh(a);
    }

    /**
     * return the tanh of the float
     * @param a a float
     * @return The tanh of value a
     */
    public static float tanh(float a) {
        return (float)StrictMath.tanh(a);
    }

    /**
     * return the absolute value of the float
     * @param a a float
     * @return The absolute value of value a
     */
    public static float abs(float a) {
        return StrictMath.abs(a);
    }

    /**
     * return the max value of two float number
     * @param a a float
     * @param b a float
     * @return The max value
     */
    public static float max(float a, float b) {
        return StrictMath.max(a, b);
    }

    /**
     * return the min value of two float number
     * @param a a float
     * @param b a float
     * @return The min value
     */
    public static float min(float a, float b) {
        return StrictMath.min(a, b);
    }

    /**
     * return the max value among multiple float number
     * @param a multiple float
     * @return The max value among a
     */
    public static float maxs(float ... a) {
        float max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
            }
        }
        return max;
    }

    /**
     * return the min value among multiple float number
     * @param a multiple float
     * @return The min value among a
     */
    public static float mins(float ... a) {
        float min = a[0];
        for (int i = 1; i < a.length; i++) {
            if (min > a[i]) {
                min = a[i];
            }
        }
        return min;
    }

    /**
     * return a random number between a and b
     * @param a a float
     * @param b a float
     * @return a random number between a and b
     */
    public static float random(float a, float b) {
        return (float)StrictMath.random() * (max(a, b) - min(a, b)) + min(a, b);
    }

    /**
     * return rotated radian value
     * @param start the start radians
     * @param delta the change in delta
     * @param counterClockwise the direction of rotating
     * @return rotated radians value
     */
    public static float rotateRadians(float start, float delta, boolean counterClockwise) {
        if (counterClockwise) {
            start += delta;
        } else {
            start -= delta;
        }
        return start;
    }

    /**
     * check is a an even number
     * @param a an integer
     * @return the check result
     */
    public static boolean isEven(int a) {
        return a % 2 == 0;
    }

    /**
     * check is a an odd number
     * @param a an integer
     * @return the check result
     */
    public static boolean isOdd(int a) {
        return a % 2 == 1;
    }

    /**
     * return the clamp of a var between boundary 1 and boundary 2
     * @param b1 boundary 1
     * @param b2 boundary 2
     * @param var a float
     * @return the clamp value
     */
    public static float clamp(float b1, float b2, float var) {
        float max = max(b1, b2);
        float min = min(b1, b2);
        if (var > max) return max;
        return Math.max(var, min);
    }

    /**
     * check whether var between boundary 1 and boundary 2
     * @param b1 boundary 1
     * @param b2 boundary 2
     * @param var a float
     * @return the check result
     */
    public static boolean between(float b1, float b2, float var) {
        float max = max(b1, b2);
        float min = min(b1, b2);
        return var >= min && var <= max;
    }

    /**
     * get difference
     * @param var1 v 1
     * @param var2 v 2
     * @return the difference
     */
    public static float diff(float var1, float var2) {
        return abs(var1 - var2);
    }
}
