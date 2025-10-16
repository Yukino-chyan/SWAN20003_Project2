import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Key {
    private Point pos;
    private Image keyImage;
    private Rectangle rect;
    private Boolean isAlive = true;
    public Key(Point pos) {
        this.pos = pos;
        this.keyImage = new Image("res/key.png");
        this.rect = keyImage.getBoundingBoxAt(pos);
    }
    public Boolean clash(Player player){ return rect.intersects(player.getterBounds()); }
    public void show(){ keyImage.draw(pos.x, pos.y); }
    public Boolean isAlive(){ return isAlive; }
    public void dead(){ isAlive = false; }
}
