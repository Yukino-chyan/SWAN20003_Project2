import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Fireball extends Projectile {
    public Fireball(Point pos, Double speedX, Double speedY, Double damage) {
        this.pos = pos;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.projectileImage = new Image("res/fireball.png");
        this.rect = projectileImage.getBoundingBoxAt(pos);
    }
}
