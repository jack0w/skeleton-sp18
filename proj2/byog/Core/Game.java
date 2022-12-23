package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /* Size available for the HUD (head-up display). */
    private static final int HUD_HEIGHT = 2;
    /* Inner width and height of the game window. */
    private static final int WINDOW_WIDTH = WIDTH;
    private static final int WINDOW_HEIGHT = HEIGHT + HUD_HEIGHT;
    /* Tiles to be used for the floor, walls, and player. */
    private static final TETile FLOOR_TILE = Tileset.FLOOR;
    private static final TETile WALL_TILE = Tileset.WALL;
    private static final TETile PLAYER_TILE = Tileset.PLAYER;

    private static final String STATE_FILENAME = "./game.ser";
    /* Maximum and minimum room sides sizes, and difference between width and height for rooms. */
    private static final int MIN_SIDE = 4;
    private static final int MAX_SIDE = 4;
    private static final int DELTA_WIDTH_HEIGHT = 1;
    /* Maximum number of rooms to draw. */
    private static final int MAX_ROOMS = 30;
    /* Maximum number of tries when adding a random room that does not overlaps with others. */
    private static final int MAX_TRIES = 30;

    /* Array of world */
    private final TETile[][] gameWorld;
    /* Coordinates on which the player can move (these points represent rooms and hallways). */
    private HashSet<Point> coordinates = null;
    /* The walls to be drawn around the traversable areas (floor of rooms and hallways). */
    private Walls walls = null;
    /* Player in the world. */
    private Player player;
    /* Game state, used mainly for saving the game state. */
    private ArrayList<Room> roomList = null;
    private ArrayList<Hallway> hallwayList = null;
    private GameState gameState;

    public Game() {
        gameWorld = new TETile[WIDTH][HEIGHT];
        initializeWorldBackground(gameWorld);
        gameState = new GameState(gameWorld, PLAYER_TILE);
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        input = input.toUpperCase();
        char firstCommand = extractFirstCommand(input).charAt(0);
        long seed = extractSeed(input);
        String commands = extractCommands(input);

        Random random = new Random(seed);
        RandomWorldGenerator rwg =
                new RandomWorldGenerator(gameWorld, FLOOR_TILE, WALL_TILE, random);

        if (firstCommand == Keys.NEW_GAME) {
            roomList = rwg.generateRoomsNoOverlap(MIN_SIDE, MAX_SIDE, DELTA_WIDTH_HEIGHT, MAX_ROOMS, MAX_TRIES);
            hallwayList = rwg.generateHallways(roomList);
            coordinates = rwg.getAllowedCoordinates(roomList, hallwayList);
            walls = new Walls(roomList, hallwayList, Tileset.WALL, gameWorld);
            player = new Player(
                    getRandomFromSet(coordinates, random),
                    coordinates, PLAYER_TILE, gameWorld);
        } else if (firstCommand == Keys.LOAD_GAME) {
            gameState = GameState.load(STATE_FILENAME);
            gameState.setWorld(gameWorld);
            gameState.setPlayerTile(PLAYER_TILE);
            coordinates = gameState.getAllowedPoints();
            roomList = gameState.getRoomList();
            hallwayList = gameState.getHallwayList();
            player = gameState.getPlayer();
            walls = new Walls(roomList, hallwayList, Tileset.WALL, gameWorld);
        } else {
            return gameWorld;
        }

        movePlayerWithString(commands, gameWorld, player, roomList, hallwayList);
        drawAtCoordinates(coordinates, gameWorld, FLOOR_TILE);
        walls.draw();
        player.draw();
        // THIS LINE IS ONLY REMOVED TO BE ABLE TO RUN WITH THE AUTOGRADER
        //ter.renderFrame(world);
        return gameWorld;
    }

    private Point getRandomFromSet(HashSet<Point> myhashSet, Random random){
        int r = RandomUtils.uniform(random, 0, coordinates.size());
        Point p = null;
        for(Point obj : myhashSet) {
            if (r -- == 0) {
                p = obj;
            }
        }
        return p;
    }

    /**
     * Moves player around the world, using the commands (characters that correspond to keys)
     * passed as parameter.
     * @param movements are the set of direction commands to move the player around.
     * @param world is the world in which the player moves.
     * @param pl is the player.
     */
    private void movePlayerWithString(String movements, TETile[][] world, Player pl, ArrayList<Room> roomList, ArrayList<Hallway> hallwayList) {
        for (char c : movements.toCharArray()) {
            switch (c) {
                case Keys.UP -> pl.moveUp();
                case Keys.DOWN -> pl.moveDown();
                case Keys.LEFT -> pl.moveLeft();
                case Keys.RIGHT -> pl.moveRight();
                case Keys.PRE_QUIT_SAVE -> {
                    int i = movements.indexOf(c);
                    if (movements.charAt(i + 1) == Keys.QUIT_SAVE) {
                        gameState.setState(roomList,hallwayList, player.position());
                        GameState.save(gameState, STATE_FILENAME);
                        return;
                    }
                }
                default -> {
                }
            }
        }
    }

    private void initializeWorldBackground(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Helper method that draws the tiles passed as parameters at the points passed as parameters.
     * @param points are the points at which to draw the tiles.
     * @param world is the world that has the coordinate points on which to draw.
     * @param tile is the type of tile to draw.
     */
    private void drawAtCoordinates(HashSet<Point> points, TETile[][] world, TETile tile) {
        for (Point p : points) {
            world[p.x()][p.y()] = tile;
        }
    }

    /**
     * @return the first command for playing with input String.
     */
    private String extractFirstCommand(String input) {
        return input.substring(0, 1);
    }

    /**
     * @return the seed for playing with input String.
     */
    private long extractSeed(String input) {
        input = input.substring(1);
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(input);
        return (matcher.find()
                ? Long.parseLong(input.substring(matcher.start(), matcher.end()))
                : 0);
    }

    /**
     * @return the keystrokes for playing with input String.
     */
    private String extractCommands(String input) {
        input = input.substring(1);
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(input);
        int start = matcher.find() ? matcher.end() : 0;
        return input.substring(start);
    }
}
