package byog.Core;

public class Point{
    private final int x;
    private final int y;

    Point(int xp, int yp){
        this.x = xp;
        this.y = yp;
    }

    /**
     * Return the x position of the point
     */
    public int x(){
        return x;
    }

    /**
     * Return the x position of the point
     */
    public int y(){
        return y;
    }

    /**
     * Equality operator.
     * @param o object on which to perform comparison.
     * @return true if objects are equal (deep equality).
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }
        Point other = (Point) o;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Returns the calculated hash code for an object of type Point.
     * @return the calculated hash code for an object of type Point.
     */
    @Override
    public int hashCode() {
        return x + y;
    }

}
