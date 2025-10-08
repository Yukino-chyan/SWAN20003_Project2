import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class KeyBulletKin {
    private Point pos;
    private Boolean isalive = true;
    private Image keyBulletKinImage = new Image("res/key_bullet_kin.png");
    private final Rectangle rect;
    public KeyBulletKin(Point pos) {
        this.pos = pos;
        rect = keyBulletKinImage.getBoundingBoxAt(pos);
    }
    public void show() {
        keyBulletKinImage.draw(pos.x, pos.y);
    }
    public Boolean clash(Player player) {
        return rect.intersects(player.getterBounds());
    }
    public Boolean isAlive() {
        return isalive;
    }
    public void dead(){
        isalive = false;
    }
    public void reset(){
        isalive = true;
    }
}
