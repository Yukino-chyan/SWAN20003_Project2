import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Bullet extends Projectile {
    public Bullet(Point pos, Double speedX, Double speedY, Double damage) {
        this.pos = pos;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.projectileImage = new Image("res/bullet.png");
        this.rect = projectileImage.getBoundingBoxAt(pos);
    }
}
