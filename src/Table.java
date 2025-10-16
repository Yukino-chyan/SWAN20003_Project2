import bagel.Image;
import bagel.util.Point;
/**
 * Table class: A table can be destroyed.
 */
public class Table extends DestructibleObject {
    /**
     * This is the constructor of the Table object.
     * @param pos The position of this object.
     * @param Coin The number of coins this table gives when destroyed.
     */
    public Table(Point pos, int Coin) {
        this.pos = pos;
        this.coin = Coin;
        this.image = new Image("res/table.png");
        this.rect = image.getBoundingBoxAt(pos);
    }
}
