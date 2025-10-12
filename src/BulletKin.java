import bagel.Image;
import bagel.util.Point;

import java.util.List;
import java.util.Properties;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class BulletKin extends Enemy {
    public int coolDown;
    public int fireballSpeed;
    public double fireballDamage;
    public BulletKin(Point pos, Properties GAME_PROPS, Properties MESSAGE_PROPS) {
        this.pos = pos;
        enemyImage = new Image("res/bullet_kin.png");
        this.haveKey = true;
        this.health = Double.parseDouble(GAME_PROPS.getProperty("bulletKinHealth"));
        this.killCoin = Integer.parseInt(GAME_PROPS.getProperty("bulletKinCoin"));
        this.shotSpeed = Integer.parseInt(GAME_PROPS.getProperty("bulletKinShootFrequency"));
        this.fireballSpeed = Integer.parseInt(GAME_PROPS.getProperty("fireballSpeed"));
        this.fireballDamage = Double.parseDouble(GAME_PROPS.getProperty("fireballDamage"));
        this.contactHurt = 0.2;
        this.rect = enemyImage.getBoundingBoxAt(pos);
        this.coolDown = this.shotSpeed;
    }
    public void setterPosx (double x){
        pos = new Point(x,pos.y);
    }
    public void setterPosy (double y){
        pos = new Point(pos.x,y);
    }
    public void setCoolDown(int coolDown) { this.coolDown = coolDown; }
    public int getCoolDown() { return coolDown; }
    public int getShotSpeed() { return shotSpeed; }
    public Fireball shot(Player player){
        double speedX,speedY,disX,disY;
        disX = player.getPosX() - pos.x;
        disY = player.getPosY() - pos.y;
        speedX = fireballSpeed * disX / sqrt(disX * disX + disY * disY);
        speedY = fireballSpeed * disY / sqrt(disX * disX + disY * disY);
        return new Fireball(pos,speedX,speedY,fireballDamage);
    }
    public void injured(double num){
        health -=  num;
        if(health <= 0){ dead(); }
    }
}
