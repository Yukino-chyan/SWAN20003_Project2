import bagel.Image;
import bagel.util.Point;
import java.util.List;
import java.util.Properties;
import static java.lang.Math.abs;
/**
 * KeyBulletKin class: An enemy that moves along predefined waypoints and carries a key.
 */
public class KeyBulletKin extends Enemy {
    private List<Point> scale;
    private int nextPos = 1;
    /**
     * This is the constructor of the KeyBulletKin object.
     * @param scale The ordered list of waypoints for movement.
     * @param GAME_PROPS The game properties for stats configuration.
     * @param MESSAGE_PROPS The message properties (unused but reserved).
     */
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
        this.originHealth = this.health;
    }
    /**
     * Move the enemy toward the next waypoint and update bounds.
     */
    public void move(){
        if(pos.equals(scale.get(nextPos))){ nextPos++; }
        if(nextPos == scale.size()){ nextPos = 0; }
        double dx = scale.get(nextPos).x - pos.x;
        double dy = scale.get(nextPos).y - pos.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        dx /= len; dy /= len;
        dx *= moveSpeed; dy *= moveSpeed;
        if(pos.x < scale.get(nextPos).x && (pos.x + dx) > scale.get(nextPos).x){ dx = scale.get(nextPos).x - pos.x; }
        if(pos.x > scale.get(nextPos).x && (pos.x + dx) < scale.get(nextPos).x){ dx = scale.get(nextPos).x - pos.x; }
        if(pos.y < scale.get(nextPos).y && (pos.y + dy) > scale.get(nextPos).y){ dy = scale.get(nextPos).y - pos.y; }
        if(pos.y > scale.get(nextPos).y && (pos.y + dy) < scale.get(nextPos).y){ dy = scale.get(nextPos).y - pos.y; }
        setterPosx(pos.x+dx); setterPosy(pos.y+dy);
        this.rect = enemyImage.getBoundingBoxAt(pos);
    }
    /**
     * Set the x coordinate of the KeyBulletKin
     * @param x The new x value.
     */
    public void setterPosx (double x){
        pos = new Point(x,pos.y);
    }
    /**
     * Set the y coordinate of the KeyBulletKin.
     * @param y The new y value.
     */
    public void setterPosy (double y){
        pos = new Point(pos.x,y);
    }
    /**
     * Reset the KeyBulletKin to its initial state.
     */
    public void reset() { isalive = true; health = originHealth; nextPos = 1; }
    /**
     * Get the current position of the KeyBulletKin.
     * @return The current position point.
     */
    public Point getPos() { return this.pos; }
}
