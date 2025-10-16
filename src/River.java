import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * River class: The River in the game.
 */
public class River {
    private Point pos;
    private Image riverImage = new Image("res/river.png");
    private Rectangle rect;
    /**
     * This is the constructor of the River object.
     * @param pos The position of this object.
     */
    River(Point pos) {
        this.pos = pos;
        this.rect = riverImage.getBoundingBoxAt(pos);
    }
    /**
     * This method is used to show the picture of this object on the screen.
     */
    public void show() {
        riverImage.draw(pos.x, pos.y);
    }
    /**
     * Deal with the clash between the river and the player.
     * @param player The player object to be tested.
     * @return true if there is a clash between the river and the player.
     */
    public Boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
}
