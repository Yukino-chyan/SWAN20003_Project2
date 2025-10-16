import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * DestructibleObject class: Base class for breakable world objects that can collide with bullets and the player.
 */
public abstract class DestructibleObject {
    protected Image image;
    protected Point pos;
    protected Rectangle rect;
    protected Boolean isAlive = true;
    protected int coin;
    /**
     * Draw this object at its position.
     */
    public void show() { image.draw(pos.x, pos.y); }
    /**
     * Test collision with the player.
     * @param player The player to test.
     * @return true if this object intersects the player's bounds.
     */
    public Boolean clash(Player player) { return rect.intersects(player.getterBounds()); }
    /**
     * Get whether this object is still active (not destroyed).
     * @return true if the object is alive.
     */
    public Boolean isAlive() { return isAlive; }
    /**
     * Reset the alive state to true.
     */
    public void reset(){ isAlive = true; }
    /**
     * Test collision with a bullet.
     * @param bullet The bullet to test.
     * @return true if this object intersects the bullet's bounds.
     */
    public Boolean clashBullet(Bullet bullet) { return rect.intersects(bullet.getterBounds()); }
    /**
     * Get the coin reward granted when destroyed.
     * @return The coin amount.
     */
    public int getCoin() { return coin; }
    /**
     * Mark this object as destroyed (no longer alive).
     */
    public void dead() { isAlive = false; }
}
