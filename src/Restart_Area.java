import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Restart_Area class: The restart area in the game.
 */
public class Restart_Area {
    private Point restartPos;
    private Image restartImage = new Image("res/restart_area.png");
    private Rectangle rect;
    /**
     * This is the constructor of the Restart_Area object.
     * @param restartPos The position of the restart area.
     */
    public Restart_Area(Point restartPos) {
        this.restartPos = restartPos;
        this.rect = restartImage.getBoundingBoxAt(restartPos);
    }
    /**
     * This method is used to show the picture of this object on the screen.
     */
    public void show() {
        restartImage.draw(restartPos.x, restartPos.y);
    }
    /**
     * Deal with the clash between the restart area and the player.
     * @param player The player object to be tested.
     * @return true if there is a clash between the restart area and the player.
     */
    public Boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
}
