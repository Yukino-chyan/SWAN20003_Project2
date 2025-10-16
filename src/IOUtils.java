import bagel.Font;
import bagel.util.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/**
 * IOUtils class: Utility methods to read config files and parse game objects from strings.
 */
public class IOUtils {
    /***
     * Read a properties file and return a Properties object.
     * @param configFile The path to the properties file.
     * @return Properties object loaded from the file.
     */
    public static Properties readPropertiesFile(String configFile) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(configFile));
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return appProps;
    }
    /**
     * Parse a coordinate string "x,y" into a Point.
     * @param coords The coordinate text in the form "x,y".
     * @return A Point at (x, y).
     */
    public static Point parseCoords(String coords) {
        String[] coordinates = coords.split(",");
        return new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
    }
    /**
     * Parse BulletKin list from "x1,y1; x2,y2; ...".
     * @param coords The coordinates text separated by semicolons.
     * @param GAME_PROPS The game properties used to construct BulletKin.
     * @param MESSAGE_PROPS The message properties used to construct BulletKin.
     * @return A list of BulletKin objects.
     */
    public static List<BulletKin> parseBulletKin(String coords, Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        java.util.List<BulletKin> bulletKins = new java.util.ArrayList<>();
        for (String seg : coords.split(";")) {
            String[] xy = seg.trim().split("\\s*,\\s*");
            if (xy.length < 2) {
                throw new IllegalArgumentException("Bad wall coord: " + seg);
            }
            double x = Double.parseDouble(xy[0]);
            double y = Double.parseDouble(xy[1]);
            Point center = new Point(x, y);
            bulletKins.add(new BulletKin(center, GAME_PROPS, MESSAGE_PROPS));
        }
        return bulletKins;
    }
    /**
     * Parse AshenBulletKin list from "x1,y1; x2,y2; ...".
     * @param coords The coordinates text separated by semicolons.
     * @param GAME_PROPS The game properties used to construct AshenBulletKin.
     * @param MESSAGE_PROPS The message properties used to construct AshenBulletKin.
     * @return A list of AshenBulletKin objects.
     */
    public static List<AshenBulletKin> parseAshenBulletKin(String coords, Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        java.util.List<AshenBulletKin> ashenBulletKins = new java.util.ArrayList<>();
        for (String seg : coords.split(";")) {
            String[] xy = seg.trim().split("\\s*,\\s*");
            if (xy.length < 2) {
                throw new IllegalArgumentException("Bad wall coord: " + seg);
            }
            double x = Double.parseDouble(xy[0]);
            double y = Double.parseDouble(xy[1]);
            Point center = new Point(x, y);
            ashenBulletKins.add(new AshenBulletKin(center, GAME_PROPS, MESSAGE_PROPS));
        }
        return ashenBulletKins;
    }
    /**
     * Parse a KeyBulletKin patrol path from "x1,y1; x2,y2; ..." and create the enemy.
     * @param coords The coordinates text separated by semicolons.
     * @param GAME_PROPS The game properties used to construct KeyBulletKin.
     * @param MESSAGE_PROPS The message properties used to construct KeyBulletKin.
     * @return A KeyBulletKin constructed with the parsed waypoints.
     */
    public static KeyBulletKin parseKeyBulletKin(String coords, Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        java.util.List<Point> scale = new java.util.ArrayList<>();
        for (String seg : coords.split(";")) {
            String[] xy = seg.trim().split("\\s*,\\s*");
            if (xy.length < 2) {
                throw new IllegalArgumentException("Bad wall coord: " + seg);
            }
            double x = Double.parseDouble(xy[0]);
            double y = Double.parseDouble(xy[1]);
            Point center = new Point(x, y);
            scale.add(center);
        }
        return new KeyBulletKin(scale, GAME_PROPS, MESSAGE_PROPS);
    }
    /**
     * Parse TreasureBox list from "x1,y1,coins; x2,y2,coins; ...".
     * @param coords The coordinates text; "0" or empty returns an empty list.
     * @return A list of TreasureBox objects.
     */
    public static List<TreasureBox> parseTreasureBox(String coords) {
        if (coords.isEmpty() || coords.equals("0")) return java.util.List.of();
        java.util.List<TreasureBox> treasureBoxes = new java.util.ArrayList<>();
        for (String seg : coords.split(";")) {
            String[] xyz = seg.trim().split("\\s*,\\s*");
            if (xyz.length < 3) {
                throw new IllegalArgumentException("Bad wall coord: " + seg);
            }
            double x = Double.parseDouble(xyz[0]);
            double y = Double.parseDouble(xyz[1]);
            int z = Integer.parseInt(xyz[2]);
            bagel.util.Point center = new bagel.util.Point(x, y);
            treasureBoxes.add(new TreasureBox(center,z));
        }
        return treasureBoxes;
    }
    /**
     * Parse a Doors object from "x,y,toRoom".
     * @param coords The comma-separated text describing door position and destination.
     * @return A Doors object constructed from the parsed data.
     */
    public static Doors parseDoors(String coords) {
        String[] coordinates = coords.split(",");
        RoomType to = RoomType.toRoomType(coordinates[2]);
        return new Doors(new Point(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1])),to);
    }
    /**
     * Parse Wall list from "x1,y1; x2,y2; ...".
     * @param coords The coordinates text; "0" or empty returns an empty list.
     * @return A list of Wall objects.
     */
    public static List<Wall> parseWalls(String coords) {
        if (coords.isEmpty() || coords.equals("0")) return java.util.List.of();
        java.util.List<Wall> walls = new java.util.ArrayList<>();
        for (String seg : coords.split(";")) {
            String[] xy = seg.trim().split("\\s*,\\s*");
            if (xy.length < 2) {
                throw new IllegalArgumentException("Bad wall coord: " + seg);
            }
            double x = Double.parseDouble(xy[0]);
            double y = Double.parseDouble(xy[1]);
            Point center = new Point(x, y);
            walls.add(new Wall(center));
        }
        return walls;
    }
    /**
     * Parse River list from "x1,y1; x2,y2; ...".
     * @param coords The coordinates text; "0" or empty returns an empty list.
     * @return A list of River objects.
     */
    public static List<River> parseRivers(String coords) {
        if (coords.isEmpty() || coords.equals("0")) return java.util.List.of();
        java.util.List<River> rivers = new java.util.ArrayList<>();
        for (String seg : coords.split(";")) {
            String[] xy = seg.trim().split("\\s*,\\s*");
            if (xy.length < 2) {
                throw new IllegalArgumentException("Bad wall coord: " + seg);
            }
            double x = Double.parseDouble(xy[0]);
            double y = Double.parseDouble(xy[1]);
            Point center = new Point(x, y);
            rivers.add(new River(center));
        }
        return rivers;
    }
    /**
     * Parse a Basket from "x,y" with a coin value.
     * @param coords The coordinates text "x,y".
     * @param coin The coin reward on destroy.
     * @return A Basket object at the given position.
     */
    public static Basket parseBasket(String coords,int coin) {
        String[] coordinates = coords.split(",");
        Point center = new Point(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1]));
        return new Basket(center,coin);
    }
    /**
     * Parse a Table from "x,y" with a coin value.
     * @param coords The coordinates text "x,y".
     * @param coin The coin reward on destroy.
     * @return A Table object at the given position.
     */
    public static Table parseTable(String coords,int coin) {
        String[] coordinates = coords.split(",");
        Point center = new Point(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1]));
        return new Table(center,coin);
    }
}
