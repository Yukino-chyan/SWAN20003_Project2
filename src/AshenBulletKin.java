import bagel.Image;
import bagel.util.Point;

import java.util.List;
import java.util.Properties;

import static java.lang.Math.abs;

public class AshenBulletKin extends Enemy {
    public AshenBulletKin(Point pos, Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        this.pos = pos;
        enemyImage = new Image("res/ashen_bullet_kin.png");
        this.haveKey = true;
        this.health = Double.parseDouble(GAME_PROPS.getProperty("ashenBulletKinHealth"));
        this.killCoin = Integer.parseInt(GAME_PROPS.getProperty("ashenBulletKinCoin"));
        this.shotSpeed = Integer.parseInt(GAME_PROPS.getProperty("ashenBulletKinShootFrequency"));
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
