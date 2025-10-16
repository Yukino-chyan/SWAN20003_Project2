import bagel.Image;
import bagel.util.Point;
import java.util.List;
import java.util.Properties;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
/**
 * BulletKin class: A ranged enemy that periodically fires fireballs toward the player.
 */
public class BulletKin extends Enemy {
    public int coolDown;
    public int fireballSpeed;
    public double fireballDamage;
    /**
     * This is the constructor of the BulletKin object.
     * @param pos The initial position of this enemy.
     * @param GAME_PROPS The game properties for stats configuration.
     * @param MESSAGE_PROPS The message properties (reserved for future use).
     */
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
        this.originHealth = this.health;
    }
    /**
     * Set the current shooting cooldown frames.
     * @param coolDown The new cooldown value.
     */
    public void setCoolDown(int coolDown) { this.coolDown = coolDown; }
    /**
     * Get the current cooldown frames until next shot.
     * @return The cooldown frame count.
     */
    public int getCoolDown() { return coolDown; }
    /**
     * Get the configured shooting frequency.
     * @return The frames between shots.
     */
    public int getShotSpeed() { return shotSpeed; }
    /**
     * Create a fireball aimed at the player's current position.
     * @param player The target player.
     * @return A new Fireball projectile directed toward the player.
     */
    public Fireball shot(Player player){
        double speedX,speedY,disX,disY;
        disX = player.getPosX() - pos.x;
        disY = player.getPosY() - pos.y;
        speedX = fireballSpeed * disX / sqrt(disX * disX + disY * disY);
        speedY = fireballSpeed * disY / sqrt(disX * disX + disY * disY);
        return new Fireball(pos,speedX,speedY,fireballDamage);
    }
    /**
     * Apply damage to this enemy and reward the player on death.
     * @param num The damage amount.
     * @param player The player to reward if this enemy dies.
     */
    public void injured(double num,Player player){
        health -=  num;
        if(health <= 0){ dead(); player.getKill(killCoin); }
    }
}
