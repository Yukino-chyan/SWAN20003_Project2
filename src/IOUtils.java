import bagel.Font;
import bagel.util.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * A utility class that provides methods to read and write files.
 */
public class IOUtils {
    /***
     * Read a properties file and return a Properties object
     * @param configFile: the path to the properties file
     * @return: Properties object
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

    public static Point parseCoords(String coords) {
        String[] coordinates = coords.split(",");
        return new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
    }
    public static List<BulletKin> parseBulletKin(String coords,Properties GAME_PROPS, Properties MESSAGE_PROPS) {
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
    public static List<AshenBulletKin> parseAshenBulletKin(String coords,Properties GAME_PROPS, Properties MESSAGE_PROPS) {
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
    public static Doors parseDoors(String coords) {
        String[] coordinates = coords.split(",");
        RoomType to = RoomType.toRoomType(coordinates[2]);
        return new Doors(new Point(Double.parseDouble(coordinates[0]),Double.parseDouble(coordinates[1])),to);
    }
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
}
