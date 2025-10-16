import bagel.Input;
import bagel.Keys;
import bagel.Image;
import bagel.Font;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import bagel.Window;
import static java.lang.Math.sqrt;
/**
 * PrepRoom class: The preparation room before battle in the game.
 */
public class PrepRoom implements Room {
    private Boolean gateDelay = false;
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private Point marinePos, robotPos;
    private Doors doorToA;
    private Restart_Area restartArea;
    private Messege title, moveMessege, marineMessege, robotMessege, selectMessege;
    private Image marineImage, robotImage;
    private List<Bullet> bullets = new ArrayList<>();
    /**
     * This is the constructor of the PrepRoom object.
     * @param doorToA The door leading to the next room.
     * @param restartPos The position of the restart area.
     * @param GAME_PROPS The game properties file.
     * @param MESSAGE_PROPS The message properties file.
     * @param marineMessegePos The position of the marine message.
     * @param robotMessegePos The position of the robot message.
     * @param marinePos The position of the marine image.
     * @param robotPos The position of the robot image.
     */
    public PrepRoom(Doors doorToA, Point restartPos, Properties GAME_PROPS, Properties MESSAGE_PROPS, Point marineMessegePos, Point robotMessegePos, Point marinePos, Point robotPos) {
        this.doorToA = doorToA;
        this.restartArea = new Restart_Area(restartPos);
        this.GAME_PROPS = GAME_PROPS;
        this.MESSAGE_PROPS = MESSAGE_PROPS;
        this.marineImage = new Image("res/marine_sprite.png");
        this.robotImage = new Image("res/robot_sprite.png");
        this.marinePos = marinePos;
        this.robotPos = robotPos;
        title = new Messege(MESSAGE_PROPS.getProperty("title"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("title.fontSize"))), new Point(Window.getWidth() / 2.0, Double.parseDouble(GAME_PROPS.getProperty("title.y"))), -1.0, true, false);
        moveMessege = new Messege(MESSAGE_PROPS.getProperty("moveMessage"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("prompt.fontSize"))), new Point(Window.getWidth() / 2.0, Double.parseDouble(GAME_PROPS.getProperty("moveMessage.y"))), -1.0, true, false);
        marineMessege = new Messege(MESSAGE_PROPS.getProperty("marineDescription"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))), marineMessegePos, -1, false, false);
        robotMessege = new Messege(MESSAGE_PROPS.getProperty("robotDescription"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("playerStats.fontSize"))), robotMessegePos, -1, false, false);
        selectMessege = new Messege(MESSAGE_PROPS.getProperty("selectMessage"), new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("prompt.fontSize"))), new Point(Window.getWidth() / 2.0, Double.parseDouble(GAME_PROPS.getProperty("selectMessage.y"))), -1, true, false);
    }
    /**
     * Display all elements of the preparation room.
     * @param input The player's input.
     */
    public void show(Input input) {
        bg.draw(width / 2, height / 2);
        restartArea.show();
        doorToA.show();
        title.show();
        moveMessege.show();
        marineMessege.show();
        robotMessege.show();
        selectMessege.show();
        marineImage.draw(marinePos.x, marinePos.y);
        robotImage.draw(robotPos.x, robotPos.y);
        for (Bullet bullet : bullets) { bullet.show(); }
    }
    /**
     * Get the primary door of this room.
     * @return The door leading to another room.
     */
    public Doors getPriDoors() { return doorToA; }
    /**
     * Get the secondary door of this room (same as the primary one in this case).
     * @return The same door object.
     */
    public Doors getSecDoors() { return doorToA; }
    /**
     * Check the player's collision with the room objects.
     * @param player The player object.
     * @return A flag representing the collision result.
     */
    public int clashTest(Player player) {
        if (!doorToA.clash(player)) { gateDelay = true; }
        if (doorToA.clash(player) && doorToA.getOpen() && gateDelay) return 3;
        if (restartArea.clash(player)) return 2;
        return 0;
    }
    /**
     * Handle player entry into this room.
     * @param player The player entering the room.
     * @param backflag Whether the player comes from the back door.
     */
    public void entry(Player player, Boolean backflag) {
        player.setterPosx(doorToA.getPosX());
        player.setterPosy(doorToA.getPosY());
        gateDelay = false;
    }
    /**
     * Reset the room to its initial state.
     */
    public void reset() {
        doorToA.reset();
        gateDelay = false;
    }
    /**
     * Handle player shooting inside the room.
     * @param player The player who shoots.
     * @param input The player's current input.
     */
    public void shotBullet(Player player, Input input) {
        Point playerPos = new Point(player.getPosX(), player.getPosY());
        Point mousePos = input.getMousePosition();
        double speedX, speedY, disX, disY;
        disX = mousePos.x - player.getPosX();
        disY = mousePos.y - player.getPosY();
        speedX = player.getBulletSpeed() * disX / sqrt(disX * disX + disY * disY);
        speedY = player.getBulletSpeed() * disY / sqrt(disX * disX + disY * disY);
        bullets.add(new Bullet(playerPos, speedX, speedY, player.getHurtPerShot()));
    }
    /**
     * Clear all bullets in the room.
     */
    public void clean() { bullets.clear(); }
    /**
     * Update bullet positions in the room.
     */
    public void move() { for (Bullet bb : bullets) { bb.move(); } }
    /**
     * Get the gate delay status.
     * @return true if the player has left the gate area, false otherwise.
     */
    public Boolean getGateDelay() { return gateDelay; }
}
