package byog.Core;

import java.util.ArrayList;
import java.util.HashSet;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import byog.TileEngine.TETile;



/**
 * GameState
 * This class is a helper to save and load, to disk, the state of a game.
 * Usage, SAVING a state:
 *      gameState.setStateForSaving(allowedCoordinates, walls.getPoints(), player.position());
 *      GameState.save(gameState, STATE_FILENAME);
 * Usage, LOADING a state:
 *      gameState = GameState.load(STATE_FILENAME);
 *      gameState.setWorld(world);
 *      gameState.setPlayerTile(PLAYER_TILE);
 *      allowedCoordinates = gameState.getAllowedPoints();
 *      player = gameState.getPlayer();
 */
public class GameState implements Serializable {


    /* Variables that are serialized. */

    /* Wall points */
    private HashSet<Point> wallsPoints;
    /* Position of the player in the world. */
    private HashSet<Point> allowedPoints;
    private Point playerPosition;

    /* Variables that are not serialized. */
    /* Variables related to world and player. */
    private TETile[][] world;
    private TETile playerTile;


    public GameState(TETile[][] worldP, TETile playerTileP) {
        this.world = worldP;
        this.playerTile = playerTileP;
    }

    public HashSet<Point> getAllowedPoints() {
        return allowedPoints;
    }

    private void setAllowedPoints(HashSet<Point> allowedPointsP) {
        this.allowedPoints = allowedPointsP;
    }

    public HashSet<Point> getWallsPoints() {
        return wallsPoints;
    }

    private void setWallsPoints(HashSet<Point> wallsP) {
        this.wallsPoints = wallsP;
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    private void setPlayerPosition(Point playerPositionP) {
        this.playerPosition = playerPositionP;
    }

    public void setWorld(TETile[][] worldP) {
        this.world = worldP;
    }

    public void setPlayerTile(TETile playerTileP) {
        this.playerTile = playerTileP;
    }


    /**
     * Returns the player that moves around the world, with its characteristics.
     * Preconditions:
     *  - The game state has to be loaded already.
     * @return a Player instance.
     */
    public Player getPlayer() {
        return new Player(playerPosition, allowedPoints, playerTile, world);
    }


    /**
     * Reads the file that contains the game state, and returns a GameState instance.
     * Preconditions:
     *  - The contents of the file must be valid to generate a GameState instance.
     * @param filename the name of the file that contains the state of a saved game.
     * @return a GameState instance if the file is valid, or else null.
     */
    public static GameState load(String filename) {
        File f = new File(filename);
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                GameState loadedGameState = (GameState) os.readObject();
                os.close();
                return loadedGameState;
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found.");
                System.exit(0);
            }
        }
        /* In the case no GameState has been saved yet, return null. */
        return null;
    }


    public void setState(HashSet<Point> allowedPts, HashSet<Point> wallsPts, Point playerPos) {
        if (allowedPts == null || wallsPts == null || playerPos == null) {
            throw new IllegalArgumentException("Points for saving game state can not be null.");
        }
        setAllowedPoints(allowedPts);
        setWallsPoints(wallsPts);
        setPlayerPosition(playerPos);
    }

    /**
     * Saves the game state to a file on disk.
     * @param gameState the instance of GameState to be saved to disk.
     * @param filename the name of the file that will contain the state of a saved game.
     */
    public static void save(GameState gameState, String filename) {
        gameState.world = null;
        gameState.playerTile = null;
        File f = new File(filename);
        try {
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(gameState);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

}
