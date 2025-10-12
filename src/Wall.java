import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Wall {
    private Point pos;
    private Image wallImage = new Image("res/wall.png");
    private Rectangle rect;
    Wall(Point pos) {
        this.pos = pos;
        this.rect = wallImage.getBoundingBoxAt(pos);
    }
    public void show() {
        wallImage.draw(pos.x, pos.y);
    }
    public Boolean clash(Player player){
        return rect.intersects(player.getterBounds());
    }
    public Boolean clashFireball(Fireball fb){ return rect.intersects(fb.getterBounds()); }
    public Boolean clashBullet(Bullet b){ return rect.intersects(b.getterBounds()); }
}
