import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Projectile {
    protected Point pos;
    protected Image projectileImage;
    protected double speedX,speedY,damage;
    protected Rectangle rect;
    public void setterPosX(double x){
        pos = new Point(x,pos.y);
    }
    public void setterPosY(double y){
        pos = new Point(pos.x,y);
    }
    public void show(){
        projectileImage.draw(pos.x, pos.y);
    }
    public void move(){
        setterPosX(pos.x + speedX);
        setterPosY(pos.y + speedY);
        setterBounds();
    }
    public void setterBounds(){
        rect = projectileImage.getBoundingBoxAt(pos);
    }
    public Rectangle getterBounds() { return rect; }
    public Boolean clash(Player player){
        return rect.intersects(player.getterBounds());
    }
    public double getDamage(){ return damage; }
}
