import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * TreasureBox class: A TreasureBox that drops coins when opened.
 */
public class TreasureBox {
    private Point pos;
    private Image treasureBoxImage = new Image("res/treasure_box.png");
    private int num;
    private Rectangle rect;
    private boolean open = false;
    /**
     * This is the constructor of the TreasureBox object.
     * @param pos The position of this object.
     * @param num The number of treasures in this box.
     */
    TreasureBox(Point pos, int num) {
        this.pos = pos;
        this.num = num;
        this.rect = treasureBoxImage.getBoundingBoxAt(pos);
    }
    /**
     * This method is used to show the picture of this object on the screen.
     */
    public void show() {
        treasureBoxImage.draw(pos.x, pos.y);
    }
    /**
     * Deal with the clash between the treasure box and the player.
     * @param player The player object to be tested.
     * @return true if there is a clash between the treasure box and the player.
     */
    public boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
    /**
     * Get the number of treasures in this box.
     * @return The number of treasures.
     */
    public int getNum() {
        return num;
    }
    /**
     * Get the open status of this treasure box.
     * @return true if the treasure box is open, false otherwise.
     */
    public boolean getOpen() {
        return open;
    }
    /**
     * Set the open status of this treasure box.
     * @param open The new open status to set.
     */
    public void setOpen(Boolean open) {
        this.open = open;
    }
    /**
     * Reset the treasure box to its initial state (closed).
     */
    public void reset() {
        open = false;
    }
}
