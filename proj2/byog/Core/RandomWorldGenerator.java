package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


import static byog.Core.Room.overlapOnX;
import static byog.Core.Room.overlapOnY;

import java.lang.Math;

public class RandomWorldGenerator {

    /* The width of the walls in number of tiles. */
    private static final int WALL_SIZE = 1;

    /* World on which the room is to be generated. */
    private TETile[][] world;
    /* Tile types for the floor and the walls. */
    private TETile floor;
    private TETile wall;
    /* Random number generator from java.util */
    private Random random;

    public RandomWorldGenerator(TETile[][] world, TETile floor, TETile wall, Random random) {
        this.world = world;
        this.floor = floor;
        this.wall = wall;
        this.random = random;
    }

    /**
     * Generates random rooms in the world.
     * No collision detection between rooms, which means that room floors can be overlapping.
     * @param sideMin is the minimum dimension for the width or height of a room.
     * @param sideMax is the maximum dimension for the width or height of a room.
     * @param deltaWH is the maximum allowable difference between the width and height of any room.
     * @param n the number of rooms to draw.
     * @return a List of rooms that might overlap.
     */
    public ArrayList<Room> generateRooms(int sideMin, int sideMax, int deltaWH, int n) {
        if (sideMax + deltaWH + 2 * WALL_SIZE >= world.length
                || sideMax + deltaWH + 2 * WALL_SIZE >= world[0].length) {
            throw new RuntimeException("Room size is too big for world size.");
        }

        int[] widths = randomUniformArray(sideMin, sideMax + 1, n);
        int[] heights = randomUniformArray(sideMin - deltaWH, sideMax + deltaWH + 1, n);
        int xMin = WALL_SIZE;
        int yMin = WALL_SIZE;
        int xMax = world.length - (sideMax + deltaWH + WALL_SIZE);
        int yMax = world[0].length - (sideMax + deltaWH + WALL_SIZE);
        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            int x = RandomUtils.uniform(random, xMin, xMax);
            int y = RandomUtils.uniform(random, yMin, yMax);
            rooms.add(new Room(new Point(x, y), widths[i], heights[i], floor, wall, world));
        }
        return rooms;
    }

    /**
     * Generates random rooms, that don't overlap, in the world.
     * Collision detection between rooms, which means that room floors won't overlap.
     * Every room will be tried to be added a fixed number of times, if fails, won't be added.
     * As some rooms may not be added, the total number of rooms might be lower than n.
     * @param sideMin is the minimum dimension for the width or height of a room.
     * @param sideMax is the maximum dimension for the width or height of a room.
     * @param deltaWH is the maximum allowable difference between the width and height of any room.
     * @param n the maximum number of rooms to draw (actual number may be lower due to collisions).
     * @param numTries is the number of times that each room will be tried to be added.
     * @return a List of rooms that don't overlap.
     */
    public ArrayList<Room> generateRoomsNoOverlap(
            int sideMin, int sideMax, int deltaWH, int n, int numTries) {

        if (sideMax + deltaWH + 2 * WALL_SIZE >= world.length
                || sideMax + deltaWH + 2 * WALL_SIZE >= world[0].length) {
            throw new RuntimeException("Room size is too big for world size.");
        }

        int[] widths = randomUniformArray(sideMin, sideMax + 1, n);
        int[] heights = randomUniformArray(sideMin - deltaWH, sideMax + deltaWH + 1, n);
        int xMin = WALL_SIZE;
        int yMin = WALL_SIZE;
        int xMax = world.length - (sideMax + deltaWH + WALL_SIZE);
        int yMax = world[0].length - (sideMax + deltaWH + WALL_SIZE);
        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = RandomUtils.uniform(random, xMin, xMax);
            int y = RandomUtils.uniform(random, yMin, yMax);
            for (int j = 0; j < numTries; j++) {
                Room room = new Room(new Point(x, y), widths[i], heights[i], floor, wall, world);
                List<Room> overlapping = rooms.stream().filter(
                        (r) -> overlapOnX(r, room) && overlapOnY(r, room)).toList();
                if (overlapping.isEmpty()) {
                    rooms.add(room);
                    break;
                }
            }
        }
        return rooms;
    }

    /**
     * Generates random straight and bent hallways to connect the rooms passed as parameter.
     * First shuffle rooms passed as parameter, to add randomness in connections.
     * Then connects rooms one by one. If n rooms, makes n - 1 connections.
     * @param rooms the Room instances to connect among.
     * @return an array of hallways connecting the rooms passed as parameter.
     */
    public ArrayList<Hallway> generateHallways(ArrayList<Room> rooms) {
        ArrayList<Hallway> hallways = new ArrayList<>();
        ArrayList<Room> connected = new ArrayList<>();
        Collections.sort(rooms);
        connected.add(rooms.get(0));
        for (int i = 1; i < rooms.size(); i++) {

            Room randomRoom = rooms.get(i);
            if (overlapOnX(randomRoom, connected.get(i - 1))) {
                hallways.add(connectAlongY(randomRoom, connected.get(i - 1)));
            } else if (overlapOnY(randomRoom, connected.get(i - 1))) {
                hallways.add(connectAlongX(randomRoom, connected.get(i - 1)));
            } else {
                hallways.add(connect(randomRoom, connected.get(i - 1)));
            }
            connected.add(i, randomRoom);
        }
        return hallways;
    }

    private Hallway connectAlongY(Room first, Room second){
        int leftbound = Math.max(first.x(), second.x());
        int rightbound = Math.min(first.x() + first.width(), second.x() + second.width());
        int randomX = RandomUtils.uniform(random, leftbound, rightbound);
        Point start = new Point(randomX, first.y());
        Point end = new Point(randomX, second.y());
        return new Hallway(start, end, true, floor, wall, world);
    }

    private Hallway connectAlongX(Room first, Room second){
        int downbound = Math.max(first.y(), second.y());
        int upbound = Math.min(first.y() + first.height(), second.y() + second.height());
        int randomY = RandomUtils.uniform(random, downbound, upbound);
        Point start = new Point(first.x(), randomY);
        Point end = new Point(second.x(), randomY);
        return new Hallway(start, end, true, floor, wall, world);
    }

    private Hallway connect(Room first, Room second){
        int firstRandomX = RandomUtils.uniform(random, first.x(), first.x() + first.width());
        int firstRandomY = RandomUtils.uniform(random, first.y(), first.y() + first.height());
        int secondRandomX = RandomUtils.uniform(random, second.x(), second.x() + second.width());
        int secondRandomY = RandomUtils.uniform(random, second.y(), second.y() + second.height());
        Point start = new Point(firstRandomX, firstRandomY);
        Point end = new Point(secondRandomX, secondRandomY);
        return new Hallway(start, end, true, floor, wall, world);
    }

    /**
     * Returns an Array of n random integers uniformly distributed in [min, max). If min < 0, let m = WALL_SIZE
     * @param min the minimum value that any number in the array will take (closed interval).
     * @param max the minimum value that any number in the array will take (open interval).
     * @param n the number of integers in the resulting array.
     * @return an Array of int, with random values uniformly distributed in [min, max).
     */
    private int[] randomUniformArray(int min, int max, int n) {
        if (min < 0){
            min = WALL_SIZE;
        }
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = RandomUtils.uniform(random, min, max);
        }
        return result;
    }

    public static void main(String[] args) {
        int WIDTH = 150;
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

        int seed = 100;
        //int seed = 99;
        Random random = new Random(seed);
        RandomWorldGenerator rwg = new RandomWorldGenerator(world, Tileset.FLOOR, Tileset.WALL, random);
        ArrayList<Room> roomList = rwg.generateRoomsNoOverlap(5, 10, 0, 10, 10);
        for (Room r: roomList){
            r.draw();
        }

        //ArrayList<Hallway> hallwayList = new ArrayList<>();
        ArrayList<Hallway> hallwayList = rwg.generateHallways(roomList);
        for (Hallway h: hallwayList){
            h.draw();
        }
        Walls walls = new Walls(roomList, hallwayList, Tileset.WALL, world);
        walls.draw();
        // draws the world to the screen
        ter.renderFrame(world);

    }
}
