import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Projectile class: The abstract base class for all projectiles in the game.
 */
public abstract class Projectile {
    protected Point pos;
    protected Image projectileImage;
    protected double speedX, speedY, damage;
    protected Rectangle rect;
    /**
     * Set the x position of the projectile.
     * @param x The new x-coordinate.
     */
    public void setterPosX(double x) {
        pos = new Point(x, pos.y);
    }
    /**
     * Set the y position of the projectile.
     * @param y The new y-coordinate.
     */
    public void setterPosY(double y) {
        pos = new Point(pos.x, y);
    }
    /**
     * Draw the projectile image on the screen.
     */
    public void show() {
        projectileImage.draw(pos.x, pos.y);
    }
    /**
     * Move the projectile according to its speed.
     */
    public void move() {
        setterPosX(pos.x + speedX);
        setterPosY(pos.y + speedY);
        setterBounds();
    }
    /**
     * Update the projectile's bounding box.
     */
    public void setterBounds() {
        rect = projectileImage.getBoundingBoxAt(pos);
    }
    /**
     * Get the bounding box of this projectile.
     * @return The rectangle representing the projectile bounds.
     */
    public Rectangle getterBounds() { return rect; }
    /**
     * Check if the projectile collides with the player.
     * @param player The player to test collision with.
     * @return true if the projectile collides with the player.
     */
    public Boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
    /**
     * Get the damage value of this projectile.
     * @return The damage dealt by this projectile.
     */
    public double getDamage() { return damage; }
}
