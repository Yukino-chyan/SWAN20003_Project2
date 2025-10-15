import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Enemy {
    protected Point pos;
    protected Boolean isalive = true;
    protected Image enemyImage;
    protected double health, originHealth;
    protected int shotSpeed,moveSpeed,killCoin;
    protected double contactHurt;
    protected Boolean haveKey;
    protected Rectangle rect;
    public int getKillCoin() { return killCoin; }
    public void show() { if(isalive) enemyImage.draw(pos.x, pos.y); }
    public Boolean clash(Player player) { return rect.intersects(player.getterBounds()); }
    public Boolean clashBullet(Bullet bullet) { return rect.intersects(bullet.getterBounds()); }
    public Boolean isAlive() { return isalive; }
    public void dead(){ isalive = false; }
    public void reset(){ isalive = true; health = originHealth; }
}
