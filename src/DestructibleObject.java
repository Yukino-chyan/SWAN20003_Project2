import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class DestructibleObject {
    protected Image image;
    protected Point pos;
    protected Rectangle rect;
    protected Boolean isAlive = true;
    protected int coin;
    public void show() { image.draw(pos.x, pos.y); }
    public Boolean clash(Player player) { return rect.intersects(player.getterBounds()); }
    public Boolean isAlive() { return isAlive; }
    public void reset(){ isAlive = true; }
    public Boolean clashBullet(Bullet bullet) { return rect.intersects(bullet.getterBounds()); }
    public int getCoin() { return coin; }
    public void dead() { isAlive = false; }
}
