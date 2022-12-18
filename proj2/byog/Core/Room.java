package byog.Core;
import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;

import java.lang.Math;

/**
 * Room
 * Represents a square room in the game.
 * The room is completely determined by its position (x, y), size (width, height), tile type for
 * the floor and the walls, and by the world in which it is placed.
 * The walls representation is not included in this class.
 */
public class Room implements Comparable<Room> {
    /* Position of the room in the world, lower left corner. */
    private final int x;
    private final int y;
    /* Size of the room, in tiles. */
    private final int width;
    private final int height;
    /* Tile types for the floor and the walls. */
    private final TETile floor;
    private final TETile wall;
    /* World in which the room is to be drawn. */
    private final TETile[][] world;

    public Room(Point p, int width, int height, TETile floor, TETile wall, TETile[][] world){
        this.x = p.x();
        this.y = p.y();
        this.width = width;
        this.height = height;
        this.floor = floor;
        this.wall = wall;
        this.world = world;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public TETile floor() {
        return floor;
    }

    public TETile wall() {
        return wall;
    }

    public TETile[][] world() {
        return world;
    }

    private double dist(int a, int b){
        return Math.sqrt((a - 1) * (a - 1) + (b - 1) * (b - 1));
    }
    @Override
    public int compareTo(Room another){
        double dist1 = dist(x, y);
        double dist2 = dist(another.x(), another.y());
        return Double.compare(dist1, dist2);
        //return (x + y) - (another.x() + another.y());
    }



    /**
     * Return the Room representation as a  List of Points, representing the set of coordinates
     * occupied by the room.
     * @return the set of points that the room occupies.
     */
    public ArrayList<Point> getPoints(){

        if (width == 0 | height == 0){
            return new ArrayList<>();
        }

        ArrayList<Point> points = new ArrayList<>();
        for (int i = x; i < x + width; i++){
            for (int j = y; j < y + height; j++){
                points.add(new Point(i, j));
            }
        }
        return points;
    }

    /**
     * Draw the room on the world which it references.
     */
    public void draw(){

        if (width == 0 | height == 0){
            return;
        }

        for (int i = x; i < x + width; i++){
            for (int j = y; j < y + height; j++){
                world[i][j] = floor;
            }
        }
    }

    /**
     * Return a 1D room given two aligned point.
     */
    public static Room RoomOneD (Point first, Point second, TETile floor, TETile wall, TETile[][] world){
        Point p;
        int width;
        int height;
        if (first.x() == second.x()){
            if (first.y() < second.y()){
                p = new Point(first.x(), first.y());
                height = second.y() - first.y() + 1;
            }
            else{
                p = new Point(second.x(), second.y());
                height = first.y() - second.y() + 1;
            }
            return new Room(p, 1, height,floor, wall, world);
        }
        else{
            if (first.x() < second.x()){
                p = new Point(first.x(), first.y());
                width = second.x() - first.x() + 1;
            }
            else{
                p = new Point(second.x(), second.y());
                width = first.x() - second.x() + 1;
            }
            return new Room(p, width, 1,floor, wall, world);
        }
    }


    /**
     * Takes two Room objects as parameters, and returns true if the body of both objects overlaps
     * on the x direction.
     * @param first is one of the rooms to check for overlapping along the x direction.
     * @param second is one of the rooms to check for overlapping along the x direction.
     * @return true if both rooms have point with common coordinates along x.
     */
    public static boolean overlapOnX(Room first, Room second){
        if(first.x() <= second.x()){
            return first.x() + first.width() > second.x();
        }
        else{
            return second.x() + second.width() > first.x();
        }
    }

    /**
     * Takes two Room objects as parameters, and returns true if the body of both objects overlaps
     * on the y direction.
     * @param first is one of the rooms to check for overlapping along the y direction.
     * @param second is one of the rooms to check for overlapping along the y direction.
     * @return true if both rooms have point with common coordinates along y.
     */
    static boolean overlapOnY(Room first, Room second) {
        if(first.y() <= second.y()){
            return first.y() + first.height() > second.y();
        }
        else{
            return second.y() + second.height() > first.y();
        }
    }

    public static void main(String[] args) {
        int WIDTH = 60;
        int HEIGHT = 60;
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        Point point = new Point(20,20);
        Room room = new Room(point, 10, 1, Tileset.FLOOR, Tileset.WALL, world);
        room.draw();

        // draws the world to the screen
        ter.renderFrame(world);
    }


}
