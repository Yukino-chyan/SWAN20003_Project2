import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Bullet class: A player projectile that moves with a fixed velocity.
 */
public class Bullet extends Projectile {
    /**
     * This is the constructor of the Bullet object.
     * @param pos The initial position of the bullet.
     * @param speedX The horizontal speed component.
     * @param speedY The vertical speed component.
     * @param damage The damage dealt on collision.
     */
    public Bullet(Point pos, Double speedX, Double speedY, Double damage) {
        this.pos = pos;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.projectileImage = new Image("res/bullet.png");
        this.rect = projectileImage.getBoundingBoxAt(pos);
    }
}
