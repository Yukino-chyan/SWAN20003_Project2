import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Enemy class: Base class for all enemies.
 */
public abstract class Enemy {
    protected Point pos;
    protected Boolean isalive = true;
    protected Image enemyImage;
    protected double health, originHealth;
    protected int shotSpeed, moveSpeed, killCoin;
    protected double contactHurt;
    protected Boolean haveKey;
    protected Rectangle rect;
    /**
     * Draw the enemy if it is alive.
     */
    public void show() { if(isalive) enemyImage.draw(pos.x, pos.y); }
    /**
     * Test collision with the player.
     * @param player The player to test.
     * @return true if this enemy intersects the player.
     */
    public Boolean clash(Player player) { return rect.intersects(player.getterBounds()); }
    /**
     * Test collision with a bullet.
     * @param bullet The bullet to test.
     * @return true if this enemy intersects the bullet.
     */
    public Boolean clashBullet(Bullet bullet) { return rect.intersects(bullet.getterBounds()); }
    /**
     * Get the alive status of this enemy.
     * @return true if the enemy is alive.
     */
    public Boolean isAlive() { return isalive; }
    /**
     * Mark this enemy as dead.
     */
    public void dead(){ isalive = false; }
    /**
     * Reset this enemy to its initial state.
     */
    public void reset(){ isalive = true; health = originHealth; }
}
