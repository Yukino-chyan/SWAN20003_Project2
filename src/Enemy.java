import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Enemy {
    protected Point pos;
    protected Boolean isalive = true;
    protected Image enemyImage;
    protected double health;
    protected int shotSpeed,moveSpeed;
    protected double contactHurt;
    protected Boolean haveKey;
    protected Rectangle rect;
    public void show() { enemyImage.draw(pos.x, pos.y); }
    public Boolean clash(Player player) { return rect.intersects(player.getterBounds()); }
    public Boolean isAlive() { return isalive; }
    public void dead(){ isalive = false; }
    public void reset(){ isalive = true; }
}
