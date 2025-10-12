import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Bullet {
    private Point pos;
    private Image BulletImage = new Image("res/bullet.png");
    private double speedX,speedY,damage;
    private Rectangle rect;
    public Bullet(Point pos, Double speedX, Double speedY, Double damage) {
        this.pos = pos;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.rect = BulletImage.getBoundingBoxAt(pos);
    }
    public void setterPosX(double x){
        pos = new Point(x,pos.y);
    }
    public void setterPosY(double y){
        pos = new Point(pos.x,y);
    }
    public void show(){
        BulletImage.draw(pos.x, pos.y);
    }
    public void move(){
        setterPosX(pos.x + speedX);
        setterPosY(pos.y + speedY);
        setterBounds();
    }
    public void setterBounds(){
        rect = BulletImage.getBoundingBoxAt(pos);
    }
    public Rectangle getterBounds() { return rect; }
    public Boolean clash(Player player){
        return rect.intersects(player.getterBounds());
    }
    public double getDamage(){ return damage; }
}
