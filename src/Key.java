import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Key class: The collectible key in the game.
 */
public class Key {
    private Point pos;
    private Image keyImage;
    private Rectangle rect;
    private Boolean isAlive = true;
    /**
     * This is the constructor of the Key object.
     * @param pos The position of this key.
     */
    public Key(Point pos) {
        this.pos = pos;
        this.keyImage = new Image("res/key.png");
        this.rect = keyImage.getBoundingBoxAt(pos);
    }
    /**
     * Check if the key collides with the player.
     * @param player The player to test collision with.
     * @return true if the key collides with the player.
     */
    public Boolean clash(Player player){ return rect.intersects(player.getterBounds()); }
    /**
     * Draw the key on the screen.
     */
    public void show(){ keyImage.draw(pos.x, pos.y); }
    /**
     * Get the alive status of the key.
     * @return true if the key is still available.
     */
    public Boolean isAlive(){ return isAlive; }
    /**
     * Mark this key as collected (no longer alive).
     */
    public void dead(){ isAlive = false; }
}
