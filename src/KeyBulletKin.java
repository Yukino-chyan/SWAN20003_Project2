import bagel.Image;
import bagel.util.Point;

import java.util.List;
import java.util.Properties;

import static java.lang.Math.abs;

public class KeyBulletKin extends Enemy {
    private List<Point> scale;
    private int nextPos = 1;
    public KeyBulletKin(List<Point> scale, Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        this.scale = scale;
        enemyImage = new Image("res/key_bullet_kin.png");
        this.haveKey = true;
        this.health = Double.parseDouble(GAME_PROPS.getProperty("keyBulletKinHealth"));
        this.moveSpeed = Integer.parseInt(GAME_PROPS.getProperty("keyBulletKinSpeed"));
        this.contactHurt = 0.2;
        this.pos = scale.get(0);
        this.rect = enemyImage.getBoundingBoxAt(pos);
    }
    public void move(){
        if(pos.equals(scale.get(nextPos))){ nextPos++; }
        if(nextPos == scale.size()){ nextPos = 0; }
        if(pos.x == scale.get(nextPos).x){
            setterPosy(pos.y + moveSpeed * (( scale.get(nextPos).y - pos.y ) / abs(( scale.get(nextPos).y - pos.y ))));
        }
        else if(pos.y == scale.get(nextPos).y){
            setterPosx(pos.x + moveSpeed * (( scale.get(nextPos).x - pos.x ) / abs(( scale.get(nextPos).x - pos.x ))));
        }
        this.rect = enemyImage.getBoundingBoxAt(pos);
    }
    public void setterPosx (double x){
        pos = new Point(x,pos.y);
    }
    public void setterPosy (double y){
        pos = new Point(pos.x,y);
    }
}
