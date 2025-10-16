import bagel.Image;
import bagel.Input;
import bagel.Window;
import java.util.Properties;
/**
 * Room interface: Defines common behaviour for all rooms (scenes) in the game.
 */
public interface Room {
    /** Background image shared by rooms. */
    public Image bg = new Image("res/background.png");
    /** Screen width in pixels. */
    public int width = Window.getWidth();
    /** Screen height in pixels. */
    public int height = Window.getHeight();
    /**
     * Draw all visual elements of this room.
     * @param input The current keyboard/mouse input.
     */
    void show(Input input);
    /**
     * Get the primary door of this room.
     * @return The primary door instance.
     */
    Doors getPriDoors();
    /**
     * Get the secondary door of this room.
     * @return The secondary door instance.
     */
    Doors getSecDoors();
    /**
     * Test interactions/collisions relevant to room logic.
     * @param player The player to test.
     * @return An integer code describing the result (e.g., door enter, restart, hazards).
     */
    int clashTest(Player player);
    /**
     * Handle player entry into this room (set spawn position).
     * @param player The entering player.
     * @param backflag true if entering from the secondary door; false for primary.
     */
    void entry(Player player,Boolean backflag);
    /**
     * Remove transient state (e.g., projectiles) from this room.
     */
    void clean();
    /**
     * Fire a player bullet within this room.
     * @param player The player who shoots.
     * @param input The current input (to get aiming information).
     */
    void shotBullet(Player player, Input input);
    /**
     * Advance per-frame movement of dynamic entities in this room.
     */
    void move();
    /**
     * Get whether the room is currently in the post-entry (gate delay/combat) state.
     * @return true if gate delay/combat is active.
     */
    Boolean getGateDelay();
}
