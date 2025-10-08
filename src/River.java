import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class River {
    private Point pos;
    private Image riverImage = new Image("res/river.png");
    private Rectangle rect;
    River(Point pos) {
        this.pos = pos;
        this.rect = riverImage.getBoundingBoxAt(pos);
    }
    public void show() {
        riverImage.draw(pos.x, pos.y);
    }
    public Boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
}
