import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Restart_Area {
    private Point restartPos;
    private Image restartImage = new Image("res/restart_area.png");
    private Rectangle rect;
    public Restart_Area(Point restartPos) {
        this.restartPos = restartPos;
        this.rect = restartImage.getBoundingBoxAt(restartPos);
    }
    public void show (){
        restartImage.draw(restartPos.x, restartPos.y);
    }
    public Boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
}
