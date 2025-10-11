import bagel.Image;
import bagel.util.Point;

public class Fireball {
    private Point pos;
    private Image fireballImage = new Image("res/fireball.png");
    private double speedX,speedY,damage;
    public Fireball(Point pos, Double speedX, Double speedY, Double damage) {
        this.pos = pos;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
    }
    public void setterPosX(double x){
        pos = new Point(x,pos.y);
    }
    public void setterPosY(double y){
        pos = new Point(pos.x,y);
    }
    public void show(){
        fireballImage.draw(pos.x, pos.y);
    }
    public void move(){
        setterPosX(pos.x + speedX);
        setterPosY(pos.y + speedY);
    }
}
