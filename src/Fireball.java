import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Fireball class: An enemy projectile that moves with a fixed velocity.
 */
public class Fireball extends Projectile {
    /**
     * This is the constructor of the Fireball object.
     * @param pos The initial position of the fireball.
     * @param speedX The horizontal speed component.
     * @param speedY The vertical speed component.
     * @param damage The damage dealt on collision.
     */
    public Fireball(Point pos, Double speedX, Double speedY, Double damage) {
        this.pos = pos;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.projectileImage = new Image("res/fireball.png");
        this.rect = projectileImage.getBoundingBoxAt(pos);
    }
}
