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
        this.killCoin = 0;
        this.contactHurt = 0.2;
        this.pos = scale.get(0);
        this.rect = enemyImage.getBoundingBoxAt(pos);
    }
    public void move(){
        if(pos.equals(scale.get(nextPos))){ nextPos++; }
        if(nextPos == scale.size()){ nextPos = 0; }
        double dx = scale.get(nextPos).x - pos.x;
        double dy = scale.get(nextPos).y - pos.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        dx /= len; dy /= len;
        dx *= moveSpeed; dy *= moveSpeed;
        if(pos.x < scale.get(nextPos).x && (pos.x + dx) > scale.get(nextPos).x){
            dx =  scale.get(nextPos).x - pos.x;
        }
        if(pos.x > scale.get(nextPos).x && (pos.x + dx) < scale.get(nextPos).x){
            dx =  scale.get(nextPos).x - pos.x;
        }
        if(pos.y < scale.get(nextPos).y && (pos.y + dy) > scale.get(nextPos).y){
            dy =  scale.get(nextPos).y - pos.y;
        }
        if(pos.y > scale.get(nextPos).y && (pos.y + dy) < scale.get(nextPos).y){
            dy =  scale.get(nextPos).y - pos.y;
        }
        setterPosx(pos.x+dx); setterPosy(pos.y+dy);
        this.rect = enemyImage.getBoundingBoxAt(pos);
    }
    public void setterPosx (double x){
        pos = new Point(x,pos.y);
    }
    public void setterPosy (double y){
        pos = new Point(pos.x,y);
    }
    public void reset() { isalive = true; health = originHealth; nextPos = 1;}
}
