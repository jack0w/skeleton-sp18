package byog.Core;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.ArrayList;
import java.util.HashSet;

public class Walls {
    private final HashSet<Point> points;
    /* gPoints (geometry points) List represents where points for rooms and walls are placed. */
    private final HashSet<Point> gPoints;
    private final ArrayList<Room> rooms;
    private final ArrayList<Hallway> hallways;
    private final TETile[][] world;
    /* TETile that represent the floor for rooms and hallways in the world. */
    private final TETile wall;

    public Walls(ArrayList<Room> rooms, ArrayList<Hallway> hallways, TETile wall, TETile[][] world){
        this.rooms = rooms;
        this.hallways = hallways;
        this.wall = wall;
        this.world = world;
        this.points = new HashSet<>();
        this.gPoints = new HashSet<>();
        populateGeometryPoints();
        generateWalls();
    }

    public Walls(HashSet<Point> geometryPoints, TETile wallP, TETile[][] worldP) {
        this.rooms = null;
        this.hallways = null;
        this.wall = wallP;
        this.world = worldP;
        this.points = new HashSet<>();
        this.gPoints = geometryPoints;
        generateWallsWithPoints();
    }

    /**
     * Populates List<Point> gPoints, which contains all the points corresponding to rooms
     * and walls that are in the world.
     */
    private void populateGeometryPoints() {
        for (Room room : rooms){
            ArrayList<Point> roomPoints = room.getPoints();
            gPoints.addAll(roomPoints);
        }
        for (Hallway hallway : hallways){
            ArrayList<Point> hallwayPoints = hallway.getPoints();
            gPoints.addAll(hallwayPoints);
        }
    }

    /**
     * Check whether a point is in the current room or hallway.
     * @param p
     * @return whether a point is in the current room or hallway.
     */
    private boolean isOccupied(Point p){
        return gPoints.contains(p);
    }

    /**
     * @param room
     * @return an array of wall points for a single room.
     */
    private ArrayList<Point> wallRoom(Room room){
        ArrayList<Point> wall = new ArrayList<>();
        Point downStart = new Point(room.x() - 1, room.y() - 1);

        for (int i = downStart.x(); i <= room.x() + room.width(); i++){
            Point pDown= new Point(i, downStart.y());
            Point pUp = new Point(i, room.y() + room.height());
            if (!isOccupied(pDown)){
                wall.add(pDown);
            }
            if (!isOccupied(pUp)){
                wall.add(pUp);
            }
        }

        for (int j = downStart.y(); j <= room.y() + room.height(); j++){
            Point pLeft = new Point(downStart.x(), j);
            Point pRight = new Point(room.x() + room.width(), j);
            if (!isOccupied(pLeft)){
                wall.add(pLeft);
            }
            if (!isOccupied(pRight)){
                wall.add(pRight);
            }
        }

        return wall;
    }

    /**
     * One hallway can be viewed as two connected room
     * @param hallway
     * @return an array of wall points for a single hallway.
     */
    private ArrayList<Point> wallHallway(Hallway hallway){
        Point start = new Point(hallway.getStartX(), hallway.getStartY());
        Point turn = new Point(hallway.getTurnX(), hallway.getTurnY());
        Point end = new Point(hallway.getEndX(), hallway.getEndY());
        Room roomFirst = Room.RoomOneD(start, turn, hallway.getFloor(), hallway.getWall(), hallway.getWorld());
        Room roomSecond = Room.RoomOneD(turn, end, hallway.getFloor(), hallway.getWall(), hallway.getWorld());
        ArrayList<Point> wall = new ArrayList<>(wallRoom(roomFirst));
        wall.addAll(wallRoom(roomSecond));
        return wall;
    }


    /**
     * Generate walls for all room and hallway.
     */
    private void generateWalls() {
        for (Room room : rooms){
            points.addAll(wallRoom(room));
        }
        for (Hallway hallway : hallways){
            points.addAll(wallHallway(hallway));
        }
    }

    /**
     * Generates all the walls for the world.
     * First, sweep the world left-right and bottom-up, to add right and upper walls,
     * and top-right corners.
     * Second, sweep the world right-left and top-down, to add left and lower walls,
     * and bottom-left corners.
     * Third, sweep the world left-right and bottom-up, to add top-left and bottom-right corners.
     */
    private void generateWallsWithPoints() {
        /* 1-sweep left-right and bottom-up, draw right and upper walls, and top-right corners */
        for (int x = 1; x < world.length; x++) {
            for (int y = 1; y < world[x].length; y++) {
                Point current = new Point(x, y);
                Point below = new Point(x, y - 1);
                Point left = new Point(x - 1, y);
                Point belowLeft = new Point(x - 1, y - 1);
                if (gPoints.contains(current)) {
                    continue;
                }
                if (gPoints.contains(left)) {
                    addWall(x, y);
                }
                if (gPoints.contains(below)) {
                    addWall(x, y);
                }
                if (points.contains(left) && points.contains(below)
                        && gPoints.contains(belowLeft)) {
                    addWall(x, y);
                }
            }
        }
        /* 2-sweep right-left and top-down, draw left and lower walls, and bottom-left corners */
        for (int x = world.length - 2; x >= 0; x--) {
            for (int y = world[x].length - 2; y >= 0; y--) {
                Point current = new Point(x, y);
                Point above = new Point(x, y + 1);
                Point right = new Point(x + 1, y);
                Point aboveRight = new Point(x + 1, y + 1);
                if (gPoints.contains(current) || points.contains(current)) {
                    continue;
                }
                if (gPoints.contains(right)) {
                    addWall(x, y);
                }
                if (gPoints.contains(above)) {
                    addWall(x, y);
                }
                if (points.contains(right) && points.contains(above)
                        && gPoints.contains(aboveRight)) {
                    addWall(x, y);
                }
            }
        }
        /* 3-sweep left-right and bottom-up, draw top-left and bottom-right corners */
        for (int x = 1; x < world.length; x++) {
            for (int y = 1; y < world[x].length; y++) {
                Point current = new Point(x, y);
                Point below = new Point(x, y - 1);
                Point left = new Point(x - 1, y);
                Point belowLeft = new Point(x - 1, y - 1);
                if (!points.contains(current)) {
                    continue;
                }
                if (gPoints.contains(below) && !gPoints.contains(left)
                        && points.contains(belowLeft)) {
                    addWall(x - 1, y);
                }
                if (!gPoints.contains(below) && gPoints.contains(left)
                        && points.contains(belowLeft)) {
                    addWall(x, y - 1);
                }
            }
        }
    }

    /**
     * Adds a wall, on the world, at the coordinates passed as parameter.
     * @param x is the x coordinate on which to add a single-tile wall.
     * @param y is the y coordinate on which to add a single-tile wall.
     */
    private void addWall(int x, int y) {
        Point p = new Point(x, y);
        points.add(p);
    }

    /**
     * Return the Walls representation as a  List of Points, representing the set of coordinates
     * occupied by the all the walls in the world.
     * @return the set of points that the walls occupy.
     */
    public HashSet<Point> getPoints() {
        return points;
    }

    /**
     * Draws the Walls on the world which they reference.
     */
    public void draw() {
        for (Point p : points) {
            world[p.x()][p.y()] = wall;
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

        Room room2 = new Room(new Point(1,1), 1, 10, Tileset.FLOOR, Tileset.WALL, world);
        room2.draw();

        Point start = new Point(30, 30);
        Point end = new Point(10, 50);
        Hallway hallway = new Hallway(start, end, false, Tileset.FLOOR, Tileset.WALL, world);
        hallway.draw();

        ArrayList<Room> roomList = new ArrayList<>();
        roomList.add(room);
        roomList.add(room2);
        ArrayList<Hallway> hallwayList = new ArrayList<>();
        hallwayList.add(hallway);


        /*
        Point point = new Point(30,30);
        Room room = new Room(point, 20, 1, Tileset.FLOOR, Tileset.WALL, world);
        room.draw();
        ArrayList<Room> roomList = new ArrayList<>();
        roomList.add(room);
        */

        /*
        Point start = new Point(30, 30);
        Point end = new Point(50, 30);
        Hallway hallway = new Hallway(start, end, false, Tileset.FLOOR, Tileset.WALL, world);
        hallway.draw();

        ArrayList<Room> roomList = new ArrayList<>();
        ArrayList<Hallway> hallwayList = new ArrayList<>();
        hallwayList.add(hallway);
        */

        Walls walls = new Walls(roomList, hallwayList, Tileset.WALL, world);
        walls.draw();
        // draws the world to the screen
        ter.renderFrame(world);
    }


}
