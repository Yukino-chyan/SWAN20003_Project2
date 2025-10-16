import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Wall class: The Wall in the game.
 */
public class Wall {
    private Point pos;
    private Image wallImage = new Image("res/wall.png");
    private Rectangle rect;
    /**
     *This is the constructor of the Wall object.
     * @param pos the position of this object.
     */
    Wall(Point pos) {
        this.pos = pos;
        this.rect = wallImage.getBoundingBoxAt(pos);
    }

    /**
     *This method is used to show the picture of this object in the screen.
     */
    public void show() {
        wallImage.draw(pos.x, pos.y);
    }
    /**
     *Deal with the clash between walls and player.
     * @param player This is the player object should be tested.
     * @return true if there is a clash between walls and player.
     */
    public boolean clash(Player player){
        return rect.intersects(player.getterBounds());
    }
    /**
     *Deal with the clash between walls and fireball.
     * @param fb This is the fireball object should be tested.
     * @return true if there is a clash between walls and fireball.
     */
    public boolean clashFireball(Fireball fb){ return rect.intersects(fb.getterBounds()); }

    /**
     *Deal with the clash between walls and bullet.
     * @param b This is the bullet object should be tested.
     * @return true if there is a clash between walls and bullet.
     */
    public boolean clashBullet(Bullet b){ return rect.intersects(b.getterBounds()); }
}
