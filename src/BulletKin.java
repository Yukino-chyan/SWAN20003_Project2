import bagel.Image;
import bagel.util.Point;

import java.util.List;
import java.util.Properties;

import static java.lang.Math.abs;

public class BulletKin extends Enemy {
    public BulletKin(Point pos, Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        this.pos = pos;
        enemyImage = new Image("res/bullet_kin.png");
        this.haveKey = true;
        this.health = Double.parseDouble(GAME_PROPS.getProperty("bulletKinHealth"));
        this.killCoin = Integer.parseInt(GAME_PROPS.getProperty("bulletKinCoin"));
        this.shotSpeed = Integer.parseInt(GAME_PROPS.getProperty("bulletKinShootFrequency"));
        this.contactHurt = 0.2;
        this.rect = enemyImage.getBoundingBoxAt(pos);
    }
    public void setterPosx (double x){
        pos = new Point(x,pos.y);
    }
    public void setterPosy (double y){
        pos = new Point(pos.x,y);
    }
}
