import bagel.Input;
import bagel.Keys;
import bagel.Image;
import bagel.Font;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import bagel.Window;
import static java.lang.Math.sqrt;
/**
 * EndRoom class: The final room of the game where the win or loss screen is displayed.
 */
public class EndRoom implements Room {
    private Boolean gateDelay = false;
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private Doors doorToB;
    private Restart_Area restartArea;
    private Messege congratesMessege, gameoverMessege;
    private boolean winFlag = false;
    private List<Bullet> bullets = new ArrayList<>();
    /**
     * This is the constructor of the EndRoom object.
     * @param doorToB The door that leads to another room.
     * @param restartPos The restart area's position.
     * @param GAME_PROPS The properties file containing game configurations.
     * @param MESSAGE_PROPS The properties file containing text messages.
     */
    public EndRoom(Doors doorToB, Point restartPos, Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        this.doorToB = doorToB;
        this.restartArea = new Restart_Area(restartPos);
        this.GAME_PROPS = GAME_PROPS;
        this.MESSAGE_PROPS = MESSAGE_PROPS;
        congratesMessege = new Messege(
                MESSAGE_PROPS.getProperty("gameEnd.won"),
                new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("title.fontSize"))),
                new Point(Window.getWidth() / 2.0, Double.parseDouble(GAME_PROPS.getProperty("title.y"))), -1.0, true, false
        );
        gameoverMessege = new Messege(
                MESSAGE_PROPS.getProperty("gameEnd.lost"),
                new Font(GAME_PROPS.getProperty("font"), Integer.parseInt(GAME_PROPS.getProperty("title.fontSize"))),
                new Point(Window.getWidth() / 2.0, Double.parseDouble(GAME_PROPS.getProperty("title.y"))), -1.0, true, false
        );
    }
    /**
     * Display the background, door, restart area, and win/loss message.
     * @param input The current keyboard/mouse input.
     */
    public void show(Input input) {
        bg.draw(width / 2, height / 2);
        restartArea.show();
        doorToB.show();
        if (!winFlag) gameoverMessege.show();
        else congratesMessege.show();
        for (Bullet bullet : bullets) bullet.show();
    }
    /**
     * Set the win flag for the player.
     * @param winFlag true if the player has won, false if lost.
     */
    public void setWinFlag(boolean winFlag) {
        this.winFlag = winFlag;
        if (winFlag) doorToB.setterOpen();
    }
    /**
     * Get the primary door of this room.
     * @return The doorToB object.
     */
    public Doors getPriDoors() { return doorToB; }
    /**
     * Get the secondary door of this room.
     * @return The doorToB object.
     */
    public Doors getSecDoors() { return doorToB; }
    /**
     * Test for collisions with the player.
     * @param player The player to test.
     * @return An integer code: 1 if door triggered, 2 if restart area, 0 otherwise.
     */
    public int clashTest(Player player) {
        if (!doorToB.clash(player)) gateDelay = true;
        if (doorToB.clash(player) && gateDelay) return 1;
        if (restartArea.clash(player)) return 2;
        return 0;
    }
    /**
     * Handle player entry into the room.
     * @param player The player entering.
     * @param backflag true if entering from the back door.
     */
    public void entry(Player player, Boolean backflag) {
        player.setterPosx(doorToB.getPosX());
        player.setterPosy(doorToB.getPosY());
        gateDelay = false;
    }
    /**
     * Reset the door and gate delay.
     */
    public void reset() {
        doorToB.reset();
        gateDelay = false;
    }
    /**
     * Remove all bullets in this room.
     */
    public void clean() { bullets.clear(); }
    /**
     * Fire a bullet from the player toward the mouse cursor.
     * @param player The player firing the bullet.
     * @param input The current input.
     */
    public void shotBullet(Player player, Input input) {
        Point playerPos = new Point(player.getPosX(), player.getPosY());
        Point mousePos = input.getMousePosition();
        double disX = mousePos.x - player.getPosX();
        double disY = mousePos.y - player.getPosY();
        double speedX = player.getBulletSpeed() * disX / sqrt(disX * disX + disY * disY);
        double speedY = player.getBulletSpeed() * disY / sqrt(disX * disX + disY * disY);
        bullets.add(new Bullet(playerPos, speedX, speedY, player.getHurtPerShot()));
    }
    /**
     * Move all bullets in the room.
     */
    public void move() { for (Bullet bb : bullets) bb.move(); }
    /**
     * Get whether gate delay has been triggered.
     * @return true if gate delay is active.
     */
    public Boolean getGateDelay() { return gateDelay; }
}
