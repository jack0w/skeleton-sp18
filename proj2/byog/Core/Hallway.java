package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.ArrayList;

public class Hallway {
    /* Position of start point. */
    private final int startX;
    private final int startY;
    /* Position of end point. */
    private final int endX;
    private final int endY;
    /* Position of turning point. */
    private final int turnX;
    private final int turnY;
    /* Tile types for the floor and the walls. */
    private final TETile floor;
    private final TETile wall;
    /* World in which the room is to be drawn. */
    private final TETile[][] world;

    //TODO: Test the cases that start and end are on the same line. How is the turn set?

    /**
     * Special case: if start and end are on the same line, the turn point will be start or end point
     * @param start
     * @param end
     * @param alignStart
     * @param floor
     * @param wall
     * @param world
     */
    public Hallway(Point start, Point end, Boolean alignStart, TETile floor, TETile wall, TETile[][] world){
        this.startX = start.x();
        this.startY = start.y();
        this.endX = end.x();
        this.endY = end.y();
        this.floor = floor;
        this.wall = wall;
        this.world = world;
        if (alignStart){
            turnX = startX;
            turnY = endY;
        }
        else{
            turnX = endX;
            turnY = startY;
        }
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getTurnX() {
        return turnX;
    }

    public int getTurnY() {
        return turnY;
    }

    public TETile getFloor() {
        return floor;
    }

    public TETile getWall() {
        return wall;
    }

    public TETile[][] getWorld() {
        return world;
    }

    /**
     * Return list of points for a vertical line connecting two points
     */
    public ArrayList<Point> getVertical(int px1, int py1, int py2){
        ArrayList<Point> points = new ArrayList<>();
        int direction = (py2 - py1)>=0 ? 1 : -1;
        int i = py1;
        while(i != py2){
            points.add(new Point(px1, i));
            i = i + direction;
        }
        points.add(new Point(px1, i));
        return points;
    }

    /**
     * Return list of points for a horizontal line connecting two points
     */
    public ArrayList<Point> getHorizontal(int px1, int py1, int px2){
        ArrayList<Point> points = new ArrayList<>();
        int direction = (px2 - px1)>=0 ? 1 : -1;
        int i = px1;
        while(i != px2){
            points.add(new Point(i, py1));
            i = i + direction;
        }
        points.add(new Point(i, py1));
        return points;
    }

    /**
     * Return list of points for a line connecting two points vertically/horizontally aligned
     */
    public ArrayList<Point> getLine(int px1, int py1, int px2, int py2){
        if (px1 == px2){
            return getVertical(px1, py1, py2);
        }
        else{
            return getHorizontal(px1, py1, px2);
        }
    }

    /**
     * Return the Hallway representation as a List of Points, representing the set of coordinates
     * occupied by the hallway.
     * @return the set of points that the hallway occupies.
     */
    public ArrayList<Point> getPoints(){
        ArrayList<Point> points = new ArrayList<>();
        points.addAll(getLine(startX, startY, turnX, turnY));
        points.addAll(getLine(turnX, turnY, endX, endY));
        return points;
    }

    /**
     * Draw the room on the world which it references.
     */
    public void draw(){
        ArrayList<Point> points = getPoints();
        for(Point p: points){
            world[p.x()][p.y()] = floor;
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
        Room room = new Room(point, 20, 20, Tileset.FLOOR, Tileset.WALL, world);
        room.draw();

        Point start = new Point(30, 30);
        Point end = new Point(30, 55);
        Hallway hallway = new Hallway(start, end, false, Tileset.FLOOR, Tileset.WALL, world);
        hallway.draw();
        // draws the world to the screen
        ter.renderFrame(world);
    }

}
