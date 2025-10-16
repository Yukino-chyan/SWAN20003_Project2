import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Doors class: A doorway that can be locked or unlocked and leads to another room.
 */
public class Doors {
    private Point pos;
    private boolean open = false;
    private RoomType toRoom;
    private final Image lockedImg = new Image("res/locked_door.png");
    private final Image openImg = new Image("res/unlocked_door.png");
    private final Rectangle rect;
    /**
     * This is the constructor of the Doors object.
     * @param pos The position of the door.
     * @param toRoom The destination room type this door leads to.
     */
    public Doors(Point pos, RoomType toRoom) {
        this.pos = pos;
        this.toRoom = toRoom;
        this.rect = lockedImg.getBoundingBoxAt(pos);
    }
    /**
     * Draw the door (locked or unlocked) at its position.
     */
    public void show() {
        if (open) openImg.draw(pos.x, pos.y);
        else lockedImg.draw(pos.x, pos.y);
    }
    /**
     * Get the door's X coordinate.
     * @return The x value of the door position.
     */
    public double getPosX() { return pos.x; }
    /**
     * Get the door's Y coordinate.
     * @return The y value of the door position.
     */
    public double getPosY() { return pos.y; }
    /**
     * Get whether the door is open.
     * @return true if open, false if locked.
     */
    public Boolean getOpen() { return open; }
    /**
     * Unlock the door.
     */
    public void setterOpen() { open = true; }
    /**
     * Get the destination room type of this door.
     * @return The RoomType this door leads to.
     */
    public RoomType getToRoom() { return toRoom; }
    /**
     * Test collision between the door and the player.
     * @param player The player to test.
     * @return true if the player's bounds intersect the door.
     */
    public Boolean clash(Player player) { return rect.intersects(player.getterBounds()); }
    /**
     * Reset the door to locked state.
     */
    public void reset() { open = false; }
    /**
     * Test collision with a bullet; only blocks when locked.
     * @param bullet The bullet to test.
     * @return true if the door is locked and intersects the bullet.
     */
    public Boolean clashBullet(Bullet bullet) { return rect.intersects(bullet.getterBounds()) & (!open); }
    /**
     * Test collision with a fireball; only blocks when locked.
     * @param fireball The fireball to test.
     * @return true if the door is locked and intersects the fireball.
     */
    public Boolean clashFireball(Fireball fireball) { return rect.intersects(fireball.getterBounds()) & (!open); }
}
