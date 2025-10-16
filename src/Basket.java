import bagel.Image;
import bagel.util.Point;
/**
 * Basket class: A destructible basket that grants coins when destroyed.
 */
public class Basket extends DestructibleObject {
    /**
     * This is the constructor of the Basket object.
     * @param pos The position of this basket.
     * @param coin The coin reward granted when destroyed.
     */
    public Basket(Point pos,int coin){
        this.pos = pos;
        this.coin = coin;
        this.image = new Image("res/basket.png");
        this.rect = image.getBoundingBoxAt(pos);
    }
}
